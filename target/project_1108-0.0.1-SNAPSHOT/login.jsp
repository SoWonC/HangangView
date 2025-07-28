<%@page import="spring.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Document</title>

  <style>
    * {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.middle_container {
	margin-top: 10%;
	margin-left: 40%;
	width: 500px;
	height: 450px;
	border: solid 1px black;
}

.login_text {
  margin-top: 3%;
  margin-bottom: 2%;
  text-align: center;
  color: grey;
}

.email_div {
  position: relative;
  width: 300px;
  margin-left: 18%;
  margin-top: 10%;
}

.pwd_div {
  position: relative;
  width: 300px;
  margin-left: 18%;
  margin-top: 10%;
}

input {
  font-size: 15px;
  color: #222222;
  width: 300px;
  border: none;
  border-bottom: solid #aaaaaa 1px;
  padding-bottom: 10px;
  padding-left: 10px;
  position: relative;
  background: none;
  z-index: 5;
}

input::placeholder { color: #aaaaaa; }
input:focus { outline: none; }

span {
  display: block;
  position: absolute;
  bottom: 0;
  left: 0%;  /* right로만 바꿔주면 오 - 왼 */
  background-color: #666;
  width: 0;
  height: 2px;
  border-radius: 2px;
  transition: 0.5s;
}

label {
  position: absolute;
  color: #aaa;
  left: 10px;
  font-size: 20px;
  bottom: 8px;
  transition: all .2s;
}

input:focus ~ label, input:valid ~ label {
  font-size: 16px;
  bottom: 40px;
  color: #666;
  font-weight: bold;
}

input:focus ~ span, input:valid ~ span {
  width: 100%;
}


.buttons_bottom {
  text-align: right;
  margin-right: 20%;
}

.findId_button {
  border: none;
  background-color: white;
}

.findId_button:hover {
  cursor: pointer;
}

.findPwd_button {
  border: none;
  background-color: white;
}

.findPwd_button:hover {
  cursor: pointer;
}

.last_buttons {
  text-align: center;
}

.login_button {
  width: 310px;
  height: 40px;
  background-color: lightseagreen;
  color: white;
  border: none;
}

.login_button:hover {
  cursor: pointer;
}

.regist_button {
  width: 310px;
  height: 40px;
  border-color: lightseagreen;
  background-color: white;
  color: black;
}

.regist_button:hover {
  cursor: pointer;
}

  </style>
</head>
<body>

<div class="middle_container">
  <h2 class="login_text">로그인</h2>
  <hr>
  <form action="login" method="post">
  <div class="email_div">
    <input type="text" name="email" id="email" required>
    <label>ID</label>
    <span></span>
  </div>
  <div class="pwd_div">
    <input type="password" name="password" id="password" required>
    <label>PASSWORD</label>
    <span></span>
  </div>
  <br>
  <div class="buttons_bottom">
    <button type="button" class="findId_button" onclick="location.href='/project_1108/findId'">아이디 찾기</button>&nbsp;|
    <button type="button" class="findPwd_button" onclick="location.href='/project_1108/findPw'">비밀번호 찾기</button><br><br>
  </div>
  <div class="last_buttons">  
    <button type="submit" class="login_button">로그인</button><br><br>
    <button type="button" class="regist_button" onclick="location.href='/project_1108/regist'">회원가입</button>
  </div>
  </form>
</div>
</body>
</html>