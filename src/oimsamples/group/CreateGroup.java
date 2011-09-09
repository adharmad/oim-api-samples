package oimsamples.group;

import java.util.HashMap;
import java.util.Properties;

import com.thortech.xl.util.config.ConfigurationClient;

import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcGroupOperationsIntf;

public class CreateGroup {
	public static void main(String[] args) throws Exception {

		String grpName = "adminGroup";
		Properties jndi = ConfigurationClient.getComplexSettingByPath(
				"Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm",
				"xelsysadm");

		tcGroupOperationsIntf grpIntf = (tcGroupOperationsIntf) factory
				.getUtility("Thor.API.Operations.tcGroupOperationsIntf");
		HashMap grpMap = new HashMap();
		grpMap.put("Groups.Group Name", grpName);

		long grpKey = grpIntf.createGroup(grpMap);
		
		System.out.println("Created grp " + grpName + " " + grpKey);
		factory.close();
		System.exit(0);

	}
}
