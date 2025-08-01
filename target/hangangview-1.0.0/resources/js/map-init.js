// === 지도 및 상태 설정 ===
const initialCenter = [14137133.82, 4511912.58];
const initialZoom = 10;
let currentBasemapType = vw.ol3.BasemapType.GRAPHIC;

let isbridgeMarkerVisible = false;
let isDamMarkerVisible = false;
let isPrecipitationeMarkerVisible = false;
let isCctvMarkerVisible = false;
let areAllMarkersVisible = false;

// === 지도 객체 생성 ===
const vmap = new vw.ol3.Map("vmap", vw.ol3.MapOptions);
vmap.getView().setCenter(initialCenter);
vmap.getView().setZoom(initialZoom);

// === 마커 레이어 초기화 ===
const bridgeMarkerLayer = new vw.ol3.layer.Marker(vmap);
const PrecipitationeMarkerLayer = new vw.ol3.layer.Marker(vmap);
const damMarkerLayer = new vw.ol3.layer.Marker(vmap);
const cctvMarkerLayer = new vw.ol3.layer.Marker(vmap);

// === 마커 토글 함수 ===
function toggleMarker(layer, stateVar) {
    if (window[stateVar]) {
        layer.hideAllMarker();
        window[stateVar] = false;
    } else {
        layer.showAllMarker();
        window[stateVar] = true;
    }
}

function toggleAllMarkers() {
    areAllMarkersVisible ? hideAllMarkers() : showAllMarkers();
    areAllMarkersVisible = !areAllMarkersVisible;
}

// 수문 마커 토글 함수
function togglebridgeMarker() {
    if (isbridgeMarkerVisible) {
        hidebridgeMarker();
        isbridgeMarkerVisible = false;
    } else {
        showbridgeMarker();
        isbridgeMarkerVisible = true;
    }
}

// 댐 마커 토글 함수
function toggleDamMarker() {
    if (isDamMarkerVisible) {
        hidedamMarker();
        isDamMarkerVisible = false;
    } else {
        showdamMarker();
        isDamMarkerVisible = true;
    }
}

// 기상 마커 토글 함수
function togglePrecipitationeMarker() {
    if (isPrecipitationeMarkerVisible) {
        hidePrecipitationeMarker();
        isPrecipitationerMarkerVisible = false;
    } else {
        showPrecipitationeMarker();
        isPrecipitationeMarkerVisible = true;
    }
}

function hideAllMarkers() {
    bridgeMarkerLayer.hideAllMarker();
    damMarkerLayer.hideAllMarker();
    PrecipitationeMarkerLayer.hideAllMarker();
    cctvMarkerLayer.hideAllMarker();
    isbridgeMarkerVisible = isDamMarkerVisible = isPrecipitationeMarkerVisible = isCctvMarkerVisible = false;
}

function showAllMarkers() {
    bridgeMarkerLayer.showAllMarker();
    damMarkerLayer.showAllMarker();
    PrecipitationeMarkerLayer.showAllMarker();
    cctvMarkerLayer.showAllMarker();
    isbridgeMarkerVisible = isDamMarkerVisible = isPrecipitationeMarkerVisible = isCctvMarkerVisible = true;
}

// === 지도 타입 전환 ===
function toggleBasemap(targetType) {
    currentBasemapType = (currentBasemapType === targetType) ? vw.ol3.BasemapType.GRAPHIC : targetType;
    vmap.setBasemapType(currentBasemapType);
}

// === 백지도 토글 ===
function toggleMap() {
    if (currentBasemapType === vw.ol3.BasemapType.GRAPHIC) {
        vmap.setBasemapType(vw.ol3.BasemapType.GRAPHIC);
        currentBasemapType = vw.ol3.BasemapType.GRAPHIC;
    } else {
        vmap.setBasemapType(vw.ol3.BasemapType.GRAPHIC);
        currentBasemapType = vw.ol3.BasemapType.GRAPHIC;
    }
}

// === 백지도 토글 ===
function toggleWhiteMap() {
    if (currentBasemapType === vw.ol3.BasemapType.GRAPHIC_WHITE) {
        vmap.setBasemapType(vw.ol3.BasemapType.GRAPHIC);
        currentBasemapType = vw.ol3.BasemapType.GRAPHIC;
    } else {
        vmap.setBasemapType(vw.ol3.BasemapType.GRAPHIC_WHITE);
        currentBasemapType = vw.ol3.BasemapType.GRAPHIC_WHITE;
    }
}

