package miscsamples.cleartrust;

import java.util.*;
import sirrus.api.client.*;
import sirrus.api.client.criteria.StringCriterion;
import sirrus.api.client.search.IApplicationSearch;
import sirrus.api.client.search.IGroupSearch;

import com.thortech.util.logging.Logger;

/**
*
* CTAcct.java will have the methods that will be called by the adapters to
* create, delete and modify users in CT. This java class in turn calls the
* CTUtilities.java to actually carry out the operation in CT.
*/
public class CTAcct {

	public Logger logger = null;
	public APIServerProxy serverProxy = null;
//	private String reconPropertyName = "XTUserLastUpdateXLI";
	Object reconPropertyValue[] = new Object[1];
	
	public static void main(String[] args)
	{
	    // Testing methods to create a group
	    CTAcct ctAcct = new CTAcct();
	    
	    ctAcct.connect(
	            "host", // machine  
	            "5601", 	// port
	            "CLEAR",	// ssl mode
	            "100000", // timeout
	            "admin", // admin userid
	            "admin", 	// admin password
	            "Default Administrative Group", // admin group
	            "Default Administrative Role", // admin role
	            "", // ca file
	            "", // ca password
	            "", // ks file
	            "", // ks password
	            "", // key alias
	            "" // private pass
	            );
	    
	    
	    System.out.println("Connected!");
	    
	    
	    // check if group exists
	    /*
	    String groupName = "COFFEE";
	    
	    System.out.println("Checking of group " + groupName + " exists in CT" );
	    boolean ret = ctAcct.checkIfGroupExists(groupName);
	    
	    System.out.println("return value = " + ret);
	    */

	    
	    // create a group
	    /*
	    String grpName = "KGBGRP";
	    String ret = ctAcct.createGroup(grpName, true, "Test Description");
	    System.out.println("return value = " + ret);
	    */
	    
	    // check if application exists
	    /*
	    String appName = "TestAppName";
	    ctAcct.searchApplication(appName);
	    */
	    
	    // provision application to group
	    String appName = "TestAppName";
	    String appURL = "/testres1/index.html";
	    String grpName = "KGBGRP";
	    ctAcct.provisionApplicationToGroup(grpName, appName, appURL);
	    
	    ctAcct.closeConnection();
	    
	    System.out.println("Connection closed!");
	    
	}
	
