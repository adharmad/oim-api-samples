package oimsamples.adapters;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

import com.thortech.xl.crypto.tcCryptoUtil;
import com.thortech.xl.crypto.tcSignatureMessage;
import com.thortech.xl.util.config.ConfigurationClient;

import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcFormInstanceOperationsIntf;
import Thor.API.Operations.tcLookupOperationsIntf;

public class PropagateIdentityDataToProcessFormCustom {
	public void transformAndCopyData(String orcKey, HashMap usrMap) throws Exception {

		Properties jndi = ConfigurationClient.getComplexSettingByPath(
			"Discovery.CoreServer").getAllSettings();
		tcSignatureMessage signedMsg = tcCryptoUtil.sign("xelsysadm", "PrivateKey");
		tcUtilityFactory factory = new tcUtilityFactory(jndi, signedMsg);
		
		tcLookupOperationsIntf lookupIntf = (tcLookupOperationsIntf)factory.getUtility("Thor.API.Operations.tcLookupOperationsIntf");
		tcFormInstanceOperationsIntf fiIntf = (tcFormInstanceOperationsIntf)factory.getUtility("Thor.API.Operations.tcFormInstanceOperationsIntf");
		
		HashMap usrUDMap = new HashMap();
		
		// print the orc key and Map
		System.out.println("orc key = " + orcKey);
		printMap(usrMap);
		
		String lookupName = "Lookup.USR.UD_BULK.Map";
		
		tcResultSet rs = lookupIntf.getLookupValues(lookupName);
		
		for (int i=0 ; i<rs.getRowCount() ; i++) {
			rs.goToRow(i);
			
			usrUDMap.put(rs.getStringValue("Lookup Definition.Lookup Code Information.Code Key"), 
					rs.getStringValue("Lookup Definition.Lookup Code Information.Decode"));
		}

		printMap(usrUDMap);
		
		// here you can transform the data in any way you want based on your custom logic
		HashMap processData = new HashMap();
		
		Iterator it = usrMap.entrySet().iterator();

		while (it.hasNext()) {
			Entry e = (Entry) it.next();
			processData.put(usrUDMap.get(e.getKey()), e.getValue());
		}
		
		printMap(processData);
		
		System.out.println("Setting process form data");
		fiIntf.setProcessFormData(Long.parseLong(orcKey), processData);
	}
	
	private static void printMap(HashMap hm) {
		Iterator it = hm.entrySet().iterator();

		System.out.println("--------------------------------");
		while (it.hasNext()) {
			Entry e = (Entry) it.next();
			System.out.println(e.getKey() + " ==> " + e.getValue());
		}
		
		System.out.println("--------------------------------");
	}
}

