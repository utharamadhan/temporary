<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"
isErrorPage="true"
import="java.io.*"
%>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<TITLE>Error</TITLE>
<LINK rel="stylesheet" href="<%=request.getContextPath()%>/theme/style.css" type="text/css">
<style type="text/css">
body.error {
	background: url(bg_error.png) no-repeat center top;
	margin: 0px;
	padding: 0px;
	font-family: Arial, Helvetica, sans-serif;
	display: block;
}
.msg {
	margin: 0px 0px 0px 100px;
	padding: 0px;
	display: block;
	position:relative;
}
.msg .msg {
	margin: 0px 0px 0px 100px;
	padding: 0px;
	height: auto;
	width: auto;
	overflow: hidden;
}
.msg h1 {
	display: block;
	font-size: 5em;
	color: #FC0;
	margin: 50px;
	padding: 0px;
	letter-spacing: -5px;
}
.msg div, .msg p, .msg h2, .msg a, .msg ul {
	display: block;
	color: #FFF;
}
.msg a.back {
	padding:10px;
	background:#FC0;
	bottom:0px;
	text-decoration: none;
	width: 150px;
	text-align: center;
	margin: 0px 0px 0px 100px;
	color: #0B67A1;
	-moz-border-radius: 5px;
	-webkit-border-radius: 5px;
	border-radius: 5px;
}
</style>
</HEAD>

<BODY class="error">
<div class="msg" style="background-image:url('<%=request.getContextPath()%>/images/PageNotFound.jpg') ; height: 600px; width:800px; background-repeat:no-repeat; background-position:center center;">
</div>
</BODY>
</HTML>
