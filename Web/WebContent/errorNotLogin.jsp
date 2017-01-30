<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"
%>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="<%=request.getContextPath()%>/theme/style.css" type="text/css">
<TITLE>Login Required</TITLE>
<script>
	if (window.parent) {	
		if ( window.name != "FIRST_PAGE" ) window.close();
		//window.parent.location = '<%=request.getContextPath()%>/errorNotLogin.jsp?message=<%=request.getAttribute("message") + ""%>';		
	}
</script>
</HEAD>


<BODY>
<table border="0" cellspacing="5" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber1" cellpadding="0">
  <tr>
    <td width="100%" style="padding:5; border:1px dotted #CC0000; background-color: #FFCCCC; font-family:Tahoma; font-size:11px; color:#CC0000">
    	<%  if (request.getAttribute("message") != null) {
    			out.print(request.getAttribute("message"));
    		} else if (request.getParameter("message") != null) {
    			out.print(request.getParameter("message"));
    		}
    	%>
    	<br>
    	<hr>
    	<br>
		* Click <a href="<%=request.getContextPath()%>/do/token/tokenExpired" target="_top">here</a> to go to the login page.
		<br>&nbsp;
    </td>
  </tr>
</table>
</BODY>
</HTML>
