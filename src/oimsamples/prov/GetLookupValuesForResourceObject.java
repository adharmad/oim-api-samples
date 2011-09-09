package oimsamples.prov;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import java.util.Map.Entry;

import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcFormDefinitionOperationsIntf;
import Thor.API.Operations.tcITResourceDefinitionOperationsIntf;
import Thor.API.Operations.tcLookupOperationsIntf;
import Thor.API.Operations.tcObjectOperationsIntf;
import Thor.API.Operations.tcProvisioningOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class GetLookupValuesForResourceObject {

	public static void main(String[] args) throws Exception {
		Properties jndi = ConfigurationClient.getComplexSettingByPath("Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm", "xelsysadm");
		
		tcObjectOperationsIntf objIntf = (tcObjectOperationsIntf)factory.getUtility("Thor.API.Operations.tcObjectOperationsIntf");
		tcProvisioningOperationsIntf provIntf = (tcProvisioningOperationsIntf)factory.getUtility("Thor.API.Operations.tcProvisioningOperationsIntf");
		tcFormDefinitionOperationsIntf fdIntf = (tcFormDefinitionOperationsIntf)factory.getUtility("Thor.API.Operations.tcFormDefinitionOperationsIntf");
		tcITResourceDefinitionOperationsIntf itdefIntf = (tcITResourceDefinitionOperationsIntf)factory.getUtility("Thor.API.Operations.tcITResourceDefinitionOperationsIntf");
		tcLookupOperationsIntf lookupIntf = (tcLookupOperationsIntf)factory.getUtility("Thor.API.Operations.tcLookupOperationsIntf");

		String objName = "oiatest";
		
		HashMap<String, String> objMap = new HashMap<String, String>();
		objMap.put("Objects.Order For", "U");
		objMap.put("Objects.Name", objName);
		
		// get object information
		HashMap<String, String> objs = new HashMap<String, String>();
		tcResultSet objrs = objIntf.findObjects(objMap);
		objrs.goToRow(0);

		String objKey = objrs.getStringValue("Objects.Key");
		
		// get process information
		Vector<String> sdkVector = new Vector<String>();
		
		tcResultSet procrs = objIntf.getProcessesForObject(Long.parseLong(objKey));
		//printResultSet(procrs);
			
		for (int i=0 ; i<procrs.getRowCount() ; i++) {
			procrs.goToRow(i);
			String procType = procrs.getStringValue("Process Definition.Type");
			if ("Provisioning".equals(procType)) {
				String sdkKey = procrs.getStringValue("Structure Utility.Key");
				if (!"".equals(sdkKey.trim())) {
					sdkVector.add(sdkKey);
				}
			}
		}
			
		// get form fields of type Lookup
		HashMap<String, String> sdcMap = new HashMap<String, String>();

		for (int i=0 ; i<sdkVector.size() ; i++) {
			String sdkKey = sdkVector.get(i);
			tcResultSet sdkrs = fdIntf.getFormVersions(Long.parseLong(sdkKey));
			String activeVersion = sdkrs.getStringValue("Structure Utility.Active Version");
				
			tcResultSet sdcrs = fdIntf.getFormFields(Long.parseLong(sdkKey), Integer.parseInt(activeVersion));
			for (int j=0 ; j<sdcrs.getRowCount() ; j++) {
				sdcrs.goToRow(j);
					
				if ("LookupField".equals(sdcrs.getStringValue("Structure Utility.Additional Columns.Field Type"))) {
					sdcMap.put(sdcrs.getStringValue("Structure Utility.Additional Columns.Key"), 
							sdcrs.getStringValue("Structure Utility.Additional Columns.Name"));
				}
			}
				
		}
		
		// get lookup code
		HashMap<String, String> sdcFieldToLookupCodeMap = new HashMap<String, String>();
		Iterator<Entry<String, String>> it1 = sdcMap.entrySet().iterator();
		
		while (it1.hasNext()) {
			Entry<String, String> e = it1.next();
			long sdcKey = Long.parseLong(e.getKey());
			String sdcName = e.getValue();
			String lookupCode = fdIntf.getFormFieldPropertyValue(sdcKey, "Lookup Code");
			sdcFieldToLookupCodeMap.put(sdcName, lookupCode);	
		}
			
		//printHM(sdcFieldToLookupCodeMap);

		Iterator<Entry<String, String>> it2 = sdcFieldToLookupCodeMap.entrySet().iterator();
		
		while (it2.hasNext()) {
			Entry<String, String> e = it2.next();
			String sdcName = e.getKey();
			String lookupCode = e.getValue();
			
			tcResultSet lookuprs = lookupIntf.getLookupValues(lookupCode);
			
			System.out.println("Lookup values for fieldname = " + sdcName + " and lookupcode = " + lookupCode);
			for (int i = 0; i < lookuprs.getRowCount(); ++i) {
				lookuprs.goToRow(i);
				String encode = lookuprs.getStringValue("Lookup Definition.Lookup Code Information.Code Key");
				String decode = lookuprs.getStringValue("Lookup Definition.Lookup Code Information.Decode");
				
				System.out.println("\t" + encode + " ==> " + decode);

			}
			
		}
		
		objIntf.close();
		provIntf.close();
		fdIntf.close();
		itdefIntf.close();
		lookupIntf.close();
		factory.close();
		
		System.exit(0);
		
	}
		
	public static void printHM(HashMap hm) {
		Iterator<Entry<String, String>> it = hm.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> e = it.next();
			String k = e.getKey();
			Object v = e.getValue();
			if (v instanceof String) {
				System.out.println(k + " ==> " + v);
			} else if (v instanceof Vector) {
				Vector v1 = (Vector)v;
				System.out.print(k + " ==> ");
				
				for (int i=0 ; i<v1.size() ; i++) {
					System.out.print(" " + v1.get(i) + " ");
				}
				System.out.println();
				
			}
		}
		
	}
}
