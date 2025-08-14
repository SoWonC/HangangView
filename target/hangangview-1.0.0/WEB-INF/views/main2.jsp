<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko" data-bs-theme="light">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    <script>
        window.APP_CTX = '<c:url value="/" />';
    </script>

    <title>공간 정보 시스템 · NEW_HANGANGVIEW</title>

    <!-- 3rd-party -->
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script src="https://map.vworld.kr/js/vworldMapInit.js.do?version=2.0&apiKey=109C097E-3484-3D28-B6DA-F484D2E5FDB2"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Bebas+Neue&family=Lato:wght@300;400;700&display=swap"
          rel="stylesheet"/>

    <!-- App styles -->
    <link href="/hangang/resources/assets/favicon.ico" rel="icon" type="image/x-icon"/>
    <link href="/hangang/resources/css/styles.css" rel="stylesheet"/>

    <style>
        :root {
            --nav-h: 64px;
            --footer-h: 56px;
            --panel-w: 320px;
        }

        html, body {
            height: 100%;
        }

        body {
            font-family: "Lato", system-ui, -apple-system, Segoe UI, Roboto, "Helvetica Neue", Arial, "Apple Color Emoji", "Segoe UI Emoji";
        }

        /* Navbar */
        .app-navbar {
            height: var(--nav-h);
            backdrop-filter: saturate(150%) blur(8px);
        }

        /* Map layout */
        .map-wrap {
            position: relative;
            height: calc(100vh - var(--nav-h) - var(--footer-h));
        }

        #vmap {
            width: 100%;
            height: 100%;
            border-radius: 16px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, .12);
        }

        /* Glass cards */
        .glass-card {
            background: color-mix(in oklab, var(--bs-body-bg) 80%, transparent);
            border: 1px solid color-mix(in oklab, var(--bs-body-color) 12%, transparent);
            border-radius: 16px;
            box-shadow: 0 8px 24px rgba(0, 0, 0, .08);
        }

        /* Left status panel (desktop) */
        .status-panel {
            position: absolute;
            top: 16px;
            left: 16px;
            width: min(90vw, var(--panel-w));
            z-index: 1040;
        }

        /* Floating toolbar (bottom center) */
        .map-toolbar {
            position: absolute;
            left: 50%;
            transform: translateX(-50%);
            bottom: 16px;
            z-index: 1040;
        }

        .toolbar-group {
            gap: 8px;
            backdrop-filter: blur(6px);
        }

        /* Right quick actions (top-right) */
        .quick-actions {
            position: absolute;
            top: 16px;
            right: 16px;
            z-index: 1040;
            display: flex;
            gap: 8px;
        }

        /* Responsive tweaks */
        @media (max-width: 992px) {
            .status-panel {
                display: none;
            }

            .map-wrap {
                height: calc(100vh - var(--nav-h));
            }
        }

        /* Table */
        .table-sm th, .table-sm td {
            vertical-align: middle;
        }

        /* Footer */
        .app-footer {
            height: var(--footer-h);
        }
    </style>
</head>
<body>
<!-- NAVBAR -->
<nav class="navbar navbar-expand app-navbar sticky-top border-bottom bg-body" id="topNav">
    <div class="container-fluid px-3">
        <a class="navbar-brand d-flex align-items-center" href="#">
            <i class="bi bi-geo-alt-fill me-2 text-primary"></i>
            <span class="fw-bold">NEW_HANGANGVIEW</span>
        </a>

        <div class="ms-auto d-flex align-items-center gap-2">
            <button class="btn btn-outline-primary btn-sm" type="button" data-bs-toggle="offcanvas"
                    data-bs-target="#panelLayers" aria-controls="panelLayers">
                <i class="bi bi-sliders"></i> 레이어/표시
            </button>
            <button id="btnTheme" class="btn btn-outline-secondary btn-sm" type="button" aria-label="테마 전환">
                <i class="bi bi-moon-stars"></i>
            </button>
        </div>
    </div>
</nav>

