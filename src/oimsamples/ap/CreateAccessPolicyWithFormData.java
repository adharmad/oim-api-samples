package oimsamples.ap;

import java.util.HashMap;
import java.util.Properties;

import oimsamples.util.OIMUtils;

import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcAccessPolicyOperationsIntf;
import Thor.API.Operations.tcFormDefinitionOperationsIntf;
import Thor.API.Operations.tcGroupOperationsIntf;
import Thor.API.Operations.tcObjectOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;
import com.thortech.xl.vo.AccessPolicyResourceData;

public class CreateAccessPolicyWithFormData {

	public static void main(String[] args) throws Exception {
		
		String provObjName = "TestResource2";
		String grpName = "apGroup2";
		String policyName = "AccessPolicy2";
		String formName = "UD_R2PFORM";
		
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
		tcFormDefinitionOperationsIntf fdIntf = (tcFormDefinitionOperationsIntf) factory
			.getUtility("Thor.API.Operations.tcFormDefinitionOperationsIntf");
		
		
		long provObjKey = OIMUtils.getObjectKey(objIntf, provObjName);
		long grpKey = OIMUtils.getGroupKey(grpIntf, grpName);
		long formDefKey = OIMUtils.getFormDefKey(fdIntf, formName);
		

		HashMap hm = new HashMap();
		hm.put("Access Policies.Retrofit Flag", "0");
		hm.put("Access Policies.By Request", "0");
		hm.put("Access Policies.Description", "test");
		hm.put("Access Policies.Name", policyName);
		hm.put("Access Policies.Note", "TEST NOTE");

		long[] provObjKeys = { provObjKey };
		boolean[] revokeObjectIfNotApply = { true };
		long[] denyObjKeys = new long[0];
		long[] groupKeys = {grpKey};
		
		
		AccessPolicyResourceData aprd = new AccessPolicyResourceData(provObjKey, 
				provObjName, formDefKey, formName, "P");
		HashMap hm1 = new HashMap(); // parent data
		hm1.put("UD_R2PFORM_PFIELD1", "aa1");
		hm1.put("UD_R2PFORM_PFIELD2", "bb1");
        aprd.setFormData(hm1);
        
        AccessPolicyResourceData[] aprdArray = { aprd };
		
		apIntf.createAccessPolicy(hm, provObjKeys, revokeObjectIfNotApply,
				denyObjKeys, groupKeys, aprdArray);
		
		System.out.println("Created accesspolicy name = " + policyName);
		
		factory.close();
		System.exit(0);
	}

}
