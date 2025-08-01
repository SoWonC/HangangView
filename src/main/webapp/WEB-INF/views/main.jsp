<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>한강뷰</title>

    <!-- Bootstrap 5 CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <!-- 기타 리소스 -->
    <link rel="stylesheet" href="<c:url value='/resources/css/main.css' />">
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script src="https://map.vworld.kr/js/vworldMapInit.js.do?version=2.0&apiKey=109C097E-3484-3D28-B6DA-F484D2E5FDB2"></script>

</head>
<body>

<c:set var="sessionName" value="user"/>

<!-- 상단 네비게이션 바 -->
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
        <span class="navbar-brand">한강 수자원 관리 종합플랫폼</span>
    </div>
</nav>

<!-- 사용자 전체 레이아웃 -->
<c:if test="${sessionName ne 'admin' && sessionName != null}">
<div id="main-layout">
    <!-- 좌측 사이드바 -->
    <div id="sidebar">
        <h5>메뉴</h5>
        <ul class="list-group">
            <li class="list-group-item"><a href="#">관측소 현황</a></li>
            <li class="list-group-item"><a href="#">강수량 정보</a></li>
            <li class="list-group-item"><a href="#">수위 경보</a></li>
        </ul>
    </div>
    <!-- 지도 + 콘텐츠 영역 -->
    <div id="content">
        <div id="vmap"></div>
    </div>
    <!-- 오른쪽 사이드 버튼 영역 -->
    <div id="buttons" style="position: fixed; top: 10%; right:7%; z-index: 9999;">
        <div class="card shadow-sm p-3" style="width: 200px;">
            <div class="d-flex flex-column gap-2">
                <button class="btn btn-sm btn-primary w-100" onclick="toggleAllMarkers()">전체 보기</button>
                <button class="btn btn-sm btn-outline-secondary w-100" onclick="toggleMap();">배경지도</button>
                <button class="btn btn-sm btn-outline-secondary w-100" onclick="toggleWhiteMap();">백지도</button>
                <button class="btn btn-sm btn-outline-dark w-100" onclick="toggleNightMap();">야간지도</button>
                <button class="btn btn-sm btn-outline-info w-100" onclick="toggleAerialMap();">항공사진</button>
                <button class="btn btn-sm btn-outline-success w-100" onclick="toggleHybridMap();">하이브리드</button>
                <button class="btn btn-sm btn-success w-100" onclick="showAllMarkers();">전체 표시</button>
                <button class="btn btn-sm btn-danger w-100" onclick="hideAllMarkers();">전체 지우기</button>
            </div>
            <div class="mt-3">
                <input type="text" id="param" class="form-control" placeholder="">
            </div>
        </div>
    </div>
    <div id="marker-controls">
        <label><input type="checkbox" onchange="togglebridgeMarker(this)" checked> 다리</label><br>
        <label><input type="checkbox" onchange="toggleDamMarker(this)" checked> 댐</label><br>
        <label><input type="checkbox" onchange="togglePrecipitationeMarker(this)" checked> 강수량</label><br>
            <%--        <label><input type="checkbox" onchange="toggleCctvMarker(this)" checked> CCTV</label>--%>
    </div>
    </c:if>

    <!-- =================== 공통 JS =================== -->
    <script src="<c:url value='/resources/js/map-init.js'/>"></script>
    <script src="<c:url value='/resources/js/marker-functions.js'/>"></script>
    <script src="<c:url value='/resources/js/notice-toggle.js' />"></script>
</body>
</html>
