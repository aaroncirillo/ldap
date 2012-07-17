package com.aaron;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import oracle.security.am.asdk.*;

public class LoginServlet extends HttpServlet
{

    public void init(ServletConfig config) throws ServletException
    {
        try
        {

            AccessClient ac = AccessClient.createDefaultInstance("/home/aaron/NetBeansProjects/ldap/build/web/WEB-INF/lib",
                    AccessClient.CompatibilityMode.OAM_10G);
        }
        catch (AccessException ae)
        {
            ae.printStackTrace();
        }
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        AuthenticationScheme authnScheme = null;
        UserSession user = null;
        ResourceRequest resource = null;
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<HTML>");
        out.println("<HEAD><TITLE>LoginServlet: Error Page</TITLE></HEAD>");
        out.println("<BODY>");
        HttpSession session = request.getSession(false);
        String requestedPage = request.getParameter("request");
        String reqMethod = request.getMethod();
        Hashtable cred = new Hashtable();
        try
        {
            if (requestedPage == null || requestedPage.length() == 0)
            {
                out.println("<p>REQUESTED PAGE NOT SPECIFIED\n");
                out.println("</BODY></HTML>");
                return;
            }
            resource = new ResourceRequest("http", requestedPage, "GET");
            if (resource.isProtected())
            {
                authnScheme = new AuthenticationScheme(resource);
                if (authnScheme.isForm())
                {
                    if (session != null)
                    {
                        String sUserName = request.getParameter("userid");
                        String sPassword = request.getParameter("password");
                        System.out.println(sUserName);
                        System.out.println(sPassword);
                        if (sUserName != null)
                        {
                            cred.put("userid", sUserName);
                            cred.put("password", sPassword);
                            user = new UserSession(resource, cred);
                            if (user.getStatus() == UserSession.LOGGEDIN)
                            {
                                if (user.isAuthorized(resource))
                                {
                                    session = request.getSession(true);
                                    session.putValue("user", user);
                                    response.sendRedirect(requestedPage);
                                }
                                else
                                {
                                    out.println("<p>User " + sUserName + " not"
                                            + " authorized for " + requestedPage + "\n");
                                }
                            }
                            else
                            {
                                out.println("<p>User" + sUserName + "NOT LOGGED IN\n");
                            }
                        }
                        else
                        {
                            out.println("<p>USERNAME PARAM REQUIRED\n");
                        }
                    }
                    else
                    {
                        user = (UserSession) session.getValue("user");
                        if (user.getStatus() == UserSession.LOGGEDIN)
                        {
                            out.println("<p>User " + user.getUserIdentity() + " already"
                                    + "LOGGEDIN\n");
                        }
                    }
                }
                else
                {
                    out.println("<p>Resource Page" + requestedPage + " is not"
                            + " protected with BASIC\n");
                }
            }
            else
            {
                out.println("<p>Page " + requestedPage + " is not protected\n");
            }
        }
        catch (AccessException ex)
        {
            out.println(ex);
        }
        out.println("</BODY></HTML>");
    }
}
