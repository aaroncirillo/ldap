<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%
    String error = request.getParameter("error");
    if (error == null || error == "null")
    {
        error = "";
    }
    String paramName = "request_id";
    String reqId = request.getParameter(paramName);

%>
<html>
    <head>
        <title>User Login JSP</title>
        <script>
            function trim(s)
            {
                return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
            }

            function validate()
            {
                if(trim(document.frmLogin.sUserName.value)=="")
                {
                    alert("Login empty");
                    document.frmLogin.sUserName.focus();
                    return false;
                }
                else if(trim(document.frmLogin.sPwd.value)=="")
                {
                    alert("password empty");
                    document.frmLogin.sPwd.focus();
                    return false;
                }
            }
        </script>
    </head>

    <body>
        
        <p>
            &nbsp;
        </p>
        <div><%=error%></div>
        <form name="frmLogin" onSubmit="return validate();" action="https://valwgpoam001vm.val.vlss.local:14101/oam/server/auth_cred_submit" method="post">
            <p>
                User Name<input type="text" name="username"/><br/>Password &nbsp;<input type="password"
                                                                                        name="password"/>
                <input name="request_id" value="<%=reqId%>" type="hidden">  <br/>
            </p>
            <p>
                <input type="submit" name="sSubmit" value="Submit"/>
            </p>
        </form>
    </body>
</html>