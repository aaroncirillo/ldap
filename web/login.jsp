<%@page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.
getServerPort()+path+"/";
String reqId = request.getParameter("request_id");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>OAM Custom Login Form</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="OAM,Custom,Login,Form">
<meta http-equiv="description" content="This is my page">
<!--
<link rel="stylesheet" type="text/css" href="styles.css">
-->
</head>

<body>
<br/>
<br/>
<div style="clear: both; text-align: center;">
<IMG alt=Logo src="images/netegrity_logo.gif">
<img src="images/freeoraclehelp.gif" hight="72" width="72" alt="Welcome to www.freeoraclehelp.com"/>
<br/>
<br/>
</div>

<form action="https://valwgpoam001vm:14101/oam/server/auth_cred_submit" method="post">
<center>
<table width="50%" height=200 border=1 cellpadding=0 cellspacing=0 >
<tr> 
<td>
<table WIDTH="100%" HEIGHT=200 BGCOLOR="#E7E8E8" border=0 cellpadding=0 cellspacing=0 >
        <tr>
          <td ALIGN="CENTER" VALIGN="CENTER" HEIGHT=40 COLSPAN=4 NOWRAP BGCOLOR="#333436">
                <font color="#FFFFFF" size="+1" face="Arial,Helvetica">
                <b>Login to OAM</b></font>
              </td>
        </tr>
<tr>
<td>Username</td>
<td><input type="text" name="username"></td>
</tr>
<tr>
<td>Password</td>
<td><input type="password" name="password"></td>
</tr>
<tr>
<td></td>
<td><input type="hidden" name="request_id" value="<%=reqId%>"></td>
</tr>
<tr>
<td><input type="submit"></td>
<td>&nbsp;</td>
</tr>
</table>
</td>
</tr>
</table>
</center>
</form>
</body>
</html>
