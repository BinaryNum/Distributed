<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="style.css">
<title>Insert title here</title>
</head>
<body>
<div class="sign-up">
<h1>워드카운터</h1>
<form action="file.do" method="post" enctype="multipart/form-data">
	pdf 파일 : <input class="form-control"   type="file" name="pdf" ><br>
	※ 영문파일만 입력 가능합니다.
	 <input type="submit"  class="btn" value="전송">
</form>
</div>
</body>
</html>