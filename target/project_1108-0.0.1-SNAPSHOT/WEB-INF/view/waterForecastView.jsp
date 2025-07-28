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
    <title>내용</title>
    
    <script>
        function confirmDelete() {
            if(confirm("해당 게시글을 삭제하시겠습니까?")) {
                window.location.href = '/project_1108/waterForecast/delete?id=${waterForecast.id}';
                alert("삭제가 완료되었습니다.");
            }
        }
        
        function goBack() {
            window.location.href = '/project_1108/waterForecasts?searchType=${param.searchType}&page=${param.page}&size=${param.size}&searchTerm=${param.searchTerm}';
        }
    </script>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
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
    <h1>제목 : ${waterForecast.title}</h1>
    <p>등록일자 : <fmt:formatDate value="${waterForecast.registerDate}" pattern="yyyy-MM-dd"/></p>
    <p>담당부서 : ${waterForecast.department}</p>
    <p>담당자 : ${waterForecast.personInCharge}</p>
    <p>전화번호 : ${waterForecast.phoneNumber}</p>
    <p>조회수 : ${waterForecast.viewCount}</p>
    <p>내용 : ${waterForecast.content}</p>
    
    <c:if test="${!empty attachedFiles}">
        <p> </p>
        <ul>
             <c:forEach items="${attachedFiles}" var="file" varStatus="status">
            <li>첨부파일${status.index + 1} : <a href="/hangangsave/${file.filePath}" download="${file.filePath}">${file.filePath}</a></li>
        </c:forEach>
        </ul>
    </c:if>
    
    <button onclick="goBack()">뒤로 가기</button><br><br>
    <button style="width: auto;" onclick="location.href='/project_1108/waterForecasts'">목록으로 돌아가기</button><br><br>
    
    <c:set var="sessionName" value="${sessionScope.authInfo != null ? sessionScope.authInfo.name : null}" />
    <c:if test="${sessionName eq 'admin'}">
  	  <button onclick="location.href='/project_1108/waterForecast/modify?id=${waterForecast.id}'">수정하기</button><br><br>
   	  <button onclick="confirmDelete()">삭제하기</button>
    </c:if>
    </div>
</body>
</html>