<!-- MAIN: MAP -->
<main class="container-fluid py-3">
    <div class="map-wrap" id="mapWrap">
        <div id="vmap" role="application" aria-label="지도"></div>

        <!-- LEFT: Status (desktop) -->
        <aside class="status-panel">
            <div class="glass-card card p-3">
                <div class="d-flex align-items-center justify-content-between mb-2">
                    <h6 class="mb-0 fw-bold"><i class="bi bi-speedometer me-1"></i> 실시간 수위</h6>
                    <span class="badge text-bg-info-subtle border">업데이트: <span id="statusTime">-</span></span>
                </div>
                <div class="table-responsive" style="max-height: 240px;">
                    <table class="table table-sm align-middle mb-0">
                        <thead class="table-light">
                        <tr>
                            <th>지점</th>
                            <th class="text-end">수위</th>
                            <th class="text-center">단계</th>
                        </tr>
                        </thead>
                        <tbody id="waterBody">
                        <tr>
                            <td>송정교</td>
                            <td class="text-end">4.2 m</td>
                            <td class="text-center"><span class="badge rounded-pill text-bg-warning">경고</span></td>
                        </tr>
                        <tr>
                            <td>의암댐</td>
                            <td class="text-end">5.1 m</td>
                            <td class="text-center"><span class="badge rounded-pill text-bg-primary">주의</span></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="d-flex justify-content-end mt-2">
                    <button class="btn btn-sm btn-outline-secondary" id="btnRefresh"><i
                            class="bi bi-arrow-clockwise"></i> 새로고침
                    </button>
                </div>
            </div>
            <div id="markerInfoCard" class="marker-info-card embedded d-none" role="dialog" aria-live="polite">
                <div class="mic-header">
                    <div class="mic-title">
                        <i class="bi bi-geo-alt-fill me-1"></i><span id="mic-title">지점명</span>
                        <small id="mic-subtitle" class="text-secondary ms-2"></small>
                    </div>
                    <button type="button" class="btn-close" aria-label="닫기" onclick="hideMarkerInfo()"></button>
                </div>
                <div class="mic-body">
                    <div class="d-flex justify-content-between">
                        <div>
                            <div class="text-secondary small">수위</div>
                            <div class="fs-5 fw-bold" id="mic-level">–</div>
                        </div>
                        <div class="text-end">
                            <div class="text-secondary small">단계</div>
                            <span id="mic-stage" class="badge rounded-pill text-bg-secondary">-</span></div>
                    </div>
                    <hr class="my-2"/>
                    <div class="small">
                        <div class="text-secondary">좌표</div>
                        <div id="mic-coord">-</div>
                    </div>
                    <div class="small mt-2">
                        <div class="text-secondary">업데이트</div>
                        <div id="mic-updated">-</div>
                    </div>
                </div>
                <div class="mic-actions">
                    <button class="btn btn-sm btn-primary" id="mic-zoomBtn">여기로 줌</button>
                    <button class="btn btn-sm btn-outline-secondary" onclick="hideMarkerInfo()">닫기</button>
                </div>
            </div>
        </aside>



        <!-- RIGHT: Quick actions -->
        <div class="quick-actions">
            <div class="btn-group shadow-sm" role="group" aria-label="뷰 컨트롤">
                <button class="btn btn-primary btn-sm" onclick="toggleAllMarkers()"><i class="bi bi-eye"></i> 전체 보기
                </button>
                <button class="btn btn-outline-success btn-sm" onclick="showAllMarkers()"><i
                        class="bi bi-check2-circle"></i></button>
                <button class="btn btn-outline-danger btn-sm" onclick="hideAllMarkers()"><i class="bi bi-x-circle"></i>
                </button>
            </div>
        </div>

        <!-- BOTTOM: Basemap toolbar -->
        <div class="map-toolbar">
            <div class="glass-card d-flex toolbar-group px-2 py-2 rounded-4">
                <div class="btn-group" role="group" aria-label="배경지도">
                    <button class="btn btn-outline-secondary btn-sm" onclick="toggleMap()"><i class="bi bi-map"></i> 지도
                    </button>
                    <button class="btn btn-outline-secondary btn-sm" onclick="toggleWhiteMap()"><i
                            class="bi bi-file-earmark"></i> 백
                    </button>
                    <button class="btn btn-outline-secondary btn-sm" onclick="toggleNightMap()"><i
                            class="bi bi-moon"></i>
                        야간
                    </button>
                    <button class="btn btn-outline-secondary btn-sm" onclick="toggleAerialMap()"><i
                            class="bi bi-image"></i>
                        항공
                    </button>
                    <button class="btn btn-outline-secondary btn-sm" onclick="toggleHybridMap()"><i
                            class="bi bi-layers"></i> 하이브리드
                    </button>
                </div>
            </div>
        </div>

    </div>
    <%--    <!-- Marker Info Card -->--%>
    <%--    <div id="markerInfoCard" class="marker-info-card d-none" role="dialog" aria-modal="false" aria-live="polite">--%>
    <%--        <div class="mic-header">--%>
    <%--            <div class="mic-title">--%>
    <%--                <i class="bi bi-geo-alt-fill me-1"></i><span id="mic-title">지점명</span>--%>
    <%--                <small id="mic-subtitle" class="text-secondary ms-2"></small>--%>
    <%--            </div>--%>
    <%--            <button type="button" class="btn-close" aria-label="닫기" onclick="hideMarkerInfo()"></button>--%>
    <%--        </div>--%>

    <%--        <div class="mic-body">--%>
    <%--            <div class="d-flex justify-content-between">--%>
    <%--                <div>--%>
    <%--                    <div class="text-secondary small">수위</div>--%>
    <%--                    <div class="fs-5 fw-bold" id="mic-level">–</div>--%>
    <%--                </div>--%>
    <%--                <div class="text-end">--%>
    <%--                    <div class="text-secondary small">단계</div>--%>
    <%--                    <span id="mic-stage" class="badge rounded-pill text-bg-secondary">-</span>--%>
    <%--                </div>--%>
    <%--            </div>--%>

    <%--            <hr class="my-2"/>--%>

    <%--            <div class="small">--%>
    <%--                <div class="text-secondary">좌표</div>--%>
    <%--                <div id="mic-coord">-</div>--%>
    <%--            </div>--%>

    <%--            <div class="small mt-2">--%>
    <%--                <div class="text-secondary">업데이트</div>--%>
    <%--                <div id="mic-updated">-</div>--%>
    <%--            </div>--%>
    <%--        </div>--%>

    <%--        <div class="mic-actions">--%>
    <%--            <button class="btn btn-sm btn-primary" id="mic-zoomBtn">여기로 줌</button>--%>
    <%--            <button class="btn btn-sm btn-outline-secondary" onclick="hideMarkerInfo()">닫기</button>--%>
    <%--        </div>--%>
    <%--    </div>--%>
