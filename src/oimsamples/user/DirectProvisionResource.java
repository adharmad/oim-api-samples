package oimsamples.user;

import java.util.Properties;

import oimsamples.util.OIMUtils;


import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcObjectOperationsIntf;
import Thor.API.Operations.tcUserOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class DirectProvisionResource {
	public static void main(String[] args) throws Exception {

		String usrLogin = "johndoe1";
		String objName = "TestResource1";
		Properties jndi = ConfigurationClient.getComplexSettingByPath("Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm", "xelsysadm");
		
		tcUserOperationsIntf usrIntf = (tcUserOperationsIntf)factory.getUtility("Thor.API.Operations.tcUserOperationsIntf");
		tcObjectOperationsIntf objIntf = (tcObjectOperationsIntf)factory.getUtility("Thor.API.Operations.tcObjectOperationsIntf");

		long usrKey = OIMUtils.getUserKey(usrIntf, usrLogin);
		long objKey = OIMUtils.getObjectKey(objIntf, objName);
		
		long obiKey = usrIntf.provisionObject(usrKey, objKey);
		
		System.out.println("Provisioned user " + usrLogin + " with resource object " + objName);

		factory.close();
		System.exit(0);
	}
}
