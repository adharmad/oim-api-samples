package oimsamples.adapters;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;

import Thor.API.Operations.tcITResourceInstanceOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class ITResourcePrepop {
	
	private static Map<String, String> CHILD_PARENT_ITRESNAME_MAP = new HashMap<String, String>();
	
	static {
		CHILD_PARENT_ITRESNAME_MAP.put("cit1", "pit1");
		CHILD_PARENT_ITRESNAME_MAP.put("cit2", "pit2");
		CHILD_PARENT_ITRESNAME_MAP.put("cit3", "pit3");
	}
	
	public Long getITResourceKey() {
		return new Long(4);
	}
	
	//public Long getITResourceKey(Long svrKey) throws Exception {
	public Long getITResourceKey(long svrKey) throws Exception {
		//Long svrKey = (Long)osvrKey;
		Properties jndi = ConfigurationClient.getComplexSettingByPath(
		"Discovery.CoreServer").getAllSettings();

		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm", "Welcome1");
		tcITResourceInstanceOperationsIntf itInstIntf = (tcITResourceInstanceOperationsIntf)factory.getUtility(
			"Thor.API.Operations.tcITResourceInstanceOperationsIntf");
		System.out.println("signature login complete");
		
		String itResName = getITResourceNameFromKey(itInstIntf, svrKey + "");
		
		String parentITResName = CHILD_PARENT_ITRESNAME_MAP.get(itResName);
		
		String parentITResKey = getITResourceKeyFromName(itInstIntf, parentITResName);
		Long retVal = Long.parseLong(parentITResKey);
		
		return retVal;
	}	
	
	private String getITResourceNameFromKey(tcITResourceInstanceOperationsIntf itInstIntf, String itresKey) throws Exception {

		HashMap map = new HashMap();
		map.put("IT Resources.Key", itresKey);

		tcResultSet rs = itInstIntf.findITResourceInstances(map);
		rs.goToRow(0);
		
		String itResName = rs.getStringValue("IT Resources.Name");
		System.out.println("IT resource name = " + itResName);

		return itResName;
	}

	private String getITResourceKeyFromName(tcITResourceInstanceOperationsIntf itInstIntf, String itresName) throws Exception {

		HashMap map = new HashMap();
		map.put("IT Resources.Name", itresName);

		tcResultSet rs = itInstIntf.findITResourceInstances(map);
		rs.goToRow(0);
		
		String itResKey = rs.getStringValue("IT Resources.Key");
		System.out.println("Returning IT resource key = " + itResKey);

		return itResKey;
	}
	
	
}
