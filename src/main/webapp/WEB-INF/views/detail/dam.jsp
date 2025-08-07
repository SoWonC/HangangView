<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>

  <meta charset="UTF-8">
  <title>Water Gate Data Graph</title>
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/moment.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/moment-with-locales.min.js"></script>
  <link rel="stylesheet" href="<c:url value='/resources/css/damStyle.css' />">

</head>
<body>
<script>
  window.dataList = JSON.parse('${list}');
</script>


<div class="select">
  <p>${dam.obsnm}</p>
  <p>${dam.addr}  ${dam.etcaddr}</p>
  <form action="openpopupdam" method="get">
    <select name="dmobscd" >
      <c:forEach var="dto" items="${dams}">
        <option value="${dto.dmobscd}" selected="${dto.dmobscd == dmobscd}">${dto.etcaddr}</option>
      </c:forEach>
    </select>
    <input type="submit" value="보기">
  </form>
  <form action="openpopupdam" method="get" onsubmit="return validateForm()">
    <label>시작 날짜 및 시간 선택:</label>
    <input type="datetime-local" id="startDate" name="startDate" required>
    <label>끝   날짜 및 시간 선택:</label>
    <input type="datetime-local" id="endDate" name="endDate" required>

    <input type="hidden" name="dmobscd" value="${dmobscd}">
    <input type="submit" value="제출">
    <select id="interval" name="interval">
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
<script src="<c:url value='/resources/js/damChart.js' />"></script>
</body>
</html>
