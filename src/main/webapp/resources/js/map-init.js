// === 지도 설정 ===
const initialCenter = [14137133.82, 4511912.58];
const initialZoom = 9;
let currentBasemapType = vw.ol3.BasemapType.GRAPHIC;

let isbridgeMarkerVisible = false;
let isDamMarkerVisible = false;
let isPrecipitationeMarkerVisible = false;
let areAllMarkersVisible = false;

// === 지도 객체 및 레이어 초기화 ===
const vmap = new vw.ol3.Map("vmap", vw.ol3.MapOptions);
vmap.getView().setCenter(initialCenter);
vmap.getView().setZoom(initialZoom);

vmap.getView().on('change:resolution', () => {
    addMarkerbridge();
    addMarkerdam();
    addMarkerPrecipitatione();
});


// OpenLayers Vector 레이어로 Feature 생성
const bridgeVectorLayer = new ol.layer.Vector({
    source: new ol.source.Vector()
});
const damVectorLayer = new ol.layer.Vector({
    source: new ol.source.Vector()
});
const precipitationeVectorLayer = new ol.layer.Vector({
    source: new ol.source.Vector()
});


function addMarkerLayers() {
    vmap.addLayer(bridgeVectorLayer);
    vmap.addLayer(damVectorLayer);
    vmap.addLayer(precipitationeVectorLayer);
    
    // Vector 레이어 클릭 이벤트 설정
    vmap.on('singleclick', function(evt) {
        const feature = vmap.forEachFeatureAtPixel(evt.pixel, function(feature) {
            return feature;
        });
        
        if (feature && feature.get('clickCallback')) {
            console.log('Feature 클릭 감지:', feature.get('title'));
            feature.get('clickCallback')();
        }
    });
}

function clusterMarkers(data) {
    const view = vmap.getView();
    const zoom = view.getZoom();
    const res  = view.getResolution?.();     // m/px (EPSG:3857 가정)
    const px   = 64;                          // 클러스터 반경(픽셀)
    const radiusM = res ? res * px : Infinity;

    // 500m 이하 → 클러스터 OFF: 개별 마커 그대로 반환
    if (radiusM <= 3000) {
        return data.map(d => ({ ...d, isCluster: false }));
    }

    // 클러스터 ON일 때만 gridSize 사용 (도 단위)
    let gridSize;
    if (zoom < 6)       gridSize = 3.0;   // 전국
    else if (zoom < 8)  gridSize = 1.5;   // 광역
    else if (zoom < 10) gridSize = 0.5;   // 시/군
else if (zoom < 12) gridSize = 0.15;  // 읍/면/동
    else if (zoom < 13) gridSize = 0.05;  // 상세
    else                gridSize = 0.01;  // 아주 촘촘 (0 금지)

    const toNum = v => { const n = Number(v); return Number.isFinite(n) ? n : null; };

    const clusters = {};
    for (const dto of data) {
        // lat_dd / lon_dd 또는 latDd / lonDd 둘 다 대응
        const lat = toNum(dto.lat_dd ?? dto.latDd ?? dto.lat);
        const lon = toNum(dto.lon_dd ?? dto.lonDd ?? dto.lon);
        if (lat == null || lon == null) continue;

        const key = `${Math.floor(lat / gridSize)}_${Math.floor(lon / gridSize)}`;
        if (!clusters[key]) clusters[key] = [];
        clusters[key].push({ ...dto, lat_dd: lat, lon_dd: lon });
    }

    return Object.values(clusters).map(group => {
        if (group.length === 1) {
            return { ...group[0], isCluster: false };
        }
        const avgLat = group.reduce((s, d) => s + d.lat_dd, 0) / group.length;
        const avgLon = group.reduce((s, d) => s + d.lon_dd, 0) / group.length;
        return {
            lat_dd: avgLat,
            lon_dd: avgLon,
            obsnm: `${group.length}개 시설`,
            isCluster: true,
            count: group.length,
            members: group
        };
    });
}



