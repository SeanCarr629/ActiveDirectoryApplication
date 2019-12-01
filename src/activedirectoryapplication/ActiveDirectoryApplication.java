/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package activedirectoryapplication;

import java.util.ArrayList;
import java.util.HashMap;
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
    
    
    //Method to choose OU and location details
    
    public static HashMap<String,String> setLocationDetails(String enteredLocation){
        
        HashMap<String,String> locationDetails = new HashMap<>();
        
        
            switch (enteredLocation) {
        case "New York":
            locationDetails.put("orgUnit", "Topaz-NY");
            locationDetails.put("city", "Holtsville");
            locationDetails.put("office", "Holtsville, NY");
            locationDetails.put("state", "NY");
            locationDetails.put("streetAddress", "925 Wavery Avenue");
            locationDetails.put("zipcode", "11742");
            break;
         case "California":
           // orgUnit = "Topaz-CA";
            break;
         case "Vero Beach":
            // orgUnit = "Vero Beach";
             break;
         case "Jackonsville":
             System.out.println("Thursday");
             break;
         case "Pennysylvania":
           //  orgUnit = "Topaz-PA";
             break;
  }
        
        return locationDetails;
        
    }
       
   
    //Method To Create New User
    
        public static void CreateUser(String userName, String enteredLocation, String enteredDepartment,
        String enteredPhoneNumber, String enteredJobTitle) throws NamingException {
        
         //Variables
         final String company = "Topaz", country = "US";   
         
         HashMap<String,String> locationDetails = new HashMap<>();
            
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
       
       //Set location details; orgUnit, city, etc.
       
      locationDetails = setLocationDetails(enteredLocation);
    
     
      
        
      int emptySpace =  userName.indexOf(" ");
      String firstName = userName.substring(0, emptySpace);
      String lastName = userName.substring(emptySpace);
      String fullName = firstName + "" + lastName;
      String emailAddress = firstName + "." + lastName.trim() + "@topaz-usa.com";
      char firstInitial = userName.charAt(0);
      String loginName = firstInitial + lastName.trim();
      
        
         //Add user attributes
        
        attrs.put("cn", userName);
        attrs.put("givenName", firstName);
        attrs.put("sn",lastName);
        attrs.put("l", locationDetails.get("city"));
        attrs.put("company", company);
        attrs.put("department", enteredDepartment);
        attrs.put("mail", emailAddress);
        attrs.put("physicalDeliveryOfficeName", locationDetails.get("office"));
        attrs.put("proxyAddresses", "SMTP:" + emailAddress);
        attrs.put("SamAccountName", loginName);
        attrs.put("st", locationDetails.get("state"));
        attrs.put("StreetAddress", locationDetails.get("streetAddress"));
        attrs.put("userPrincipalName", loginName + "@topaz.local");
        attrs.put("telephoneNumber", enteredPhoneNumber);
        attrs.put("description", enteredJobTitle);
        attrs.put("displayName",fullName);
        attrs.put("c", country);
        attrs.put("postalCode", locationDetails.get("zipcode"));
        attrs.put("title", enteredJobTitle);
        
        
        
        
        
       
        //Create user
        ctx.createSubcontext("CN="+userName + ",OU=users" + ",OU=" + locationDetails.get("orgUnit") + ",DC=lab,DC=home", attrs);
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

    

        
//Method to delete user
  public static void DeleteUser(String userName, String orgUnit) throws NamingException{
     
      DirContext ctx = AuthenticateAD();
      
      ctx.destroySubcontext("CN="+userName + ",OU=users" + ",OU=" + orgUnit + ",DC=lab,DC=home");
      
    
     }   
    
    
    
    
    

}
