package iamsamples.idadmin;

import java.util.HashMap;
import java.util.Hashtable;

import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.identity.usermgmt.vo.UserManagerResult;
import oracle.iam.platform.OIMClient;

public class OIMServerThread implements Runnable {
	private String userLogin;
    static Hashtable env = new Hashtable();
    
    static {
    	env.put("java.naming.provider.url", "t3://localhost:8003/oim");
    	env.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
    }
	
	private OIMClient oimClient;
	
	public OIMServerThread(String login) {
		try{ 
			userLogin = login;
			l = new OIMClient(env);
			oimClient.login("xelsysadm", "Welcome1");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
			
	}

	public void run() {
		try {
			UserManager usrMgr = oimClient.getService(UserManager.class);
			UserManagerResult userResult = null;
			HashMap<String, Object> createAttributes = new HashMap<String, Object>();
			String userKey = null;

			createAttributes.put("User Login", userLogin);
			createAttributes.put("First Name", userLogin + "_FN");
			createAttributes.put("Last Name", "Nepal");
			createAttributes.put("act_key", 1l);
			createAttributes.put(oracle.iam.identity.utils.Constants.PASSWORD, "Welcome1");
			createAttributes.put("Xellerate Type", "End-User");
			createAttributes.put("Role", "Full-Time");

			userResult = usrMgr.create(new User(null,createAttributes));
			userKey = userResult.getEntityId();
			System.out.println("Thread " + Thread.currentThread().getName() + " creataed user with key = " + userKey);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			oimClient.logout(); 
		}
	}
	

}