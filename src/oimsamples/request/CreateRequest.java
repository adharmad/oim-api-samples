package oimsamples.request;

import java.util.HashMap;
import java.util.Properties;

import oimsamples.util.OIMUtils;


import com.thortech.xl.util.config.ConfigurationClient;

import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcObjectOperationsIntf;
import Thor.API.Operations.tcRequestOperationsIntf;
import Thor.API.Operations.tcUserOperationsIntf;


public class CreateRequest {
	public static void main(String[] args) throws Exception {

		String usrLogin = "johndoe4";
		String resourceName = "TestResource1";
		Properties jndi = ConfigurationClient.getComplexSettingByPath("Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm", "xelsysadm");		
		tcUserOperationsIntf usrIntf = (tcUserOperationsIntf)factory.getUtility("Thor.API.Operations.tcUserOperationsIntf");
		tcRequestOperationsIntf reqIntf = (tcRequestOperationsIntf)factory.getUtility("Thor.API.Operations.tcRequestOperationsIntf");
		tcObjectOperationsIntf objIntf = (tcObjectOperationsIntf)factory.getUtility("Thor.API.Operations.tcObjectOperationsIntf");

		HashMap reqHM = new HashMap();
		reqHM.put("Requests.Request Priority", "High");
		reqHM.put("Requests.Target Type", "U");
		reqHM.put("Requests.Object Request Type", "Add");

		long reqKey = reqIntf.createRequest(reqHM);
		System.out.println("reqkey = " + reqKey);
		long objKey = OIMUtils.getObjectKey(objIntf, resourceName);
		reqIntf.addRequestObject(reqKey, objKey);
		
		long usrKey = OIMUtils.getUserKey(usrIntf, usrLogin);

		reqIntf.addRequestUser(reqKey, usrKey);

		reqIntf.completeRequestCreation(reqKey);
		System.out.println("completed request creation");

		factory.close();
		System.exit(0);
	}

}
