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
    <button type="button" onclick="javascript:hideAllMarker();" >마커전체숨기기</button>
    <button type="button" onclick="javascript:showAllMarker();" >마커전체보기</button>
    <button type="button" onclick="javascript:hidewaterMarker();" >수문 숨기기</button>
    <button type="button" onclick="javascript:showwaterMarker();" >수문 보기</button>
    <button type="button" onclick="javascript:hideweatherMarker();" >기상 숨기기</button>
    <button type="button" onclick="javascript:showweatherMarker();" >기상 보기</button>
    <button type="button" onclick="javascript:hidedamMarker();" >댐 숨기기</button>
    <button type="button" onclick="javascript:showdamMarker();" >댐 보기</button>
    <input type="text" id="param" value="" size="20"/>
</div>
<link rel="stylesheet"
      href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0"/>


<script language=javascript>

    var vmap = new vw.ol3.Map("vmap", vw.ol3.MapOptions);

    var waterMarkerLayer = new vw.ol3.layer.Marker(vmap);
    var weatherMarkerLayer = new vw.ol3.layer.Marker(vmap);
    var damMarkerLayer = new vw.ol3.layer.Marker(vmap);

    function setMode(basemapType) {
        vmap.setBasemapType(basemapType);
    }

    // vmap 객체에 클릭 이벤트 리스너를 등록합니다.
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
                // console.log(x);
                // console.log(y);
            } else {
                // 특징이 없거나 올바른 레이어에 속하지 않는 경우 false를 반환합니다.
                return false;
            }
        });
    });

    function addMarkerLayers() {
        vmap.addLayer(waterMarkerLayer);
        vmap.addLayer(weatherMarkerLayer);
        vmap.addLayer(damMarkerLayer);
    }


    function openPopup(asd) {
        window.open("http://localhost:8809/waterdata?wlobscd=" + asd, "_blank", "width=500, height=500");
    }

    var dtoList = new Array();

    // 마커를 추가하는 함수
    function addMarkerwater() {
        // waterso 데이터 목록을 순회하며 각 항목을 처리
        <c:forEach var="dto" items="${waterso}">
        // 각 DTO 정보를 dtoList 배열에 추가
        dtoList.push({
            // 위도, 경도 및 좌표 정보를 설정
            x: ${dto.lon},
            y: ${dto.lat},
            epsg: "EPSG:4326",

            // 마커에 표시될 정보 설정
            title: "<a href='javascript:openPopup(${dto.wlobscd});'>${dto.obsnm}</a>",
            contents:
                "해발 고도: ${dto.gdt}" +
                "<br>경보 수위: ${dto.attwl}" +
                "<br>경고 수위: ${dto.wrnwl}" +
                "<br>주의 수위: ${dto.almwl}" +
                "<br>안전 수위: ${dto.srswl}" +
                "<br>최고 수위: ${dto.pfh}" +
                "<br>홍수 위험 예고 ${dto.fstnyn}",
            // 마커 아이콘의 이미지 URL
            iconUrl: '//img.icons8.com/ultraviolet/40/bridge.png',

            // 마커에 표시될 텍스트 스타일 설정
            text: {
                offsetX: 0.5,
                offsetY: 20,
                font: '12px Calibri, sans-serif',
                fill: {color: '#000'},
                stroke: {color: '#fff', width: 2},
            },

            // 마커의 속성 설정
            attr: {"id": "maker01", "name": "속성명1"}
        });
        </c:forEach>

        // dtoList 배열에 있는 각 DTO 정보를 가져와서 지도에 마커로 추가
        dtoList.forEach(function (dto) {
            waterMarkerLayer.addMarker(dto);
        });
    }
    function addMarkerweather() {
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
            weatherMarkerLayer.addMarker(dto);
        });
    }
    function addMarkerdam() {
        var dtoList = new Array();
        <c:forEach var="dto" items="${damso}">
        // 위에서 'list'를 사용하려 했으나, 'dto'를 사용해야 합니다.
        dtoList.push({
            x: ${dto.lon},
            y: ${dto.lat},
            epsg: "EPSG:4326",
            title: "<a href='javascript:openPopup(${dto.dmobscd});'>${dto.obsnm}</a>",
            contents:
                "홍수 우려 수위: ${dto.pfh}" +
                "<br>수위 제한선: ${dto.fldlmtwl}",
            iconUrl: '//img.icons8.com/cotton/64/dam.png',
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
            damMarkerLayer.addMarker(dto);
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


    // 마커 입력 오류 확인 함수
    function checkMarkerParam() {
        // markerLayer가 아직 생성되지 않은 경우
        if (markerLayer == null) {
            alert("마커 레이어가 생성되지 않았습니다.\n마커 입력 버튼을 먼저 실행해 주세요.");
            return false; // 함수 실행 중단
        }

        // 마커 설정 객체 생성
        vw.ol3.markerOption = {
            epsg: "EPSG:5179", // 좌표계 설정
            title: '네이버 좌표계',
            contents: '덕수초등학교', // 마커 내용
            text: {
                offsetX: 0.5, // 텍스트 X축 위치 설정
                offsetY: 20,   // 텍스트 Y축 위치 설정
                font: '12px Calibri, sans-serif', // 폰트 설정
                fill: {color: '#000'}, // 텍스트 색상 설정
                stroke: {color: '#fff', width: 2}, // 텍스트 테두리 스타일
                text: '테스트 마커4' // 마커에 표시될 텍스트
            },
            attr: {"id": "maker04", "name": "속성명4"} // 속성 추가
        };

        // 마커 레이어에 마커 추가
        markerLayer.addMarker(vw.ol3.markerOption);
    }

    // 선택된 마커가 있는지 확인하는 함수
    function isSelectMarker() {
        // 마커 레이어가 아직 생성되지 않은 경우
        if (markerLayer == null) {
            alert("마커 레이어가 생성되지 않았습니다.\n마커 입력 버튼을 먼저 실행해 주세요.");
            return false; // 함수 실행 중단
        } else {
            // 마커가 하나도 생성되지 않은 경우
            if (this.markerLayer.getSource().getFeatures().length < 1) {
                alert("생성된 마커가 없습니다.");
                return false; // 함수 실행 중단
            } else {
                // 선택된 마커가 없는 경우
                if ($('#param').val() == '') {
                    alert("선택된 마커가 없습니다. 마커에 마우스를 올려주세요.");
                    return false; // 함수 실행 중단
                } else {
                    return true; // 마커 선택 확인
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

    // 마커를 보여주는 함수
    function showMarker() {
        // 선택된 마커가 있는지 확인
        if (isSelectMarker()) {
            // 선택된 마커를 화면에 보이도록 함
            this.markerLayer.showMarker(selectMarker);
            // #param 요소의 값을 초기화하여 선택된 마커 없음을 나타냄
            $('#param').val('');
        }
    }


    function hideAllMarker() {
            this.waterMarkerLayer.hideAllMarker();
            this.damMarkerLayer.hideAllMarker();
            this.weatherMarkerLayer.hideAllMarker();
    }

    function showAllMarker() {
            this.waterMarkerLayer.showAllMarker();
            this.damMarkerLayer.showAllMarker();
            this.weatherMarkerLayer.showAllMarker();
    }
    function hidewaterMarker() {
        this.waterMarkerLayer.hideAllMarker();
    }

    function showwaterMarker() {
        this.waterMarkerLayer.showAllMarker();
    }

    function hidedamMarker() {
        this.damMarkerLayer.hideAllMarker();
    }

    function showdamMarker() {
        this.damMarkerLayer.showAllMarker();

    }
    function hideweatherMarker() {
        this.weatherMarkerLayer.hideAllMarker();
    }

    function showweatherMarker() {
        this.weatherMarkerLayer.showAllMarker();
    }

    // 마커 삭제 함수
    function removeMarker() {
        // 선택된 마커가 있는지 확인
        if (isSelectMarker()) {
            // 마커 레이어에서 모든 피처(마커)를 가져옴
            var features = this.markerLayer.getSource().getFeatures();

            // 가져온 모든 마커들을 순회하며 특정 마커를 찾고 삭제
            for (var i = 0; i < features.length; i++) {
                // 선택한 마커 ID 값과 일치하는 마커 찾기
                if ($('#param').val() == features[i].get('id')) {
                    // 선택된 마커를 마커 레이어에서 삭제
                    this.markerLayer.removeMarker(selectMarker);

                    // #param 입력란 초기화
                    $('#param').val('');

                    // 선택된 마커를 null로 설정 (선택 해제)
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
    addMarkerLayers();
    addMarkerwater();
    addMarkerweather();
    addMarkerdam();
</script>
</body>
</html>
