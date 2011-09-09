package oimsamples.prov;

import java.util.HashMap;
import java.util.Properties;

import oimsamples.util.OIMUtils;

import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;

import Thor.API.Operations.tcFormDefinitionOperationsIntf;
import Thor.API.Operations.tcLookupOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;
import com.thortech.xl.vo.ProvisioningData;

public class GetDepLookupQueryValues {
	public static void main(String[] args) throws Exception {

		Properties jndi = ConfigurationClient.getComplexSettingByPath("Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm", "xelsysadm");
		
		tcFormDefinitionOperationsIntf fdIntf = (tcFormDefinitionOperationsIntf)factory.getUtility("Thor.API.Operations.tcFormDefinitionOperationsIntf");
		tcLookupOperationsIntf lookupIntf = (tcLookupOperationsIntf)factory.getUtility("Thor.API.Operations.tcLookupOperationsIntf");
		
		long sdcKey = 3233;
		//select lkv_encoded,lkv_decoded from lkv lkv,lku lku where lkv.lku_key=lku.lku_key and lku_type_string_key='Lookup.ASD' and lkv_encoded like '$Form data.UD_ASDCHILD_COL1$%'
		
		
		HashMap hm = new HashMap();
		hm.put("UD_ASDCHILD_COL1", "");
		
		ProvisioningData pd = new ProvisioningData();
		
		pd.setFormData(hm);
		tcResultSet rs = lookupIntf.getLookupValuesForColumn("UD_ASDCHILD_COL2", new HashMap(), pd);	
		
		OIMUtils.printResultSet(rs);
		
		
		fdIntf.close();
		lookupIntf.close();
		factory.close();
		
		System.exit(0);
		
			
	}
}
