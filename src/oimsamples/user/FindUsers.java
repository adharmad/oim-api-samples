package oimsamples.user;

import java.util.HashMap;
import java.util.Properties;

import oimsamples.util.OIMUtils;


import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcUserOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class FindUsers {

	public static void main(String[] args) throws Exception {

		String login = args[0];
		String password = args[1];
		String firstName = "*";
		Properties jndi = ConfigurationClient.getComplexSettingByPath(
				"Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, login,
				password);

		tcUserOperationsIntf usrIntf = (tcUserOperationsIntf) factory
				.getUtility("Thor.API.Operations.tcUserOperationsIntf");

		String[] columnsToFetch = new String[] {
			"Users.User ID",
			"Users.First Name",
			"Users.Last Name"
		};
		
		HashMap usrMap = new HashMap();
		usrMap.put("Users.First Name", firstName);

		tcResultSet usrRS = usrIntf.findUsersFiltered(usrMap, columnsToFetch);
		
		OIMUtils.printResultSet(usrRS);

		factory.close();
		System.exit(0);
	}
}
