package oimsamples.ap;

import java.util.HashMap;
import java.util.Properties;

import oimsamples.util.OIMUtils;


import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcAccessPolicyOperationsIntf;
import Thor.API.Operations.tcGroupOperationsIntf;
import Thor.API.Operations.tcObjectOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;
import com.thortech.xl.vo.AccessPolicyResourceData;

public class CreateAccessPolicy {
	public static void main(String[] args) throws Exception {
		
		String provObjName = "TestResource3";
		String denyObjName = "TestResource1";
		String grpName = "apGroup1";
		String policyName = "AccessPolicy1";
		
		Properties jndi = ConfigurationClient.getComplexSettingByPath(
				"Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm",
				"xelsysadm");

		tcAccessPolicyOperationsIntf apIntf = (tcAccessPolicyOperationsIntf) factory
				.getUtility("Thor.API.Operations.tcAccessPolicyOperationsIntf");
		tcGroupOperationsIntf grpIntf = (tcGroupOperationsIntf) factory
			.getUtility("Thor.API.Operations.tcGroupOperationsIntf");
		tcObjectOperationsIntf objIntf = (tcObjectOperationsIntf) factory
			.getUtility("Thor.API.Operations.tcObjectOperationsIntf");
		
		
		long provObjKey = OIMUtils.getObjectKey(objIntf, provObjName);
		long denyObjKey = OIMUtils.getObjectKey(objIntf, denyObjName);
		long grpKey = OIMUtils.getGroupKey(grpIntf, grpName);
		

		HashMap hm = new HashMap();
		hm.put("Access Policies.Retrofit Flag", "0");
		hm.put("Access Policies.By Request", "0");
		hm.put("Access Policies.Description", "test");
		hm.put("Access Policies.Name", policyName);
		hm.put("Access Policies.Note", "TEST NOTE");

		long[] provObjKeys = { provObjKey };
		boolean[] revokeObjectIfNotApply = { true };
		long[] denyObjKeys = { denyObjKey };
		long[] groupKeys = {grpKey};
		apIntf.createAccessPolicy(hm, provObjKeys, revokeObjectIfNotApply,
				denyObjKeys, groupKeys, new AccessPolicyResourceData[0]);
		
		System.out.println("Created accesspolicy name = " + policyName);
		
		factory.close();
		System.exit(0);
	}
}

