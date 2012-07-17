/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaron;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Hashtable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import oracle.security.am.asdk.*;

/**
 *
 * @author aaron
 */
public class OAMLoginServlet extends HttpServlet
{
    private AccessClient ac;
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        try
        {
            ac = AccessClient.createDefaultInstance("/home/aaron/NetBeansProjects/ldap/build/web/WEB-INF/lib", AccessClient.CompatibilityMode.OAM_10G);
            ResourceRequest rrq = new ResourceRequest("http", request.getParameter("resource"), "get");
            if (rrq.isProtected())
            {
                AuthenticationScheme authnScheme = new AuthenticationScheme(rrq);
                if (authnScheme.isForm())
                {
                    Hashtable credentials = new Hashtable();
                    credentials.put("userid", request.getParameter("userid"));
                    credentials.put("password", request.getParameter("password"));
                    UserSession session = new UserSession(rrq, credentials);
                    if (session.getStatus() == UserSession.LOGGEDIN)
                    {
                        String token = session.getSessionToken();
                        if (session.isAuthorized(rrq))
                        {
                            System.out.println("OAM Session Token: " + token);
                        }
                    }
                }
            }
  
            ac.shutdown();
        }
        catch (AccessException e)
        {
            Date date = new Date();
            System.out.println(date);
            System.out.println("Access Exception: " + e.getMessage());

        }
    }
}
