package oimsamples.recon;

import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import oimsamples.util.OIMUtils;

import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcReconciliationOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class UntrustedDeletionDetection {

	public static void main(String[] args) throws Exception {
		Properties jndi = ConfigurationClient.getComplexSettingByPath(
		"Discovery.CoreServer").getAllSettings();
	tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm",
		"xelsysadm");
	tcReconciliationOperationsIntf reconIntf = (tcReconciliationOperationsIntf) factory
		.getUtility("Thor.API.Operations.tcReconciliationOperationsIntf");
		
		HashMap hm = new HashMap();
		hm.put("itres", "101");
		hm.put("uid", "SUTTER1");

		HashMap hm1 = new HashMap();
		hm1.put("itres", "101");
		hm1.put("uid", "SUTTER2");
		
		HashMap hm2 = new HashMap();
		hm2.put("itres", "101");
		hm2.put("uid", "SUTTER3");		
	
		// This account should be deleted
		//HashMap hm3 = new HashMap();
		//hm3.put("itres", "101");		
		//hm3.put("uid", "SUTTER4");

		HashMap hm4 = new HashMap();
		hm4.put("itres", "101");	
		hm4.put("uid", "SUTTER5");
		
		HashMap[] hmArray = {
				hm,
				hm1,
				hm2,
				hm4,
		};
		
		Set s =  reconIntf.provideDeletionDetectionData("delrecon", hmArray);
		
		System.out.println(s);
		
		tcResultSet rs = reconIntf.getMissingAccounts("delrecon", s);
		
		OIMUtils.printResultSet(rs);
		
		reconIntf.deleteDetectedAccounts(rs);

		factory.close();
		System.exit(0);
	}


}
