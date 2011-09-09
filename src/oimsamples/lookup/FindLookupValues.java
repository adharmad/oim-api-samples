package oimsamples.lookup;

import java.util.Properties;

import oimsamples.util.OIMUtils;


import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcLookupOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class FindLookupValues {

	public static void main(String[] args) throws Exception {
		String lookupCode = "Lookup.Foo.Bar";
		
		Properties jndi = ConfigurationClient.getComplexSettingByPath(
				"Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm",
				"xelsysadm");
		tcLookupOperationsIntf lookupIntf = (tcLookupOperationsIntf) factory
				.getUtility("Thor.API.Operations.tcLookupOperationsIntf");

		tcResultSet rs = lookupIntf.getLookupValues(lookupCode);

		OIMUtils.printResultSet(rs);
		
		factory.close();
		System.exit(0);
	}

}
