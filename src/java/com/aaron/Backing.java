/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaron;

import java.util.ArrayList;
import java.util.Properties;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

//ldap stuff
import javax.naming.*;
import javax.naming.directory.*;

/**
 *
 * @author aaron
 */
@ManagedBean
@RequestScoped
public class Backing
{

    private String server;
    private String username = "cn=orcladmin";
    private String password;
    private String context = "dc=val,dc=vlss,dc=local";
    private String port = "3060";
    private ArrayList duplicates = new ArrayList();
    private String ldapError = "";
    private String connectionStatus = "true";
    private String ldapErrorLevel = "black";

    public String getLdapErrorLevel() {
        return ldapErrorLevel;
    }

    public void setLdapErrorLevel(String ldapErrorLevel) {
        this.ldapErrorLevel = ldapErrorLevel;
    }

    public String getConnectionStatus() {
        if(connectionStatus == "false")
        {
            return "true";
        }
        else
        {
            return "false";
        }
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public String getLdapError() {
        return ldapError;
    }

    public void setLdapError(String ldapError) {
        this.ldapError = ldapError;
    }

    public String getDuplicates()
    {
       String i = new String();
       for(int j = 0; j < duplicates.size(); j++)
       {
           i += duplicates.get(j);
           i += "\n";
       }
       return i;
    }

    public void setDuplicates(String duplicates)
    {
        this.duplicates.add(duplicates);
    }

    public String getPort()
    {
        return port;
    }

    public void setPort(String port)
    {
        this.port = port;
    }

    public String getContext()
    {
        return context;
    }

    public void setContext(String context)
    {
        this.context = context;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getServer()
    {
        return server;
    }

    public void setServer(String server)
    {
        this.server = server;
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
     * Creates a new instance of Backing
     */
    public Backing()
    {
    }

    public void method()
    {
        //System.out.println("This is a working backing bean");
        //System.out.println(server);
        //System.out.println(username);
        //System.out.println(password);
        
        ArrayList uidList = new ArrayList();
        duplicates = new ArrayList();
        DirContext ctx;
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://" + server + ":" + port + "/");
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, password);
        
        try
        {
            // obtain initial directory context using the environment
            ctx = new InitialDirContext(env);
            ldapErrorLevel = "black";
            ldapError = "Successfully connected to LDAP server";
            SearchControls ctls = new SearchControls();
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            ctls.setReturningAttributes(new String[]
                    {
                        "uid"
                    });
            ctls.setCountLimit(50000);

            NamingEnumeration resultEnum = ctx.search(context, "objectclass=orcladuser", ctls);
            while (resultEnum.hasMore())
            {
                SearchResult result = (SearchResult) resultEnum.next();

                // print attributes returned by search
                Attributes attrs = result.getAttributes();
                NamingEnumeration e = attrs.getAll();
                while (e.hasMore())
                {
                    Attribute attr = (Attribute) e.next();
                    if(uidList.contains(attr))
                    {
                        System.out.println(attr);
                        System.out.println(result.getNameInNamespace());
                        duplicates.add(attr);
                        duplicates.add(result.getNameInNamespace());
                        //also have to print out the DN of the object that matches here
                        //it should be in the array already
                        //need to have both of them to comapre
                    }
                    else
                    {
                        uidList.add(attr);
                    }
                   
                }

            }
            ctx.close();
        }
        catch (Exception e)
        {
            System.err.println(e);
            //duplicates.add(e);
            ldapErrorLevel = "red";
            ldapError = e.getMessage();
            
        }        


    }
}
