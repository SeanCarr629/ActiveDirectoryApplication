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
    
        public static void CreateUser(String userName, String enteredLocation, String enteredDepartment,
        String enteredPhoneNumber, String enteredJobTitle) throws NamingException {
        
         String orgUnit = " ", city = " ", office = " ", company = "Topaz", state = " "
                 ,streetAddress = " ";   
            
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
            orgUnit = "Topaz-NY";
            city = "Holtsville";
            office = "Holtsville, NY";
            state = "NY";
            streetAddress = "925 Wavery Avenue";
            break;
         case "California":
            orgUnit = "Topaz-CA";
            break;
         case "Vero Beach":
             orgUnit = "Vero Beach";
             break;
         case "Jackonsville":
             System.out.println("Thursday");
             break;
         case "Pennysylvania":
             orgUnit = "Topaz-PA";
             break;
  }
        
        
      int emptySpace =  userName.indexOf(" ");
      String firstName = userName.substring(0, emptySpace);
      String lastName = userName.substring(emptySpace);
      String emailAddress = firstName + "." + lastName.trim() + "@topaz-usa.com";
      char firstInitial = userName.charAt(0);
      String loginName = firstInitial + lastName.trim();
      
        
         //Add user attributes
        
        attrs.put("cn", userName);
        attrs.put("givenName", firstName);
        attrs.put("sn",lastName);
        attrs.put("l", city);
        attrs.put("company", company);
        attrs.put("department", enteredDepartment);
        attrs.put("mail", emailAddress);
        attrs.put("physicalDeliveryOfficeName", office);
        attrs.put("proxyAddresses", "SMTP:" + emailAddress);
        attrs.put("SamAccountName", loginName);
        attrs.put("st", state);
        attrs.put("StreetAddress", streetAddress);
        attrs.put("userPrincipalName", loginName + "@topaz.local");
        attrs.put("telephoneNumber", enteredPhoneNumber);
        attrs.put("des", enteredPhoneNumber);
        
        
        
        
        ctx.createSubcontext("CN="+userName + ",OU=users" + ",OU=" + orgUnit + ",DC=lab,DC=home", attrs);
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
