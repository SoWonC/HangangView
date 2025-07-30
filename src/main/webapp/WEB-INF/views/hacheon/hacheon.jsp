<%@page import="spring.AuthInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Title</title>
<style>
body {
	margin: 0;
	padding: 0;
}

.top_container {
	margin-top: 2%;
	width: 100%;
	height: 100px;
	border-bottom: solid 1px black;
}

.title_name {
	background-color: white;
	color: black;
	font-size: 24px;
	border: none;
	font-weight: 600;
}

.title_name:hover {
	cursor: pointer;
	border: none;
}

.title_h2 {
	margin-left: 5%;
}

.buttons {
	text-align: right;
	margin-right: 5%;
}

.top_nav_buttons {
	margin-left: 25%;
	margin-top: 3%;
}

.admin_move_buttons {
	margin-left: 40%;
	margin-top: 4%;
}



.inf_manage {
	background-color: ghostwhite;
	border: 1px solid black;
	border-radius: none;
	width: 300px;
	height: 30px;
}

.inf_manage:hover {
	border: none;
	cursor: pointer;
}

.hacheon_button {
	background-color: lightblue;
	border: 1px solid black;
	border-radius: none;
	width: 300px;
	height: 30px;
}

.hacheon_button:hover {
	border: none;
	cursor: pointer;
}

.yooyeok_button {
	background-color: ghostwhite;
	border: 1px solid black;
	border-radius: none;
	width: 300px;
	height: 30px;
}

.yooyeok_button:hover {
	border: none;
	cursor: pointer;
}

.jawon_button {
	background-color: ghostwhite;
	border: 1px solid black;
	border-radius: none;
	width: 300px;
	height: 30px;
}

.jawon_button:hover {
	border: none;
	cursor: pointer;
}

.dropdown_list {
	margin-left: 25%;
	margin-top: 5%;
}

.hacheon {
	width: 120px;
}

.search_button {
	width: 60px;
}

.dropdown-menu {
    max-height: 200px; /* 드롭다운 내 최대 높이 */
    overflow-y: auto; /* 세로 스크롤이 필요한 경우만 표시 */
}


        #map {
    width: 100%; 
    height: 500px;  
        }
    </style>




 <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"></script>
    <meta charset="UTF-8">

    <script src="https://cdn.jsdelivr.net/npm/ol@v8.2.0/dist/ol.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ol@v8.2.0/ol.css">


</head>
<body>
	<%
	AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
	if (authInfo != null) {
		String sessionName = authInfo.getName();
	}
	%>
	<c:set var="sessionName"
		value="${sessionScope.authInfo != null ? sessionScope.authInfo.name : null}" />
		
	<c:if test="${sessionName eq 'admin'}">
		<div class="top_container">
				<div class="top_container_title">
					<h2 class="title_h2">
					<input type="button" class="title_name" value="한강 수자원 관리 종합플랫폼">
					</h2>
					<div class="buttons">
						${sessionName }계정입니다. 
						<a href="<c:url value="/logout" />"><input type="button" style="text-align:center;" value="로그아웃"></a>
					</div>
				</div>
			</div>
			
			
	</c:if>

	<c:if test="${sessionName ne 'admin' }">
	<c:choose>
		<c:when test="${sessionName != null }">
			<div class="top_container">
				<div class="top_container_title">
					<h2 class="title_h2">
					<input type="button" class="title_name" value="한강 수자원 관리 종합플랫폼" onclick="location.href='/project_1108/'">
					</h2>
					<div class="buttons">
						${sessionName }님 안녕하세요 
						<a href="<c:url value="/logout" />"><input type="button" style="margin-right: 5%;" value="로그아웃"></a>
					</div>
				</div>
			</div>
		</c:when>
		
		<c:otherwise>
			<div class="top_container">
				<div class="top_container_title">
					<h2 class="title_h2">
					<input type="button" class="title_name" value="한강 수자원 관리 종합플랫폼" onclick="location.href='/project_1108/'">
					</h2>
					<div class="buttons">
						<input type="button" value="로그인"
							onclick="location.href='/project_1108/login'"> <input
							type="button" value="회원가입"
							onclick="location.href='/project_1108/regist'">
					</div>
				</div>
			</div>
		</c:otherwise>
	</c:choose>


	</c:if>
	
	
	
	
	
	
	
	
	
