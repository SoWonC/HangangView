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

function clusterMarkers(data) {
    const zoom = vmap.getView().getZoom();

    // 줌 레벨별 클러스터 간격 설정
    let gridSize;
    if (zoom < 7) gridSize = 2.0;        // 전국 단위 1~2개 클러스터
    else if (zoom < 8) gridSize = 1.0;   // 도 단위
    else if (zoom < 10) gridSize = 0.5;  // 시/군 단위
    else if (zoom < 12) gridSize = 0.1;  // 읍/면 단위
    else gridSize = 0.02;                // 상세 마커

    const clusters = {};
    data.forEach(dto => {
        const key = `${Math.floor(dto.lat / gridSize)}_${Math.floor(dto.lon / gridSize)}`;
        if (!clusters[key]) clusters[key] = [];
        clusters[key].push(dto);
    });

    return Object.values(clusters).map(group => {
        const avgLat = group.reduce((sum, d) => sum + d.lat, 0) / group.length;
        const avgLon = group.reduce((sum, d) => sum + d.lon, 0) / group.length;

        return {
            lon: avgLon,
            lat: avgLat,
            obsnm: `${group.length}개 시설`,
            isCluster: true,
            members: group
        };
    });
}



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
            fill: { color: '#000' },
            stroke: { color: '#fff', width: 2 }
        },
        attr: { id: 'maker01', name: '속성명1' }
    };
}

// === 공통 fetch 마커 로딩 함수 ===
function loadMarkers(url, layer, dtoToMarker, options = {}) {
    const { enableCluster = false, clusterZoom = 11 } = options;

    fetch(url)
        .then(res => res.json())
        .then(data => {
            layer.removeAllMarker();

            const zoom = vmap.getView().getZoom();
            const source = (enableCluster && zoom < clusterZoom) ? clusterMarkers(data) : data;

            source.forEach(dto => {
                const marker = dtoToMarker(dto);
                layer.addMarker(marker);
            });
        })
        .catch(err => console.error('마커 로딩 실패:', err));
}

function addMarkerbridge() {
    loadMarkers('/hangang/api/bridge', bridgeMarkerLayer, dto => {

        console.log(dto.wlobscd)
        const isCluster = dto.isCluster;


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

        return createMarker(dto.lon, dto.lat, title, contents, '//img.icons8.com/ios-filled/50/bridge.png');
    }, { enableCluster: true });
}

function addMarkerdam() {
    loadMarkers('/hangang/api/dam', damMarkerLayer, dto => {
        const isCluster = dto.isCluster;

        const title = isCluster
            ? `<span>${dto.obsnm}</span>`
            : `<a href="javascript:openPopupdam('${dto.dmobscd}');">${dto.obsnm}</a>`;

        const contents = isCluster
            ? dto.members.map(d => d.obsnm).join('<br>')
            : `홍수 우려 수위: ${dto.pfh}<br>수위 제한선: ${dto.fldlmtwl}`;

        const iconUrl = isCluster
            ? '//img.icons8.com/emoji/48/large-blue-circle.png'
            : '//img.icons8.com/ios-filled/100/dam.png';

        return createMarker(dto.lon, dto.lat, title, contents, iconUrl);
    }, { enableCluster: true });
}


function addMarkerPrecipitatione() {
    loadMarkers('/hangang/api/precipitatione', PrecipitationeMarkerLayer, dto => {
        const isCluster = dto.isCluster;

        const title = isCluster
            ? `<span>${dto.obsnm}</span>`
            : `<a href="javascript:openPopupPrecipitatione('${dto.rfobscd}');">${dto.obsnm}</a>`;

        const contents = isCluster
            ? dto.members.map(d => d.obsnm).join('<br>')
            : `${dto.obsnm}`;

        const iconUrl = isCluster
            ? '//img.icons8.com/emoji/48/large-green-circle.png'
            : '//img.icons8.com/ios/100/rain-sensor--v2.png';

        return createMarker(dto.lon, dto.lat, title, contents, iconUrl);
    }, { enableCluster: true });
}


// === 초기 실행 ===
addMarkerLayers();
addMarkerbridge();
addMarkerPrecipitatione();
addMarkerdam();

