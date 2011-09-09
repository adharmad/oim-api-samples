package oimsamples.org;

import java.util.HashMap;
import java.util.Properties;

import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcOrganizationOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class CreateOrg {
	public static void main(String[] args) throws Exception {
		String orgName = "testorg1";
		
		Properties jndi = ConfigurationClient.getComplexSettingByPath(
				"Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm",
				"xelsysadm");
		tcOrganizationOperationsIntf orgIntf = (tcOrganizationOperationsIntf) factory
				.getUtility("Thor.API.Operations.tcOrganizationOperationsIntf");

		
		HashMap orgMap = new HashMap();
		orgMap.put("Organizations.Organization Name", orgName);
		orgMap.put("Organizations.Type", "Company");

		long orgKey = orgIntf.createOrganization(orgMap);
		System.out.println("Created organization " + orgName + " " + orgKey);

		factory.close();
		System.exit(0);
	}
}
