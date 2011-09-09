package oimsamples.form;

import java.util.Properties;

import oimsamples.util.OIMUtils;

import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcFormInstanceOperationsIntf;
import Thor.API.Operations.tcProvisioningOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class GetFormData {


	public static void main(String[] args) throws Exception {

		Properties jndi = ConfigurationClient.getComplexSettingByPath("Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm", "xelsysadm");
		
		tcFormInstanceOperationsIntf fiIntf = (tcFormInstanceOperationsIntf)factory.getUtility("Thor.API.Operations.tcFormInstanceOperationsIntf");
		tcResultSet rs = fiIntf.getProcessFormData(10101);
		OIMUtils.printResultSet(rs);
		factory.close();
		System.exit(0);
	}


}
