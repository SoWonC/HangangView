// === 지도 설정 ===
const initialCenter = [14137133.82, 4511912.58];
const initialZoom = 9;
let currentBasemapType = vw.ol3.BasemapType.GRAPHIC;

let isbridgeMarkerVisible = false;
let isDamMarkerVisible = false;
let isPrecipitationeMarkerVisible = false;
let isCctvMarkerVisible = false;
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

// 지도 클릭 이벤트로 마커 클릭 감지
vmap.on('singleclick', function(evt) {
    const pixel = evt.pixel;
    let foundMarker = null;
    
    // 각 마커 레이어에서 클릭된 마커 찾기
    const layers = [
        { layer: bridgeMarkerLayer, type: 'bridge' },
        { layer: damMarkerLayer, type: 'dam' },
        { layer: PrecipitationeMarkerLayer, type: 'rain' }
    ];
    
    for (const layerInfo of layers) {
        if (foundMarker) break;
        
        try {
            const markers = layerInfo.layer.getMarkers ? layerInfo.layer.getMarkers() : [];
            
            for (const marker of markers) {
                // 마커 좌표를 화면 좌표로 변환
                const coord = ol.proj.transform([marker.x, marker.y], 'EPSG:4326', vmap.getView().getProjection());
                const markerPixel = vmap.getPixelFromCoordinate(coord);
                
                if (markerPixel) {
                    const distance = Math.sqrt(
                        Math.pow(pixel[0] - markerPixel[0], 2) + 
                        Math.pow(pixel[1] - markerPixel[1], 2)
                    );
                    
                    // 25픽셀 반경 내에서 클릭 감지
                    if (distance <= 25) {
                        foundMarker = { marker, type: layerInfo.type };
                        break;
                    }
                }
            }
        } catch (e) {
            console.log('마커 검사 오류:', e);
        }
    }
    
    // 마커가 감지되면 바로 카드 표시
    if (foundMarker && foundMarker.marker.attr) {
        const markerType = foundMarker.marker.attr.markerType || foundMarker.type;
        const markerCode = foundMarker.marker.attr.markerCode;
        const isCluster = foundMarker.marker.attr.isCluster;
        
        if (isCluster) {
            expandCluster(foundMarker.marker.x, foundMarker.marker.y, markerType);
        } else {
            // 바로 카드 표시 함수 호출
            if (markerType === 'bridge' && markerCode) {
                openPopupBridge(markerCode);
            } else if (markerType === 'dam' && markerCode) {
                openPopupdam(markerCode);
            } else if (markerType === 'rain' && markerCode) {
                openPopupPrecipitatione(markerCode);
            }
        }
        
        // 기본 팝업 표시 방지
        evt.stopPropagation();
        return false;
    }
});


const bridgeMarkerLayer = new vw.ol3.layer.Marker(vmap);
const PrecipitationeMarkerLayer = new vw.ol3.layer.Marker(vmap);
const damMarkerLayer = new vw.ol3.layer.Marker(vmap);
const cctvMarkerLayer = new vw.ol3.layer.Marker(vmap);

