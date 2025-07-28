<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="spring.AuthInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <title>api2.0_maptest</title>
    <script type="text/javascript"
            src="https://map.vworld.kr/js/vworldMapInit.js.do?version=2.0&apiKey=109C097E-3484-3D28-B6DA-F484D2E5FDB2"></script>
  <style>
    * {
      margin: 0%;
    }

    .top_bar {
      position: fixed;
      background-color: white;
      height: 10%;
      width: 100%;
      border: 3px solid aqua;
    }

    .left_sideBar {
      position: fixed;
      height: 88.5%;
      width: 14.7%;
      top: 10.5%;
      margin-left: 0%;
      background-color: white;
      border: 3px solid aqua;
      flex: 1;
      overflow-x:hidden;
      overflow-y:auto;
    }
    
    .right_sideBar {
      position: fixed;
      width: 11%;
      top: 10.5%;
      right: 0;
      background-color: aliceblue;
    }

    button {
      margin-top: 2%;
      position: fixed;
      width: 120px;
      height: 25px;
      background-color: aquamarine;
    }
    
    .logout_button {
    	width: 80px;
    	height: 20px;
    	background-color: none;
    	border-bottom: 2px solid black;
    }
    
    .logout_button:hover {
    	cursor: pointer;
    	background-color: darkgrey;
    }
    
    .top_bar_text {
    	text-align:right;
    	margin-right: 2%;
    }
    
    .tabs {
      margin-right: 75.5%;
      margin-top: 0.6%;
    }

    .list_tab {
      width: 75px;
      height: 20px;
    }

    .map_tab {
      width: 75px;
      height: 20px;
    }
    
    .sideBar_button {
    	width: 100%;
    	height:25px;
    	border: 1px solid black;
    	background-color: gray;
    	color: white;
    }
    
    .sideBar_button:hover {
    	cursor: pointer;
    }
    
  </style>
  	<script>
    // 공지사항 목록의 표시 상태를 관리하는 변수
    var isNoticesVisible = false;

    function toggleNotices() {
        if(isNoticesVisible) {
            // 공지사항 목록이 이미 표시되어 있다면, 숨김
            $('#noticeArea').html('');
            isNoticesVisible = false;
        } else {
            // 공지사항 목록이 표시되어 있지 않다면, Ajax 호출을 통해 불러옴
            $.ajax({
                type: 'GET',
                url: '/project_1108/notices',
                success: function(response) {
                    $('#noticeArea').html(response);
                    isNoticesVisible = true; // 공지사항 목록이 표시되었음을 표시
                },
                error: function() {
                    alert('공지사항 목록을 불러오는 데 실패했습니다.');
                }
            });
        }
    }
</script>
	<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
</head>
<body>
<%
  AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
  if (authInfo != null) {
      String sessionName = authInfo.getName();
	} 
  
  List<String> list = (List<String>) request.getAttribute("list");
%>
<c:set var="sessionName" value="${sessionScope.authInfo != null ? sessionScope.authInfo.name : null}" />

<div class="top_bar">

	<h2 style="margin-top: 1%; margin-left: 2%;">한강 수자원 관리 종합플랫폼</h2>
	<div class="top_bar_text">
	
	<c:if test="${sessionName eq 'admin'}">
		${sessionName } 계정입니다.
		<a href="<c:url value="/logout" />"><input type="button" class="logout_button" value="로그아웃"></a>
		
	</c:if>
    <c:if test="${sessionName ne 'admin'}">
    	<c:choose>
    		<c:when test="${sessionName != null }">
    		${sessionName }님 안녕하세요
    		<a href="<c:url value="/logout" />"><input type="button" class="logout_button" value="로그아웃"></a>
    		<hr>
    		</c:when>
    		<c:otherwise>
    		</c:otherwise>
    	</c:choose>
    </c:if>
    
    </div>
    
</div>

<c:if test="${sessionName eq 'admin' }">
<div style="position:fixed; top:30%; left:40%;">

<button onclick="location.href='/project_1108/notices'">공지사항</button><br><br><br>
<button onclick="location.href='/project_1108/waterForecasts'">갈수 예보</button><br><br><br>
<button onclick="location.href='/project_1108/droughtForecasts'">가뭄 예경보</button><br><br><br>
<button onclick="location.href='/project_1108/waterViews'">갈수 전망</button><br><br><br>
<button onclick="location.href='/project_1108/waterRequests'">용수 신청 목록</button><br><br><br>

