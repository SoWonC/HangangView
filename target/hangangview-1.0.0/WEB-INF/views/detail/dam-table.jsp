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

<div class="statistics-graph1">
  <table border="1">
    <thead>
    <tr>
      <th>측정일</th>
      <th>수위</th>
      <th>유량</th>
      <th>유입량</th>
      <th>저수량</th>
      <th>총 방류 유량</th>
    </tr>
    </thead>
    <tbody>
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
    </tbody>
  </table>
</div>
</div>


</body>
</html>