function addMarkerLayers() {
    vmap.addLayer(bridgeMarkerLayer);
    vmap.addLayer(PrecipitationeMarkerLayer);
    vmap.addLayer(damMarkerLayer);
    vmap.addLayer(cctvMarkerLayer);
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



// === 공통 마커 생성 함수 ===
function createMarker(x, y, title, contents, iconUrl, markerData = {}) {
    const marker = {
        x, y,
        epsg: "EPSG:4326",
        title: markerData.displayName || title,
        contents,

        iconUrl,
        text: {
            offsetX: 0.5,
            offsetY: 20,
            font: '12px Calibri, sans-serif',
            fill: {color: '#000'},
            stroke: {color: '#fff', width: 2}
        },
        attr: {
            id: markerData.id || 'marker',
            name: markerData.name || '마커',
            markerType: markerData.type || 'unknown',
            markerCode: markerData.code || '',
            isCluster: markerData.isCluster || false,
            members: markerData.members || null
        },
        // 마커 클릭 시 바로 카드 표시
        clickEvent: function() {
            const markerType = this.attr.markerType;
            const markerCode = this.attr.markerCode;
            const isCluster = this.attr.isCluster;
            
            if (isCluster) {
                expandCluster(this.x, this.y, markerType);
            } else {
                if (markerType === 'bridge' && markerCode) {
                    openPopupBridge(markerCode);
                } else if (markerType === 'dam' && markerCode) {
                    openPopupdam(markerCode);
                } else if (markerType === 'rain' && markerCode) {
                    openPopupPrecipitatione(markerCode);
                }
            }
        }
    };
    
    return marker;
}

// VWorld MarkerLayer 대응형 loadMarkers (방어적)
window.loadMarkers = async function(url, layer, mapDtoToMarker, options = {}) {
    try {
        const res = await fetch(url, { cache: 'no-store' });
        if (!res.ok) throw new Error(`HTTP ${res.status} ${res.statusText}`);
        const list = await res.json();
        if (!Array.isArray(list)) throw new Error('JSON 배열이 아닙니다.');

        // 1) 기존 마커 비우기 (레이어별 메서드 차이 방어)
        if (layer) {
            if (typeof layer.removeAllMarker === 'function') layer.removeAllMarker();
            else if (typeof layer.clearMarkers === 'function') layer.clearMarkers();
            else if (typeof layer.clearAll === 'function') layer.clearAll();
            else if (typeof layer.clear === 'function') layer.clear();
            // 없으면 스킵
        }

        // 2) 클러스터링 적용 (활성화 시)
        let processedData = list;
        if (options.enableCluster) {
            processedData = clusterMarkers(list);
        }

        // 3) 마커 생성 & 추가
        const batch = [];
        for (const dto of processedData) {
            const marker = mapDtoToMarker(dto);   // addMarkerbridge() 안의 return createMarker(...)
            if (!marker) continue;
            batch.push(marker);

            if (layer) {
                if (typeof layer.addMarker === 'function') layer.addMarker(marker);
                else if (typeof layer.add === 'function') layer.add(marker);
                // addMarkers 일괄 추가형은 아래에서 처리
            }
        }

        // 레이어가 일괄 추가를 지원하면 한 번에 추가
        if (layer && typeof layer.addMarkers === 'function' && batch.length) {
            layer.addMarkers(batch);
        }

        // 4) VWorld 자체 클러스터 옵션(지원할 때만)
        if (options.enableCluster && layer && typeof layer.setCluster === 'function') {
            layer.setCluster(true);
        }

    } catch (e) {
        console.error('마커 로드 실패:', e);
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
    loadMarkers(`${CTX}api/bridge`, bridgeMarkerLayer, (dto) => {
        const isCluster = dto.isCluster === true;

        const title = isCluster
            ? `<a href="javascript:expandCluster(${dto.lon_dd}, ${dto.lat_dd}, 'bridge');">${dto.obsnm} (클릭하여 확대)</a>`
            : `<a href="javascript:openPopupBridge('${dto.wlobscd}');">${dto.obsnm}</a>`;

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

        return createMarker(dto.lon_dd, dto.lat_dd, title, contents, iconUrl);
    }, { enableCluster: true });
}

/* ===== 댐 마커 ===== */
function addMarkerdam() {
    loadMarkers(`${CTX}api/dam`, damMarkerLayer, (dto) => {
        const isCluster = dto.isCluster === true;

        const title = isCluster
            ? `<a href="javascript:expandCluster(${dto.lon_dd}, ${dto.lat_dd}, 'dam');">${dto.obsnm} (클릭하여 확대)</a>`
            : `<a href="javascript:openPopupdam('${dto.dmobscd}');">${dto.obsnm}</a>`;

        const contents = isCluster
            ? `포함된 시설: ${dto.members.map(d => d.obsnm).join(', ')}`
            : `홍수 우려 수위: ${dto.pfh}<br>수위 제한선: ${dto.fldlmtwl}`;

        const iconUrl = isCluster ? ICONS.cluster : ICONS.dam;

        return createMarker(dto.lon_dd, dto.lat_dd, title, contents, iconUrl);
    }, { enableCluster: true });
}

/* ===== 강우 마커 ===== */
function addMarkerPrecipitatione() {
    loadMarkers(`${CTX}api/precipitatione`, PrecipitationeMarkerLayer, (dto) => {
        const isCluster = dto.isCluster === true;

        const title = isCluster
            ? `<a href="javascript:expandCluster(${dto.lon_dd}, ${dto.lat_dd}, 'rain');">${dto.obsnm} (클릭하여 확대)</a>`
            : `<a href="javascript:openPopupPrecipitatione('${dto.rfobscd}');">${dto.obsnm}</a>`;

        const contents = isCluster
            ? `포함된 시설: ${dto.members.map(d => d.obsnm).join(', ')}`
            : `${dto.obsnm}`;

        const iconUrl = isCluster ? ICONS.cluster : ICONS.rain;

        return createMarker(dto.lon_dd, dto.lat_dd, title, contents, iconUrl);
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


// === 마커 클릭 이벤트 유틸리티 함수 ===
function findMarkerAtPixel(pixel) {
    const threshold = 20; // 클릭 감지 반경 (픽셀)
    
    // 모든 마커 레이어 검사
    const layers = [
        {layer: bridgeMarkerLayer, name: 'bridge'},
        {layer: damMarkerLayer, name: 'dam'},
        {layer: PrecipitationeMarkerLayer, name: 'rain'}
    ];
    
    for (const {layer, name} of layers) {
        if (!layer) continue;
        
        // VWorld 마커 레이어에서 마커 목록 가져오기
        let markers = [];
        if (typeof layer.getMarkers === 'function') {
            markers = layer.getMarkers() || [];
        } else if (typeof layer.getAllMarkers === 'function') {
            markers = layer.getAllMarkers() || [];
        }
        
        // 각 마커와 클릭 지점의 거리 계산
        for (const marker of markers) {
            if (!marker || !marker.x || !marker.y) continue;
            
            try {
                // 마커의 지리 좌표를 화면 픽셀로 변환
                const markerCoord = vmap.getView().getProjection().transform 
                    ? ol.proj.transform([marker.x, marker.y], 'EPSG:4326', vmap.getView().getProjection())
                    : [marker.x, marker.y];
                    
                const markerPixel = vmap.getPixelFromCoordinate(markerCoord);
                
                if (markerPixel) {
                    const distance = Math.sqrt(
                        Math.pow(markerPixel[0] - pixel[0], 2) + 
                        Math.pow(markerPixel[1] - pixel[1], 2)
                    );
                    
                    if (distance <= threshold) {
                        return {marker, layer, layerName: name};
                    }
                }
            } catch (error) {
                // 좌표 변환 실패 시 원본 좌표로 시도
                try {
                    const markerPixel = vmap.getPixelFromCoordinate([marker.x, marker.y]);
                    if (markerPixel) {
                        const distance = Math.sqrt(
                            Math.pow(markerPixel[0] - pixel[0], 2) + 
                            Math.pow(markerPixel[1] - pixel[1], 2)
                        );
                        
                        if (distance <= threshold) {
                            return {marker, layer, layerName: name};
                        }
                    }
                } catch (e) {
                    console.debug(`마커 위치 계산 실패:`, e);
                }
            }
        }
    }
    
    return null;
}

// === 마커 클릭 이벤트 처리 ===
vmap.on('singleclick', function(evt) {
    const pixel = evt.pixel;
    const markerInfo = findMarkerAtPixel(pixel);
    
    if (markerInfo) {
        const {marker} = markerInfo;
        const attr = marker.attr || {};
        
        console.log('마커 클릭됨:', attr); // 디버깅용
        
        const isCluster = attr.isCluster === true;
        const code = attr.markerCode;
        const type = attr.markerType;
        
        if (isCluster) {
            // 클러스터인 경우 확대
            console.log('클러스터 확대:', marker.x, marker.y, type);
            expandCluster(marker.x, marker.y, type);
        } else if (code && type) {
            // 개별 마커인 경우 팝업 표시
            console.log('마커 팝업 표시:', type, code);
            if (type === 'bridge') {
                openPopupBridge(code);
            } else if (type === 'dam') {
                openPopupdam(code);
            } else if (type === 'rain') {
                openPopupPrecipitatione(code);
            }
        }
    }
});

// === 초기 실행 ===
addMarkerLayers();
addMarkerbridge();
addMarkerPrecipitatione();
addMarkerdam();

console.log('✅ VWorld 마커 클릭 이벤트 핸들러가 활성화되었습니다.');
console.log('📍 마커를 클릭하면 직접 카드가 표시됩니다 (HTML 링크 방식 대신).');

