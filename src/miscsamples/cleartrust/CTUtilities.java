package miscsamples.cleartrust;

import java.util.*;
import sirrus.api.client.*;
import sirrus.connect.*;
import com.thortech.xl.schedule.tasks.*;
//import Thor.API.Operations.*;
//import Thor.API.*;
import com.thortech.xl.dataaccess.tcDataSetException;
import com.thortech.xl.util.adapters.*;
import com.thortech.util.logging.Logger;
/**
 *
 * CTUtilities.java is a utility class that has all the methods that are needed
 * in Provisioning, Reconciliation and Testing to connect to CT and carry out
 * operations on that system. Having this method in a separate utilities class
 * makes it accessible to all the other classes and also has all the CT related
 * code in one place so that modification if needed at a later stage have to be
 * done at only one place.
 */
public class CTUtilities {

	public String machineName,
			sslMode,
			caFile,
			caPassword,
			ksFile,
			ksPassword,
			keyAlias,
			privatePass,
			
			aUserId,
			aPassword,
			aGroup,
			aRole=""; //$NON-NLS-1$
	
	public int port,timeOut=0;
	ConnectionDescriptor cd = null;
	public Logger logger = null;

	public CTUtilities(){
		logger = Logger.getLogger("Adapters.CTIntegration");
	}

