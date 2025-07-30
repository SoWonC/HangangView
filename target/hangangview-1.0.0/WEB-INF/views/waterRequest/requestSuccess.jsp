<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>신청 성공</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f3f3f3;
            text-align: center;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
        }
        h1 {
            color: #333;
        }
        .success-message {
            color: green;
            font-weight: bold;
            margin-top: 20px;
        }
        .info-label {
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>신청이 성공적으로 제출되었습니다!</h1>
        <div class="success-message">
            신청 내용이 정상적으로 저장되었습니다.
        </div>
        <hr>
   
    </div>
     <a href="${pageContext.request.contextPath}/main">페이지로 돌아가기</a>
</body>
</html>