// === 야간지도 토글 ===
function toggleNightMap() {
    if (currentBasemapType === vw.ol3.BasemapType.GRAPHIC_NIGHT) {
        vmap.setBasemapType(vw.ol3.BasemapType.GRAPHIC);
        currentBasemapType = vw.ol3.BasemapType.GRAPHIC;
    } else {
        vmap.setBasemapType(vw.ol3.BasemapType.GRAPHIC_NIGHT);
        currentBasemapType = vw.ol3.BasemapType.GRAPHIC_NIGHT;
    }
}

// === 항공사진 토글 ===
function toggleAerialMap() {
    if (currentBasemapType === vw.ol3.BasemapType.PHOTO) {
        vmap.setBasemapType(vw.ol3.BasemapType.GRAPHIC);
        currentBasemapType = vw.ol3.BasemapType.GRAPHIC;
    } else {
        vmap.setBasemapType(vw.ol3.BasemapType.PHOTO);
        currentBasemapType = vw.ol3.BasemapType.PHOTO;
    }
}

// === 하이브리드 토글 ===
function toggleHybridMap() {
    if (currentBasemapType === vw.ol3.BasemapType.PHOTO_HYBRID) {
        vmap.setBasemapType(vw.ol3.BasemapType.GRAPHIC);
        currentBasemapType = vw.ol3.BasemapType.GRAPHIC;
    } else {
        vmap.setBasemapType(vw.ol3.BasemapType.PHOTO_HYBRID);
        currentBasemapType = vw.ol3.BasemapType.PHOTO_HYBRID;
    }
}

// === 팝업/선택 마커 ===
function showPopup() {
    [bridgeMarkerLayer, PrecipitationeMarkerLayer, damMarkerLayer, cctvMarkerLayer].forEach(layer => {
        layer.showPop(selectMarker);
    });
}

function hidePopup() {
    markerLayer.hidePop(selectMarker);
}

function hideMarker() {
    markerLayer.hideMarker(selectMarker);
}

function showMarker() {
    [bridgeMarkerLayer, PrecipitationeMarkerLayer, damMarkerLayer, cctvMarkerLayer].forEach(layer => {
        layer.showMarker(selectMarker);
    });
    $('#param').val('');
}


// === 팝업 이동 ===
function openPopup(url, code) {
    window.open(url + code, "_blank", "width=1200, height=600");
}

// === 마커 레이어 추가 ===
function addMarkerLayers() {
    vmap.addLayer(bridgeMarkerLayer);
    vmap.addLayer(PrecipitationeMarkerLayer);
    vmap.addLayer(damMarkerLayer);
    vmap.addLayer(cctvMarkerLayer);
}

// (다리)수위 마커를 추가하는 함수
function addMarkerbridge() {
    // console.log("브릿지 마커 함수 실행됨");
    fetch('/hangang/api/bridge')
        .then(response => response.json())
        .then(data => {
            // console.log("받은 다리 데이터:", data);
            data.forEach(dto => {
                const marker = {
                    x: dto.lon,
                    y: dto.lat,
                    epsg: "EPSG:4326",
                    title: `<a href="javascript:openPopupBridge('${dto.dmobscd}');">${dto.obsnm}</a>`,
                    contents: `해발 고도: ${dto.gdt}<br>` + `경보 수위: ${dto.attwl}<br>` + `경고 수위: ${dto.wrnwl}<br>` + `주의 수위: ${dto.almwl}<br>` + `안전 수위: ${dto.srswl}<br>` + `최고 수위: ${dto.pfh}<br>` + `홍수 위험 예고: ${dto.fstnyn}`,
                    iconUrl: '//img.icons8.com/ios-filled/50/bridge.png',
                    text: {
                        offsetX: 0.5,
                        offsetY: 20,
                        font: '12px Calibri, sans-serif',
                        fill: {color: '#000'},
                        stroke: {color: '#fff', width: 2}
                    },
                    attr: {id: 'maker01', name: '속성명1'}
                };
                // console.log(marker)
                bridgeMarkerLayer.addMarker(marker);
            });
        })
        .catch(error => {
            console.error('마커 데이터 로딩 실패:', error);
        });
    // console.log(marker)
}


