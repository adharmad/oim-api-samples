package oimsamples.scripts;

import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

import oimsamples.util.OIMUtils;

import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcFormDefinitionOperationsIntf;
import Thor.API.Operations.tcFormInstanceOperationsIntf;
import Thor.API.Operations.tcITResourceDefinitionOperationsIntf;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;
import Thor.API.Operations.tcLookupOperationsIntf;
import Thor.API.Operations.tcObjectOperationsIntf;
import Thor.API.Operations.tcUserOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class CreateEntitlements {
	public static void main(String[] args) throws Exception {

		int count = Integer.parseInt(args[0].trim());
		System.out.println("count = " + count);
		Random r = new Random(System.currentTimeMillis());
		
		
		Properties jndi = ConfigurationClient.getComplexSettingByPath("Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm", "xelsysadm");
		tcObjectOperationsIntf objIntf = (tcObjectOperationsIntf) factory.getUtility("Thor.API.Operations.tcObjectOperationsIntf;");
		tcITResourceDefinitionOperationsIntf itdefIntf = (tcITResourceDefinitionOperationsIntf) factory.getUtility("Thor.API.Operations.tcITResourceDefinitionOperationsIntf;");
	    tcITResourceInstanceOperationsIntf itinstIntf = (tcITResourceInstanceOperationsIntf) factory.getUtility("Thor.API.Operations.tcITResourceInstanceOperationsIntf;");
	    tcFormDefinitionOperationsIntf fdIntf = (tcFormDefinitionOperationsIntf) factory.getUtility("Thor.API.Operations.tcFormDefinitionOperationsIntf;");
	    tcFormInstanceOperationsIntf fiIntf = (tcFormInstanceOperationsIntf) factory.getUtility("Thor.API.Operations.tcFormInstanceOperationsIntf");
	    tcLookupOperationsIntf lookupIntf = (tcLookupOperationsIntf) factory.getUtility("Thor.API.Operations.tcLookupOperationsIntf");

	    System.out.println("logged in");
	    
		String objName = "XXXX";
		String pForm = "UD_XXXXP";
		String cForm = "UD_XXXXC";
		String lookupName = "Lookup.Entitlements.XXXX";
		
		String[] itResNames = {
			"XXXX1", "XXXX2", "XXXX3" 	
		};
		
		long objKey = OIMUtils.getObjectKey(objIntf, objName);
		System.out.println("objName = " + objName + " obj_key=" + objKey);
		long psdkKey = OIMUtils.getFormDefKey(fdIntf, pForm);
		System.out.println("pForm = " + pForm + " sdk_key=" + psdkKey);
		long csdkKey = OIMUtils.getFormDefKey(fdIntf, cForm);
		System.out.println("cForm = " + cForm + " sdk_key=" + csdkKey);
		
		long itResKeys[] = new long[itResNames.length];
		
		for (int i=0 ; i<itResNames.length ; i++) {
			String itresName = itResNames[i];
			long itresKey = OIMUtils.getITResKey(itinstIntf, itresName);
			itResKeys[i] = itresKey;
			System.out.println("itresName = " + itresName + " svr_key=" + itresKey);
		}
		
		// create lookup defs
		
		for (int i=0 ; i<10000 ; i++) {
			HashMap hm = new HashMap();
			long itresKey = itResKeys[i%itResKeys.length];
			String encode = itresKey + "~ent_" + i;
			String decode = itresKey + "~desc_" + i;
			
			System.out.println("Adding lookup value = " + encode);
			lookupIntf.addLookupValue(lookupName, encode, decode, "en", "US");
		}

        objIntf.close();
        itdefIntf.close();
        itinstIntf.close();
        fdIntf.close();
        fiIntf.close();
        lookupIntf.close();
        
        factory.close();
        System.exit(0);
			
	}
}
