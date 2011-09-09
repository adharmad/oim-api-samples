package oimsamples.classexample;

import java.util.Properties;

import oimsamples.util.OIMUtils;

import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcObjectOperationsIntf;
import Thor.API.Operations.tcUserOperationsIntf;

import com.thortech.xl.dataaccess.tcDataProvider;
import com.thortech.xl.util.config.ConfigurationClient;

public class DirectProvisionAdapter {

	// public static void main(String[] args) throws Exception {
	public static String provisionDummyResource2(tcDataProvider dataProvider,
			String usrLogin, String resourceName) {

		try {

			// Setup local variables
			// String usrLogin = "johndoe13";
			// String resourceName = "DummyResource1";

			// Get the JNDI properties to connect to the server
			//Properties jndi = ConfigurationClient.getComplexSettingByPath(
			//		"Discovery.CoreServer").getAllSettings();

			// Create a new instance of tcUtility Factory
			//tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm",
			//		"xelsysadm");

			// Get a handle to the various OperationsIntf using the utility
			// factory
//			tcUserOperationsIntf usrIntf = (tcUserOperationsIntf) factory
//				.getUtility("Thor.API.Operations.tcUserOperationsIntf");
//			tcObjectOperationsIntf objIntf = (tcObjectOperationsIntf) factory
//				.getUtility("Thor.API.Operations.tcObjectOperationsIntf");
			tcUserOperationsIntf usrIntf = (tcUserOperationsIntf) tcUtilityFactory
				.getUtility(dataProvider, "Thor.API.Operations.tcUserOperationsIntf");
			tcObjectOperationsIntf objIntf = (tcObjectOperationsIntf) tcUtilityFactory
				.getUtility(dataProvider, "Thor.API.Operations.tcObjectOperationsIntf");

			
			
			// Get the key for the resource object we want to provision
			long objKey = OIMUtils.getObjectKey(objIntf, resourceName);

			// Get the user key for the target user
			long usrKey = OIMUtils.getUserKey(usrIntf, usrLogin);

			// Invoke the direct provision API
			long oiuKey = usrIntf.provisionObject(usrKey, objKey);
			System.out.println("Provisioned object " + resourceName
					+ " to user " + usrLogin + " oiu_key = " + oiuKey);

		} catch (Exception ex) {
			ex.printStackTrace();
			return "FAILURE";
		}
		
		return "SUCCESS";
	}

}
