<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="spring.AuthInfo"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>갈수 전망</title>
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
    <h1>갈수 전망 목록</h1>
    
    <form action="/project_1108/waterViews" method="get">
        <select name="searchType">
            <option value="title" ${searchType == 'title' ? 'selected' : ''}>제목</option>
            <option value="content" ${searchType == 'content' ? 'selected' : ''}>내용</option>
        </select>
        <input type="hidden" name="page" value="${currentPage}"/>
        <input type="hidden" name="size" value="${pageSize}"/>
        <input type="text" name="searchTerm" value="${searchTerm}" placeholder="제목이나 내용 검색"/>
        <input type="submit" value="검색"/>
    </form>
    
    <table border="1">
        <thead>
            <tr>
                <th>번호</th>
                <th>날짜</th>
                <th>제목</th>
                <th>주관부서</th>
                <th>전화번호</th>
                <th>조회수</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="waterView" items="${waterViews}">
                <tr>
                    <td>${waterView.id}</td>
                    <td><fmt:formatDate value="${waterView.registerDate}" pattern="yyyy-MM-dd"/></td>
                    <td><a href="/project_1108/waterView?id=${waterView.id}&searchType=${param.searchType}&page=${param.page}&size=${param.size}&searchTerm=${param.searchTerm}">${waterView.title}</a></td>
                    <td>${waterView.department}</td>
                    <td>${waterView.phoneNumber}</td>
                    <td>${waterView.viewCount}</td>	
                </tr>
            </c:forEach>
        </tbody>
    </table>
    
    <div class="paging-controls">
        <c:if test="${currentPage > 1}">
            <a href="/project_1108/waterViews?searchType=${searchType}&page=${currentPage - 1}&size=${pageSize}&searchTerm=${searchTerm}">&laquo; 이전</a>
        </c:if>
        
        <c:forEach begin="1" end="${totalPages}" var="i">
            <a href="/project_1108/waterViews?searchType=${searchType}&page=${i}&size=${pageSize}&searchTerm=${searchTerm}" class="${currentPage == i ? 'current-page' : ''}">${i}</a>
        </c:forEach>
        
        <c:if test="${currentPage < totalPages}">
            <a href="/project_1108/waterViews?searchType=${searchType}&page=${currentPage + 1}&size=${pageSize}&searchTerm=${searchTerm}">다음 &raquo;</a>
        </c:if>
    </div>

	<c:set var="sessionName" value="${sessionScope.authInfo != null ? sessionScope.authInfo.name : null}" />
    <c:if test="${sessionName eq 'admin'}">
    	<button style="width: auto;" onclick="location.href='/project_1108/waterView/add'">갈수 전망 등록하기</button><br><br>
    </c:if>  
    
 <button style="width: auto;" onclick="location.href='/project_1108/main'">메인으로 돌아가기</button>
 </div>
</body>
</html>
