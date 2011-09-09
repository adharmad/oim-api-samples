package oimsamples.user;

import java.util.Properties;

import oimsamples.util.OIMUtils;


import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcUserOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class DisableUser {

	public static void main(String[] args) throws Exception {

		String usrLogin = "johndoe3";
		Properties jndi = ConfigurationClient.getComplexSettingByPath("Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm", "xelsysadm");
		
		tcUserOperationsIntf usrIntf = (tcUserOperationsIntf)factory.getUtility("Thor.API.Operations.tcUserOperationsIntf");
		long usrKey = OIMUtils.getUserKey(usrIntf, usrLogin);
		usrIntf.disableUser(usrKey);
		System.out.println("Disabled user " + usrLogin );

		factory.close();
		System.exit(0);
	}

}
