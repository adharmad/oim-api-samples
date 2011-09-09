package oimsamples.classexample;

import java.util.HashMap;
import java.util.Properties;

import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcUserOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class AddUser {

	public static void main(String[] args) throws Exception {
		// Get the JNDI properties to connect to the server
		Properties jndi = ConfigurationClient.getComplexSettingByPath(
				"Discovery.CoreServer").getAllSettings();
		
		// Create a new instance of tcUtility Factory
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm", "xelsysadm");
		
		// Get a handle to the tcUserOperationsIntf using the utility factory
		// (INTERFACE_NAME)factory.getUtility("INTERFACE_NAME")
		tcUserOperationsIntf usrIntf = (tcUserOperationsIntf)factory.getUtility(
				"Thor.API.Operations.tcUserOperationsIntf");
		
		// Set up the variables and values we need to pass to the createUser API
		// If you are running against 1.5 JDK:
		// HashMap<String, String> usrMap = new HashMap<String, String>();
		HashMap usrMap = new HashMap();
		usrMap.put("Users.User ID", 		"johndoe16");
		usrMap.put("Users.First Name", 		"John");
		usrMap.put("Users.Last Name", 		"Doe");
		usrMap.put("Users.Xellerate Type", 	"End-User");
		usrMap.put("Users.Role", 			"Full-Time");
		usrMap.put("Users.Password", 		"password1");
		usrMap.put("Users.Email", 			"john.doe@company.com");
		usrMap.put("USR_UDF_SSN", 			"213-444-0000"); 			// this is optional		
		usrMap.put("Organizations.Key", 	"1");
		
		// Invoke the tcUserOperationsIntf->createUser API
		long usrKey = usrIntf.createUser(usrMap);
		System.out.println("Created user with key " + usrKey);
		
		// Perform cleanup operations
		factory.close();
		
		// Exit
		System.exit(0);
		
	}

}
