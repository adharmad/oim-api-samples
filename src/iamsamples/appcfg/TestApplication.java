package iamsamples.appcfg;

import java.util.HashMap;
import java.util.Hashtable;

import oracle.iam.platform.OIMClient;
import oracle.iam.provisioning.api.ApplicationService;

public class TestApplication {



	public static void main(String[] args) throws Exception {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put("java.naming.provider.url", "t3://localhost:7001/oim");
    	env.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
		OIMClient oimClient = new OIMClient(env);
		oimClient.login("xelsysadm", "Welcome1");
		
		String entityType = "testentity2"; 
		
		ApplicationService appSvc = oimClient.getService(ApplicationService.class);

		for (int i=0 ; i<100 ; i++) {
			appSvc.testConnection(entityType);
		}

		oimClient.logout();
		System.exit(0);
		
	}




}
