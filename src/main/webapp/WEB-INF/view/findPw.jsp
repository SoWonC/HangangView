
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
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
	height: 400px;
	border: solid 1px black;
}

.login_text {
  margin-top: 3%;
  margin-bottom: 2%;
  text-align: center;
  color: grey;
}


.name_div {
  position: relative;
  width: 300px;
  margin-left: 18%;
  margin-top: 10%;
}

.email_div {
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

.last_buttons {
  margin-left: 18%;
}

.findPw_button {
  width: 300px;
  height: 40px;
  background-color: lightseagreen;
  color: white;
  border: none;
}

.findPw_button:hover {
  cursor: pointer;
}

.previous_button {
  width: 300px;
  height: 40px;
  border-color: lightseagreen;
  background-color: white;
  color: black;
}

.previous_button:hover {
  cursor: pointer;
}
  </style>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
	function maskPassword(password) {
		if (password.length < 4) {
			return '*'.repeat(password.length);
		}
		
		var maskedText = password.substring(0, password.length - 4) + '****'; // 뒤 두 자리를 '**'로 대체
        return maskedText;
	}

	function findPw() {
		var memName = $("#name").val();
		var memEmail = $("#email").val();

		var sendData = {
			name : memName,
			email : memEmail
		};

		$.ajax({
			url : "findPw",
			method : "POST",
			data : sendData,
			dataType : "text",
			success : function(text) {
				if (text == 0) {
					$("#result_pw").html("해당하는 정보가 없습니다.");
				} else {
					var maskedPassword = maskPassword(text);
					$("#result_pw").html("비밀번호= " + maskedPassword);
				}
			},

		})
	}
</script>
</head>
<body>
<div class="middle_container">
  <form action="findPw" method="POST">
  <h2 class="login_text">비밀번호</h2>
  <hr>
  <div class="name_div">
    <input type="text" name="name" id="name" required>
    <label>NAME</label>
    <span></span>
  </div>
  <div class="email_div">
    <input type="text" name="email" id="email" required>
    <label>EMAIL</label>
    <span></span>
  </div>
  <br>
  <div class="last_buttons">  
    <button type="button" class="findPw_button" onclick="findPw()">비밀번호 찾기</button><br><br>
    <button type="button" class="previous_button" onclick="location.href='/project_1108/'">이전</button>
  </div>
  <br>
  <div class="findPw_container">
    <div id="result_pw" style="text-align: center; color:red;"></div>
  </div>
</div>
</form>
</body>
</html>