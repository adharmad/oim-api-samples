package oimsamples.prov;

import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

import oimsamples.util.OIMUtils;


import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Exceptions.tcPropertyNotFoundException;
import Thor.API.Operations.tcFormDefinitionOperationsIntf;
import Thor.API.Operations.tcObjectOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class GetLookupQueryValues {
	public static void main(String[] args) throws Exception {
		Properties jndi = ConfigurationClient.getComplexSettingByPath("Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm", "xelsysadm");
		
		tcObjectOperationsIntf objIntf = (tcObjectOperationsIntf)factory.getUtility("Thor.API.Operations.tcObjectOperationsIntf");
		tcFormDefinitionOperationsIntf fdIntf = (tcFormDefinitionOperationsIntf)factory.getUtility("Thor.API.Operations.tcFormDefinitionOperationsIntf");

		String objName = "asd";
		
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
		Vector sdcFields = new Vector();

		for (int i=0 ; i<sdkVector.size() ; i++) {
			String sdkKey = sdkVector.get(i);
			tcResultSet sdkrs = fdIntf.getFormVersions(Long.parseLong(sdkKey));
			String activeVersion = sdkrs.getStringValue("Structure Utility.Active Version");
				
			tcResultSet sdcrs = fdIntf.getFormFields(Long.parseLong(sdkKey), Integer.parseInt(activeVersion));
			for (int j=0 ; j<sdcrs.getRowCount() ; j++) {
				sdcrs.goToRow(j);
					
				if ("LookupField".equals(sdcrs.getStringValue("Structure Utility.Additional Columns.Field Type"))) {
					sdcFields.add(sdcrs.getStringValue("Structure Utility.Additional Columns.Key"));
				}
			}
				
		}

		sdcFields.add("3235");
		
		for (int i=0 ; i<sdcFields.size() ; i++) {
			long sdcKey = Long.parseLong((String)sdcFields.get(i));
			
			// ignore if a property of the name "Lookup Query" is not found
			try {
				String lookupQueryProp = fdIntf.getFormFieldPropertyValue(sdcKey, "Lookup Query");
			} catch (tcPropertyNotFoundException pnfe) {
				continue;
			}
			
			
			tcResultSet rs = fdIntf.getFormFieldLookupValues(sdcKey);
			OIMUtils.printResultSet(rs);
		}
		
		
		fdIntf.close();
		objIntf.close();
		factory.close();
		
		System.exit(0);
		
	}
}
