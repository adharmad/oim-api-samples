package miscsamples.ctrecon;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import sirrus.api.client.APIServerProxy;
import sirrus.api.client.ExpiredPasswordException;
import sirrus.api.client.IAPIObject;
import sirrus.api.client.ISparseData;
import sirrus.api.client.IUser;
import sirrus.api.client.ServerTimeoutException;
import sirrus.api.client.TransportException;
import sirrus.api.client.UserNotAuthorizedException;
import sirrus.api.client.criteria.DateCriterion;
import sirrus.api.client.criteria.StringCriterion;
import sirrus.api.client.search.IUserSearch;
import sirrus.connect.ConnectionDescriptor;

public class CTController implements CTSearchCriterion {

    private String ctAdminGroup;
    private String ctAdminRole;
    private String ctAdminUserId;
    private String ctAdminPassword;
    private String caFileLocation;
    private String caPassword;
    private String defaultUserGroup;
    private String keyAlias;
    private String ksFileLocation;
    private String ksPassword;
    private String serverAddress;
    private int serverPort;
    private String privatePassword;
    private int sslMode;
    private int timeOut;
    private ConnectionDescriptor eserver;
    private APIServerProxy apiProxy;
    
   public CTController(CTITRes ctItRes)
   {
       initParams(ctItRes);
       
       eserver = new ConnectionDescriptor(serverAddress,
               serverPort, sslMode, timeOut);
       
   }

   public void initParams(CTITRes ctItRes)
   {
       ctAdminGroup = ctItRes.getCtAdminGroup();
       ctAdminRole = ctItRes.getCtAdminRole();
       ctAdminUserId = ctItRes.getCtAdminUserId();
       ctAdminPassword = ctItRes.getCtAdminPassword();
       caFileLocation = ctItRes.getCaFileLocation();
       caPassword = ctItRes.getCaPassword();
       defaultUserGroup = ctItRes.getDefaultUserGroup();
       keyAlias = ctItRes.getKeyAlias();
       ksFileLocation = ctItRes.getKsFileLocation();
       ksPassword = ctItRes.getKsPassword();
       serverAddress = ctItRes.getServerAddress();
       serverPort = Integer.parseInt(ctItRes.getServerPort().trim());
       privatePassword = ctItRes.getPrivatePassword();
       sslMode = Integer.parseInt(ctItRes.getSslMode().trim());
       timeOut = Integer.parseInt(ctItRes.getTimeOut().trim());
   }

   /**
    * Open a connection to ClearTrust. This assumes that the Connection
    * information has been set. 
    * This function is called from the constructor
    * 
    * @return String indicating success or failure
    */
   public String openConnection() {
       try {
           if (eserver == null) {
               String msg = "Connection not set. Did you call the correct Constructor or setConnection()?";
               throw new Exception(msg);
           }
           if (apiProxy == null) {
               apiProxy = new APIServerProxy(eserver);
               apiProxy.connect(ctAdminUserId, ctAdminPassword, ctAdminGroup, ctAdminRole);
           }

           return "RSA_OPENCONNECTION_SUCCESS";
       } catch (java.io.IOException io) {
           io.printStackTrace();
       } catch (UserNotAuthorizedException una) {
           una.printStackTrace();
       } catch (ExpiredPasswordException epe) {
           epe.printStackTrace();
           return "RSA_OPENCONNECTION_EXPIREDPASSWORD_FAIL";
       } catch (ServerTimeoutException ste) {
           ste.printStackTrace();
           return "RSA_OPENCONNECTION_TIMEOUT_FAIL";
       } catch (Exception e) {
           e.printStackTrace();
       }
       return "RSA_OPENCONNECTION_UNKNOWN_FAIL";
   }
   
   /**
    * Get user records with specific search criterion.
    */
   public Vector searchUsers(HashMap criterion)
   {
       
       
       
       return null;
   }
   
   /**
    * Get user records with specific search criterion.
    */
   public Vector searchUsers(int criterion, String data)
   {
       Vector searchResult = new Vector();
       
       IUserSearch userSearch = apiProxy.searchUserObjects();
       
       // Set the search criterion
       setSearchCriterion(userSearch, criterion, data);

       // Obtain the SparseData of Users matching the
       // specified Criteria.
       ISparseData usersInSearch = userSearch.search();

       // Find out how many users are in the SparseData
       int numOfUsers = 0;
       try 
       {
           IAPIObject[] retObj = usersInSearch.getByRange(0,
                   Integer.MAX_VALUE);
           numOfUsers = retObj.length;
           
           System.out.println("Number of Users returned "
                   + "by the Search: " + numOfUsers);
           
           for (int i=0 ; i<numOfUsers ; i++) 
           {
               IUser user = (IUser)retObj[i];
               searchResult.add(user);
           }
           
           
       } catch (Exception e) {
           e.printStackTrace();
       } 
       
       return searchResult;
   }   
   
   public void setSearchCriterion(IUserSearch userSearch, int criterion, String data)
   {
       StringCriterion sc = null;
       DateCriterion dc = null;
       
       switch (criterion)
       {
       		case USERID_STARTSWITH:
       		    sc = new StringCriterion(StringCriterion.STARTS_WITH, data);
       		    userSearch.setUserIDCriterion(sc);
       		    break;
       		
       		case USERID_EQUALS:
       		    sc = new StringCriterion(StringCriterion.EQUALS, data);
       		    userSearch.setUserIDCriterion(sc);
       		    break;
       		    
       		case MODIFIED_AFTER:
       		    dc = new DateCriterion(DateCriterion.AFTER, new Date());
       		    
       		    
       		default:
       		    break;
       }
       
       return;
   }
   
   /**
    * Close a connection to ClearTrust. This assumes that the Connection has
    * been set.
    * 
    * @return String indicating success or failure
    */
   public void closeConnection() {
       try {
           apiProxy.disconnect();
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
   
   public static void main(String[] args)
   {
       CTITRes ctItRes = new CTITRes("Default Administrative Group", "Default Administrative Role",
               "admin", "admin", "c:/CA.keystore", "capassword", "allusers", "testalias", 
               "c:/test.keystore", "password", "host", "5601", "privatePassword", 
               "0", "5000", "0");
       
       CTController ctController = new CTController(ctItRes);
       String retVal = ctController.openConnection();
       System.out.println(retVal);
       
       ctController.searchUsers(CTSearchCriterion.USERID_STARTSWITH, "gandalf");
       
   }
   
   
}
