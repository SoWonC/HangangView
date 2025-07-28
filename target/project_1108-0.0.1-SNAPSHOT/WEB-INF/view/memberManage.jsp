<%@page import="spring.AuthInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<style>
body {
	margin: 0;
	padding: 0;
}

.top_container {
	margin-top: 2%;
	width: 100%;
	height: 100px;
	border-bottom: solid 1px black;
}

.title_name {
	background-color: white;
	color: black;
	font-size: 24px;
	border: none;
	font-weight: 600;
}

.title_name:hover {
	cursor: pointer;
	border: none;
}

.title_h2 {
	margin-left: 5%;
}

.buttons {
	text-align: right;
	margin-right: 5%;
}

.admin_move_buttons {
	margin-left: 27%;
	margin-top: 4%;
}

.choose_button {
	background-color: blue;
	color: white;
	border: 1px solid black;
	border-radius: none;
	width: 300px;
	height: 30px;
}

.choose_button:hover {
	border: none;
	cursor: pointer;
}

.inf_manage {
	background-color: ghostwhite;
	border: 1px solid black;
	border-radius: none;
	width: 300px;
	height: 30px;
}

.inf_manage:hover {
	border: none;
	cursor: pointer;
}

.member_list_table {
	width: 500px;
	margin-top: 5%;
	margin-left: 35%;
}



</style>
<meta charset="UTF-8">
<title>회원 정보</title>
</head>
<body>
<%
  AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
  if (authInfo != null) {
      String sessionName = authInfo.getName();
	} 
%>
<c:set var="sessionName" value="${sessionScope.authInfo != null ? sessionScope.authInfo.name : null}" />
                
		<div class="top_container">
            <div class="top_container_title">
                <h2 class="title_h2">
                <input type="button" class="title_name" value="한강 수자원 관리 종합플랫폼" onclick="location.href='/project_1108/'">
                </h2>
                <div class="buttons">
						${sessionName }님 안녕하세요 
						<a href="<c:url value="/logout" />"><input type="button" value="로그아웃"></a>
					</div>
            </div>
		</div>
		<div class="admin_move_buttons">
			<input type="button" value="수문 정보 관리" class="inf_manage" onclick="location.href='/project_1108/information_management'">
			<input type="button" value="용수 관리" class="inf_manage" onclick="location.href='/project_1108/water_management'">
			<input type="button" value="회원 관리" class="choose_button" onclick="location.href='/project_1108/member_management'">
		</div>
		
    <table border="1" class="member_list_table">
        <thead>
            <tr>
                <th>Email</th>
                <th>회원이름</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="member" items="${list }">
                <tr>
                    <td>${member.email}</td>
                    <td>${member.name}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>