	public synchronized String provisionApplicationToGroup(String grpName, String appName, String appURL)
	{
	    IGroupSearch groupSearch = serverProxy.searchGroupObjects();

        StringCriterion sc1 = new StringCriterion(StringCriterion.EQUALS, grpName);
        groupSearch.setNameCriterion(sc1);

        // Obtain the SparseData of groups matching the specified Criteria.
        ISparseData grpInSearch = groupSearch.search();
        
	    IApplicationSearch appSearch = serverProxy.searchApplicationObjects();
	    
	    StringCriterion sc = new StringCriterion(StringCriterion.EQUALS, appName);
        appSearch.setNameCriterion(sc);

        // Obtain the SparseData of applications matching the specified Criteria.
        ISparseData appInSearch = appSearch.search();
        
        try 
        {
            IGroup grp = (IGroup)grpInSearch.getByName(grpName);
            IEntity entity = (IEntity)grp;
            
            System.out.println("Entity name is " + entity.getName());

            IApplication app = (IApplication)appInSearch.getByName(appName);
            
            ISparseData urls = app.getApplicationURLs();
            IAPIObject[] urlObj = urls.getByRange(0,Integer.MAX_VALUE);
            IApplicationURL url = null;
            
            System.out.println("application URL is = " + appURL);
            
            for (int j=0 ; j<urlObj.length ; j++)
            {
                IApplicationURL url1 = (IApplicationURL) urlObj[j];
                String uri = url1.getURI().trim();
                
                System.out.println("uri = " + uri);
                
                if (uri.equalsIgnoreCase(appURL))
                {
                    url = url1;
                    System.out.println("url number " + j + " = " + url.getURI() + " --> " + url.getUniqueIdentifier());
                    break;
                }
            }
            
            //IApplicationURL url = (IApplicationURL)urls.getByName(appURL);
            
            entity.createExplicitEntitlement(url, true);
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
	    
	    
	    return "";
	}
	
	public synchronized void searchApplication(String applicationName)
	{
	    IApplicationSearch appSearch = serverProxy.searchApplicationObjects();
	    
	    StringCriterion sc = new StringCriterion(StringCriterion.EQUALS, applicationName);
        appSearch.setNameCriterion(sc);

        // Obtain the SparseData of Users matching the
        // specified Criteria.
        ISparseData appInSearch = appSearch.search();
        
        try 
        {
            IAPIObject[] retObj = appInSearch.getByRange(0,Integer.MAX_VALUE);
            
            for (int i=0 ; i<retObj.length ; i++)
            {
                IApplication app = (IApplication)retObj[0];
                
                ISparseData urls = app.getApplicationURLs();
                IAPIObject[] urlObj = urls.getByRange(0,Integer.MAX_VALUE);
                
                
                for (int j=0 ; j<urlObj.length ; j++)
                {
                    IApplicationURL url = (IApplicationURL) urlObj[j];
                    
                    System.out.println("url number " + j + " = " + url.getURI() + " --> " + url.getUniqueIdentifier());
                }
                
                
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        
        
	}
	
	public synchronized String createGroup(String groupName, boolean isPublic, String description)
	{
	    IGroup grp = serverProxy.createGroup(groupName, true, description);
	    
	    try
	    {
	        grp.save();
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	        return "CT_GRP_CREATE_FAILURE";
	    }
	    
	    return "CT_GRP_CREATE_SUCCESS";
	    
	}
	
	
	public synchronized boolean checkIfGroupExists(String groupName)
	{
	    IGroupSearch groupSearch = serverProxy.searchGroupObjects();

        StringCriterion sc = new StringCriterion(StringCriterion.EQUALS, groupName);
        groupSearch.setNameCriterion(sc);

        // Obtain the SparseData of Users matching the
        // specified Criteria.
        ISparseData grpInSearch = groupSearch.search();

        try
        {
            IAPIObject[] retObj = grpInSearch.getByRange(0,Integer.MAX_VALUE);
            
            if (retObj.length < 1)
            {
                return false;
            } 
            else
            {
                IGroup grp = (IGroup)retObj[0];
                if (grp.getName().equals(groupName))
                {
                    return true;
                }
            }
            
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
	    
	    return false;
	}
	
	public CTAcct(){
			logger = Logger.getLogger("Adapters.CTIntegration"); 
			logger.info("***** Clear Trust *****");
			reconPropertyValue[0] = new Date();
		}
	
	public synchronized String connect(String machine, String port, 
			String sslMode, String timeout, String adminUserId, String adminPassword, String adminGroup, 
			String adminRole, String caFile, String caPassword, String ksFile, 
			String ksPassword, String keyAlias,	String privatePass)
	{
		Hashtable ht = new Hashtable();
		ht.put("machinename",machine);
		ht.put("port",port);
		ht.put("sslmode",sslMode);
		ht.put("timeout",timeout);
		ht.put("admin",adminUserId);
		ht.put("adminpassword",adminPassword);
		ht.put("admingroup",adminGroup);
		ht.put("adminrole",adminRole);
		if(sslMode.equals("SSL_AUTH"))
		{
			ht.put("cafile",caFile);
			ht.put("capassword",caPassword);
			ht.put("ksfile",ksFile);
			ht.put("kspassword",ksPassword);
			ht.put("keyalias",keyAlias);
			ht.put("privatepass",privatePass);
		}
		return openConnection(ht);
	}
	
	public synchronized String openConnectionWithSSLAUTH(String machine, String port, 
	String sslMode, String timeout, String adminUserId, String adminPassword, String adminGroup, 
	String adminRole, String caFile, String caPassword, String ksFile, 
	String ksPassword, String keyAlias,	String privatePass){
		Hashtable ht = new Hashtable();
		ht.put("machinename",machine);
		ht.put("port",port);
		ht.put("sslmode",sslMode);
		ht.put("timeout",timeout);
		ht.put("admin",adminUserId);
		ht.put("adminpassword",adminPassword);
		ht.put("admingroup",adminGroup);
		ht.put("adminrole",adminRole);
		ht.put("cafile",caFile);
		ht.put("capassword",caPassword);
		ht.put("ksfile",ksFile);
		ht.put("kspassword",ksPassword);
		ht.put("keyalias",keyAlias);
		ht.put("privatepass",privatePass);
		return openConnection(ht);
	}
	
	public synchronized String openConnectionWithoutSSLAUTH(String machine, String port, 
	String sslMode, String timeout, String adminUserId, String adminPassword, String adminGroup, 
	String adminRole){
		Hashtable ht = new Hashtable();
		ht.put("machinename",machine);
		ht.put("port",port);
		ht.put("sslmode",sslMode);
		ht.put("timeout",timeout);
		ht.put("admin",adminUserId);
		ht.put("adminpassword",adminPassword);
		ht.put("admingroup",adminGroup);
		ht.put("adminrole",adminRole);
		return openConnection(ht);
	}	
	
	/**
	* Establishes a connection with the CT Target Source
	*
	* @param serverAttrib <code>Hashtable</code> containing CT server attributes
	* @return "CT_CONNECTION_SUCCESS" if successful or an error message
	*/
	public synchronized String openConnection(Hashtable serverAttrib){
		String response = "";
		CTUtilities ct = new CTUtilities();
		response = ct.setServerProperties(serverAttrib);
		logger.info("CONNECT TO CLEAR TRUST");
		logger.debug("SERVERATTRIBUTES - "+serverAttrib.toString());
		if(!("CT_SETPROPERTIES_COMPLETE").equals(response)){
			logger.fatal("CT_USERCREATION_SETCONPROPERTIES_FAIL");
			return "CT_USERCREATION_SETCONPROPERTIES_FAIL";
		}
		try{
			serverProxy = ct.openConnection();
			logger.info("CT_CONNECTION_SUCCESS");
			return "CT_CONNECTION_SUCCESS";
		}
		catch (UserNotAuthorizedException e)
		{
			e.printStackTrace();
			logger.fatal("CT_USERCREATION_ADMINNOTAUTH_FAIL",e);
			return "CT_USERCREATION_ADMINNOTAUTH_FAIL";
		}
		catch(Exception e){
			e.printStackTrace();
			logger.fatal("CT_CONNECTION_FAIL",e);
			return "CT_CONNECTION_FAIL";
		}

	}



	/**
	* Closes the connection with the CT Target Source
	*
	* @param serverProxy contains the open connection
	*/
	public synchronized String closeConnection(){
		CTUtilities ct = new CTUtilities();
		try{
			ct.closeConnection(serverProxy);
		}
		catch(Exception e){
			logger.fatal("CT_CLOSECONNECTION_FAILED",e);
			return "CT_CLOSECONNECTION_FAILED";
		}
		logger.info("CT_CLOSECONNECTION_SUCCESS");
		return "CT_CLOSECONNECTION_SUCCESS";
	}


	/**
	* Does the work of either adding or removing a user from a group
	*
	* @param userId The unique userid
	* @param groupId The group the user is to be added to or removed from
	* @param actionType Action to be taken values are "ADD" or "DELETE"
	* @return <code>String</code> "CT_USERGROUP_SUCCESS" if success or an error message
	*/
	private synchronized String groupAction(String userId, String groupId, String actionType) {
		try {
			IUser user = null;
			IGroup group = null;
			if (userId == null || userId.equals("")){
				logger.fatal("CT_USERGROUP_NOUSERID_FAIL");
				return "CT_USERGROUP_NOUSERID_FAIL";
			}
				
			if(serverProxy==null){
				logger.fatal("CT_USERGROUP_NOTCONNECTED_FAIL");
				return "CT_USERGROUP_NOTCONNECTED_FAIL";
			}
			ISparseData userRS = serverProxy.getUsers();
			sirrus.api.client.IAPIObject userObject = userRS.getByName(userId);
			ISparseData groupRS = serverProxy.getGroups();
			sirrus.api.client.IAPIObject groupObject = groupRS.getByName(groupId);
			if (!(userObject instanceof IUser) || !(groupObject instanceof IGroup))
				return "CT_USERGROUP_OBJCREATION_FAILED";
			user = (IUser) userObject;
			group = (IGroup) groupObject;
			if (actionType.equals("ADD"))
				group.addChild(user);
			else if (actionType.equals("DELETE"))
				group.removeChild(user);
			group.save();
/*			try{
				logger.info("Setting Xellerate Property timestamp in ClearTrust in property "+reconPropertyName);
				user.setUserProperty(reconPropertyName,reconPropertyValue);
				user.save();
			}catch(InvalidTypeException ite){
				logger.fatal(reconPropertyName+" property not present in ClearTrust. Creating the property");
				serverProxy.createUserPropertyDefinition(reconPropertyName,IUserPropertyTypes.DATE_VAL,"Timestamp to reconcile with Xellerate",false,false);
				logger.fatal("Created "+reconPropertyName+" and setting the property value to current date for user");
				user.setUserProperty(reconPropertyName,reconPropertyValue);
				user.save();
			}
*/			logger.info("CT_USERGROUP_SUCCESS for userid="+userId+" and group="+groupId+" and the action taken is="+actionType);
			return "CT_USERGROUP_SUCCESS";

		}
		catch (ObjectNotFoundException onf) {
			onf.printStackTrace();
			logger.fatal("CT_USERGROUP_OBJECTNOTFOUND_FAIL",onf);
			return "CT_USERGROUP_OBJECTNOTFOUND_FAIL";
		}
		catch (UserNotAuthorizedException una) {
			una.printStackTrace();
			logger.fatal("CT_USERGROUP_NORIGHTS_FAIL",una);
			return "CT_USERGROUP_NORIGHTS_FAIL";
		}
		catch (DuplicateObjectException doe) {
			doe.printStackTrace();
			logger.fatal("CT_USERGROUP_DUPLICATEOBJECT_FAIL",doe);
			return "CT_USERGROUP_DUPLICATEOBJECT_FAIL";
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.fatal("CT_USERGROUP_UNKNOWN_FAIL",e);
			return "CT_USERGROUP_UNKNOWN_FAIL";
		}
	}

	public synchronized Hashtable getAttributes(String userId)throws Exception{
		Hashtable ht = new Hashtable();
		try{
			IUser user = null;
			if (userId == null || userId.equals(""))
//		return "CT_GETPROPERTY_NOUSERID_FAIL";
				throw new Exception("CT_GETPROPERTY_NOUSERID_FAIL");
			if(serverProxy==null){
//		return "CT_GETPROPERTY_NOTCONNECTED_FAIL";
				throw new Exception("CT_GETPROPERTY_NOTCONNECTED_FAIL");
			}
			ISparseData userRS = serverProxy.getUsers();
			sirrus.api.client.IAPIObject userObject = userRS.getByName(userId);
			if (!(userObject instanceof IUser))
				throw new Exception("CT_GETPROPERTY_OBJCREATION_FAILED");
//				return "CT_GETPROPERTY_OBJCREATION_FAILED";
			user = (IUser) userObject;
			ht.put("firstname",user.getFirstName());
			ht.put("lastname",user.getLastName());
			ht.put("email",user.getEmailAddress());
			ht.put("startdate",user.getStartDate());
			ht.put("enddate",user.getEndDate());
			ht.put("passwordexpirationdate",user.getPasswordExpirationDate());
			ht.put("islock",new Boolean(user.isAdminLockedout()).toString());
		}catch (ObjectNotFoundException onf) {
			onf.printStackTrace();
			throw new Exception("CT_GETPROPERTY_OBJECTNOTFOUND_FAIL");
		}
		catch (IllegalPasswordException ipe) {
			ipe.printStackTrace();
			throw new Exception("CT_GETPROPERTY_INVALIDPASSWORD_FAIL");
		}
		catch (UserNotAuthorizedException una) {
			una.printStackTrace();
			throw new Exception("CT_GETPROPERTY_NORIGHTS_FAIL");
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception("CT_GETPROPERTY_UNKNOWN_FAIL");
		}
		return ht;
	}

}
