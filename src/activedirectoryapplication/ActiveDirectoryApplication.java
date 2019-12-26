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
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

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
    
     public static void main(String[] args) {
    
    /*     try {

             doSearchOU();
         } catch (NamingException ex) {
             Logger.getLogger(ActiveDirectoryApplication.class.getName()).log(Level.SEVERE, null, ex);
         }    */
    
     
     }  
     
     
     
     
    //Method to search specific OU for all users in that OU 
     public static ArrayList<String> doSearchOU(String ou) throws NamingException {
         
         
         
       
         
       DirContext ctx =  AuthenticateAD();
       ArrayList<String> users = new ArrayList<String>();
         
        
        String searchFilter = "(&(objectClass=person))";
        String domain = "OU=users,OU=" + ou + ",DC=lab,DC=home";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> answer = ctx.search(domain, searchFilter, searchControls);
        while (answer.hasMoreElements()) {
            SearchResult sr = (SearchResult) answer.next();
            
           
            String cn = sr.getName();
            int equalSign =  cn.indexOf("=") + 1;
            String userName = cn.substring(equalSign);
            
            //System.out.println(sr.getName());
            users.add(userName);
            
            
        }
        
        return users;
    }
     
     
     
     
     
     
     
    
    
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
            locationDetails.put("orgUnit", "Topaz-CA");
            locationDetails.put("city", "San Fernando");
            locationDetails.put("office", "San Fernando, CA");
            locationDetails.put("state", "CA");
            locationDetails.put("streetAddress", "225 Parkside Drive");
            locationDetails.put("zipcode", "91340");
            break;
         case "Vero Beach":
            // orgUnit = "Vero Beach";
            locationDetails.put("orgUnit", "Topaz-FL");
            locationDetails.put("city", "Jacksonville");
            locationDetails.put("office", "Jacksonville, FL");
            locationDetails.put("state", "FL");
            locationDetails.put("streetAddress", "780 Whittaker Road");
            locationDetails.put("zipcode", "32218");
            break;
         case "Jackonsville":
            locationDetails.put("orgUnit", "Topaz-FL");
            locationDetails.put("city", "Jacksonville");
            locationDetails.put("office", "Jacksonville, FL");
            locationDetails.put("state", "FL");
            locationDetails.put("streetAddress", "780 Whittaker Road");
            locationDetails.put("zipcode", "32218");
             break;
         case "Pennysylvania":
            locationDetails.put("orgUnit", "Topaz-PA");
            locationDetails.put("city", "Langhorne");
            locationDetails.put("office", "Langhorne, PA");
            locationDetails.put("state", "PA");
            locationDetails.put("streetAddress", "905 Wheeler Way");
            locationDetails.put("zipcode", "19047");
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
     
   //   DirContext ctx = AuthenticateAD();
      
   //   ctx.destroySubcontext("CN="+userName + ",OU=users" + ",OU=" + orgUnit + ",DC=lab,DC=home");
      
    
     }   
    
    
    
    
    

}
