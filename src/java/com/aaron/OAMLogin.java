/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//the full documentation for the OAM API is available here:
// http://docs.oracle.com/cd/E21764_01/doc.1111/e12491/as_api.htm#CHDDAJHD


package com.aaron;

import java.sql.Time;
import java.util.Date;
import java.util.Hashtable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import oracle.security.am.asdk.*;

/*
 * @author aaron
 */
@ManagedBean
@RequestScoped
public class OAMLogin
{

    private String username;
    private String password;
    private static final String resource = "//amcirillo-linux/myprotectedurl/index.html";
    private static final String protocol = "http";
    private static final String method = "GET";
    private String resourceProtected;
    private String authenticated;
    private String authorized;
    private String formAuth;
    private String OAMError;
    private String token;
    private UserSession session;

    public String getToken()
    {
        if(token != null)
        {
            return "Session token:" + token;
        }
        else
        {
            return "";
        }
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getOAMError()
    {
        return OAMError;
    }

    public void setOAMError(String OAMError)
    {
        this.OAMError = OAMError;
    }

    public String getAuthenticated()
    {
        return authenticated;
    }

    public void setAuthenticated(String authenticated)
    {
        this.authenticated = authenticated;
    }

    public String getAuthorized()
    {
        return authorized;
    }

    public void setAuthorized(String authorized)
    {
        this.authorized = authorized;
    }

    public String getFormAuth()
    {
        return formAuth;
    }

    public void setFormAuth(String formAuth)
    {
        this.formAuth = formAuth;
    }

    public String getResourceProtected()
    {
        return resourceProtected;
    }

    public void setResourceProtected(String resourceProtected)
    {
        this.resourceProtected = resourceProtected;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * Creates a new instance of OAMLogin
     */
    public OAMLogin()
    {
    }

    public void login()
    {
        AccessClient ac;
        try
        {
            ac = AccessClient.createDefaultInstance("/home/aaron/NetBeansProjects/ldap/build/web/WEB-INF/lib", AccessClient.CompatibilityMode.OAM_10G);
            ResourceRequest rrq = new ResourceRequest(protocol, resource, method);
            if (rrq.isProtected())
            {
                resourceProtected = "Requested resource is protected";
                AuthenticationScheme authnScheme = new AuthenticationScheme(rrq);
                if (authnScheme.isForm())
                {
                    formAuth = "Form based auth detected";
                    Hashtable credentials = new Hashtable();
                    credentials.put("userid", username);
                    credentials.put("password", password);
                    session = new UserSession(rrq, credentials);
                    if (session.getStatus() == UserSession.LOGGEDIN)
                    {
                        authenticated = "User is authenticated to OAM";
                        token = session.getSessionToken();
                        if (session.isAuthorized(rrq))
                        {
                            authorized = "User is authorized for requested resource";
                        }
                        else
                        {
                            authorized = "User not authorized for requested resource";
                        }
                    }
                    else
                    {
                        authenticated = "User not authenticated to OAM";
                    }
                }
                else
                {
                    formAuth = "Form based auth not deteced";
                }
            }
            else
            {
                resourceProtected = "Requested resource is not protected";
            }
            ac.shutdown();
        }
        catch (AccessException e)
        {
            Date date = new Date();
            System.out.println(date);
            System.out.println("Access Exception: " + e.getMessage());
            OAMError = e.getMessage();

        }
    }
}
