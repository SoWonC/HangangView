<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
	height: 600px;
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

.name_div {
  position: relative;
  width: 300px;
  margin-left: 18%;
  margin-top: 10%;
}

.password_div {
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
  margin-right: 18%;
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
  margin-left: 18%;
}

.submit_button {
  width: 300px;
  height: 40px;
  background-color: lightseagreen;
  color: white;
  border: none;
}

.submit_button:hover {
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

.email_dupCheck {
  width: 300px;
  height: 40px;
  margin-left: 18%;
  border: gray;
  background-color: blanchedalmond;
}

.email_dupCheck:hover {
  cursor: pointer;
}

#result {
  text-align: center;
  color: lightseagreen;
}
  </style>
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <script>
  $(document).ready(function () {
	    var isEmailValid = false;

	    $("#checkEmail").click(function () {
	        var email = $("#email").val();

	        if (email === "") {
	            alert("이메일을 입력하세요.");
	            return;
	        }

	        // 이메일 중복 확인을 위한 Ajax 요청
	        $.ajax({
	            url: 'checkEmail', // 서버에서 요청을 처리할 URL 경로
	            type: 'POST',
	            data: { email: email },
	            dataType: 'text',
	            success: function (response) {
	                if (parseInt(response) === 1) {
	                    $("#result").html('사용 가능한 이메일');
	                    isEmailValid = true;
	                    // Enable the submit button
	                    $("#submitBtn").prop("disabled", false);
	                } else {
	                    $("#result").html('이메일 중복됨');
	                    isEmailValid = false;
	                    // Disable the submit button
	                    $("#submitBtn").prop("disabled", true);
	                }
	            }
	        });
	    });

	    function validateEmail() {
	        var email = document.getElementById("email").value;
	        var emailForm = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;

	        if (!email.match(emailForm)) {
	            alert("이메일 형식을 다시 확인해주세요.");
	            isEmailValid = false;
	            // Disable the submit button
	            $("#submitBtn").prop("disabled", true);
	            return false;
	        }
	        return true;
	    }

	    function checkPassword() {
	        var password = document.getElementById("password").value;
	        var confirmPassword = document.getElementById("confirmPassword").value;

	        if (password !== confirmPassword) {
	            alert("비밀번호와 확인이 일치하지 않습니다.");
	            return false;
	        }
	        return true;
	    }

	    // 폼 제출 이벤트 처리
	    $("form").submit(function (event) {
	        event.preventDefault(); // 기본 제출 동작 막기

	        if (validateEmail() && checkPassword() && isEmailValid) {
	            // 이메일 유효성 검사, 비밀번호 확인 및 이메일 중복 확인이 모두 만족하면 폼 제출
	            this.submit();
	        } else {
	            alert("이메일 중복 확인을 먼저 진행해주세요.");
	        }
	    });
	});
  </script>

</head>
<body>
<div class="middle_container">
  <h2 class="login_text">회원가입</h2>
  <hr>
  <form action="regist" method="post" onsubmit="return validateEmail() && checkPassword();">
  <div class="email_div">
    <input type="text" name="email" id="email" required>
    <label>EMAIL</label>
    <span></span>
  </div>
  <br>
  <button type="button" id="checkEmail" class="email_dupCheck">중복확인</button><p id="result"></p>
  <div class="name_div">
    <input type="text" name="name" id="name" required>
    <label>NAME</label>
    <span></span>
  </div>
  <div class="password_div">
    <input type="password" name="password" id="password" required>
    <label>PASSWORD</label>
    <span></span>
  </div>
  <div class="password_div">
    <input type="password" name="confirmPassword" id="confirmPassword" required>
    <label>PASSWORD_CHECK</label>
    <span></span>
  </div>
  <br>
  <div class="last_buttons">  
    <button type="submit" class="submit_button">제출</button><br><br>
    <button type="button" class="previous_button" onclick="location.href='/project_1108/'">이전</button>
  </div>
  </form>
</div>
</body>
</html>