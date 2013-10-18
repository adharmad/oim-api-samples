package iamsamples.idadmin;

import java.util.HashMap;
import java.util.Hashtable;

import oracle.iam.identity.rolemgmt.api.RoleCategoryManager;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;
import oracle.iam.identity.rolemgmt.vo.RoleCategory;
import oracle.iam.identity.rolemgmt.vo.RoleManagerResult;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.identity.usermgmt.vo.UserManagerResult;
import oracle.iam.platform.OIMClient;

public class CreateRoleCategory {

	public static void main(String[] args) throws Exception {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put("java.naming.provider.url", "t3://localhost:8003/oim");
    	env.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
		OIMClient oimClient = new OIMClient(env);
		oimClient.login("xelsysadm", "Welcome1");
		
		String categoryName = "roleCat6"; 
		
		RoleCategoryManager roleCategoryMgr = oimClient.getService(RoleCategoryManager.class);
		RoleManagerResult roleResult = null;
		HashMap<String, Object> createAttributes = new HashMap<String, Object>();
		
		createAttributes.put(RoleManagerConstants.ROLE_CATEGORY_NAME, categoryName);
		createAttributes.put(RoleManagerConstants.ROLE_CATEGORY_DESCRIPTION, categoryName + " Description");
		RoleCategory roleCategory = new RoleCategory(createAttributes);

		roleResult = roleCategoryMgr.create(roleCategory);
		String entityId = roleResult.getEntityId();
		System.out.println("Created role category with key = " + entityId);
		
		oimClient.logout();
		System.exit(0);
		
	}

}
