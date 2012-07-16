/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

   public String username;
   public String password;
   public static final String resource = "//instore/ApplicationValWCPortalApp/faces/oracle/webcenter/portalapp/pages/home.jspx";
   public static final String protocol = "http";
   public static final String method = "GET";
   public String m_configLocation = "/myfolder";

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
            ac = AccessClient.createDefaultInstance("", AccessClient.CompatibilityMode.OAM_10G);
            ResourceRequest rrq = new ResourceRequest(protocol, resource, method);
            if(rrq.isProtected())
            {
                System.out.println("Requested resource is protected");
                AuthenticationScheme authnScheme = new AuthenticationScheme(rrq);
                if(authnScheme.isForm())
                {
                    System.out.println("Form based auth detected");
                    Hashtable credentials = new Hashtable();
                    credentials.put("userid", username);
                    credentials.put("password", password);
                    UserSession session = new UserSession(rrq, credentials);
                    if(session.getStatus() == UserSession.LOGGEDIN)
                    {
                        System.out.println("User is authenticated to OAM");
                        if(session.isAuthorized(rrq))
                        {
                            System.out.println("User is authorized for requested resource");
                        }
                        else
                        {
                            System.out.println("User not authorized for requested resource");
                        }
                    }
                    else
                    {
                        System.out.println("User not authenticated to OAM");
                    }
                }
                else
                {
                    System.out.println("Form based auth not deteced");
                }
            }
            else
            {
                System.out.println("Requested resource is not protected");
            }
            ac.shutdown();            
        }
        catch(AccessException e)
        {
            System.out.println("Access Exception: " + e.getMessage());
            Date date = new Date();
            System.out.println(date);
        }
    }
}
