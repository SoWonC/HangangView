<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script src="https://map.vworld.kr/js/vworldMapInit.js.do?version=2.0&apiKey=109C097E-3484-3D28-B6DA-F484D2E5FDB2"></script>
    <meta charset="utf-8"/>
    <meta content="width=device-width, initial-scale=1, shrink-to-fit=no" name="viewport"/>
    <title>공간 정보 시스템</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="/hangang/resources/assets/favicon.ico" rel="icon" type="image/x-icon"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css?family=Lato:300,400,700,300italic,400italic,700italic"
          rel="stylesheet"/>
    <link href="/hangang/resources/css/styles.css" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Bebas+Neue&display=swap" rel="stylesheet">

</head>
<body>
<nav class="navbar navbar-light bg-light static-top">
    <div class="container">
        <a class="navbar-brand" href="#!">NEW_HANGANHVIEW</a>
<%--        <a class="btn btn-primary" href="#signup">Sign Up</a>--%>
    </div>
</nav>

<header class="masthead">
    <div class="container position-relative">
        <div id="content">
            <div id="vmap" style="width:100%; height:600px; border-radius:8px;"></div>

        </div>

    </div>
</header>

<%--<section class="features-icons bg-light text-center">--%>
<%--    <div class="container">--%>
<%--        <div class="row">--%>
<%--            <div class="col-lg-4">--%>
<%--                <div class="features-icons-item mx-auto mb-5 mb-lg-0 mb-lg-3">--%>
<%--                    <div class="features-icons-icon d-flex">--%>
<%--                        <i class="bi-window m-auto text-primary"></i>--%>
<%--                    </div>--%>
<%--                    <h3>반응형 디자인</h3>--%>
<%--                    <p class="lead mb-0">기기 크기와 상관없이 잘 보입니다.</p>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--            <div class="col-lg-4">--%>
<%--                <div class="features-icons-item mx-auto mb-5 mb-lg-0 mb-lg-3">--%>
<%--                    <div class="features-icons-icon d-flex">--%>
<%--                        <i class="bi-layers m-auto text-primary"></i>--%>
<%--                    </div>--%>
<%--                    <h3>부트스트랩 5 기반</h3>--%>
<%--                    <p class="lead mb-0">최신 부트스트랩 프레임워크 사용.</p>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--            <div class="col-lg-4">--%>
<%--                <div class="features-icons-item mx-auto mb-0 mb-lg-3">--%>
<%--                    <div class="features-icons-icon d-flex">--%>
<%--                        <i class="bi-terminal m-auto text-primary"></i>--%>
<%--                    </div>--%>
<%--                    <h3>사용 용이성</h3>--%>
<%--                    <p class="lead mb-0">쉽게 수정하고 사용할 수 있는 구조입니다.</p>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</section>--%>

<div id="left-panel" style="position: fixed; top: 10%; left: 2%; width: 250px; z-index: 9999;">
    <div class="card shadow-sm p-3">
        <h5 class="mb-3">실시간 수위 정보</h5>
        <div class="table-responsive">
            <table class="table table-sm">
                <thead>
                <tr><th>지점</th><th>수위</th><th>단계</th></tr>
                </thead>
                <tbody>
                <tr><td>송정교</td><td>4.2m</td><td>경고</td></tr>
                <tr><td>의암댐</td><td>5.1m</td><td>주의</td></tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
<!-- 오른쪽 사이드 버튼 영역 -->
<div id="buttons" style="position: fixed; top: 10%; right: 4%; z-index: 9999;">
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

        <hr class="my-3"/>

        <div id="marker-controls" class="form-check form-check-inline">
            <label class="form-check-label d-block">
                <input type="checkbox" class="form-check-input me-1" onchange="togglebridgeMarker(this)" checked> 다리
            </label>
            <label class="form-check-label d-block">
                <input type="checkbox" class="form-check-input me-1" onchange="toggleDamMarker(this)" checked> 댐
            </label>
            <label class="form-check-label d-block">
                <input type="checkbox" class="form-check-input me-1" onchange="togglePrecipitationeMarker(this)" checked> 강수량
            </label>
            <%-- <label class="form-check-label d-block">
              <input type="checkbox" class="form-check-input me-1" onchange="toggleCctvMarker(this)" checked> CCTV
            </label> --%>
        </div>
    </div>
</div>

<footer class="footer bg-light">
    <div class="container">
        <div class="row">
            <!-- 왼쪽 -->
            <div class="col-lg-6 h-100 text-center text-lg-start my-auto">
                <ul class="list-inline mb-2">
                    <li class="list-inline-item"><a href="#!">About</a></li>
                    <li class="list-inline-item">⋅</li>
                    <li class="list-inline-item"><a href="#!">Contact</a></li>
                    <li class="list-inline-item">⋅</li>
                    <li class="list-inline-item"><a href="#!">Terms of Use</a></li>
                    <li class="list-inline-item">⋅</li>
                    <li class="list-inline-item"><a href="#!">Privacy Policy</a></li>
                </ul>
                <p class="text-muted small mb-4 mb-lg-0">© Your Website 2023. All Rights Reserved.</p>
            </div>

            <!-- 오른쪽 -->
            <div class="col-lg-6 h-100 text-center text-lg-end my-auto">
                <ul class="list-inline mb-0">
                    <li class="list-inline-item me-4"><a href="#!"><i class="bi-facebook fs-3"></i></a></li>
                    <li class="list-inline-item me-4"><a href="#!"><i class="bi-twitter fs-3"></i></a></li>
                    <li class="list-inline-item"><a href="#!"><i class="bi-instagram fs-3"></i></a></li>
                </ul>
            </div>
        </div>
    </div>
</footer>
<!-- 부트스트랩 core JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>

<!-- SB Forms용 JS (API 연동용) -->
<script src="https://cdn.startbootstrap.com/sb-forms-latest.js"></script>

<!-- =================== 공통 JS =================== -->
<script src="<c:url value='/resources/js/map-init.js'/>"></script>
<script src="<c:url value='/resources/js/marker-functions.js'/>"></script>
<script src="<c:url value='/resources/js/notice-toggle.js' />"></script>
</body>
</html>
