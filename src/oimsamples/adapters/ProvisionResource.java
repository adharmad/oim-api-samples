package oimsamples.adapters;

import oimsamples.util.OIMUtils;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcObjectOperationsIntf;
import Thor.API.Operations.tcUserOperationsIntf;

import com.thortech.xl.dataaccess.tcDataProvider;

public class ProvisionResource {
	public static String provision(tcDataProvider dp, String usrLogin, String resourceName) {
		try {
		
			tcUserOperationsIntf usrIntf = (tcUserOperationsIntf)tcUtilityFactory.getUtility(dp, "Thor.API.Operations.tcUserOperationsIntf");
			tcObjectOperationsIntf objIntf = (tcObjectOperationsIntf)tcUtilityFactory.getUtility(dp, "Thor.API.Operations.tcObjectOperationsIntf");

			long usrKey = OIMUtils.getUserKey(usrIntf, usrLogin);
			long objKey = OIMUtils.getObjectKey(objIntf, resourceName);
		
			long obiKey = usrIntf.provisionObject(usrKey, objKey);
		
			System.out.println("Provisioned user " + usrLogin + " with resource object " + resourceName);
			return "SUCCESS";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "FAILURE";
		}
	}
}
