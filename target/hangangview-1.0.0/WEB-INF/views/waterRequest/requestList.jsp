<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>용수 허가 신청 목록</title>
</head>
<body>
    <h2>용수 허가 신청 목록</h2>
    <c:if test="${!empty droughts}">
        <table border="1">
            <tr>
                <th>고객명</th>
                <th>날짜</th>
                <th>신청인</th>
                <th>휴대폰</th>
                <th>email</th>
            </tr>
            <c:forEach var="drought" items="${droughts}">
                <tr>
                    <td>${drought.id}</td>
                    <td><fmt:formatDate value="${drought.date}" pattern="yyyy-MM-dd HH:mm" /></td>
                    <td><a href="drought?id=${drought.id}">${drought.title}</a></td>
                    <td>${drought.department}</td>
                    <td>${drought.tel}</td>
                    <td>${drought.views}</td>
                </tr>
                <input type="button" value="뒤로가기" onclick="location.href='/project_1108/main'">
            </c:forEach>
        </table>
    </c:if>
    
</body> 