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
            margin-top: 160px;
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
    <title>Title</title>
     <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
     <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/moment.min.js"></script>
     <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/moment-with-locales.min.js"></script>
</head>
<body>
${pt.addr}
${pt.etcaddr}
<form action="precipitation2" method="get">
    <select name="rfobscd" >
        <c:forEach var="dto" items="${pts}">
            <option value="${dto.rfobscd}" selected="${dto.rfobscd == rfobscd}">${dto.etcaddr}</option>
          </c:forEach>
    </select>
    <input type="submit" value="보기">
</form>

<form action="  precipitation2" method="get" onsubmit="return validateForm()">
    <label>시작 날짜 및 시간 선택:</label>
    <input type="datetime-local" id="startDate" name="startDate" required>
    <label>끝 날짜 및 시간 선택:</label>
    <input type="datetime-local" id="endDate" name="endDate" required>

    <input type="hidden" name="rfobscd" value="${rfobscd}">
    <input type="submit" value="제출">
    <select id="sd" name="sd">
        <option value="1">10분</option>
        <option value="2">한시간</option>
        <option value="3">하루</option>
    </select>
</form>


<div class="graph">
         <div class="statistics-graph" style="width: 600px; height: 400px;">
                <canvas id="myChart"></canvas>
            </div>
    <div class="statistics-graph1">
        <table border="1">
        <tr>
         <thead>
           <th>측정일</th>
           <th>강우</th>
            </thead>
           </tr>

           <c:forEach var="dto" items="${list2}">
             <tr>
                <td><fmt:formatDate value="${dto.ymdhm}" pattern="yyyy/MM/dd HH:mm" />     </td>
                <td>${dto.rf}</td>
             </tr>
           </c:forEach>
        </table>
    </div>
</div>
<script>
    var dataList = JSON.parse('${list}');

   var dates = dataList.map(function(item) {
         var ymdhmDate = new Date(item.ymdhm.time);
         var formattedDate = ymdhmDate.toLocaleString('en-US', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' });
         return formattedDate;
     });

    var rain = dataList.map(function(item) {
        return item.rf;
    });
    var ctx = document.getElementById('myChart').getContext('2d');
    var chartData = {
        labels: dates,
        datasets: [
            {
                label: '강우',
                data:  rain,
                borderColor: '#000069',
                borderWidth: 1,
                pointRadius: 0,
                fill: true,
                backgroundColor: '#000069',
          },
        ]
    };

    var myChart = new Chart(ctx, {
      type: 'line',
      data: chartData,
      options: {
          scales: {

          }
      }
  });

      function validateForm() {
        var startDate = new Date(document.getElementById('startDate').value);
        var endDate = new Date(document.getElementById('endDate').value);
        var currentDate = new Date();
        var st = document.getElementById('st').value;

        // 조건 1: startDate와 endDate의 차이가 14일 이하 또는 30일 이하
        var timeDifference = endDate - startDate;
        var daysDifference = timeDifference / (1000 * 60 * 60 * 24);

        if (st == 1) {
            if (daysDifference > 14) {
                alert("시작 날짜와 끝 날짜 간격은 14일 이하여야 합니다.");
                return false;
            }
        } else if (st == 2 || st== 3) {
            if (daysDifference > 30) {
                alert("시작 날짜와 끝 날짜 간격은 30일 이하여야 합니다.");
                return false;
            }
        }

        // 조건 2: startDate와 endDate는 현재 시간 이후
        if (startDate > currentDate || endDate > currentDate) {
            alert("시작 날짜와 끝 날짜는 현재 시간 이전이여야 합니다.");
            return false;
        }

        // 조건 3: startDate가 endDate 이후
        if (startDate > endDate) {
            alert("시작 날짜는 끝 날짜 이후일 수 없습니다.");
            return false;
        }

        // 모든 조건을 충족하면 true 반환하여 폼 제출
        return true;
    }


</script>
</body>



</body>
</html>