package oimsamples.oiadc;

import java.util.HashMap;
import java.util.Properties;

import Thor.API.tcUtilityFactory;
import Thor.API.Operations.DataCollectionOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class StartDataCollection {


	public static void main(String[] args) throws Exception {

		String sessionID = args[0];
		
		Properties jndi = ConfigurationClient.getComplexSettingByPath("Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm", "xelsysadm");
		
		DataCollectionOperationsIntf dcIntf = (DataCollectionOperationsIntf)factory
		.getUtility("Thor.API.Operations.DataCollectionOperationsIntf"); 
		
		HashMap<String, Object> entityMap = new HashMap<String, Object>();
		entityMap.put("User", null);
		
		dcIntf.startDataCollection(sessionID, entityMap);
		
		factory.close();
		System.exit(0);
	}

}
