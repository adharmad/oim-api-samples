package oimsamples.prov;

import java.util.Properties;

import com.thortech.xl.util.config.ConfigurationClient;

import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Exceptions.tcPropertyNotFoundException;
import Thor.API.Operations.tcFormDefinitionOperationsIntf;
import Thor.API.Operations.tcFormInstanceOperationsIntf;

public class GetEntitlementPropertyOnChildForm {
	public static void main(String[] args) throws Exception {
		Properties jndi = ConfigurationClient.getComplexSettingByPath("Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm", "xelsysadm");
		
		tcFormDefinitionOperationsIntf fdIntf = (tcFormDefinitionOperationsIntf)factory.getUtility("Thor.API.Operations.tcFormDefinitionOperationsIntf");
		tcFormInstanceOperationsIntf fiIntf = (tcFormInstanceOperationsIntf)factory.getUtility("Thor.API.Operations.tcFormInstanceOperationsIntf");

		long sdkKey = 1941;
		int version = 0;
		
		tcResultSet rs = fdIntf.getFormFields(sdkKey, version);

		for (int i=0 ; i<rs.getRowCount() ; i++) {
			
			rs.goToRow(i);
		
			long sdcKey = rs.getLongValue("Structure Utility.Additional Columns.Key");
	
			// ignore if a property of the name "Entitlement" is not found
			try {
				String entitlementProp = fdIntf.getFormFieldPropertyValue(sdcKey, "Entitlement");
				System.out.println("Found property for sdcKey = " + sdcKey + " value = " + entitlementProp);
			} catch (tcPropertyNotFoundException pnfe) {
				System.out.println("Not found property for sdcKey = " + sdcKey);
				continue;
			}
			
			
			
		}
		
		fiIntf.close();
		fdIntf.close();
		factory.close();
		
		System.exit(0);
	}
}
