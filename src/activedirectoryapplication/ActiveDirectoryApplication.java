/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package activedirectoryapplication;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

/**
 *
 * @author scarroll
 * 
 * 
 * 
 * 
 * 
 * 
 */




public class ActiveDirectoryApplication {

    /**
     * @param args the command line arguments
     */
    
    
        public static void CreateUser(String userName, String department) throws NamingException {
        
            
         DirContext ctx = AuthenticateAD();
        Attributes attrs = new BasicAttributes(true);
        Attribute oc = new BasicAttribute("objectclass");
        oc.add("top");
        oc.add("person");
        oc.add("organizationalPerson");
        oc.add("user");
        attrs.put(oc);
        attrs.put(new BasicAttribute("cn", userName));
        ctx.createSubcontext("CN="+userName + ",OU=" + department +",DC=lab,DC=home", attrs);
        ctx.close();
    
    
    
    
    
    
        }
    
    
    
    
    
    public static DirContext AuthenticateAD() throws NamingException
{
Hashtable<String, String> ldapEnv;
ldapEnv = new Hashtable<String, String>(11);
ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
//ldapEnv.put(Context.PROVIDER_URL,  "ldap://WIN-4H1ILULLH7H.l:3
ldapEnv.put(Context.PROVIDER_URL,  "ldap://192.168.1.20:389");
ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
//ldapEnv.put(Context.SECURITY_PRINCIPAL, "cn=administrateur,cn=users,dc=societe,dc=fr");
ldapEnv.put(Context.SECURITY_PRINCIPAL, "jdoe@lab.home");
ldapEnv.put(Context.SECURITY_CREDENTIALS, "Password1234");
       
            //ldapEnv.put(Context.SECURITY_PROTOCOL, "ssl");
//ldapEnv.put(Context.SECURITY_PROTOCOL, "simple");
DirContext ctx = new InitialDirContext(ldapEnv);
 return ctx;
       





}

    
    
    
    
    
    
    
    public static void main(String[] args) throws NamingException {
        // TODO code application logic here
        
        
          
          
          
    }
    
}