	/**
	* Sets the CT Target Properties for getting the connection
	*
	* @param serverAttrib <code>Hashtable</code> containing CT server attributes
	* @return "CT_SETPROPERTIES_COMPLETE" if successful or an error message
	*/
	public synchronized String setServerProperties(Hashtable ht){

		try{
			machineName = (String)ht.get("machinename");//$NON-NLS-1$
			validate(machineName);
		}catch(Exception e){
			e.printStackTrace();
			logger.fatal("CT_MACHINENAME_FAIL",e);
			return "CT_MACHINENAME_FAIL";//$NON-NLS-1$
		}
		try{
			port = new Integer((String)ht.get("port")).intValue();//$NON-NLS-1$
		}catch(Exception e){
			e.printStackTrace();
			logger.fatal("CT_PORT_FAIL",e);
			return "CT_PORT_FAIL";//$NON-NLS-1$
		}
		try{
			sslMode = ((String)ht.get("sslmode")).toUpperCase();//$NON-NLS-1$
			validate(sslMode);
			if((!("CLEAR").equals(sslMode))&&(!("SSL_ANON").equals(sslMode))&&//$NON-NLS-1$
			(!("SSL_AUTH").equals(sslMode))){//$NON-NLS-1$
				logger.fatal("SSLMODE VALUE SHOULD BE CLEAR, SSL_ANON OR SSL_AUTH - sslmode="+sslMode);
				throw new Exception("SSLMODE VALUE SHOULD BE CLEAR, SSL_ANON OR SSL_AUTH");//$NON-NLS-1$
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.fatal("CT_SSLMODE_FAIL",e);
			return "CT_SSLMODE_FAIL";//$NON-NLS-1$
		}
		try{
			timeOut = new Integer((String)ht.get("timeout")).intValue();//$NON-NLS-1$
		}catch(Exception e){
			e.printStackTrace();
			logger.fatal("CT_TIMEOUT_FAIL",e);
			return "CT_TIMEOUT_FAIL";//$NON-NLS-1$
		}
		try{
			aUserId = (String)ht.get("admin");//$NON-NLS-1$
			validate(aUserId);
		}catch(Exception e){
			e.printStackTrace();
			logger.fatal("CT_ADMINUSERID_FAIL",e);
			return "CT_ADMINUSERID_FAIL";//$NON-NLS-1$
		}
		try{
			aPassword = (String)ht.get("adminpassword");//$NON-NLS-1$
			validate(aPassword);
		}catch(Exception e){
			e.printStackTrace();
			logger.fatal("CT_ADMINPASSWORD_FAIL",e);
			return "CT_ADMINPASSWORD_FAIL";//$NON-NLS-1$
		}
		try{
			aGroup = (String)ht.get("admingroup");//$NON-NLS-1$
			validate(aGroup);
		}catch(Exception e){
			e.printStackTrace();
			logger.fatal("CT_ADMINGROUP_FAIL",e);
			return "CT_ADMINGROUP_FAIL";//$NON-NLS-1$
		}
		try{
			aRole = (String)ht.get("adminrole");//$NON-NLS-1$
			validate(aRole);
		}catch(Exception e){
			e.printStackTrace();
			logger.fatal("CT_ADMINROLE_FAIL",e);
			return "CT_ADMINROLE_FAIL";//$NON-NLS-1$
		}
		if(("SSL_AUTH").equals(sslMode))
		{
			try{
				caFile = (String)ht.get("cafile");//$NON-NLS-1$
				validate(caFile);
			}catch(Exception e){
				e.printStackTrace();
				logger.fatal("CT_CAFILE_FAIL",e);
				return "CT_CAFILE_FAIL";//$NON-NLS-1$
			}
			try{
				caPassword = (String)ht.get("capassword");//$NON-NLS-1$
				validate(caPassword);
			}catch(Exception e){
				e.printStackTrace();
				logger.fatal("CT_CAPASSWORD_FAIL",e);
				return "CT_CAPASSWORD_FAIL";//$NON-NLS-1$
			}
			try{
				ksFile = (String)ht.get("ksfile");//$NON-NLS-1$
				validate(ksFile);
			}catch(Exception e){
				e.printStackTrace();
				logger.fatal("CT_KSFILE_FAIL",e);
				return "CT_KSFILE_FAIL";//$NON-NLS-1$
			}
			try{
				ksPassword = (String)ht.get("kspassword");//$NON-NLS-1$
				validate(ksPassword);
			}catch(Exception e){
				e.printStackTrace();
				logger.fatal("CT_KSPASSWORD_FAIL",e);
				return "CT_KSPASSWORD_FAIL";//$NON-NLS-1$
			}
			try{
				keyAlias = (String)ht.get("keyalias");//$NON-NLS-1$
				validate(keyAlias);
			}catch(Exception e){
				e.printStackTrace();
				logger.fatal("CT_KEYALIAS_FAIL",e);
				return "CT_KEYALIAS_FAIL";//$NON-NLS-1$
			}
			try{
				privatePass = (String)ht.get("privatepass");//$NON-NLS-1$
				validate(privatePass);
			}catch(Exception e){
				e.printStackTrace();
				logger.fatal("CT_PRIVATEPASS_FAIL",e);
				return "CT_PRIVATEPASS_FAIL";//$NON-NLS-1$
			}
			try{
				cd = new ConnectionDescriptor(machineName, port, ConnectionDescriptor.SSL_AUTH
				, caFile, caPassword, ksFile, ksPassword, keyAlias, privatePass
				, timeOut);
			}catch(Exception e){
				e.printStackTrace();
				logger.fatal("CT_CONNECTIONDESCRIPTOR_FAIL - WHEN SSL=SSL_AUTH",e);
				return "CT_CONNECTIONDESCRIPTOR_FAIL";//$NON-NLS-1$
			}
		}
		else 
		{
			try{
				cd = new ConnectionDescriptor(machineName, port,
						(("SSL_ANON").equals(sslMode)?ConnectionDescriptor.SSL_ANON:ConnectionDescriptor.CLEAR)
						, timeOut);
			}catch(Exception e){
				e.printStackTrace();
				logger.fatal("CT_CONNECTIONDESCRIPTOR_FAIL - WHEN SSL!=SSL_AUTH",e);
				return "CT_CONNECTIONDESCRIPTOR_FAIL";//$NON-NLS-1$
			}
		}
		return "CT_SETPROPERTIES_COMPLETE";
	}

	/**
	* Establishes a connection with the CT Target Source
	*
	* @return <code>APIServerProxy</code> contains the open connection
	*/
	public synchronized APIServerProxy openConnection()throws Exception{
		APIServerProxy serverProxy = new APIServerProxy(cd);
		serverProxy.connect(aUserId, aPassword,aGroup,aRole);
		return serverProxy;
	}

	/**
	* Validates whether the String is empty or null
	*
	* @param value <code>String</code> to be validated
	*/
	public synchronized static void validate(String value)throws Exception{
		if((("").equals(value))||(value==null)){
			throw new Exception("VALUE NOT FOUND");
		}
	}

	/**
	* Closes the connection with the CT Target Source
	*
	* @param <code>APIServerProxy</code> contains the open connection
	*/
	public synchronized void closeConnection(APIServerProxy serverProxy)throws Exception{
		serverProxy.disconnect();
	}
	
	public Hashtable getITResource(tcBaseTask tc){
		Hashtable serverProperties = null;
		try
		{
			String isServerName = tc.getAttribute("Server");
			/* Get property-value pairs for the specified IT Resource */
			Hashtable ht = tcUtilXellerateOperations.getITAssetProperties(tc.getDataBase(), isServerName);
			serverProperties = new Hashtable();
			serverProperties.put("admin",(((String)ht.get("CTAdminUserId"))==null?"":((String)ht.get("CTAdminUserId"))));
			serverProperties.put("machinename",(((String)ht.get("MachineName/IPAddress"))==null?"":((String)ht.get("MachineName/IPAddress"))));
			serverProperties.put("keyalias",(((String)ht.get("KeyAlias"))==null?"":((String)ht.get("KeyAlias"))));
			serverProperties.put("sslmode",(((String)ht.get("SSLMode"))==null?"":((String)ht.get("SSLMode"))));
			serverProperties.put("privatepass",(((String)ht.get("PrivatePass"))==null?"":((String)ht.get("PrivatePass"))));
			serverProperties.put("ksfile",(((String)ht.get("KsFile"))==null?"":((String)ht.get("KsFile"))));
			serverProperties.put("adminpassword",(((String)ht.get("CTAdminPassword"))==null?"":((String)ht.get("CTAdminPassword"))));
			serverProperties.put("cafile",(((String)ht.get("CaFile"))==null?"":((String)ht.get("CaFile"))));
			serverProperties.put("port",(((String)ht.get("Port"))==null?"":((String)ht.get("Port"))));
			serverProperties.put("timeout",(((String)ht.get("TimeOut"))==null?"":((String)ht.get("TimeOut"))));
			serverProperties.put("admingroup",(((String)ht.get("CTAdmin Group"))==null?"":((String)ht.get("Admin Group"))));
			serverProperties.put("adminrole",(((String)ht.get("CTAdmin Role"))==null?"":((String)ht.get("Admin Role"))));
			serverProperties.put("kspassword",(((String)ht.get("KsPassword"))==null?"":((String)ht.get("KsPassword"))));
			serverProperties.put("capassword",(((String)ht.get("CaPassword"))==null?"":((String)ht.get("CaPassword"))));
			serverProperties.put("timestamp",(((String)ht.get("TimeStamp"))==null?"":((String)ht.get("TimeStamp"))));
		}
		catch(tcDataSetException dse)
		{
		  dse.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		return serverProperties;
	}

}