// === OpenLayers Feature 생성 함수 ===
function createFeature(x, y, title, contents, iconUrl, clickCallback, featureId) {
    // 좌표를 EPSG:3857로 변환 (Web Mercator)
    const coordinate = ol.proj.fromLonLat([x, y]);
    
    // Point geometry 생성
    const geometry = new ol.geom.Point(coordinate);
    
    // Feature 생성
    const feature = new ol.Feature({
        geometry: geometry,
        title: title,
        contents: contents,
        clickCallback: clickCallback
    });
    
    // Feature에 고유 ID 설정 (DTO의 실제 ID 사용)
    const id = featureId || `feature-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
    feature.setId(id);
    
    // 스타일 설정
    const style = new ol.style.Style({
        image: new ol.style.Icon({
            src: iconUrl,
            scale: 1,
            anchor: [0.5, 1]
        }),
        text: new ol.style.Text({
            text: title,
            font: '12px Calibri, sans-serif',
            fill: new ol.style.Fill({color: '#000'}),
            stroke: new ol.style.Stroke({color: '#fff', width: 2}),
            offsetY: 20
        })
    });
    
    feature.setStyle(style);
    
    return feature;
}


// OpenLayers Vector 레이어용 loadFeatures 
window.loadFeatures = async function(url, vectorLayer, mapDtoToFeature, options = {}) {
    try {
        const res = await fetch(url, { cache: 'no-store' });
        if (!res.ok) throw new Error(`HTTP ${res.status} ${res.statusText}`);
        const list = await res.json();
        if (!Array.isArray(list)) throw new Error('JSON 배열이 아닙니다.');

        // 1) 기존 피처 비우기
        const source = vectorLayer.getSource();
        source.clear();

        // 2) 클러스터링 적용
        let processedData = list;
        if (options.enableCluster) {
            processedData = clusterMarkers(list);
        }
        
        // 3) 다리 데이터 저장
        if (url.includes('api/bridge')) {
            window.currentBridgeData = processedData;
        }

        // 4) 피처 생성 & 추가
        const features = [];
        for (const dto of processedData) {
            const feature = mapDtoToFeature(dto);
            if (feature) {
                features.push(feature);
            }
        }
        
        // Vector source에 모든 피처 추가
        source.addFeatures(features);
        console.log(`${features.length}개 피처 추가됨`);

    } catch (e) {
        console.error('피처 로드 실패:', e);
    }
};



/* ===== 공통: 컨텍스트/에셋 헬퍼 ===== */
// JSP에서 내려준 APP_CTX 사용(없으면 '/')
const CTX = (window.APP_CTX || '/');
// '/resources/...' 앞에 컨텍스트 자동 부착
const asset = (p) => CTX + String(p).replace(/^\//, '');

// 아이콘 경로 상수
const ICONS = {
    bridge: asset('/resources/assets/img/bridge.svg'),
    dam:    asset('/resources/assets/img/dam.svg'),
    rain:   asset('/resources/assets/img/water.svg'),
    // 필요하면 클러스터 전용 별도 지정
    cluster: asset('/resources/assets/img/cluster.svg')
};



/* ===== 다리 마커 ===== */

function addMarkerbridge() {
    loadFeatures(`${CTX}api/bridge`, bridgeVectorLayer, (dto) => {
        const isCluster = dto.isCluster === true;

        // const title = isCluster
        //     ? `<a href="javascript:expandCluster(${dto.lon_dd}, ${dto.lat_dd}, 'bridge');">${dto.obsnm} (클릭하여 확대)</a>`
        //     : `<a href="javascript:openPopupBridge('${dto.wlobscd}');">${dto.obsnm}</a>`;

        // Feature는 클릭 이벤트로 처리하므로 단순 텍스트만
        const title = isCluster ? `${dto.obsnm} (클릭하여 확대)` : dto.obsnm;


        const contents = isCluster
            ? `포함된 시설: ${dto.members.map(d => d.obsnm).join(', ')}`
            : `해발 고도: ${dto.gdt}<br>
         경보 수위: ${dto.attwl}<br>
         경고 수위: ${dto.wrnwl}<br>
         주의 수위: ${dto.almwl}<br>
         안전 수위: ${dto.srswl}<br>
         최고 수위: ${dto.pfh}<br>
         홍수 위험 예고: ${dto.fstnyn}`;

        const iconUrl = isCluster ? ICONS.cluster : ICONS.bridge;

        // 클릭 콜백 함수 추가
        const clickCallback = isCluster
            ? () => expandCluster(dto.lon_dd, dto.lat_dd, 'bridge')
            : () => showMarkerInfo({
                id: `bridge-${dto.wlobscd}`,
                type: 'bridge',
                name: dto.obsnm,
                subtitle: dto.city || '한강',
                waterLevel: dto.pfh ?? dto.gdt,
                stage: dto.stage || '-',
                coord: { lon_dd: dto.lon_dd, lat_dd: dto.lat_dd },
                updatedAt: dto.updatedAt,
                zoomTo: () => vmap.setCenterAndZoom(dto.lon_dd, dto.lat_dd, 12)
            });


        // Feature ID로 DTO의 고유값 사용 (클러스터면 좌표 기반, 개별이면 코드 사용)
        const featureId = isCluster 
            ? `cluster-${dto.lat_dd}-${dto.lon_dd}` 
            : `bridge-${dto.wlobscd}`;
            
        return createFeature(dto.lon_dd, dto.lat_dd, title, contents, iconUrl, clickCallback, featureId);
    }, { enableCluster: true });
}

/* ===== 댐 마커 ===== */
function addMarkerdam() {
    loadFeatures(`${CTX}api/dam`, damVectorLayer, (dto) => {
        const isCluster = dto.isCluster === true;

        // Feature는 클릭 이벤트로 처리하므로 단순 텍스트만
        const title = isCluster ? `${dto.obsnm} (클릭하여 확대)` : dto.obsnm;

        const contents = isCluster
            ? `포함된 시설: ${dto.members.map(d => d.obsnm).join(', ')}`
            : `홍수 우려 수위: ${dto.pfh}<br>수위 제한선: ${dto.fldlmtwl}`;

        const iconUrl = isCluster ? ICONS.cluster : ICONS.dam;

        // 클릭 콜백 함수 추가
        const clickCallback = isCluster
            ? () => expandCluster(dto.lon_dd, dto.lat_dd, 'dam')
            : () => showMarkerInfo({
                id: `dam-${dto.dmobscd}`,
                type: 'dam',
                name: dto.obsnm,
                subtitle: dto.city || '댐',
                waterLevel: dto.fldlmtwl ?? dto.pfh,
                stage: dto.stage || '-',
                coord: { lon_dd: dto.lon_dd, lat_dd: dto.lat_dd },
                updatedAt: dto.updatedAt,
                zoomTo: () => vmap.setCenterAndZoom(dto.lon_dd, dto.lat_dd, 12)
            });

        // Feature ID로 DTO의 고유값 사용
        const featureId = isCluster 
            ? `cluster-dam-${dto.lat_dd}-${dto.lon_dd}` 
            : `dam-${dto.dmobscd}`;
            
        return createFeature(dto.lon_dd, dto.lat_dd, title, contents, iconUrl, clickCallback, featureId);
    }, { enableCluster: true });
}

/* ===== 강우 마커 ===== */
function addMarkerPrecipitatione() {
    loadFeatures(`${CTX}api/precipitatione`, precipitationeVectorLayer, (dto) => {
        const isCluster = dto.isCluster === true;

        // Feature는 클릭 이벤트로 처리하므로 단순 텍스트만
        const title = isCluster ? `${dto.obsnm} (클릭하여 확대)` : dto.obsnm;

        const contents = isCluster
            ? `포함된 시설: ${dto.members.map(d => d.obsnm).join(', ')}`
            : `${dto.obsnm}`;

        const iconUrl = isCluster ? ICONS.cluster : ICONS.rain;

        // 클릭 콜백 함수 추가
        const clickCallback = isCluster
            ? () => expandCluster(dto.lon_dd, dto.lat_dd, 'rain')
            : () => showMarkerInfo({
                id: `rain-${dto.rfobscd}`,
                type: 'rain',
                name: dto.obsnm,
                subtitle: dto.city || '강수',
                waterLevel: dto.value ?? dto.rain ?? '-',
                stage: dto.stage || '-',
                coord: { lon_dd: dto.lon_dd, lat_dd: dto.lat_dd },
                updatedAt: dto.updatedAt,
                zoomTo: () => vmap.setCenterAndZoom(dto.lon_dd, dto.lat_dd, 12)
            });

        // Feature ID로 DTO의 고유값 사용
        const featureId = isCluster 
            ? `cluster-rain-${dto.lat_dd}-${dto.lon_dd}` 
            : `rain-${dto.rfobscd}`;
            
        return createFeature(dto.lon_dd, dto.lat_dd, title, contents, iconUrl, clickCallback, featureId);
    }, { enableCluster: true });
}

// === 클러스터 확대 함수 ===
window.expandCluster = function(lon_dd, lat_dd, type) {
    const currentZoom = vmap.getView().getZoom();
    const newZoom = Math.min(currentZoom + 3, 18); // 최대 줌 레벨 18로 제한

    // 클러스터 위치로 이동하고 줌 인
    vmap.setCenterAndZoom(lon_dd, lat_dd, newZoom);
    
    // 잠시 후 마커를 다시 로드해서 클러스터가 분해되도록 함
    setTimeout(() => {
        if (type === 'bridge') addMarkerbridge();
        else if (type === 'dam') addMarkerdam();
        else if (type === 'rain') addMarkerPrecipitatione();
    }, 300);
};







// // === 마커 클릭 이벤트 유틸리티 함수 ===
// function findMarkerAtPixel(pixel) {
//     const threshold = 20; // 클릭 감지 반경 (픽셀)
//
//     // 모든 마커 레이어 검사
//     const layers = [
//         {layer: bridgeMarkerLayer, name: 'bridge'},
//         {layer: damMarkerLayer, name: 'dam'},
//         {layer: PrecipitationeMarkerLayer, name: 'rain'}
//     ];
//
//     for (const {layer, name} of layers) {
//         if (!layer) continue;
//
//         // VWorld 마커 레이어에서 마커 목록 가져오기
//         let markers = [];
//         if (typeof layer.getMarkers === 'function') {
//             markers = layer.getMarkers() || [];
//         } else if (typeof layer.getAllMarkers === 'function') {
//             markers = layer.getAllMarkers() || [];
//         }
//
//         // 각 마커와 클릭 지점의 거리 계산
//         for (const marker of markers) {
//             if (!marker || !marker.x || !marker.y) continue;
//
//             try {
//                 // 마커의 지리 좌표를 화면 픽셀로 변환
//                 const markerCoord = vmap.getView().getProjection().transform
//                     ? ol.proj.transform([marker.x, marker.y], 'EPSG:4326', vmap.getView().getProjection())
//                     : [marker.x, marker.y];
//
//                 const markerPixel = vmap.getPixelFromCoordinate(markerCoord);
//
//                 if (markerPixel) {
//                     const distance = Math.sqrt(
//                         Math.pow(markerPixel[0] - pixel[0], 2) +
//                         Math.pow(markerPixel[1] - pixel[1], 2)
//                     );
//
//                     if (distance <= threshold) {
//                         return {marker, layer, layerName: name};
//                     }
//                 }
//             } catch (error) {
//                 // 좌표 변환 실패 시 원본 좌표로 시도
//                 try {
//                     const markerPixel = vmap.getPixelFromCoordinate([marker.x, marker.y]);
//                     if (markerPixel) {
//                         const distance = Math.sqrt(
//                             Math.pow(markerPixel[0] - pixel[0], 2) +
//                             Math.pow(markerPixel[1] - pixel[1], 2)
//                         );
//
//                         if (distance <= threshold) {
//                             return {marker, layer, layerName: name};
//                         }
//                     }
//                 } catch (e) {
//                     console.debug(`마커 위치 계산 실패:`, e);
//                 }
//             }
//         }
//     }
//
//     return null;
// }

// // === 마커 클릭 이벤트 처리 ===
// vmap.on('singleclick', function(evt) {
//     const pixel = evt.pixel;
//     const markerInfo = findMarkerAtPixel(pixel);
//
//     if (markerInfo) {
//         const {marker} = markerInfo;
//         const attr = marker.attr || {};
//
//         console.log('마커 클릭됨:', attr); // 디버깅용
//
//         const isCluster = attr.isCluster === true;
//         const code = attr.markerCode;
//         const type = attr.markerType;
//
//         if (isCluster) {
//             // 클러스터인 경우 확대
//             console.log('클러스터 확대:', marker.x, marker.y, type);
//             expandCluster(marker.x, marker.y, type);
//         } else if (code && type) {
//             // 개별 마커인 경우 팝업 표시
//             console.log('마커 팝업 표시:', type, code);
//             if (type === 'bridge') {
//                 openPopupBridge(code);
//             } else if (type === 'dam') {
//                 openPopupdam(code);
//             } else if (type === 'rain') {
//                 openPopupPrecipitatione(code);
//             }
//         }
//     }
// });



// === 초기 실행 ===
addMarkerLayers();
addMarkerbridge();
addMarkerPrecipitatione();
addMarkerdam();

