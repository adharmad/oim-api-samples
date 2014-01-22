package iamsamples.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Exceptions.tcPropertyNotFoundException;
import Thor.API.Operations.tcAccessPolicyOperationsIntf;
import Thor.API.Operations.tcFormDefinitionOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;
import com.thortech.xl.vo.AccessPolicyResourceData;
import com.thortech.xl.vo.PolicyChildTableRecord;

public class GetEntitlementsForAccessPolicy {

	public static void main(String[] args) throws Exception {

		String policyName = args[0].trim();
		System.out.println("Policy name = " + policyName);
		
		Properties jndi = ConfigurationClient.getComplexSettingByPath(
				"Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm",
				"Welcome1");

		tcAccessPolicyOperationsIntf apIntf = (tcAccessPolicyOperationsIntf) factory
				.getUtility("Thor.API.Operations.tcAccessPolicyOperationsIntf");
		tcFormDefinitionOperationsIntf fdIntf = (tcFormDefinitionOperationsIntf) factory
				.getUtility("Thor.API.Operations.tcFormDefinitionOperationsIntf");
		
		ArrayList entitlements = new ArrayList();

		HashMap hm = new HashMap();
		hm.put("Access Policies.Name", policyName);

		tcResultSet rs = apIntf.findAccessPolicies(hm);
		rs.goToRow(0);
		
		long polKey = rs.getLongValue("Access Policies.Key");

		AccessPolicyResourceData[] data = apIntf.getDataSpecifiedForObjects(polKey);
		// For each resource that is a part of the policy do the following
		for (int i=0 ; i<data.length ; i++) {
			// Get the AP resource data
			AccessPolicyResourceData aprd = data[i];
			//OIMUtils.printAPRD(aprd);
			
			// Get all the child tables for that resource
			hm.clear();
			hm = aprd.getChildTables();

			// Each child table map is of the form <CHILD_TABLE_KEY> ==> <CHILD_TABLE_NAME>
			Iterator it = hm.keySet().iterator();
			while (it.hasNext()) {
				String sdkKey = (String)it.next();
				
				// Get the form information
				HashMap hm1 = new HashMap();
				hm1.put("Structure Utility.Key", sdkKey);
				tcResultSet sdkRS = fdIntf.findForms(hm1);
				sdkRS.goToRow(0);
				int activeVersion = sdkRS.getIntValue("Structure Utility.Active Version");
				
				// Get the form fields
				tcResultSet sdcRS = fdIntf.getFormFields(Long.parseLong(sdkKey), activeVersion);
				
				// Get the entitlement field name (assuming single)
				String entFieldName = "";
				for (int j=0 ; j<sdcRS.getRowCount() ; j++) {
					sdcRS.goToRow(j);
					long fieldKey = sdcRS.getLongValue("Structure Utility.Additional Columns.Key");
					String fieldName = sdcRS.getStringValue("Structure Utility.Additional Columns.Name");
					try {
						String propertyValue = fdIntf.getFormFieldPropertyValue(fieldKey, "Entitlement");
						if ("true".equalsIgnoreCase(propertyValue)) {
							entFieldName = fieldName;
							break;
						}
						
					} catch (tcPropertyNotFoundException ex) {
						// eat it - field does not have entitlement property defined
						//System.out.println("Entitlement property not defined for field " + fieldName);
					}
				}
				
				System.out.println(entFieldName);
				
				// Get the list of entitlements from child table records
				PolicyChildTableRecord[] childRecs = aprd.getChildTableRecords(sdkKey);
				
				for (int k=0 ; k<childRecs.length ; k++) {
					PolicyChildTableRecord rec = childRecs[i];
					HashMap recData = rec.getRecordData();
					//System.out.println("\t" + rec.getRecordNumber() + " :: " + rec.getRecordData());
					entitlements.add(recData.get(entFieldName));
				}
			}
			
			System.out.println(entitlements);
			
			
		}
		
		apIntf.close();
		factory.close();
		System.exit(0);
	}

}
