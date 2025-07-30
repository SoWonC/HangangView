<%@page import="java.util.List"%>
<%@page import="spring.AuthInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>가뭄 예경보 등록</title>
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
<!-- <script>
                alert("관리자만 접근할 수 있습니다.");
                window.location.href = '/project_1108/droughtForecasts';
</script> -->
</head>
<body>
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
            <h1>가뭄 예경보 등록하기</h1>
            <form:form action="${pageContext.request.contextPath}/droughtForecast/add" method="post" modelAttribute="droughtForecast" enctype="multipart/form-data">
                <div>
                    <form:label path="title">제목:</form:label><br>
                    <form:input path="title" required="true"/><br>
                </div>
                
                <div>
                    <form:label path="department">담당부서:</form:label><br>
                    <form:input path="department" required="true"/><br>
                </div>
                
                <div>
                    <form:label path="personInCharge">담당자:</form:label><br>
                    <form:input path="personInCharge" required="true" readonly="true"/><br>
                </div>
                
                <div>
                    <form:label path="phoneNumber">전화번호:</form:label><br>
                    <form:input path="phoneNumber" required="true"/><br>
                </div>
                
                <div>
                    <form:label path="content">내용:</form:label><br>
                    <form:textarea path="content" rows="5" cols="30" required="true"></form:textarea><br>
                </div>
                
                <div>
                    <form:label path="attachedFiles">첨부파일:</form:label><br>
                    <input id="attachedFiles" name="attachedFiles" type="file" multiple="true"/><br>
                </div>
                
                <input type="submit" value="가뭄 예경보 등록"/>
            </form:form>
            <button onclick="location.href='/project_1108/droughtForecasts'">취소</button>
</div>
</body>
</html>
