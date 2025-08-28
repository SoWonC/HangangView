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
    // Vector 레이어들 숨기기
    bridgeVectorLayer.setVisible(false);
    damVectorLayer.setVisible(false);
    precipitationeVectorLayer.setVisible(false);
    isbridgeMarkerVisible = isDamMarkerVisible = isPrecipitationeMarkerVisible = false;
}

function showAllMarkers() {
    // Vector 레이어들 표시하기
    bridgeVectorLayer.setVisible(true);
    damVectorLayer.setVisible(true);
    precipitationeVectorLayer.setVisible(true);
    isbridgeMarkerVisible = isDamMarkerVisible = isPrecipitationeMarkerVisible = true;
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




// ===== 마커 정보 카드 표시 API =====
window.showMarkerInfo = function(data){
    // 카드 DOM 요소 가져오기
    const $card = document.getElementById('markerInfoCard');
    
    // 텍스트 설정 헬퍼 함수 (null/undefined 값 처리)
    const setText = (id, val) => (document.getElementById(id).textContent = val ?? '-');

    // 기본 정보 설정
    setText('mic-title', data.name);                                                    // 지점명
    setText('mic-subtitle', data.subtitle || (data.type ? `#${data.type}` : ''));     // 부제목 또는 타입
    setText('mic-level', Number.isFinite(data.waterLevel) ? `${data.waterLevel} m` : (data.waterLevel ?? '-')); // 수위
    setText('mic-updated', data.updatedAt || '-');                                     // 업데이트 시간

    // 좌표 정보 포맷팅 (소수점 6자리까지 표시)
    const coordTxt = (data.coord && (data.coord.lon_dd!=null) && (data.coord.lat_dd!=null))
        ? `${Number(data.coord.lon_dd).toFixed(6)}, ${Number(data.coord.lat_dd).toFixed(6)}`
        : '-';
    setText('mic-coord', coordTxt);

    // 경보 단계 배지 설정 (색상 및 텍스트)
    const stageEl = document.getElementById('mic-stage');
    const stage = (data.stage || '-').trim();
    stageEl.textContent = stage;
    stageEl.className = 'badge rounded-pill ' + ({
        '정상': 'text-bg-success',    // 초록색 배지
        '주의': 'text-bg-primary',    // 파란색 배지
        '경고': 'text-bg-warning',    // 노란색 배지
        '위험': 'text-bg-danger'      // 빨간색 배지
    }[stage] || 'text-bg-secondary'); // 기본: 회색 배지

    // 줌 버튼 클릭 이벤트 설정
    const zoomBtn = document.getElementById('mic-zoomBtn');
    zoomBtn.onclick = () => {
        // 커스텀 줌 함수가 있으면 우선 사용
        if (typeof data.zoomTo === 'function') return data.zoomTo();
        
        // 기본 VWorld 지도 줌 기능
        if (window.vmap && data.coord) {
            vmap.setCenterAndZoom(data.coord.lon_dd, data.coord.lat_dd, 12);
        }
    };

    // 카드 표시 (d-none 클래스 제거)
    $card.classList.remove('d-none');
};

window.hideMarkerInfo = function(){
    document.getElementById('markerInfoCard').classList.add('d-none');
};

window.addEventListener('keydown', (e) => { if (e.key === 'Escape') hideMarkerInfo(); });

/** 유틸: 배열 API에서 코드로 하나 찾기 */
function findBy(arr, key, val){ return arr.find(d => String(d[key]) === String(val)); }

/** 기존 팝업 오픈 함수를 카드로 치환 */
// window.openPopupBridge = async function(wlobscd){
//     // 목록 API를 재사용해 해당 코드의 DTO를 찾는다 (현재 마커도 이 API에서 만들어짐) :contentReference[oaicite:3]{index=3}
//     const res = await fetch(`${CTX}api/bridge`);
//     const list = await res.json();
//     const dto = findBy(list, 'wlobscd', wlobscd);
//     if (!dto) return alert('지점을 찾을 수 없습니다.');
//
//     showMarkerInfo({
//         id: `bridge-${dto.wlobscd}`,
//         type: 'bridge',
//         name: dto.obsnm,
//         subtitle: dto.city || '한강',
//         waterLevel: dto.pfh ?? dto.gdt, // 가용 필드로 임시 노출
//         stage: dto.stage || '-',        // 서버에 단계 필드가 있으면 매핑
//         coord: { lon_dd: dto.lon_dd, lat_dd: dto.lat_dd },
//         updatedAt: dto.updatedAt,
//         zoomTo: () => vmap.setCenterAndZoom(dto.lon_dd, dto.lat_dd, 12)
//     });
// };

window.openPopupBridge = async function(wlobscd){
    // 목록 API를 재사용해 해당 코드의 DTO를 찾는다 (현재 마커도 이 API에서 만들어짐) :contentReference[oaicite:3]{index=3}
    const res = await fetch(`${CTX}api/bridge`);
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
        coord: { lon_dd: dto.lon_dd, lat_dd: dto.lat_dd },
        updatedAt: dto.updatedAt,
        zoomTo: () => vmap.setCenterAndZoom(dto.lon_dd, dto.lat_dd, 12)
});
};

window.openPopupdam = async function(dmobscd){
    const res = await fetch(`${CTX}api/dam`);
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
        coord: { lon_dd: dto.lon_dd, lat_dd: dto.lat_dd },
        updatedAt: dto.updatedAt,
        zoomTo: () => vmap.setCenterAndZoom(dto.lon_dd, dto.lat_dd, 12)
    });
};

window.openPopupPrecipitatione = async function(rfobscd){
    const res = await fetch(`${CTX}api/precipitatione`);
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
        coord: { lon_dd: dto.lon_dd, lat_dd: dto.lat_dd },
        updatedAt: dto.updatedAt,
        zoomTo: () => vmap.setCenterAndZoom(dto.lon_dd, dto.lat_dd, 12)
    });
};
