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
    <script>
        function confirmDelete() {
            if(confirm("해당 게시글을 삭제하시겠습니까?")) {
                window.location.href = '/project_1108/waterView/delete?id=${waterView.id}';
                alert("삭제가 완료되었습니다.");
            }
        }
        
        function goBack() {
            window.location.href = '/project_1108/waterViews?searchType=${param.searchType}&page=${param.page}&size=${param.size}&searchTerm=${param.searchTerm}';
        }
    </script>
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
    <h1>제목 : ${waterView.title}</h1>
    <p>등록일자 : <fmt:formatDate value="${waterView.registerDate}" pattern="yyyy-MM-dd"/></p>
    <p>담당부서 : ${waterView.department}</p>
    <p>담당자 : ${waterView.personInCharge}</p>
    <p>전화번호 : ${waterView.phoneNumber}</p>
    <p>조회수 : ${waterView.viewCount}</p>
    <p>내용 : ${waterView.content}</p>
    
    <c:if test="${!empty attachedFiles}">
        <p> </p>
        <ul>
             <c:forEach items="${attachedFiles}" var="file" varStatus="status">
            <li>첨부파일${status.index + 1} : <a href="/hangangsave/${file.filePath}" download="${file.filePath}">${file.filePath}</a></li>
        </c:forEach>
        </ul>
    </c:if>
    
    <button onclick="goBack()">뒤로 가기</button><br><br>
    <button style="width: auto;" onclick="location.href='/project_1108/waterViews'">목록으로 돌아가기</button><br><br>
    
    <c:set var="sessionName" value="${sessionScope.authInfo != null ? sessionScope.authInfo.name : null}" />
    <c:if test="${sessionName eq 'admin'}">
    	<button onclick="location.href='/project_1108/waterView/modify?id=${waterView.id}'">수정하기</button><br><br>
    	<button onclick="confirmDelete()">삭제하기</button>
     </c:if>
</div>
</body>
</html>
