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
// function showPopup() {
//     [bridgeMarkerLayer, PrecipitationeMarkerLayer, damMarkerLayer].forEach(layer => {
//         layer.showPop(selectMarker);
//     });
// }
//
// function hidePopup() {
//     markerLayer.hidePop(selectMarker);
// }
//
// function hideMarker() {
//     markerLayer.hideMarker(selectMarker);
// }
//
// function showMarker() {
//     [bridgeMarkerLayer, PrecipitationeMarkerLayer, damMarkerLayer, cctvMarkerLayer].forEach(layer => {
//         layer.showMarker(selectMarker);
//     });
//     $('#param').val('');
// }
//
//
// // === 팝업 이동 ===
// function openPopup(url, code) {
//     window.open(url + code, "_blank", "width=1200, height=600");
// }
//
//
// function showPopup() {
//     this.bridgeMarkerLayer.showPop(selectMarker);
//     this.PrecipitationeMarkerLayer.showPop(selectMarker);
//     this.damMarkerLayer.showPop(selectMarker);
//     this.cctvMarkerLayer.showPop(selectMarker);
// }
//
// function hidePopup() {
//     this.markerLayer.hidePop(selectMarker);
// }
//
// function hideMarker() {
//     this.markerLayer.hideMarker(selectMarker);
// }
//
// // 마커를 보여주는 함수
// function showMarker() {
//     this.bridgeMarkerLayer.showMarker(selectMarker);
//     this.PrecipitationeMarkerLayer.showMarker(selectMarker);
//     this.damMarkerLayer.showMarker(selectMarker);
//     this.cctvMarkerLayer.showMarker(selectMarker);
//     // #param 요소의 값을 초기화하여 선택된 마커 없음을 나타냄
//     $('#param').val('');
//
// }
//
// function openPopupBridge(asd) {
//     window.open("http://localhost:8081/hangang/api/openpopupbridge?wlobscd=" + asd, "_blank", "width=1200, height=600");
// }
// function openPopupdam(asd) {
//     window.open("http://localhost:8081/hangang/api/openpopupdam?dmobscd=" + asd, "_blank", "width=1200, height=600");
// }
// function openPopupPrecipitatione(asd) {
//     window.open("http://localhost:8081/hangang/api/openpopupprecipitatione?rfobscd=" + asd, "_blank", "width=1200, height=600");
// }
//
//
// function isSelectMarker() {
//     true;
// }

// ===== Marker Info Card API =====
window.showMarkerInfo = function(data){
    const $card = document.getElementById('markerInfoCard');
    const setText = (id, val) => (document.getElementById(id).textContent = val ?? '-');

    setText('mic-title', data.name);
    setText('mic-subtitle', data.subtitle || (data.type ? `#${data.type}` : ''));
    setText('mic-level', Number.isFinite(data.waterLevel) ? `${data.waterLevel} m` : (data.waterLevel ?? '-'));
    setText('mic-updated', data.updatedAt || '-');

    const coordTxt = (data.coord && (data.coord.lon!=null) && (data.coord.lat!=null))
        ? `${Number(data.coord.lon).toFixed(6)}, ${Number(data.coord.lat).toFixed(6)}`
        : '-';
    setText('mic-coord', coordTxt);

    const stageEl = document.getElementById('mic-stage');
    const stage = (data.stage || '-').trim();
    stageEl.textContent = stage;
    stageEl.className = 'badge rounded-pill ' + ({
        '정상': 'text-bg-success',
        '주의': 'text-bg-primary',
        '경고': 'text-bg-warning',
        '위험': 'text-bg-danger'
    }[stage] || 'text-bg-secondary');

    const zoomBtn = document.getElementById('mic-zoomBtn');
    zoomBtn.onclick = () => {
        if (typeof data.zoomTo === 'function') return data.zoomTo();
        if (window.vmap && data.coord) {
            // VWorld는 위경도 기반 이동 API를 제공 (초기화에서 vmap 사용 중) :contentReference[oaicite:2]{index=2}
            vmap.setCenterAndZoom(data.coord.lon, data.coord.lat, 12);
        }
    };

    $card.classList.remove('d-none');
};

window.hideMarkerInfo = function(){
    document.getElementById('markerInfoCard').classList.add('d-none');
};

window.addEventListener('keydown', (e) => { if (e.key === 'Escape') hideMarkerInfo(); });

/** 유틸: 배열 API에서 코드로 하나 찾기 */
function findBy(arr, key, val){ return arr.find(d => String(d[key]) === String(val)); }

/** 기존 팝업 오픈 함수를 카드로 치환 */
window.openPopupBridge = async function(wlobscd){
    // 목록 API를 재사용해 해당 코드의 DTO를 찾는다 (현재 마커도 이 API에서 만들어짐) :contentReference[oaicite:3]{index=3}
    const res = await fetch('/hangang/api/bridge');
    const list = await res.json();
    const dto = findBy(list, 'wlobscd', wlobscd);
    if (!dto) return alert('지점을 찾을 수 없습니다.');

    showMarkerInfo({
        id: `bridge-${dto.wlobscd}`,
        type: 'bridge',
        name: dto.obsnm,
        subtitle: dto.city || '한강',
        waterLevel: dto.pfh ?? dto.gdt, // 가용 필드로 임시 노출
        stage: dto.stage || '-',        // 서버에 단계 필드가 있으면 매핑
        coord: { lon: dto.lon, lat: dto.lat },
        updatedAt: dto.updatedAt,
        zoomTo: () => vmap.setCenterAndZoom(dto.lon, dto.lat, 12)
    });
};

window.openPopupdam = async function(dmobscd){
    const res = await fetch('/hangang/api/dam');
    const list = await res.json();
    const dto = findBy(list, 'dmobscd', dmobscd);
    if (!dto) return alert('지점을 찾을 수 없습니다.');

    showMarkerInfo({
        id: `dam-${dto.dmobscd}`,
        type: 'dam',
        name: dto.obsnm,
        subtitle: dto.city || '댐',
        waterLevel: dto.fldlmtwl ?? dto.pfh,
        stage: dto.stage || '-',
        coord: { lon: dto.lon, lat: dto.lat },
        updatedAt: dto.updatedAt,
        zoomTo: () => vmap.setCenterAndZoom(dto.lon, dto.lat, 12)
    });
};

window.openPopupPrecipitatione = async function(rfobscd){
    const res = await fetch('/hangang/api/precipitatione');
    const list = await res.json();
    const dto = findBy(list, 'rfobscd', rfobscd);
    if (!dto) return alert('지점을 찾을 수 없습니다.');

    showMarkerInfo({
        id: `rain-${dto.rfobscd}`,
        type: 'rain',
        name: dto.obsnm,
        subtitle: dto.city || '강수',
        waterLevel: dto.value ?? dto.rain ?? '-', // 가용 필드로 임시 노출
        stage: dto.stage || '-',
        coord: { lon: dto.lon, lat: dto.lat },
        updatedAt: dto.updatedAt,
        zoomTo: () => vmap.setCenterAndZoom(dto.lon, dto.lat, 12)
    });
};
