package oimsamples.user;

import java.util.HashMap;
import java.util.Properties;

import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcUserOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class UpdateUser {

	public static void main(String[] args) throws Exception {

		String usrLogin = "johndoe2";
		Properties jndi = ConfigurationClient.getComplexSettingByPath("Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm", "xelsysadm");
		
		tcUserOperationsIntf usrIntf = (tcUserOperationsIntf)factory.getUtility("Thor.API.Operations.tcUserOperationsIntf");

		HashMap usrMap = new HashMap();
		usrMap.put("Users.User ID", usrLogin);
		
		tcResultSet usrRS = usrIntf.findUsers(usrMap);
		
		HashMap usrMap1 = new HashMap();
		usrMap1.put("Users.User ID", usrLogin);
		usrMap1.put("Users.First Name", "TestFirst");
		usrMap1.put("Users.Last Name", "TestLast");

		usrIntf.updateUser(usrRS, usrMap1);
		System.out.println("Updated user " + usrLogin);

		factory.close();
		System.exit(0);
	}

}
