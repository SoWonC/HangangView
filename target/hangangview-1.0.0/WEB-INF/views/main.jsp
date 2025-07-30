<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>한강뷰</title>

    <!-- 외부 CSS 및 라이브러리 -->
    <link rel="stylesheet" href="<c:url value='/resources/css/main.css' />">
    <link rel="stylesheet"
          href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0"/>
<%--    <script src="https://openlayers.org/en/latest/build/ol.js"></script>--%>
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script type="text/javascript"
            src="https://map.vworld.kr/js/vworldMapInit.js.do?version=2.0&apiKey=109C097E-3484-3D28-B6DA-F484D2E5FDB2"></script>
</head>

<body>
<%--<c:set var="sessionName" value="${sessionScope.authInfo != null ? sessionScope.authInfo.name : null}" />--%>
<%--<c:set var="sessionName" value="admin" />--%>
<c:set var="sessionName" value="user"/>

<!-- =================== 상단 공통 영역 =================== -->
<div class="top_bar">
    <h2 style="margin-top: 1%; margin-left: 2%;">한강 수자원 관리 종합플랫폼</h2>
    <%--    <div class="top_bar_text">--%>
    <%--        <c:if test="${sessionName eq 'admin'}">--%>
    <%--            ${sessionName } 계정입니다.--%>
    <%--            <a href="<c:url value="/logout" />"><input type="button" class="logout_button" value="로그아웃"></a>--%>

    <%--        </c:if>--%>
    <%--        <c:if test="${sessionName ne 'admin'}">--%>
    <%--            <c:choose>--%>
    <%--                <c:when test="${sessionName != null }">--%>
    <%--                    ${sessionName }님 안녕하세요--%>
    <%--                    <a href="<c:url value="/logout" />"><input type="button" class="logout_button" value="로그아웃"></a>--%>
    <%--                    <hr>--%>
    <%--                </c:when>--%>
    <%--                <c:otherwise>--%>
    <%--                </c:otherwise>--%>
    <%--            </c:choose>--%>
    <%--        </c:if>--%>
    <%--    </div>--%>
</div>

<!-- =================== 관리자 전용 영역 =================== -->
<c:if test="${sessionName eq 'admin'}">
    <div class="admin_panel" style="position:fixed; top:30%; left:40%;">
        <button onclick="location.href='/project_1108/notices'">공지사항</button>
        <br><br><br>
        <button onclick="location.href='/project_1108/waterForecasts'">갈수 예보</button>
        <br><br><br>
        <button onclick="location.href='/project_1108/droughtForecasts'">가뭄 예경보</button>
        <br><br><br>
        <button onclick="location.href='/project_1108/waterViews'">갈수 전망</button>
        <br><br><br>
        <button onclick="location.href='/project_1108/waterRequests'">용수 신청 목록</button>
        <br><br><br>
        <div id="noticeArea"></div>
    </div>
</c:if>

<!-- =================== 일반 사용자 영역 =================== -->
<c:if test="${sessionName ne 'admin' && sessionName != null}">
    <!-- 좌측 사이드바 -->
    <div class="left_sideBar">
        <input type="button" class="sideBar_button" onclick="toggleWaterMarker()" value="수문"/>
        <input type="button" class="sideBar_button" onclick="toggleDamMarker()" value="댐"/>
        <input type="button" class="sideBar_button" onclick="location.href='/project_1108/hacheon'" value="하천"/>
        <input type="button" class="sideBar_button" onclick="toggleWeatherMarker()" value="기상"/>
        <input type="button" class="sideBar_button" onclick="toggleCctvMarkers()" value="CCTV"/>
        <input type="button" class="sideBar_button" value="갈수 가뭄 예보 및 전망"/>
        <ul style="text-align:center;">
            <li><a href="/project_1108/waterForecasts">갈수 예보</a></li>
            <li><a href="/project_1108/droughtForecasts">가뭄 예경보</a></li>
            <li><a href="/project_1108/waterViews">갈수 전망</a></li>
        </ul>
        <input type="button" class="sideBar_button" onclick="location.href='/project_1108/waterRequest/add'"
               value="용수 신청"/>
        <input type="button" class="sideBar_button" onclick="location.href='/project_1108/notices'" value="공지사항"/>
    </div>

    <!-- 지도 영역 -->
    <div id="vmap" style="position: fixed; width:73%; height:90%; left:15%; top:10.6%;"></div>

    <!-- 우측 버튼 영역 -->
    <div class="right_sideBar">
        <div id="buttons" style="position: fixed; top: 18%; right:2%;">
            <button type="button" onclick="toggleAllMarkers()">전체 보기</button>
            <br><br>
            <button type="button" onclick="setMode(vw.ol3.BasemapType.GRAPHIC);">배경지도</button>
            <br><br>
            <button type="button" onclick="toggleWhiteMap();">백지도</button>
            <br><br>
            <button type="button" onclick="toggleNightMap();">야간지도</button>
            <br><br>
            <button type="button" onclick="toggleAerialMap();">항공사진</button>
            <br><br>
            <button type="button" onclick="toggleHybridMap();">하이브리드</button>
            <br><br>
            <button type="button" onclick="hideAllMarkers();">전체 표시</button>
            <br><br>
            <button type="button" onclick="showAllMarkers();">전체 지우기</button>
            <br><br><br><br>
            <input type="text" id="param" value="" size="20"/>
        </div>
    </div>
</c:if>

<!-- =================== 공통 JS =================== -->
<script src="<c:url value='/resources/js/map-init.js'/>"></script>
<script src="<c:url value='/resources/js/notice-toggle.js' />"></script>
</body>
</html>
