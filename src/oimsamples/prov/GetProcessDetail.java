package oimsamples.prov;


import java.util.Properties;

import oimsamples.util.OIMUtils;


import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcProvisioningOperationsIntf;


import com.thortech.xl.util.config.ConfigurationClient;

public class GetProcessDetail {

	public static void main(String[] args) throws Exception {

		Properties jndi = ConfigurationClient.getComplexSettingByPath("Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm", "xelsysadm");
		
		tcProvisioningOperationsIntf provIntf = (tcProvisioningOperationsIntf)factory.getUtility("Thor.API.Operations.tcProvisioningOperationsIntf");
		tcResultSet rs = provIntf.getProcessDetail(6748);
		OIMUtils.printResultSet(rs);
		factory.close();
		System.exit(0);
	}

}
