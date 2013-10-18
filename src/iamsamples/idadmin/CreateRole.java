package iamsamples.idadmin;

import java.util.HashMap;
import java.util.Hashtable;

import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;
import oracle.iam.identity.rolemgmt.vo.Role;
import oracle.iam.identity.rolemgmt.vo.RoleCategory;
import oracle.iam.identity.rolemgmt.vo.RoleManagerResult;
import oracle.iam.platform.OIMClient;

public class CreateRole {


	public static void main(String[] args) throws Exception {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put("java.naming.provider.url", "t3://localhost:8003/oim");
    	env.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
		OIMClient oimClient = new OIMClient(env);
		oimClient.login("xelsysadm", "Welcome1");
		
		String roleName = "helloRole"; 
		
		RoleManager roleMgr = oimClient.getService(RoleManager.class);
		RoleManagerResult roleResult = null;
		HashMap<String, Object> createAttributes = new HashMap<String, Object>();
		
		createAttributes.put(RoleManagerConstants.ROLE_NAME, roleName);
		createAttributes.put(RoleManagerConstants.ROLE_DISPLAY_NAME, roleName);
		createAttributes.put(RoleManagerConstants.ROLE_DESCRIPTION, roleName + " Description");
		Role role = new Role(createAttributes);

		roleResult = roleMgr.create(role);
		String entityId = roleResult.getEntityId();
		System.out.println("Created role with key = " + entityId);
		
		oimClient.logout();
		System.exit(0);
		
	}



}
