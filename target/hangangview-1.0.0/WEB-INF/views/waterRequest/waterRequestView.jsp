<%@page import="java.util.List"%>
<%@page import="spring.AuthInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>용수 신청</title>
<style>
	* {
      margin: 0%;
    }

    .top_bar {
      position: fixed;
      background-color: white;
      height: 10%;
      width: 100%;
      border: 3px solid aqua;
    }

    button {
      margin-top: 2%;
      position: fixed;
      width: 120px;
      height: 25px;
      background-color: aquamarine;
    }
    
    .logout_button {
    	width: 80px;
    	height: 20px;
    	background-color: none;
    	border-bottom: 2px solid black;
    }
    
    .logout_button:hover {
    	cursor: pointer;
    	background-color: darkgrey;
    }
    
    .top_bar_text {
    	text-align:right;
    	margin-right: 2%;
    }
    
    .notices_list {
    	position: fixed;
    	left: 35%;
    	top: 20%;
    }
</style>
</head>
<body>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
   <%
  AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
  if (authInfo != null) {
      String sessionName = authInfo.getName();
	} 
  
  List<String> list = (List<String>) request.getAttribute("list");
%>

<c:set var="sessionName" value="${sessionScope.authInfo != null ? sessionScope.authInfo.name : null}" />

<div class="top_bar">

	<h2 style="margin-top: 1%; margin-left: 2%;">한강 수자원 관리 종합플랫폼</h2>
	<div class="top_bar_text">
	
	<c:if test="${sessionName eq 'admin'}">
		${sessionName } 계정입니다.
		<a href="<c:url value="/logout" />"><input type="button" class="logout_button" value="로그아웃"></a>
		
	</c:if>
    <c:if test="${sessionName ne 'admin'}">
    	<c:choose>
    		<c:when test="${sessionName != null }">
    		${sessionName }님 안녕하세요
    		<a href="<c:url value="/logout" />"><input type="button" class="logout_button" value="로그아웃"></a>
    		<hr>
    		</c:when>
    		<c:otherwise>
    		</c:otherwise>
    	</c:choose>
    </c:if>
    
    </div>
    
</div>
<div class="notices_list">

    <h2>용수 신청서</h2>

    <table border="1">
        <tr>
            <th>이름</th>
            <td>${waterRequest.name}</td>
        </tr>
        <tr>
            <th>신청자</th>
            <td>${waterRequest.applicant}</td>
        </tr>
        <tr>
            <th>전화번호</th>
            <td>${waterRequest.tel}</td>
        </tr>
        <tr>
            <th>E.mail</th>
            <td>${waterRequest.email}</td>
        </tr>
        <tr>
            <th>Fax</th>
            <td>${waterRequest.fax}</td>
        </tr>
        <tr>
            <th>신청유역</th>
            <td>${waterRequest.river}</td>
        </tr>
        <tr>
            <th>Message</th>
            <td>${waterRequest.message}</td>
        </tr>
        <tr>
            <th>제출 시간</th>
            <td>
                <fmt:formatDate value="${waterRequest.requestDate}" pattern="yyyy-MM-dd HH:mm"/>
            </td>
        </tr>
        <tr>
            <th>첨부파일</th>
            <td>
                <c:if test="${!empty attachedFiles}">
        <p> </p>
        <ul>
             <c:forEach items="${attachedFiles}" var="file" varStatus="status">
            <li>첨부파일${status.index + 1} : <a href="/hangangsave/${file.filePath}" download="${file.filePath}">${file.filePath}</a></li>
        </c:forEach>
        </ul>
    </c:if>
            </td>
        </tr>
    </table>

    

    <a href="${pageContext.request.contextPath}/waterRequests">목록</a>
</div>
</body>
</html>
