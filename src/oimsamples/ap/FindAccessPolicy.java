package oimsamples.ap;

import java.util.HashMap;
import java.util.Properties;

import oimsamples.util.OIMUtils;

import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcAccessPolicyOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;
import com.thortech.xl.vo.AccessPolicyResourceData;

public class FindAccessPolicy {
	public static void main(String[] args) throws Exception {

		String policyName = args[0].trim();
		System.out.println("Policy name = " + policyName);
		
		Properties jndi = ConfigurationClient.getComplexSettingByPath(
				"Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm",
				"xelsysadm");

		tcAccessPolicyOperationsIntf apIntf = (tcAccessPolicyOperationsIntf) factory
				.getUtility("Thor.API.Operations.tcAccessPolicyOperationsIntf");

		HashMap hm = new HashMap();
		hm.put("Access Policies.Name", policyName);

		tcResultSet rs = apIntf.findAccessPolicies(hm);

		System.out.println("Policy Information is:");
		OIMUtils.printResultSet(rs);
		rs.goToRow(0);
		
		long polKey = rs.getLongValue("Access Policies.Key");
		System.out.println("Groups for access policy with pol_key=" + polKey);
		
		tcResultSet grpRS = apIntf.getAssignedGroups(polKey);
		OIMUtils.printResultSet(grpRS);
		
		System.out.println("Resources for access policy with pol_key=" + polKey);
		
		tcResultSet objRS = apIntf.getAssignedObjects(polKey);
		OIMUtils.printResultSet(objRS);
		
		System.out.println("Defaults for access policy with pol_key=" + polKey);
		
		AccessPolicyResourceData[] data = apIntf.getDataSpecifiedForObjects(polKey);
		for (int i=0 ; i<data.length ; i++) {
			AccessPolicyResourceData aprd = data[i];
			OIMUtils.printAPRD(aprd);
		}
		
		apIntf.close();
		factory.close();
		System.exit(0);
	}
}
