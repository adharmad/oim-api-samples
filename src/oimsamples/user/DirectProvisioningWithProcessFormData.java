package oimsamples.user;

import java.util.HashMap;
import java.util.Properties;

import oimsamples.util.OIMUtils;



import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcFormInstanceOperationsIntf;
import Thor.API.Operations.tcObjectOperationsIntf;
import Thor.API.Operations.tcUserOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class DirectProvisioningWithProcessFormData {
	public static void main(String[] args) throws Exception {
		String userLogin = "johndoe2";
		String resourceName = "TestResource2";

		Properties jndi = ConfigurationClient.getComplexSettingByPath(
				"Discovery.CoreServer").getAllSettings();
		System.out.println(jndi);
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm",
				"xelsysadm");
		tcObjectOperationsIntf objIntf = (tcObjectOperationsIntf) factory
				.getUtility("Thor.API.Operations.tcObjectOperationsIntf");
		tcUserOperationsIntf usrIntf = (tcUserOperationsIntf) factory
				.getUtility("Thor.API.Operations.tcUserOperationsIntf");
		tcFormInstanceOperationsIntf fiIntf = (tcFormInstanceOperationsIntf) factory
				.getUtility("Thor.API.Operations.tcFormInstanceOperationsIntf");

		long usrKey = OIMUtils.getUserKey(usrIntf, userLogin);
		long objKey = OIMUtils.getObjectKey(objIntf, resourceName);
		
		long oiuKey = usrIntf.provisionObject(usrKey, objKey);
		
		System.out.println("oiu key = " + oiuKey);

		long orcKey = OIMUtils.getProcessInstanceKey(usrIntf, usrKey, oiuKey);
		
		HashMap procData = new HashMap();
		procData.put("UD_R2PFORM_PFIELD1", "process parent field 1 data");
		procData.put("UD_R2PFORM_PFIELD2", "process parent field 2 data");
		
		fiIntf.setProcessFormData(orcKey, procData);
		
		System.out.println("provisioning complete");
		
		factory.close();
		System.exit(0);
	}
	
	
	
	
	
	



}
