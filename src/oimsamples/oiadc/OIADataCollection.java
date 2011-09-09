package oimsamples.oiadc;

import java.util.HashMap;
import java.util.Properties;

import Thor.API.tcUtilityFactory;
import Thor.API.Operations.DataCollectionOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class OIADataCollection {

	public static void main(String[] args) throws Exception {

		String whatToDo = args[0];
		String sessionID = args[1];
		
		Properties jndi = ConfigurationClient.getComplexSettingByPath("Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm", "xelsysadm");
		
		DataCollectionOperationsIntf dcIntf = (DataCollectionOperationsIntf)factory
		.getUtility("Thor.API.Operations.DataCollectionOperationsIntf"); 
		
		if ("START".equalsIgnoreCase(whatToDo)) {
			String entityName = "eBusiness Suite User";
			if (args.length > 2 && !"".equals(args[2].trim())) {
				entityName = args[2];
			}
			System.out.println("entityName = " + entityName);
			
			HashMap<String, Object> entityMap = new HashMap<String, Object>();
			entityMap.put(entityName, null);
			
			dcIntf.startDataCollection(sessionID, entityMap);
			
			System.out.println("Started data collection session with session ID " + sessionID);
		} else if ("STATUS".equalsIgnoreCase(whatToDo)) {
			String status = dcIntf.getDataCollectionStatus(sessionID);
			System.out.println("Status of data collection session with session ID " + sessionID + " = " + status);
		} else if ("FINALIZE".equalsIgnoreCase(whatToDo)) {
			dcIntf.finalizeDataCollectionSession(sessionID);
			System.out.println("Finalized data collection session with session ID " + sessionID);
		} else {
			System.out.println("Unknowm action");
		}
		
		factory.close();
		System.exit(0);
	}


}
