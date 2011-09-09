package oimsamples.group;

import java.util.HashMap;
import java.util.Properties;

import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcGroupOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class CreateGroups {

	public static void main(String[] args) throws Exception {
		String prefix = args[0];
		int count = Integer.parseInt(args[1].trim());

		Properties jndi = ConfigurationClient.getComplexSettingByPath(
				"Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm",
				"xelsysadm");

		tcGroupOperationsIntf grpIntf = (tcGroupOperationsIntf) factory
				.getUtility("Thor.API.Operations.tcGroupOperationsIntf");
		
		for (int i=0 ; i<count ; i++) {
			String grpName = prefix + i;
			HashMap grpMap = new HashMap();
			grpMap.put("Groups.Group Name", grpName);
			
			long grpKey = grpIntf.createGroup(grpMap);
			System.out.println("Created grp " + grpName + " " + grpKey);
		}
		
		factory.close();
		System.exit(0);

	}

}
