package oimsamples.recon;


import java.util.HashMap;
import java.util.Properties;

import com.thortech.xl.util.config.ConfigurationClient;

import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcReconciliationOperationsIntf;


public class TrustedRecon {

	public static void main(String[] args) throws Exception {
		
		String usrLogin = "JOHNDOE5";
		Properties jndi = ConfigurationClient.getComplexSettingByPath(
			"Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm",
			"xelsysadm");
		tcReconciliationOperationsIntf reconIntf = (tcReconciliationOperationsIntf) factory
			.getUtility("Thor.API.Operations.tcReconciliationOperationsIntf");

			HashMap hm = new HashMap();

			hm.put("login", usrLogin);
			hm.put("first", usrLogin + "1st");
			hm.put("last", usrLogin + "Lst");
			hm.put("type", "End-User");
			hm.put("org", "Xellerate Users");
			hm.put("role", "Full-Time");
			hm.put("ssn", "000-00-0000");

			long rceKey = reconIntf.createReconciliationEvent("Xellerate User",
					hm, true);
			System.out.println("Created recon event rceKey =  " + rceKey);
		
		factory.close();
		System.exit(0);
	}

}
