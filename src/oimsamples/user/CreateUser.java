package oimsamples.user;

import java.util.HashMap;
import java.util.Properties;

import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcUserOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class CreateUser {
	public static void main(String[] args) throws Exception {

		String usrLogin = args[0];
		Properties jndi = ConfigurationClient.getComplexSettingByPath("Discovery.CoreServer").getAllSettings();
		String testing = ConfigurationClient.getComplexSettingByPath("Discovery.CoreServer").getString("testing", "hello");
		System.out.println(jndi);
		System.out.println(testing);
		/*
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm", "xelsysadm");
		
		tcUserOperationsIntf usrIntf = (tcUserOperationsIntf)factory.getUtility("Thor.API.Operations.tcUserOperationsIntf");
		
		HashMap usrMap = new HashMap();
		usrMap.put("Organizations.Key", 1 + "");
		usrMap.put("Users.User ID", usrLogin);
		usrMap.put("Users.First Name", usrLogin + "_First");
		usrMap.put("Users.Last Name",  usrLogin + "_Last");
		usrMap.put("Users.Password", "foo");
		usrMap.put("Users.Role", "Full-Time");
		usrMap.put("Users.Xellerate Type", "End-User");
		usrMap.put("Users.Email", usrLogin + "_First." + usrLogin + "_Last" + "@example.com");
		usrMap.put("USR_UDF_FNAME", "System");

		long userKey = usrIntf.createUser(usrMap);
		System.out.println("Created user " + usrLogin + " " + userKey);

		factory.close();
		*/
		System.exit(0);
	}
}
