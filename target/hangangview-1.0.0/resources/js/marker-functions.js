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

function togglebridgeMarker(checkbox) {
    if (checkbox.checked) {
        bridgeMarkerLayer.showAllMarker();
        isbridgeMarkerVisible = true;
    } else {
        bridgeMarkerLayer.hideAllMarker();
        isbridgeMarkerVisible = false;
    }
}

function toggleDamMarker(checkbox) {
    if (checkbox.checked) {
        damMarkerLayer.showAllMarker();
        isDamMarkerVisible = true;
    } else {
        damMarkerLayer.hideAllMarker();
        isDamMarkerVisible = false;
    }
}

function togglePrecipitationeMarker(checkbox) {
    if (checkbox.checked) {
        PrecipitationeMarkerLayer.showAllMarker();
        isPrecipitationeMarkerVisible = true;
    } else {
        PrecipitationeMarkerLayer.hideAllMarker();
        isPrecipitationeMarkerVisible = false;
    }
}

function toggleCctvMarker(checkbox) {
    if (checkbox.checked) {
        cctvMarkerLayer.showAllMarker();
        isCctvMarkerVisible = true;
    } else {
        cctvMarkerLayer.hideAllMarker();
        isCctvMarkerVisible = false;
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