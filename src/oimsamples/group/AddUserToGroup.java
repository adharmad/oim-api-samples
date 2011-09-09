package oimsamples.group;

import java.util.HashMap;
import java.util.Properties;

import oimsamples.util.OIMUtils;


import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcGroupOperationsIntf;
import Thor.API.Operations.tcUserOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class AddUserToGroup {

	public static void main(String[] args) throws Exception {

		String grpName = "adminGroup";
		String usrLogin = "johndoe4";
		
		Properties jndi = ConfigurationClient.getComplexSettingByPath(
				"Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm",
				"xelsysadm");

		tcGroupOperationsIntf grpIntf = (tcGroupOperationsIntf) factory
				.getUtility("Thor.API.Operations.tcGroupOperationsIntf");
		tcUserOperationsIntf usrIntf = (tcUserOperationsIntf)factory.getUtility("Thor.API.Operations.tcUserOperationsIntf");

		long grpKey = OIMUtils.getGroupKey(grpIntf, grpName);
		long usrKey = OIMUtils.getUserKey(usrIntf, usrLogin);
		
		grpIntf.addMemberUser(grpKey, usrKey);
		
		System.out.println("Added user " + usrLogin + " to  group " +  grpName);
		factory.close();
		System.exit(0);

	}

}
