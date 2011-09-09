package oimsamples.lookup;

import java.util.Properties;

import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcLookupOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class CreateLookups {
	public static void main(String[] args) throws Exception {
		String lookupCode = "Test.Lookup.Code";
		
		Properties jndi = ConfigurationClient.getComplexSettingByPath(
				"Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm",
				"xelsysadm");
		tcLookupOperationsIntf lookupIntf = (tcLookupOperationsIntf) factory
				.getUtility("Thor.API.Operations.tcLookupOperationsIntf");
		
		lookupIntf.addLookupCode(lookupCode);
		for (int i = 0; i < 10; i++) {
			System.out.println("Creating lookup entry number " + i);
			lookupIntf.addLookupValue(lookupCode, "foo" + i, "foodesc" + i,
					"en", "US");
		}

		factory.close();
		System.exit(0);
	}
}