<div id="noticeArea"></div>
</div>
</c:if>



<c:if test="${sessionName ne 'admin' }">
<c:choose>
<c:when test="${sessionName != null }">


  
  <div class="left_sideBar">
  <input type="button" class="sideBar_button" onclick="toggleWaterMarker()" value="수문">    
  <input type="button" class="sideBar_button" onclick="toggleDamMarker()" value="댐">    
  <input type="button" class="sideBar_button" onclick="location.href='/project_1108/hacheon'" value="하천">
  <input type="button" class="sideBar_button" onclick="toggleWeatherMarker()" value="기상">    
  <input type="button" class="sideBar_button" value="cctv" onclick="toggleCctvMarkers()">   
  <input type="button" class="sideBar_button" value="갈수 가뭄 예보 및 전망">
  <ul style="text-align:center;">
     <li><a href="/project_1108/waterForecasts">갈수 예보</a></li>
    <li><a href="/project_1108/droughtForecasts">가뭄 예경보</a></li>
    <li><a href="/project_1108/waterViews">갈수 전망</a></li>
  </ul>
  <input type="button" class="sideBar_button" onclick="location.href='/project_1108/waterRequest/add'" value="용수 신청">
  <input type="button" class="sideBar_button" onclick="location.href='/project_1108/notices'" value="공지사항">
  </div>
  
<div id="vmap" style="position: fixed;width:73%;height:90%;left:15%;top:10.6%"></div>
<div class="right_sideBar">
<div id="buttons" style="position: fixed; top: 18%; right:2%; ">
   <button type="button" onclick="toggleAllMarkers()">전체 보기</button><br><br>
    <button type="button" onclick="javascript:setMode(vw.ol3.BasemapType.GRAPHIC);">배경지도</button><br><br>
    <button type="button" onclick="toggleWhiteMap();">백지도</button><br><br>
    <button type="button" onclick="toggleNightMap();">야간지도</button><br><br>
    <button type="button" onclick="toggleAerialMap();">항공사진</button><br><br>
    <button type="button" onclick="toggleHybridMap();">하이브리드</button><br><br><br><br>
    <input type="text" id="param" value="" size="20"/>
</div>
</div>

