package iamsamples.idadmin;

import java.util.HashMap;
import java.util.Hashtable;

import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.identity.usermgmt.vo.UserManagerResult;
import oracle.iam.platform.OIMClient;

public class CreateUser {
	public static void main(String[] args) throws Exception {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put("java.naming.provider.url", "t3://localhost:8003/oim");
    	env.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
		OIMClient oimClient = new OIMClient(env);
		oimClient.login("xelsysadm", "Welcome1");
		
		String userLogin = "HIGGS"; 
		
		UserManager usrMgr = oimClient.getService(UserManager.class);
		UserManagerResult userResult = null;
		HashMap<String, Object> createAttributes = new HashMap<String, Object>();
		String userKey = null;

		createAttributes.put("User Login", userLogin);
		createAttributes.put("First Name", userLogin + "_First");
		createAttributes.put("Last Name", userLogin + "_Last");
		createAttributes.put("act_key", 1l);
		createAttributes.put(oracle.iam.identity.utils.Constants.PASSWORD, "Welcome1");
		createAttributes.put("Xellerate Type", "End-User");
		createAttributes.put("Role", "Full-Time");

		userResult = usrMgr.create(new User(null,createAttributes));
		userKey = userResult.getEntityId();
		System.out.println("Created user with key = " + userKey);
		
		oimClient.logout();
		System.exit(0);
		
	}
}
