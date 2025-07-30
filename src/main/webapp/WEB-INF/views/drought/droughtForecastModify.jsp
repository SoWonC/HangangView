<%@page import="java.util.List"%>
<%@page import="spring.AuthInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>가뭄 예경보 수정</title>
<script>
    function deleteFile(fileId, droughtForecastId) {
        var xhr = new XMLHttpRequest();
        xhr.open('GET', '/project_1108/droughtForecast/deleteFile?fileId=' + fileId + '&droughtForecastId=' + droughtForecastId, true);
        xhr.onreadystatechange = function() {
            if (xhr.readyState == 4 && xhr.status == 200) {
                var li = document.getElementById('file-' + fileId);
                if (li) {
                    li.parentNode.removeChild(li);
                }
            }
        }
        xhr.send();
    }
    
    
    function submitForm() {
        if (confirm("해당 게시글을 수정하시겠습니까?")) {
            var form = document.getElementById('editForm');
            var formData = new FormData(form);

            var xhr = new XMLHttpRequest();
            xhr.open("POST", "/project_1108/droughtForecast/modify", true);
            xhr.onreadystatechange = function() {
                if (xhr.readyState == 4 && xhr.status == 200) {
                    alert("수정이 완료되었습니다");
                    window.location.href = '/project_1108/droughtForecasts';
                }
            };
            xhr.send(formData);
        }
        return false; 
    }
    </script>
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
    <h1>가뭄 예경보 수정</h1>
    
    
    
    <form id="editForm" onsubmit="return submitForm()" method="post" enctype="multipart/form-data">
        <input type="hidden" name="id" value="${droughtForecast.id}"/>
        
        <label for="title">제목:</label>
        <input type="text" id="title" name="title" value="${droughtForecast.title}" required/>
        <br>
        
        <label for="department">담당부서:</label>
        <input type="text" id="department" name="department" value="${droughtForecast.department}" required/>
        <br>
        
        <label for="personInCharge">담당자:</label>
        <input type="text" id="personInCharge" name="personInCharge" value="${droughtForecast.personInCharge}" required readonly="true"/>
        <br>
        
        <label for="phoneNumber">전화번호:</label>
        <input type="text" id="phoneNumber" name="phoneNumber" value="${droughtForecast.phoneNumber}" required/>
        <br>
        
        <label for="content">내용:</label>
        <textarea id="content" name="content" rows="5" required>${droughtForecast.content}</textarea>
        <br>
        
        <p>현재 첨부된 파일 목록:</p>
        <ul>
            <c:forEach items="${attachedFiles}" var="file" varStatus="status">
                <li id="file-${file.fileId}">
                    첨부파일${status.index + 1} : ${file.filePath}
                    <a href="javascript:deleteFile(${file.fileId}, ${droughtForecast.id})">[삭제]</a>
                </li>
            </c:forEach>
        </ul>
        
        <label for="newFiles">새로운 첨부파일 추가:</label>
        <input type="file" id="newFiles" name="newFiles" multiple accept=".jpg, .jpeg, .png, .pdf"/>
        
        <input type="submit" value="수정"/>
    </form>
    
    <button onclick="location.href='/project_1108/droughtForecast?id=${droughtForecast.id}'">뒤로가기</button>
</div>
</body>
</html>
