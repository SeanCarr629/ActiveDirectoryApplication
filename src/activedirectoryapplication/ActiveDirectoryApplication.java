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

   
    //Method To Create New User
    
        public static void CreateUser(String userName, String enteredLocation) throws NamingException {
        
         String location = " ";   
            
        //Call method to authenticate to AD    
        DirContext ctx = AuthenticateAD();
        Attributes attrs = new BasicAttributes(true);
        
        //LDAP hierarchy
        Attribute oc = new BasicAttribute("objectclass");
        oc.add("top");
        oc.add("person");
        oc.add("organizationalPerson");
        oc.add("user");
        attrs.put(oc);
       
 
        //Switch statement to choose proper OU     
        switch (enteredLocation) {
        case "New York":
            location = "Topaz-NY";
            break;
         case "California":
            location = "Topaz-CA";
            break;
         case "Vero Beach":
             location = "Vero Beach";
             break;
         case "Jackonsville":
             System.out.println("Thursday");
             break;
         case "Pennysylvania":
             location = "Topaz-PA";
             break;
  }
        
        
      int emptySpace =  userName.indexOf(" ");
      
      
        
         //Add user attributes
        
        attrs.put("cn", userName);
        attrs.put("givenName", userName.substring(0, emptySpace));
        attrs.put("sn", userName.substring(emptySpace));
        
        
        
        
        ctx.createSubcontext("CN="+userName + ",OU=users" + ",OU=" + location + ",DC=lab,DC=home", attrs);
        ctx.close();
    
    
    
    
    
    
        }
    
    
    
    
    // Method To Authenticate to AD
        
    public static DirContext AuthenticateAD() throws NamingException
{
Hashtable<String, String> ldapEnv;
ldapEnv = new Hashtable<String, String>(11);
ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

//Replace with DC IP Address

ldapEnv.put(Context.PROVIDER_URL,  "ldap://192.168.1.20:389");
ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");

//Use admin credentials to connect
ldapEnv.put(Context.SECURITY_PRINCIPAL, "jdoe@lab.home");
ldapEnv.put(Context.SECURITY_CREDENTIALS, "Password1234");
       
//Create and return context
DirContext ctx = new InitialDirContext(ldapEnv);
 return ctx;
       
}

    
    
    
    
    
    
    
    public static void main(String[] args) throws NamingException {
        // TODO code application logic here
        
        
          
          
          
    }
    
}
