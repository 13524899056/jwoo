<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>expiration time</title>

<style type="text/css">
body {
	margin: 0;
	padding: 0;
	background:#DCDCDC
}
#div1 {
	width: 90%;
	height: 60%;
	line-height: 200px;
	overflow: hidden;
	position: relative;
	text-align: center;
	margin: auto;
	background:#DCDCDC
}
#tr1{
	width: 90%;
	height: 60%;
	line-height: 200px;
	/*border:1px solid #ccc;*/
	overflow: hidden;
	position: relative;
	text-align: center;
	margin: auto
}
div p {
	position: static; +
	position: absolute;
	top: 50%
}
img {
	position: static; +
	position: relative;
	top: -50%;
	left: -50%;
	width: 90%;
	height: 70%;
	vertical-align: middle
}
span{ 
 	position:absolute;
 	top:58%;
    width: 100%; 
	height:100px;
	line-height:40px; 
	text-align:center; 
 	font-size: 23px;
	display:block
	/* background:#999999 */
}
p:after {
	content: ".";
	font-size: 1px;
	visibility: hidden
}
</style>
</head>
<body>
<table id="tr1">
	<tr id="tr1">
		<td>
			<div id="div1">
				<p><img src="${pageContext.request.contextPath }/resources/images/expiration_time.jpg"/></p>
					<div style="text-align:center;">
						<span >${message}</span>
					</div>
			</div>
		</td>
	</tr>
</table>
</body>
</html>
















