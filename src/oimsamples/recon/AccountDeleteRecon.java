package oimsamples.recon;

import java.util.HashMap;
import java.util.Properties;

import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcReconciliationOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class AccountDeleteRecon {

	public static void main(String[] args) throws Exception {

		String usrLogin = "ESPN002";
		Properties jndi = ConfigurationClient.getComplexSettingByPath(
				"Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm",
				"xelsysadm");
		tcReconciliationOperationsIntf reconIntf = (tcReconciliationOperationsIntf) factory
				.getUtility("Thor.API.Operations.tcReconciliationOperationsIntf");

		HashMap hm = new HashMap();

		hm.put("itres", "101");
		hm.put("uid", usrLogin);

		long rceKey = reconIntf.createDeleteReconciliationEvent("delrecon", hm);
		System.out.println("Created recon event rceKey =  " + rceKey);

		factory.close();
		System.exit(0);
	}

}
