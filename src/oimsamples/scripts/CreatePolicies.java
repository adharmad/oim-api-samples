package oimsamples.scripts;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import oimsamples.util.OIMUtils;

import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcAccessPolicyOperationsIntf;
import Thor.API.Operations.tcFormDefinitionOperationsIntf;
import Thor.API.Operations.tcGroupOperationsIntf;
import Thor.API.Operations.tcITResourceDefinitionOperationsIntf;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;
import Thor.API.Operations.tcLookupOperationsIntf;
import Thor.API.Operations.tcObjectOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;
import com.thortech.xl.vo.AccessPolicyResourceData;

public class CreatePolicies {
	public static void main(String[] args) throws Exception {
		Random r = new Random(System.currentTimeMillis());
		
		int count = Integer.parseInt(args[0].trim());
		System.out.println("count = " + count);
		String grpPrefix = "rocketrole";
		String policyPrefix = args[1];
		
		String provObjName = "YYYY";
		String pFormName = "UD_YYYYP";
		String cFormName = "UD_YYYYC";
		String lookupName = "Lookup.Entitlements.YYYY";
		String[] itResNames = {
			"YYYY1", "YYYY2", "YYYY3" 	
		};
		
		Properties jndi = ConfigurationClient.getComplexSettingByPath("Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm", "xelsysadm");

		tcAccessPolicyOperationsIntf apIntf = (tcAccessPolicyOperationsIntf) factory.getUtility("Thor.API.Operations.tcAccessPolicyOperationsIntf");
		tcGroupOperationsIntf grpIntf = (tcGroupOperationsIntf) factory.getUtility("Thor.API.Operations.tcGroupOperationsIntf");
		tcObjectOperationsIntf objIntf = (tcObjectOperationsIntf) factory.getUtility("Thor.API.Operations.tcObjectOperationsIntf");
		tcFormDefinitionOperationsIntf fdIntf = (tcFormDefinitionOperationsIntf) factory.getUtility("Thor.API.Operations.tcFormDefinitionOperationsIntf");
		tcLookupOperationsIntf lookupIntf = (tcLookupOperationsIntf) factory.getUtility("Thor.API.Operations.tcLookupOperationsIntf");
		tcITResourceDefinitionOperationsIntf itdefIntf = (tcITResourceDefinitionOperationsIntf) factory.getUtility("Thor.API.Operations.tcITResourceDefinitionOperationsIntf;");
	    tcITResourceInstanceOperationsIntf itinstIntf = (tcITResourceInstanceOperationsIntf) factory.getUtility("Thor.API.Operations.tcITResourceInstanceOperationsIntf;");
		
		long itResKeys[] = new long[itResNames.length];
			
		for (int i=0 ; i<itResNames.length ; i++) {
			String itresName = itResNames[i];
			long itresKey = OIMUtils.getITResKey(itinstIntf, itresName);
			itResKeys[i] = itresKey;
			System.out.println("itresName = " + itresName + " svr_key=" + itresKey);
		}
		
		// get entitlements
		tcResultSet rs1 = lookupIntf.getLookupValues(lookupName);
		String[] entitlements = new String[rs1.getRowCount()];
		System.out.println("entitlements count = " + entitlements.length);
		for (int i=0 ; i<rs1.getRowCount() ; i++) {
			rs1.goToRow(i);
			String encode = rs1.getStringValue("Lookup Definition.Lookup Code Information.Code Key");
			entitlements[i] = encode;
			//System.out.println("entitlements # " + i + " = " + encode );
		}		
		
		long provObjKey = OIMUtils.getObjectKey(objIntf, provObjName);
		long pFormDefKey = OIMUtils.getFormDefKey(fdIntf, pFormName);
		long cFormDefKey = OIMUtils.getFormDefKey(fdIntf, cFormName);
		
		for (int i=0 ; i<count ; i++) {
			String policyName = policyPrefix + i;
			long itresKey = itResKeys[i%itResKeys.length];			
		
			HashMap hm = new HashMap();
			hm.put("Access Policies.Retrofit Flag", "0");
			hm.put("Access Policies.By Request", "0");
			hm.put("Access Policies.Description", policyName + "_description");
			hm.put("Access Policies.Name", policyName);
			hm.put("Access Policies.Note", policyName + " NOTE");

			long[] provObjKeys = { provObjKey };
			boolean[] revokeObjectIfNotApply = { true };
			long[] denyObjKeys = new long[0];
			
			
			AccessPolicyResourceData aprd = new AccessPolicyResourceData(provObjKey, 
				provObjName, pFormDefKey, pFormName, "P");
			HashMap hm1 = new HashMap(); // parent data
			hm1.put(pFormName + "_SERVER", itresKey + "");
			aprd.setFormData(hm1);
			
			for (int j=0 ; j<5 ; j++) {
				HashMap cRecords = new HashMap();
				int rNum = r.nextInt();
				int idx = Math.abs(rNum%entitlements.length);
				
				System.out.println("rNum = " + rNum + " idx = " + idx);
				
				String ent = entitlements[idx];
				String entDesc = ent + "_desc" + "_" + j;
				
				System.out.println("ent = " + ent);
				
				cRecords.put(cFormName + "_ENT", ent);
				cRecords.put(cFormName + "_ENTDESC", entDesc);
				
				aprd.addChildTableRecord(cFormDefKey+"", cFormName, "Add", cRecords);
			}
			
			long[] groupKeys = getRandomGroupKeys(grpIntf, grpPrefix, r);
        
			AccessPolicyResourceData[] aprdArray = { aprd };
		
			long apKey = apIntf.createAccessPolicy(hm, provObjKeys, revokeObjectIfNotApply,
				denyObjKeys, groupKeys, aprdArray);
		
			System.out.println("Created accesspolicy name = " + policyName + " key = " + apKey);
		}

		itdefIntf.close();
		itinstIntf.close();
		lookupIntf.close();
		apIntf.close();
		grpIntf.close();
		objIntf.close();
		fdIntf.close();
		
		factory.close();
		System.exit(0);
			
	}
	
	public static long[] getRandomGroupKeys(tcGroupOperationsIntf grpIntf, String grpPrefix, Random r) {
		Set<String> grpSet = new HashSet<String>();
		
		while (grpSet.size() != 10) {
		//for (int k=0 ; k<10 ; k++) {
			int rNum = Math.abs(r.nextInt() % 100);				
			String grpName = grpPrefix + rNum;
			long grpKey = OIMUtils.getGroupKey(grpIntf, grpName);
			//System.out.println("grp name = " + grpName + " grp key[" + k + "] = " + grpKey);
			System.out.println("grp name = " + grpName + " grp key = " + grpKey);
			//groupKeys[k] = grpKey;
			grpSet.add("" + grpKey);
		}

		String[] grpKeyArray = grpSet.toArray(new String[0]);
		long[] groupKeys = OIMUtils.toNativeLongArray(grpKeyArray);
		
		return groupKeys;
	}
}
