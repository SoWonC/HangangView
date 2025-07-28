<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <style>
           body {
               font-family: Arial, sans-serif;
               margin: 0;
               padding: 0;
           }

           header {
               background-color: #333;
               padding: 10px;
               color: white;
               text-align: center;
           }

           .container {
               display: flex;
               margin: 20px;
           }

           .select {
               flex: 1;
           }

           .graph {
               flex: 1;
               display: flex;
           }

           .statistics-graph {
               width: 50%;
           }

           .statistics-graph1 {
            flex: 1;
            position: fixed;
            top: 0;
            right: 0;
            width: 50%;
            height: 100%;
            overflow-y: auto;
            margin-top: 180px;
           }

           form {
               margin-bottom: 20px;
           }

           table {
               width: 100%;
                      border-collapse: collapse;
                      margin-top: 10px;
                      font-size: 11px;
           }

           th, td {
               border: 1px solid #ddd;
               padding: 8px;
               text-align: center;
           }

           th {
               background-color: #f2f2f2;
           }


        .statistics-graph1 thead {
            position: sticky;
            top: 0;
            z-index: 1;
        }
       </style>
    <meta charset="UTF-8">
    <title>Water Gate Data Graph</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/moment.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/moment-with-locales.min.js"></script>
</head>
<body>

<div class="select">
<p>${dam.addr}</p>
<p>${dam.etcaddr}</p>
<form action="dam2" method="get">
    <select name="dmobscd" >
        <c:forEach var="dto" items="${dams}">
            <option value="${dto.dmobscd}" selected="${dto.dmobscd == dmobscd}">${dto.etcaddr}</option>
        </c:forEach>
    </select>
    <input type="submit" value="보기">
</form>
<form action="dam2" method="get" onsubmit="return validateForm()">
    <label>시작 날짜 및 시간 선택:</label>
    <input type="datetime-local" id="startDate" name="startDate" required>
    <label>끝   날짜 및 시간 선택:</label>
    <input type="datetime-local" id="endDate" name="endDate" required>

    <input type="hidden" name="dmobscd" value="${dmobscd}">
    <input type="submit" value="제출">
    <select id="sd" name="sd">
        <option value="1">10분</option>
        <option value="2">한시간</option>
        <option value="3">하루</option>
    </select>
</form>
</div>
<div class="graph">
    <div class="statistics-graph" style="width: 600px; height: 400px;">
        <canvas id="myChart"></canvas>
    </div>
    <div class="statistics-graph1">
        <table border="1">
        <tr>
            <thead>
                <th>측정일</th><th>수위</th><th>유량</th><th>유입량</th><th>저수량</th><th>총 방류 유량</th>
            </thead>
        <tr>
            <c:forEach var="dto" items="${list2}">
                <tr>
                    <td><fmt:formatDate value="${dto.ymdhm}" pattern="yyyy/MM/dd HH:mm" /></td>
                    <td>${dto.swl}</td>
                    <td>${dto.ecpc}</td>
                    <td>${dto.inf}</td>
                    <td>${dto.sfw}</td>
                    <td>${dto.tototf}</td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>

<script>
    var dataList = JSON.parse('${list}')

    var dates = dataList.map(function(item) {
          var ymdhmDate = new Date(item.ymdhm.time);
          var formattedDate = ymdhmDate.toLocaleString('en-US', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' });
          return formattedDate;
      });

    var swl = dataList.map(function(item) {
        return item.swl;
    });

    var ecpc = dataList.map(function(item) {
        return item.ecpc;
    });

    var inf = dataList.map(function(item) {
        return item.inf;
    });

    var sfw = dataList.map(function(item) {
        return item.sfw;
    });

    var tototf = dataList.map(function(item) {
        return item.tototf;
    });

    var fldlmtwl = dataList.map(function(item) {
        return item.fldlmtwl;
    });

    var pfh = dataList.map(function(item) {
        return item.pfh;
    });

    var ctx = document.getElementById('myChart').getContext('2d');
    var chartData = {
        labels: dates,
        datasets: [
            {
                label: '수위',
                data: swl,
                borderColor: '#00BFFF',
                borderWidth: 0,
                pointRadius: 0,
                fill: true,
                backgroundColor: 'rgba(0, 0, 65, 0.7)',
                lineTension: 0.6
            },
            {
                label: '유량',
                data: ecpc,
                borderColor: '#3DFF92',
                borderWidth: 2,
                pointRadius: 0,
                fill: false,
                lineTension: 0.6
            },
            {
                label: '유입량',
                data: inf,
                borderColor: '#FFE650',
                borderWidth: 1,
                pointRadius: 0,
                fill: true,
                backgroundColor: 'rgba(255, 255, 0, 0.3)'
            },
            {
                label: '저수량',
                data: sfw,
                borderColor: '#64CD3C',
                borderWidth: 2,
                pointRadius: 0,
                fill: false
            },
            {
                label: '총 방류량',
                data: tototf,
                borderColor: '#0000CD',
                borderWidth: 2,
                pointRadius: 0,
                fill: false
            },
            {
                label: '홍수 우려 수위',
                data: fldlmtwl,
                borderColor: '#CD0000',
                borderWidth: 1,
                pointRadius: 0,
                fill: false
            },
            {
                label: '수위 제한선',
                data: pfh,
                borderColor: '#000000',
                borderWidth: 1,
                pointRadius: 0,
                fill: false
            }
        ]
    };

    var myChart = new Chart(ctx, {
        type: 'line',
        data: chartData,
        options: {
            scales: {}
        }
    });

    function validateForm() {
        var startDate = new Date(document.getElementById('startDate').value);
        var endDate = new Date(document.getElementById('endDate').value);
        var currentDate = new Date();
        var st = document.getElementById('sd').value;

        var timeDifference = endDate - startDate;
        var daysDifference = timeDifference / (1000 * 60 * 60 * 24);

        if (st == 1) {
            if (daysDifference > 14) {
                alert("시작 날짜와 끝 날짜 간격은 14일 이하여야 합니다.");
                return false;
            }
        } else if (st == 2 || st == 3) {
            if (daysDifference > 30) {
                alert("시작 날짜와 끝 날짜 간격은 30일 이하여야 합니다.");
                return false;
            }
        }

        if (startDate > currentDate || endDate > currentDate) {
            alert("시작 날짜와 끝 날짜는 현재 시간 이전이여야 합니다.");
            return false;
        }

        if (startDate > endDate) {
            alert("시작 날짜는 끝 날짜 이후일 수 없습니다.");
            return false;
        }

        return true;
    }
</script>
</body>
</html>
