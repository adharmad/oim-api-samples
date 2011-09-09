package oimsamples.prov;

import java.util.Properties;

import oimsamples.util.OIMUtils;

import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcObjectOperationsIntf;


import com.thortech.xl.util.config.ConfigurationClient;

public class GetProcessesForObject {

	public static void main(String[] args) throws Exception {

		String objName = "XXXX";
		
		Properties jndi = ConfigurationClient.getComplexSettingByPath(
				"Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm",
				"xelsysadm");

		tcObjectOperationsIntf objIntf = (tcObjectOperationsIntf) factory
				.getUtility("Thor.API.Operations.tcObjectOperationsIntf");

		long objKey = OIMUtils.getObjectKey(objIntf, objName);

		tcResultSet rs = objIntf.getProcessesForObject(objKey);
		
		OIMUtils.printResultSet(rs);
		
		
		factory.close();
		System.exit(0);
	}
}