</main>

<!-- FOOTER -->
<footer class="app-footer border-top bg-body-tertiary d-none d-lg-block">
    <div class="container-fluid h-100 d-flex align-items-center justify-content-between small">
        <div class="text-muted">© Your Website 2025. All Rights Reserved.</div>
        <ul class="list-inline m-0">
            <li class="list-inline-item"><a class="link-secondary" href="#">About</a></li>
            <li class="list-inline-item">·</li>
            <li class="list-inline-item"><a class="link-secondary" href="#">Contact</a></li>
            <li class="list-inline-item">·</li>
            <li class="list-inline-item"><a class="link-secondary" href="#">Terms</a></li>
        </ul>
    </div>
</footer>

<!-- OFFCANVAS: Layers & markers -->
<div class="offcanvas offcanvas-end" tabindex="-1" id="panelLayers" aria-labelledby="panelLayersLabel">
    <div class="offcanvas-header">
        <h5 class="offcanvas-title" id="panelLayersLabel"><i class="bi bi-sliders me-2"></i> 레이어 / 표시</h5>
        <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
    </div>
    <div class="offcanvas-body">
        <div class="mb-4">
            <div class="form-text mb-2">마커 그룹</div>
            <div class="form-check form-switch">
                <input class="form-check-input" type="checkbox" id="chkBridge" checked
                       onchange="togglebridgeMarker(this)"/>
                <label class="form-check-label" for="chkBridge">다리</label>
            </div>
            <div class="form-check form-switch">
                <input class="form-check-input" type="checkbox" id="chkDam" checked onchange="toggleDamMarker(this)"/>
                <label class="form-check-label" for="chkDam">댐</label>
            </div>
            <div class="form-check form-switch">
                <input class="form-check-input" type="checkbox" id="chkRain" checked
                       onchange="togglePrecipitationeMarker(this)"/>
                <label class="form-check-label" for="chkRain">강수량</label>
            </div>
        </div>

        <hr/>

        <div class="mb-3">
            <div class="form-text mb-2">배경지도</div>
            <div class="btn-group w-100" role="group">
                <button class="btn btn-outline-secondary" onclick="toggleMap()">일반</button>
                <button class="btn btn-outline-secondary" onclick="toggleWhiteMap()">백지도</button>
                <button class="btn btn-outline-secondary" onclick="toggleNightMap()">야간</button>
            </div>
            <div class="btn-group w-100 mt-2" role="group">
                <button class="btn btn-outline-secondary" onclick="toggleAerialMap()">항공</button>
                <button class="btn btn-outline-secondary" onclick="toggleHybridMap()">하이브리드</button>
            </div>
        </div>

        <div class="d-flex gap-2">
            <button class="btn btn-success flex-fill" onclick="showAllMarkers()"><i class="bi bi-check2-circle"></i> 전체
                표시
            </button>
            <button class="btn btn-outline-danger flex-fill" onclick="hideAllMarkers()"><i class="bi bi-x-circle"></i>
                전체 지우기
            </button>
        </div>
    </div>
</div>
<!-- Scripts -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<!-- ✅ 먼저 공통/마커 함수 -->
<script src="<c:url value='/resources/js/marker-functions.js'/>"></script>

<!-- ✅ 그 다음 지도 초기화(여기서 addMarker*/loadMarkers 사용) -->
<script src="<c:url value='/resources/js/map-init.js'/>"></script>

<script src="<c:url value='/resources/js/notice-toggle.js' />"></script>

<script>
    // ===== Theme toggle (light/dark) =====
    (function () {
        const root = document.documentElement;
        const key = 'app-theme';
        const btn = document.getElementById('btnTheme');
        const apply = (mode) => {
            root.setAttribute('data-bs-theme', mode);
            localStorage.setItem(key, mode);
        };
        const current = localStorage.getItem(key) || 'light';
        apply(current);
        btn.addEventListener('click', () => apply(root.getAttribute('data-bs-theme') === 'light' ? 'dark' : 'light'));
    })();

    // ===== Dummy refresh for "실시간 수위" (연동 시 Ajax로 교체) =====
    document.getElementById('btnRefresh').addEventListener('click', () => {
        const t = new Date();
        document.getElementById('statusTime').textContent = t.toLocaleTimeString();
        // TODO: 실제 API 연동 후 tbody 갱신
    });
    // 초기 표시 시간
    document.getElementById('statusTime').textContent = new Date().toLocaleTimeString();
</script>
</body>
</html>
