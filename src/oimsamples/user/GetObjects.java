package oimsamples.user;

import java.util.HashMap;
import java.util.Properties;

import oimsamples.util.OIMUtils;

import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcUserOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class GetObjects {


	public static void main(String[] args) throws Exception {

		long usrKey = 6;
		
		Properties jndi = ConfigurationClient.getComplexSettingByPath(
				"Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm",
				"xelsysadm");

		tcUserOperationsIntf usrIntf = (tcUserOperationsIntf) factory
				.getUtility("Thor.API.Operations.tcUserOperationsIntf");


		tcResultSet rs = usrIntf.getObjects(usrKey);
		
		OIMUtils.printResultSet(rs);
		
		
		factory.close();
		System.exit(0);
	}

}
