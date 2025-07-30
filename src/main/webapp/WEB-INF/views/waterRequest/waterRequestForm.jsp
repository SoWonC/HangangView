<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>용수 신청 등록</title>
    <style>
        .form_container {
            text-align: center;
        }
    </style>
</head>
<body>
<div class="form_container">
    <h1>용수 신청 등록하기</h1>
    <hr>
    <form:form action="${pageContext.request.contextPath}/waterRequest/add" method="post" modelAttribute="waterRequest" enctype="multipart/form-data">
        <div>
            <form:label path="name">이름 : </form:label><br>
            <form:input path="name" required="true"/><br>
        </div>
        
        <div>
            <form:label path="applicant">신청인 : </form:label><br>
            <form:input path="applicant" required="true" readonly="true"/><br>
        </div>
        
        <div>
            <form:label path="tel">전화번호 : </form:label><br>
            <form:input path="tel" required="true"/><br>
        </div>
        
        <div>
            <form:label path="email">이메일 :</form:label><br>
            <form:input path="email" required="true"/><br>
        </div>
        
        <div>
            <form:label path="fax">팩스 :</form:label><br>
            <form:input path="fax" required="true"/><br>
        </div>
        
        <div>
            <label for="river">신청유역 및 지사:</label><br>
            <select name="river" id="river">
                <option value="금강유역본부">금강유역본부</option>
                <option value="낙동강유역본부">낙동강유역본부</option>
                <option value="염,섬유역본부">염,섬유역본부</option>
                <option value="한강유역본부">한강유역본부</option>
            </select><br>
        </div>
        
        <div>
            <form:label path="message">내용:</form:label><br>
            <form:textarea path="message" rows="5" cols="30" required="true"></form:textarea><br>
        </div>
        
        <div>
            <form:label path="attachedFiles">사업계획서 : </form:label><br>
            <input id="attachedFiles" name="attachedFiles" type="file" multiple="true"/><br><br>
        </div>
        
        <div>
            <form:label path="attachedFiles">용수신청서 : </form:label><br>
            <input id="attachedFiles" name="attachedFiles" type="file" multiple="true"/><br><br>
        </div>
                    <div style="border: 1px solid #ccc; padding: 10px; margin-top: 10px; font-size: 90%;">
    <b>개인정보 수집 및 이용 동의</b>
    <p>개인정보 수집목적 : 댐.광역상수도신청서 접수를 위해 필요한 최소한의 개인정보를 수집</p>
    <p>개인정보 이용목적 : 사용신청 접수 처리.사후관리 서비스 제공</p>
    <p>개인정보 수집항목 : 이름, E-mail, 휴대폰, FAX</p>
    <p>개인정보 보유·이용기간 : 수집된 개인정보 보유기간은 개인정보보호법에 의해 2년이며 고객님의 삭제요청이 있을 시 고객님의 개인정보는 재생이 불가능한 방법으로 즉시 파기되며 이를 알려드립니다.</p>
    <p>동의를 거부할 권리 및 동의를 거부할 경우의 불이익 : 수집하는 최소한의 정보외의 개인정보 수집에 동의를 거부할 권리가 있으나 최소한의 개인정보 수집동의 거부시에는 해당서비스가 제한됩니다.</p>
</div>
        
        
        <input type="submit" value="용수 신청 등록"/>
    </form:form>
     <button onclick="location.href='/project_1108/main'">취소</button>
</div>
</body>
</html>