<div class="dropdown">
    <input type="text" id="hacheonSearch" onkeyup="filterHacheon()" placeholder="하천 검색">
    
    <!-- 등급 선택 드롭다운 -->
    <select id="gradeSelect" onchange="filterHacheon()">
        <option value="전체">전체</option>
        <option value="국가">국가</option>
        <option value="지방">지방</option>
        <!-- 추가 등급 추가 가능 -->
    </select>
    
    <button class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
        Dropdown button
    </button>
    <ul class="dropdown-menu" id="hacheonDropdown">
        <!-- 하천 목록은 여기에 표시될 것입니다. -->
        <c:forEach items="${hacheonList}" var="hacheon">
            <li data-grade="${hacheon.hacheon_grade}">
                <a class="dropdown-item"
                    href="javascript:void(0);"
                    onclick="openNewWindow('/project_1108/hacheonDetail?hacheon_code=${hacheon.hacheon_code}')">
                    ${hacheon.hacheon_name} (${hacheon.hacheon_code})(${hacheon.hacheon_grade})
                </a>
            </li>
        </c:forEach>
    </ul>
</div>
<script>
    function openNewWindow(url) {
        window.open(url, 'hacheonDetailWindow', 'width=800,height=600');
    }

    function filterHacheon() {
        const input = document.getElementById('hacheonSearch');
        const filter = input.value.toUpperCase();
        const select = document.getElementById('gradeSelect');
        const selectedGrade = select.options[select.selectedIndex].value;
        const ul = document.getElementById('hacheonDropdown');
        const li = ul.getElementsByTagName('li');
        
        for (let i = 0; i < li.length; i++) {
            const a = li[i].getElementsByTagName('a')[0];
            const txtValue = a.textContent || a.innerText;
            const grade = li[i].getAttribute('data-grade');
            
            if ((txtValue.toUpperCase().indexOf(filter) > -1) && (selectedGrade === '전체' || grade === selectedGrade)) {
                li[i].style.display = '';
            } else {
                li[i].style.display = 'none';
            }
        }
    }
</script>

    
    
    <div id="map" class="map">

    </div>

    <script>
    var map = new ol.Map({
        target: 'map',
        layers: [
            new ol.layer.Tile({
                source: new ol.source.OSM() // 기본 배경지도
            }),
            new ol.layer.Tile({
                source: new ol.source.TileWMS({
                    url: 'http://localhost:8080/geoserver/hacheon/wms', // Geoserver WMS URL
                    params: {
                        'LAYERS': '	hacheon:original', // 레이어 이름
                        'TILED': true,
                       
                    },
                    serverType: 'geoserver'
                })
            })
        ],
        view: new ol.View({
            center: ol.proj.fromLonLat([126.9780, 37.5665]), // 서울의 위도와 경도
            zoom: 12
        })
    });
    
    map.on(
    	    "click", 
    	    function(e) {
    	        let values = null; // 클릭한 피처의 속성 값을 저장할 변수를 선언합니다.
    	        map.forEachFeatureAtPixel(
    	            e.pixel, 
    	            function (feature, layer) {
    	                values = feature.getProperties(); // 클릭한 피처의 속성 값을 가져옵니다.
    	                console.log('클릭한 피처 정보:', values); // 콘솔에 클릭한 피처의 정보를 출력합니다.
    	            }, 
    	            {
    	                hitTolerance: 2,
    	                layerFilter: function(layer) {
    	                    return layer === buildingLayer;
    	                }
    	            }
    	        );
    	    }
    	);

    
 
    </script>
    
   
    
  

</body>
</html>