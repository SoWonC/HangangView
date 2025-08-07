<%--
  Created by IntelliJ IDEA.
  User: GEO2
  Date: 25. 8. 6.
  Time: 오후 6:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<div class="graph">
  <div class="statistics-graph" style="width: 600px; height: 400px;">
    <canvas id="myChart"></canvas>
  </div>



<script>
  var dataList = JSON.parse('${dam}');
  var dates = dataList.map(item => {
    var ymdhmDate = new Date(item.ymdhm.time);
    return ymdhmDate.toLocaleString('en-US', {
      year: 'numeric', month: '2-digit', day: '2-digit',
      hour: '2-digit', minute: '2-digit'
    });
  });

  var swl = dataList.map(item => item.swl);
  var ecpc = dataList.map(item => item.ecpc);
  var inf = dataList.map(item => item.inf);
  var sfw = dataList.map(item => item.sfw);
  var tototf = dataList.map(item => item.tototf);
  var fldlmtwl = dataList.map(item => item.fldlmtwl);
  var pfh = dataList.map(item => item.pfh);

  var ctx = document.getElementById('myChart').getContext('2d');
  var myChart = new Chart(ctx, {
    type: 'line',
    data: {
      labels: dates,
      datasets: [
        { label: '수위', data: swl, borderColor: '#00BFFF', pointRadius: 0, fill: true, backgroundColor: 'rgba(0, 0, 65, 0.7)', lineTension: 0.6 },
        { label: '유량', data: ecpc, borderColor: '#3DFF92', pointRadius: 0, fill: false, lineTension: 0.6 },
        { label: '유입량', data: inf, borderColor: '#FFE650', pointRadius: 0, fill: true, backgroundColor: 'rgba(255, 255, 0, 0.3)' },
        { label: '저수량', data: sfw, borderColor: '#64CD3C', pointRadius: 0, fill: false },
        { label: '총 방류량', data: tototf, borderColor: '#0000CD', pointRadius: 0, fill: false },
        { label: '홍수 우려 수위', data: fldlmtwl, borderColor: '#CD0000', pointRadius: 0, fill: false },
        { label: '수위 제한선', data: pfh, borderColor: '#000000', pointRadius: 0, fill: false }
      ]
    },
    options: { scales: {} }
  });

  function validateForm() {
    var startDate = new Date(document.getElementById('startDate').value);
    var endDate = new Date(document.getElementById('endDate').value);
    var currentDate = new Date();
    var st = document.getElementById('sd').value;

    var timeDifference = endDate - startDate;
    var daysDifference = timeDifference / (1000 * 60 * 60 * 24);

    if ((st == 1 && daysDifference > 14) || ((st == 2 || st == 3) && daysDifference > 30)) {
      alert("날짜 간격이 허용 범위를 초과합니다.");
      return false;
    }

    if (startDate > currentDate || endDate > currentDate) {
      alert("시작/끝 날짜는 현재 시간 이전이어야 합니다.");
      return false;
    }

    if (startDate > endDate) {
      alert("시작 날짜는 끝 날짜보다 빠를 수 없습니다.");
      return false;
    }

    return true;
  }
</script>
</body>
</html>
