/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaron;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Hashtable;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import oracle.security.am.asdk.*;
import org.apache.commons.codec.digest.*;

/**
 *
 * @author aaron
 */
public class OAMLoginServlet extends HttpServlet
{

    private AccessClient ac;

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
        try
        {
            PrintWriter out = response.getWriter();
            ResourceRequest rrq = new ResourceRequest("http", "//amcirillo-linux/myprotectedurl/index.html", "get");
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
                        if (session.isAuthorized(rrq))
                        {
                            String token = session.getSessionToken();
                            out.println("OAM Session Token: " + token + "\n");
                            boolean haveObSSOCookie = false;
                            Cookie currentCookies[] = request.getCookies();
                            if (currentCookies != null)
                            {
                                for (int i = 0; i < currentCookies.length; i++)
                                {
                                    out.println(currentCookies[i].getName() + "\n");
                                    if (currentCookies[i].getName().contentEquals("ObSSOCookie"))
                                    {
                                        haveObSSOCookie = true;
                                    }
                                }
                            }
                            if (!haveObSSOCookie)
                            {
                                DigestUtils digest = new DigestUtils();
                                //oracle support said to do it like this, I think it's wrong
                                //Cookie ObSSOCookie = new Cookie("ObSSOCookie", "ObSSOCookie=" + URLEncoder.encode(session.getSessionToken(), "ISO-8859-1" ));
                                //I'm doing it this way
                                Cookie ObSSOCookie = new Cookie("ObSSOCookie", URLEncoder.encode(session.getSessionToken(), "ISO-8859-1" ));
                                out.println("Adding ObSSOCookie:" + ObSSOCookie.getValue());
                                ObSSOCookie.setDomain(".val.vlss.local");
                                ObSSOCookie.setPath("/");
                                response.addCookie(ObSSOCookie);
                            }
                            else
                            {
                                out.println("ObSSOCookie already detected, not adding\n");
                                
                            }
                        }
                        else
                        {
                            out.println("User not authorized\n");
                        }
                    }
                    else
                    {
                        out.println("User not authenticated");
                    }
                }
                else
                {
                    out.println("For based authentication no detected\n");
                }
            }
            else
            {
                out.println("Resource not protected\n");
            }
        }
        catch (AccessException e)
        {
            Date date = new Date();
            System.out.println(date);
            System.out.println("Access Exception: " + e.getMessage());

        }
    }
}