function addMarkerPrecipitatione() {
    fetch('/hangang/api/precipitatione')
        .then(response => response.json())
        .then(data => {
            // console.log("받은 강수 데이터:", data);
            data.forEach(dto => {
                const marker = {
                    x: dto.lon,
                    y: dto.lat,
                    epsg: "EPSG:4326",
                    title: `<a href="javascript:openPopupPrecipitatione('${dto.rfobscd}');">${dto.obsnm}</a>`,
                    contents: `${dto.obsnm}`,
                    iconUrl: '//img.icons8.com/ios/100/rain-sensor--v2.png',
                    text: {
                        offsetX: 0.5,
                        offsetY: 20,
                        font: '12px Calibri, sans-serif',
                        fill: {color: '#000'},
                        stroke: {color: '#fff', width: 2}
                    },
                    attr: {id: 'maker01', name: '속성명1'}
                };
                PrecipitationeMarkerLayer.addMarker(marker);
            });
        })
        .catch(error => {
            console.error('강수량 마커 데이터 로딩 실패:', error);
        });
}

function addMarkerdam() {
    fetch('/hangang/api/dam')
        .then(response => response.json())
        .then(data => {
            // console.log("받은 댐 데이터:", data);
            data.forEach(dto => {
                const marker = {
                    x: dto.lon,
                    y: dto.lat,
                    epsg: "EPSG:4326",
                    title: `<a href="javascript:openPopupDam('${dto.dmobscd}');">${dto.obsnm}</a>`,
                    contents:  `홍수 우려 수위: ${dto.pfh}` +
                        `<br>수위 제한선: ${dto.fldlmtwl}`,

                    iconUrl: '//img.icons8.com/ios-filled/100/dam.png',
                    text: {
                        offsetX: 0.5,
                        offsetY: 20,
                        font: '12px Calibri, sans-serif',
                        fill: {color: '#000'},
                        stroke: {color: '#fff', width: 2},
                    },
                    attr: {id: 'maker01', name: '속성명1'}
                };
                // console.log(marker)
                damMarkerLayer.addMarker(marker);
            });
        })
        .catch(error => {
            console.error('마커 데이터 로딩 실패:', error);
        });
    // console.log(marker)
}

//
// function addMarkercctv() {
//     var dtoList = new Array();
//     <c:forEach var="dto" items="${cctvs}">
//         // 위에서 'list'를 사용하려 했으나, 'dto'를 사용해야 합니다.
//         dtoList.push({
//         x: ${dto.lon},
//         y: ${dto.lat},
//         epsg: "EPSG:4326",
//         title: "<a href='javascript:openPopupcctv(${dto.wlobscd});'>${dto.etcaddr}</a>",
//         contents:
//         "<br>주소: ${dto.addr}",
//         iconUrl: '//img.icons8.com/ios-filled/50/video-call.png',
//         text: {
//         offsetX: 0.5,
//         offsetY: 20,
//         font: '12px Calibri, sans-serif',
//         fill: {color: '#000'},
//         stroke: {color: '#fff', width: 2},
//     },
//         attr: {"id": "maker01", "name": "속성명1"}
//     });
//     </c:forEach>
//
//     dtoList.forEach(function (dto) {
//         cctvMarkerLayer.addMarker(dto);
//     });
// }

// === 지도 클릭 이벤트 ===
// vmap.on('click', function (evt) {
//     const feature = vmap.forEachFeatureAtPixel(evt.pixel, (feature) => {
//         $('#param').val(feature.get('id'));
//         selectMarker = feature;
//         showPopup();
//         const [y, x] = evt.coordinate.map(coord => parseFloat(coord));
//         vmap.getView().setCenter([x, y]);
//         vmap.getView().setZoom(12);
//     });
// });
function showPopup() {
    this.bridgeMarkerLayer.showPop(selectMarker);
    this.PrecipitationeMarkerLayer.showPop(selectMarker);
    this.damMarkerLayer.showPop(selectMarker);
    this.cctvMarkerLayer.showPop(selectMarker);
}

function hidePopup() {
    this.markerLayer.hidePop(selectMarker);
}

function hideMarker() {
    this.markerLayer.hideMarker(selectMarker);
}

// 마커를 보여주는 함수
function showMarker() {
    this.bridgeMarkerLayer.showMarker(selectMarker);
    this.PrecipitationeMarkerLayer.showMarker(selectMarker);
    this.damMarkerLayer.showMarker(selectMarker);
    this.cctvMarkerLayer.showMarker(selectMarker);
    // #param 요소의 값을 초기화하여 선택된 마커 없음을 나타냄
    $('#param').val('');

}

function isSelectMarker() {
    true;
}

// === 초기 실행 ===
addMarkerLayers();
addMarkerbridge();
addMarkerPrecipitatione();
addMarkerdam();

// addMarkercctv();
