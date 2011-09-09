package oimsamples.prov;

import java.util.HashMap;

import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import java.util.Map.Entry;

//import oimsamples.util.OIMUtils;

import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Exceptions.tcPropertyNotFoundException;
import Thor.API.Operations.tcFormDefinitionOperationsIntf;
import Thor.API.Operations.tcITResourceDefinitionOperationsIntf;
import Thor.API.Operations.tcObjectOperationsIntf;
import Thor.API.Operations.tcProvisioningOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class ResourceObjectToITResource {
	public static void main(String[] args) throws Exception {
		Properties jndi = ConfigurationClient.getComplexSettingByPath("Discovery.CoreServer").getAllSettings();
		//Properties jndi = new Properties();
		//jndi.put("java.naming.provider.url", "jnp://rvm3168.central.sun.com:1099");
		//jndi.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm", "xelsysadm");
		
		tcObjectOperationsIntf objIntf = (tcObjectOperationsIntf)factory.getUtility("Thor.API.Operations.tcObjectOperationsIntf");
		tcProvisioningOperationsIntf provIntf = (tcProvisioningOperationsIntf)factory.getUtility("Thor.API.Operations.tcProvisioningOperationsIntf");
		tcFormDefinitionOperationsIntf fdIntf = (tcFormDefinitionOperationsIntf)factory.getUtility("Thor.API.Operations.tcFormDefinitionOperationsIntf");
		tcITResourceDefinitionOperationsIntf itdefIntf = (tcITResourceDefinitionOperationsIntf)factory.getUtility("Thor.API.Operations.tcITResourceDefinitionOperationsIntf");
		
		HashMap<String, String> objMap = new HashMap<String, String>();
		objMap.put("Objects.Order For", "U");
		
		// get object information
		HashMap<String, String> objs = new HashMap<String, String>();
		tcResultSet objrs = objIntf.findObjects(objMap);
		for (int i=0 ; i<objrs.getRowCount() ; i++) {
			objrs.goToRow(i);
			String objKey = objrs.getStringValue("Objects.Key");
			String objName = objrs.getStringValue("Objects.Name");
			objs.put(objKey, objName);
		}
		
		//printHM(objs);
		
		// get process information
		HashMap<String, Vector<String>> objSdkMap = new HashMap<String, Vector<String>>();
		
		Iterator<Entry<String, String>> it = objs.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> e = it.next();
			tcResultSet procrs = objIntf.getProcessesForObject(Long.parseLong(e.getKey()));
			//OIMUtils.printResultSet(procrs);
			
			Vector<String> v = new Vector<String>();
			
			for (int i=0 ; i<procrs.getRowCount() ; i++) {
				procrs.goToRow(i);
				String procType = procrs.getStringValue("Process Definition.Type");
				if ("Provisioning".equals(procType)) {
					String sdkKey = procrs.getStringValue("Structure Utility.Key");
					if (!"".equals(sdkKey.trim())) {
						v.add(sdkKey);
					}
				}
			}
			
			objSdkMap.put(e.getValue(), v);
		}
		
		//printHM(objSdkMap);
		
		
		// get form fields of type IT resource
		HashMap<String, Vector<String>> objToSdcMap = new HashMap<String, Vector<String>>();
		Iterator<Entry<String, Vector<String>>> it1 = objSdkMap.entrySet().iterator();

		while (it1.hasNext()) {
			Entry<String, Vector<String>> e = it1.next();
			String objKey = e.getKey();
			Vector<String> v = e.getValue();
			for (int i=0 ; i<v.size() ; i++) {
				Vector<String> itres = new Vector<String>();
				
				String sdkKey = v.get(i);
				tcResultSet sdkrs = fdIntf.getFormVersions(Long.parseLong(sdkKey));
				//OIMUtils.printResultSet(sdkrs);
				String activeVersion = sdkrs.getStringValue("Structure Utility.Active Version");
				
				tcResultSet sdcrs = fdIntf.getFormFields(Long.parseLong(sdkKey), Integer.parseInt(activeVersion));
				//OIMUtils.printResultSet(sdcrs);
				for (int j=0 ; j<sdcrs.getRowCount() ; j++) {
					sdcrs.goToRow(j);
					
					if ("ITResourceLookupField".equals(sdcrs.getStringValue("Structure Utility.Additional Columns.Field Type"))) {
						itres.add(sdcrs.getStringValue("Structure Utility.Additional Columns.Key"));
					}
				}
				
				objToSdcMap.put(objKey, itres);
			}
		}
		
		//printHM(objToSdcMap);

		// get the properties for the it resource fields.
		HashMap<String, Vector<String>> objToITResMap = new HashMap<String, Vector<String>>();
		Iterator<Entry<String, Vector<String>>> it2 = objToSdcMap.entrySet().iterator();

		while (it2.hasNext()) {
			Entry<String, Vector<String>> e = it2.next();
			Vector<String> sdcFields = e.getValue();
			
			Vector<String> itResources = new Vector<String>();
			
			for (int i=0 ; i<sdcFields.size() ; i++) {
				long sdcKey = Long.parseLong(sdcFields.get(i));
				
				try {
					String itResPropertyValue = fdIntf.getFormFieldPropertyValue(sdcKey, "ITResource");
				} catch (tcPropertyNotFoundException pnfe) {
					continue;
				}
				
				String itResTypeDefKey = fdIntf.getFormFieldPropertyValue(sdcKey, "Type");
				
				HashMap itDefMap = new HashMap();
				itDefMap.put("IT Resources Type Definition.Key", itResTypeDefKey);
				tcResultSet itdefrs = itdefIntf.getITResourceDefinition(itDefMap);
				itdefrs.goToRow(0);
				String itResDefName = itdefrs.getStringValue("IT Resource Type Definition.Server Type");
				//OIMUtils.printResultSet(itdefrs);
				itResources.add(itResDefName);
			}
			
			objToITResMap.put(e.getKey(), itResources);
		}

		printHM(objToITResMap);
		
		objIntf.close();
		provIntf.close();
		fdIntf.close();
		itdefIntf.close();
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