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

// function clusterMarkers(data) {
//     const zoom = vmap.getView().getZoom();
//
//     // 줌 레벨별 클러스터 간격 설정
//     let gridSize;
//     if (zoom < 7) gridSize = 2.0;        // 전국 단위 1~2개 클러스터
//     else if (zoom < 8) gridSize = 1.0;   // 도 단위
//     else if (zoom < 10) gridSize = 0.5;  // 시/군 단위
//     else if (zoom < 12) gridSize = 0.1;  // 읍/면 단위
//     else gridSize = 0.02;                // 상세 마커
//
//     const clusters = {};
//     data.forEach(dto => {
//         const key = `${Math.floor(dto.lat / gridSize)}_${Math.floor(dto.lon / gridSize)}`;
//         if (!clusters[key]) clusters[key] = [];
//         clusters[key].push(dto);
//     });
//
//     return Object.values(clusters).map(group => {
//         const avgLat = group.reduce((sum, d) => sum + d.lat, 0) / group.length;
//         const avgLon = group.reduce((sum, d) => sum + d.lon, 0) / group.length;
//
//         return {
//             lon: avgLon,
//             lat: avgLat,
//             obsnm: `${group.length}개 시설`,
//             isCluster: true,
//             members: group
//         };
//     });
// }


// === 공통 마커 생성 함수 ===
function createMarker(x, y, title, contents, iconUrl) {
    return {
        x, y,
        epsg: "EPSG:4326",
        title,
        contents,

        iconUrl,
        text: {
            offsetX: 0.5,
            offsetY: 20,
            font: '12px Calibri, sans-serif',
            fill: {color: '#000'},
            stroke: {color: '#fff', width: 2}
        },
        attr: {id: 'maker01', name: '속성명1'}
    };
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

        // 2) 마커 생성 & 추가
        const batch = [];
        for (const dto of list) {
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

        // 3) 클러스터 옵션(지원할 때만)
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
            ? `<span>${dto.obsnm}</span>`
            : `<a href="javascript:openPopupBridge('${dto.wlobscd}');">${dto.obsnm}</a>`;

        const contents = isCluster
            ? dto.members.map(d => d.obsnm).join('<br>')
            : `해발 고도: ${dto.gdt}<br>
         경보 수위: ${dto.attwl}<br>
         경고 수위: ${dto.wrnwl}<br>
         주의 수위: ${dto.almwl}<br>
         안전 수위: ${dto.srswl}<br>
         최고 수위: ${dto.pfh}<br>
         홍수 위험 예고: ${dto.fstnyn}`;

        const iconUrl = isCluster ? ICONS.bridge : ICONS.bridge; // 원하면 클러스터는 ICONS.cluster로

        return createMarker(dto.lon, dto.lat, title, contents, iconUrl);
    }, { enableCluster: true });
}

/* ===== 댐 마커 ===== */
function addMarkerdam() {
    loadMarkers(`${CTX}api/dam`, damMarkerLayer, (dto) => {
        const isCluster = dto.isCluster === true;

        const title = isCluster
            ? `<span>${dto.obsnm}</span>`
            : `<a href="javascript:openPopupdam('${dto.dmobscd}');">${dto.obsnm}</a>`;

        const contents = isCluster
            ? dto.members.map(d => d.obsnm).join('<br>')
            : `홍수 우려 수위: ${dto.pfh}<br>수위 제한선: ${dto.fldlmtwl}`;

        const iconUrl = isCluster ? ICONS.dam : ICONS.dam;

        return createMarker(dto.lon, dto.lat, title, contents, iconUrl);
    }, { enableCluster: true });
}

/* ===== 강우 마커 ===== */
function addMarkerPrecipitatione() {
    loadMarkers(`${CTX}api/precipitatione`, PrecipitationeMarkerLayer, (dto) => {
        const isCluster = dto.isCluster === true;

        const title = isCluster
            ? `<span>${dto.obsnm}</span>`
            : `<a href="javascript:openPopupPrecipitatione('${dto.rfobscd}');">${dto.obsnm}</a>`;

        const contents = isCluster
            ? dto.members.map(d => d.obsnm).join('<br>')
            : `${dto.obsnm}`;

        const iconUrl = isCluster ? ICONS.rain : ICONS.rain;

        return createMarker(dto.lon, dto.lat, title, contents, iconUrl);
    }, { enableCluster: true });
}


// === 초기 실행 ===
addMarkerLayers();
addMarkerbridge();
addMarkerPrecipitatione();
addMarkerdam();

