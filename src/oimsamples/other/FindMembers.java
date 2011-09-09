package oimsamples.other;

import java.util.Properties;

import oimsamples.util.OIMUtils;

import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcGroupOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class FindMembers {

	public static void main(String[] args) throws Exception {

		String grpName = "";
		
		Properties jndi = ConfigurationClient.getComplexSettingByPath(
				"Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm",
				"xelsysadm");

		tcGroupOperationsIntf grpIntf = (tcGroupOperationsIntf) factory
				.getUtility("Thor.API.Operations.tcGroupOperationsIntf");

		long grpKey = OIMUtils.getGroupKey(grpIntf, grpName);
		tcResultSet members = grpIntf.getAllMembers(grpKey);

		OIMUtils.printResultSet(members);

		factory.close();
		System.exit(0);

	}

}
