package oimsamples.recon;

import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import oimsamples.util.OIMUtils;


import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcReconciliationOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class DeletionDetection {

	public static void main(String[] args) throws Exception {
		Properties jndi = ConfigurationClient.getComplexSettingByPath(
		"Discovery.CoreServer").getAllSettings();
	tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm",
		"xelsysadm");
	tcReconciliationOperationsIntf reconIntf = (tcReconciliationOperationsIntf) factory
		.getUtility("Thor.API.Operations.tcReconciliationOperationsIntf");
		
		HashMap hm = new HashMap();
		hm.put("login", "johndoe1");

		HashMap hm1 = new HashMap();
		hm1.put("login", "johndoe2");
		
		HashMap hm2 = new HashMap();
		hm2.put("login", "johndoe3");		
	
		// This user should be deleted
		//HashMap hm3 = new HashMap();
		//hm3.put("login", "johndoe4");		

		HashMap hm4 = new HashMap();
		hm4.put("login", "sa");		
		
		HashMap[] hmArray = {
				hm,
				hm1,
				hm2,
				hm4,
		};
		
		Set s =  reconIntf.provideDeletionDetectionData("Xellerate User", hmArray);
		
		System.out.println(s);
		
		tcResultSet rs = reconIntf.getMissingAccounts("Xellerate User", s);
		
		OIMUtils.printResultSet(rs);
		
		reconIntf.deleteDetectedAccounts(rs);

		factory.close();
		System.exit(0);
	}

}
