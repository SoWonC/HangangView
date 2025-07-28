<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="https://fonts.googleapis.com/
    icon?family=Material+Icons|Material+Icons+Sharp|Material+Icons+Two+Tone|Material+Icons+Outlined"
      rel="stylesheet">
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>api2.0_maptest</title>
    <script type="text/javascript"
            src="https://map.vworld.kr/js/vworldMapInit.js.do?version=2.0&apiKey=9D580A84-A8AD-3703-B7F0-E508371AC215"></script>
</head>
<body>
<div id="vmap" style="width:100%;height:350px;left:0px;top:0px"></div>
<div id="buttons">
    <button type="button" onclick="javascript:setMode(vw.ol3.BasemapType.GRAPHIC);">배경지도</button>
    <button type="button" onclick="javascript:setMode(vw.ol3.BasemapType.GRAPHIC_WHITE);">백지도</button>
    <button type="button" onclick="javascript:setMode(vw.ol3.BasemapType.GRAPHIC_NIGHT);">야간지도</button>
    <button type="button" onclick="javascript:setMode(vw.ol3.BasemapType.PHOTO);">항공사진</button>
    <button type="button" onclick="javascript:setMode(vw.ol3.BasemapType.PHOTO_HYBRID);">하이브리드</button>
    <input type="text" id="param" value="" size="20"/>
</div>
<link rel="stylesheet"
      href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0"/>

<script language=javascript>

    var vmap = new vw.ol3.Map("vmap", vw.ol3.MapOptions);

    function setMode(basemapType) {
        vmap.setBasemapType(basemapType);
    }

    vmap.on('click', function (evt) {
        // 클릭한 픽셀 위치에서 지도 위에 있는 특징(feature)을 찾습니다.
        var feature = vmap.forEachFeatureAtPixel(evt.pixel, function (feature, layer) {
            // 특징이 존재하고, 해당 특징이 'vw.ol3.layer.Marker' 클래스를 가진 레이어에 속하는 경우 실행합니다.
            if (layer != null && layer.className == 'vw.ol3.layer.Marker') {
                // 찾은 특징의 'id' 속성 값을 가져와서 'param' 요소에 설정합니다.
                $('#param').val(feature.get('id'));

                // 선택된 마커를 변수에 할당합니다.
                selectMarker = feature;

                // 팝업을 표시하는 함수를 호출합니다.
                showPopup();

                // 클릭한 지점의 좌표를 가져옵니다.
                let coordinate = evt.coordinate;

                // 좌표 값을 파싱하여 x 및 y 좌표로 추출합니다.
                var x = parseFloat(coordinate[1]);
                var y = parseFloat(coordinate[0]);

                // 지도의 중심 좌표를 설정합니다.
                vmap.getView().setCenter([y, x]);

                // 지도의 확대 수준을 조절합니다.
                vmap.getView().setZoom(9);

                // 콘솔에 x 및 y 좌표를 출력합니다.
                console.log(x);
                console.log(y);
            } else {
                // 특징이 없거나 올바른 레이어에 속하지 않는 경우 false를 반환합니다.
                return false;
            }
        });
    });

    var markerLayer;

    function addMarkerLayer() {
        // 마커 레이어가 이미 존재하는지 확인
        // 마커 레이어가 존재하지 않는 경우
        // 새로운 마커 레이어 생성
        markerLayer = new vw.ol3.layer.Marker(vmap);
        // 새로운 마커 레이어를 지도에 추가
        vmap.addLayer(markerLayer);
        addMarker();
    }

    function openPopup(asd) {
        window.open("http://localhost:8809/precipitation2?rfobscd=" + asd, "_blank", "width=500, height=500");
    }

    function addMarker() {
        var dtoList = new Array();
        <c:forEach var="dto" items="${weatherso}">
        // 위에서 'list'를 사용하려 했으나, 'dto'를 사용해야 합니다.
        dtoList.push({
            x: ${dto.lon},
            y: ${dto.lat},
            epsg: "EPSG:4326",
            title: "<a href='javascript:openPopup(${dto.rfobscd});'>${dto.obsnm}</a>",
            contents:"asdf",
            iconUrl: '////img.icons8.com/ultraviolet/40/blur.png',
            text: {
                offsetX: 0.5,
                offsetY: 20,
                font: '12px Calibri, sans-serif',
                fill: {color: '#000'},
                stroke: {color: '#fff', width: 2},
            },
            attr: {"id": "maker01", "name": "속성명1"}
        });
        </c:forEach>

        dtoList.forEach(function (dto) {
            markerLayer.addMarker(dto);
        });
    }

    function checkMarkerParam() {
        if (markerLayer == null) {
            alert("마커레이어가 생성되지 않았습니다.\n마커입력버튼을 먼저 실행하십시요.");
            return false;
        }
        vw.ol3.markerOption = {
            epsg: "EPSG:5179",
            title: '네이버좌표계',
            contents: '덕수초등학교',
            text: {
                offsetX: 0.5, //위치설정
                offsetY: 20,   //위치설정
                font: '12px Calibri,sans-serif',
                fill: {color: '#000'},
                stroke: {color: '#fff', width: 2},
                text: '테스트마커4'
            },
            attr: {"id": "maker04", "name": "속성명4"}
        };
        markerLayer.addMarker(vw.ol3.markerOption);
    }

    function isSelectMarker() {
        if (markerLayer == null) {
            alert("마커레이어가 생성되지 않았습니다.\n마커입력버튼을 먼저 실행하십시요.");
            return false;
        } else {
            if (this.markerLayer.getSource().getFeatures().length < 1) {
                alert("생성된 마커가 없습니다.");
                return false;
            } else {
                if ($('#param').val() == '') {
                    alert("선택된 마커가 없습니다. 마커에 마우스를 올리세요.");
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    function showPopup() {
        if (isSelectMarker()) {
            this.markerLayer.showPop(selectMarker);
        }
    }

    function hidePopup() {
        if (isSelectMarker()) {
            this.markerLayer.hidePop(selectMarker);
        }
    }

    function hideMarker() {
        if (isSelectMarker()) {
            this.markerLayer.hideMarker(selectMarker);
        }
    }

    function showMarker() {
        if (isSelectMarker()) {
            this.markerLayer.showMarker(selectMarker);
            $('#param').val('');
        }
    }

    function hideAllMarker() {
        if (markerLayer == null) {
            alert("마커레이어가 생성되지 않았습니다.\n마커입력버튼을 먼저 실행하십시요.");
        } else {
            this.markerLayer.hideAllMarker();
        }
    }

    function showAllMarker() {
        if (markerLayer == null) {
            alert("마커레이어가 생성되지 않았습니다.\n마커입력버튼을 먼저 실행하십시요.");
        } else {
            this.markerLayer.showAllMarker();
        }
    }

    function removeMarker() {
        if (isSelectMarker()) {
            var features = this.markerLayer.getSource().getFeatures();
            for (var i = 0; i < features.length; i++) {
                if ($('#param').val() == features[i].get('id')) {
                    this.markerLayer.removeMarker(selectMarker);
                    $('#param').val('');
                    selectMarker = null;
                }
            }
        }
    }

    function removeAllMarker() {
        if (markerLayer == null) {
            alert("마커레이어가 생성되지 않았습니다.\n마커입력버튼을 먼저 실행하십시요.");
        } else {
            this.markerLayer.removeAllMarker();
        }
    }
    addMarkerLayer()
    addMarker()
</script>
</body>
</html>