</c:when>
</c:choose>
</c:if>
<link rel="stylesheet"
    href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0" />
    <script src="https://openlayers.org/en/latest/build/ol.js"></script>

   <script language=javascript>

 // 수문 마커의 표시 상태를 관리하는 변수
    // 마커의 표시 상태를 관리하는 변수들
    var isWaterMarkerVisible = false;
    var isDamMarkerVisible = false;
    var isWeatherMarkerVisible = false;
    var areAllMarkersVisible = false;

    // 수문 마커 토글 함수
    function toggleWaterMarker() {
        if(isWaterMarkerVisible) {
            hidewaterMarker();
            isWaterMarkerVisible = false;
        } else {
            showwaterMarker();
            isWaterMarkerVisible = true;
        }
    }

    // 댐 마커 토글 함수
    function toggleDamMarker() {
        if(isDamMarkerVisible) {
            hidedamMarker();
            isDamMarkerVisible = false;
        } else {
            showdamMarker();
            isDamMarkerVisible = true;
        }
    }

    // 기상 마커 토글 함수
    function toggleWeatherMarker() {
        if(isWeatherMarkerVisible) {
            hideweatherMarker();
            isWeatherMarkerVisible = false;
        } else {
            showweatherMarker();
            isWeatherMarkerVisible = true;
        }
    }

    // 수문 마커를 숨기는 함수
    function hidewaterMarker() {
        waterMarkerLayer.hideAllMarker();
    }

    // 수문 마커를 보여주는 함수
    function showwaterMarker() {
        waterMarkerLayer.showAllMarker();
    }
    
 // 댐 마커를 숨기는 함수
    function hidedamMarker() {
        waterMarkerLayer.hideAllMarker();
    }

    // 댐 마커를 보여주는 함수
    function showdamMarker() {
        waterMarkerLayer.showAllMarker();
    }
    
 // 기상 마커를 숨기는 함수
    function hideweatherMarker() {
        waterMarkerLayer.hideAllMarker();
    }

    // 기상 마커를 보여주는 함수
    function showweatherMarker() {
        waterMarkerLayer.showAllMarker();
    }
    
    // 전체 마커 토글 함수
    function toggleAllMarkers() {
        if(areAllMarkersVisible) {
            hideAllMarkers();
            areAllMarkersVisible = false;
        } else {
            showAllMarkers();
            areAllMarkersVisible = true;
        }
    }

    // 전체 마커 숨기기/보이기 함수들
    function hideAllMarkers() {
        hidewaterMarker();
        hidedamMarker();
        hideweatherMarker();
        // 전체 마커 숨기기 상태를 각각의 마커 변수에 반영
        isWaterMarkerVisible = false;
        isDamMarkerVisible = false;
        isWeatherMarkerVisible = false;
    }

    function showAllMarkers() {
        showwaterMarker();
        showdamMarker();
        showweatherMarker();
        // 전체 마커 보이기 상태를 각각의 마커 변수에 반영
        isWaterMarkerVisible = true;
        isDamMarkerVisible = true;
        isWeatherMarkerVisible = true;
    }

    
    var currentBasemapType = vw.ol3.BasemapType.GRAPHIC; // 초기 지도 타입 설정

    // 백지도 토글 함수
    function toggleWhiteMap() {
        if(currentBasemapType === vw.ol3.BasemapType.GRAPHIC_WHITE) {
            // 현재 백지도 상태라면 기본 지도 타입으로 되돌림
            vmap.setBasemapType(vw.ol3.BasemapType.GRAPHIC);
            currentBasemapType = vw.ol3.BasemapType.GRAPHIC;
        } else {
            // 백지도 상태가 아니라면 백지도 타입으로 설정
            vmap.setBasemapType(vw.ol3.BasemapType.GRAPHIC_WHITE);
            currentBasemapType = vw.ol3.BasemapType.GRAPHIC_WHITE;
        }
    }
    
    function toggleNightMap() {
        if(currentBasemapType === vw.ol3.BasemapType.GRAPHIC_NIGHT) {
            // 현재 야간지도 타입이라면 기본 지도 타입으로 되돌림
            vmap.setBasemapType(vw.ol3.BasemapType.GRAPHIC);
            currentBasemapType = vw.ol3.BasemapType.GRAPHIC;
        } else {
            // 현재 야간지도 타입이 아니라면 야간지도 타입으로 설정
            vmap.setBasemapType(vw.ol3.BasemapType.GRAPHIC_NIGHT);
            currentBasemapType = vw.ol3.BasemapType.GRAPHIC_NIGHT;
        }
    }
    
    function toggleAerialMap() {
        if(currentBasemapType === vw.ol3.BasemapType.PHOTO) {
            // 현재 항공사진 타입이라면 기본 지도 타입으로 되돌림
            vmap.setBasemapType(vw.ol3.BasemapType.GRAPHIC);
            currentBasemapType = vw.ol3.BasemapType.GRAPHIC;
        } else {
            // 현재 항공사진 타입이 아니라면 항공사진 타입으로 설정
            vmap.setBasemapType(vw.ol3.BasemapType.PHOTO);
            currentBasemapType = vw.ol3.BasemapType.PHOTO;
        }
    }
    
    function toggleHybridMap() {
        if(currentBasemapType === vw.ol3.BasemapType.PHOTO_HYBRID) {
            // 현재 하이브리드 타입이라면 기본 지도 타입으로 되돌림
            vmap.setBasemapType(vw.ol3.BasemapType.GRAPHIC);
            currentBasemapType = vw.ol3.BasemapType.GRAPHIC;
        } else {
            // 현재 하이브리드 타입이 아니라면 하이브리드 타입으로 설정
            vmap.setBasemapType(vw.ol3.BasemapType.PHOTO_HYBRID);
            currentBasemapType = vw.ol3.BasemapType.PHOTO_HYBRID;
        }
    }
    
    
    
        var vmap = new vw.ol3.Map("vmap", vw.ol3.MapOptions);
    
        var initialCenter = [14137133.82,4511912.58]; // 예시 중심 좌표 [위도, 경도]
        var initialZoom = 10; // 예시 확대 수준
        vmap.getView().setCenter(initialCenter);
        vmap.getView().setZoom(initialZoom);
    
        var waterMarkerLayer = new vw.ol3.layer.Marker(vmap);
        var weatherMarkerLayer = new vw.ol3.layer.Marker(vmap);
        var damMarkerLayer = new vw.ol3.layer.Marker(vmap);
        var cctvMarkerLayer = new vw.ol3.layer.Marker(vmap);

    
        function setMode(basemapType) {
            vmap.setBasemapType(basemapType);
        }
    
        // vmap 객체에 클릭 이벤트 리스너를 등록합니다.
        vmap.on('click', function (evt) {
            // 클릭한 픽셀 위치에서 지도 위에 있는 특징(feature)을 찾습니다.
            var feature = vmap.forEachFeatureAtPixel(evt.pixel, function (feature, layer) {
                // 특징이 존재하고, 해당 특징이 'vw.ol3.layer.Marker' 클래스를 가진 레이어에 속하는 경우 실행합니다.
    
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
                    vmap.getView().setZoom(12);
                    console.log(x);
                    console.log(y);
    
    
            });
        });
    
        function addMarkerLayers() {
            vmap.addLayer(waterMarkerLayer);
            vmap.addLayer(weatherMarkerLayer);
            vmap.addLayer(damMarkerLayer);
            vmap.addLayer(cctvMarkerLayer);
        }
    
    
        function openPopupwater(asd) {
            window.open("http://localhost:8809/project_1108/waterdata?wlobscd=" + asd, "_blank", "width=1200, height=600");
        }
        function openPopupdam(asd) {
            window.open("http://localhost:8809/project_1108/dam2?dmobscd=" + asd, "_blank", "width=1200, height=600");
        }
        function openPopupweather(asd) {
            window.open("http://localhost:8809/project_1108/precipitation2?rfobscd=" + asd, "_blank", "width=1200, height=600");
        }
        function openPopupcctv(asd) {
            window.open("https://hrfco.go.kr/popup/cctvMainView.do?Obscd=" + asd, "_blank", "width=320, height=600");
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
                title: "<a href='javascript:openPopupwater(${dto.wlobscd});'>${dto.obsnm}</a>",
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
                title: "<a href='javascript:openPopupweather(${dto.rfobscd});'>${dto.obsnm}</a>",
                contents: "${dto.obsnm}",
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
                title: "<a href='javascript:openPopupdam(${dto.dmobscd});'>${dto.obsnm}</a>",
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
    
        function addMarkercctv() {
            var dtoList = new Array();
            <c:forEach var="dto" items="${cctvs}">
            // 위에서 'list'를 사용하려 했으나, 'dto'를 사용해야 합니다.
            dtoList.push({
                x: ${dto.lon},
                y: ${dto.lat},
                epsg: "EPSG:4326",
                title: "<a href='javascript:openPopupcctv(${dto.wlobscd});'>${dto.etcaddr}</a>",
                contents:
                    "<br>주소: ${dto.addr}",
                iconUrl: '//img.icons8.com/ios-filled/50/video-call.png',
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
                cctvMarkerLayer.addMarker(dto);
            });
        }
        
        // 선택된 마커가 있는지 확인하는 함수
        function isSelectMarker() {
            true;
        }
    
    
        function showPopup() {
            this.waterMarkerLayer.showPop(selectMarker);
            this.weatherMarkerLayer.showPop(selectMarker);
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
            this.waterMarkerLayer.showMarker(selectMarker);
            this.weatherMarkerLayer.showMarker(selectMarker);
            this.damMarkerLayer.showMarker(selectMarker);
            this.cctvMarkerLayer.showMarker(selectMarker);
            // #param 요소의 값을 초기화하여 선택된 마커 없음을 나타냄
            $('#param').val('');
    
        }
    
    
        function hideAllMarker() {
            this.waterMarkerLayer.hideAllMarker();
            this.damMarkerLayer.hideAllMarker();
            this.weatherMarkerLayer.hideAllMarker();
   	        this.cctvMarkerLayer.hideAllMarker();
        }
    
        function showAllMarker() {
            this.waterMarkerLayer.showAllMarker();
            this.damMarkerLayer.showAllMarker();
            this.weatherMarkerLayer.showAllMarker();
            this.cctvMarkerLayer.showAllMarker();
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
        
     // CCTV 마커 표시 상태를 추적하는 변수
        var isCctvMarkersVisible = false;

        // CCTV 마커를 토글하는 함수
        function toggleCctvMarkers() {
            if (isCctvMarkersVisible) {
                hidecctvMarker();
            } else {
                showcctvMarker();
            }
            isCctvMarkersVisible = !isCctvMarkersVisible;
        }
        
        function hidecctvMarker() {
            this.cctvMarkerLayer.hideAllMarker();
        }

        function showcctvMarker() {
            this.cctvMarkerLayer.showAllMarker();
        }

    
        addMarkerLayers();
        addMarkerwater();
        addMarkerweather();
        addMarkerdam();
        addMarkercctv();

    </script>
</body>
</html>
