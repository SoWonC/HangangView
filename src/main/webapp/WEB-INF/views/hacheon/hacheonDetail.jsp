<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Hacheon Detail Information</title>
<style>


/* 추가적인 스타일들 */
#map {
   width: 100%;
   height: 400px;
   margin-top: 20px;
}
</style>
<style>
        /* 스타일들 */
        table {
            width: 70%;
            margin: 20px auto;
            border-collapse: collapse;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        th {
            background-color: #f2f2f2;
            padding: 10px;
            text-align: left;
        }

        td {
            padding: 10px;
            border-bottom: 1px solid #ddd;
        }

        th, td {
            font-family: Arial, sans-serif;
        }

        .hidden {
            display: none;
        }
    </style>

<script src="https://cdn.jsdelivr.net/npm/ol@v8.2.0/dist/ol.js"></script>
<link rel="stylesheet"
   href="https://cdn.jsdelivr.net/npm/ol@v8.2.0/ol.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"></script>
</head>

<body>
   <h1>Hacheon Detail Information</h1>

<div class="accordion accordion-flush" id="accordionFlushExample">
  <div class="accordion-item">
    <h2 class="accordion-header">
      <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapseOne" aria-expanded="false" aria-controls="flush-collapseOne">
        하천정보
      </button>
    </h2>
    <div id="flush-collapseOne" class="accordion-collapse collapse" data-bs-parent="#accordionFlushExample">
      <div class="accordion-body">
      <table>
      <tr>
         <th>하천명</th>
         <td>${hacheonDetailList.hacheonName}</td>
      </tr>
      <tr>
         <th>하천코드</th>
         <td>${hacheonDetailList.hacheonCode}</td>
      </tr>
      <tr>
         <th>하천등급</th>
         <td>${hacheonDetailList.hacheonGrade}</td>
      </tr>
      </table>
      
      </div>
    </div>
  </div>
  <div class="accordion-item">
    <h2 class="accordion-header">
      <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapseTwo" aria-expanded="false" aria-controls="flush-collapseTwo">
        구간정보
      </button>
    </h2>
    <div id="flush-collapseTwo" class="accordion-collapse collapse" data-bs-parent="#accordionFlushExample">
      <div class="accordion-body">
<table>
<tr>
         <th>기점 위치 시도</th>
         <td>${hacheonDetailList.startLocationProvince}</td>
      </tr>
      <tr>
         <th>기점 위치 시군구</th>
         <td>${hacheonDetailList.startLocationDistrict}</td>
      </tr>
      <tr>
         <th>기점 위치 읍면동</th>
         <td>${hacheonDetailList.startLocationTown}</td>
      </tr>
      <tr>
         <th>기점 위치 경계</th>
         <td>${hacheonDetailList.startLocationBoundary}</td>
      </tr>
      <tr>
         <th>기점 계획 빈도</th>
         <td>${hacheonDetailList.startPlanFrequency}</td>
      </tr>
      <tr>
         <th>기점 계획 홍수량</th>
         <td>${hacheonDetailList.startPlanFloodVolume}</td>
      </tr>
      <tr>
         <th>기점 계획 홍수위</th>
         <td>${hacheonDetailList.startPlanFloodLevel}</td>
      </tr>
      <tr>
         <th>기점 계획 하폭</th>
         <td>${hacheonDetailList.startPlanWidth}</td>
      </tr>
      <tr>
         <th>종점 위치 시도</th>
         <td>${hacheonDetailList.endLocationProvince}</td>
      </tr>
      <tr>
         <th>종점 위치 시군구</th>
         <td>${hacheonDetailList.endLocationDistrict}</td>
      </tr>
      <tr>
         <th>종점 위치 읍면동</th>
         <td>${hacheonDetailList.endLocationTown}</td>
      </tr>
      <tr>
         <th>종점 위치 경계</th>
         <td>${hacheonDetailList.endLocationBoundary}</td>
      </tr>
      <tr>
         <th>종점 계획 빈도</th>
         <td>${hacheonDetailList.endPlanFrequency}</td>
      </tr>
      <tr>
         <th>종점 계획 홍수량</th>
         <td>${hacheonDetailList.endPlanFloodVolume}</td>
      </tr>
      <tr>
         <th>종점 계획 홍수위</th>
         <td>${hacheonDetailList.endPlanFloodLevel}</td>
      </tr>
      <tr>
         <th>종점 계획 하폭</th>
         <td>${hacheonDetailList.endPlanWidth}</td>
      </tr>

</table>
      </div>
    </div>
  </div>
  <div class="accordion-item">
    <h2 class="accordion-header">
      <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapseThree" aria-expanded="false" aria-controls="flush-collapseThree">
        연장,정비
      </button>
    </h2>
    <div id="flush-collapseThree" class="accordion-collapse collapse" data-bs-parent="#accordionFlushExample">
      <div class="accordion-body">
     <table>
     <tr>
         <th>하천연장 계</th>
         <td>${hacheonDetailList.riverLength}</td>
      </tr>
      <tr>
         <th>하천연장 하천기본계획 수립구간 고시일</th>
         <td>${hacheonDetailList.riverPlanDeclaredDate}</td>
      </tr>
      <tr>
         <th>하천연장 하천기본계획 수립구간 연장</th>
         <td>${hacheonDetailList.riverPlanExtension}</td>
      </tr>
      <tr>
         <th>하천연장 하천기본계획 미수립구간</th>
         <td>${hacheonDetailList.riverPlanUnestablished}</td>
      </tr>
      <tr>
         <th>유로연장</th>
         <td>${hacheonDetailList.euroLength}</td>
      </tr>
      <tr>
         <th>유역면적</th>
         <td>${hacheonDetailList.drainageArea}</td>
      </tr>
      <tr>
         <th>하천정비현황 합계</th>
         <td>${hacheonDetailList.riverRemedyTotal}</td>
      </tr>
      <tr>
         <th>하천정비현황 제방정비완료구간</th>
         <td>${hacheonDetailList.riverRemedyCompletionZone}</td>
      </tr>
      <tr>
         <th>하천정비현황 제방보강필요구간</th>
         <td>${hacheonDetailList.riverRemedyReinforcementZone}</td>
      </tr>
      <tr>
         <th>하천정비현황 제방신설필요구간</th>
         <td>${hacheonDetailList.riverRemedyCreationZone}</td>
      </tr>
     
     
     </table>
       </div>
    </div>
  </div>
</div>

   

   <div id="map" class="map"></div>

    <script>
        function toggleSection(sectionId) {
            var section = document.getElementById(sectionId);
            if (section.style.display === "none") {
                section.style.display = "table-row";
            } else {
                section.style.display = "none";
            }
        }
    </script>
<script>
var hacheonCode = '${hacheonDetailList.hacheonCode}'; // JSP 변수 값을 JavaScript 변수에 할당
console.log("하천코드 값:", hacheonCode); // 콘솔에 하천코드 값 출력

    var map = new ol.Map({
        target: 'map',
        layers: [
            new ol.layer.Tile({
                source: new ol.source.OSM() // 기본 배경지도
            }),
            new ol.layer.Tile({
                source: new ol.source.TileWMS({
                    url: 'http://localhost:8085/geoserver/opengis/wms', // Geoserver WMS URL
                    params: {
                        'LAYERS': 'opengis:original', // 레이어 이름
                        'TILED': true,
                        'CQL_FILTER': 'rvnu=' + hacheonCode // JSP 변수 사용
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
</script>


    
</body>
</html>