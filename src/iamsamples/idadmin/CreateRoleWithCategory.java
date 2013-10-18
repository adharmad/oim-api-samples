package iamsamples.idadmin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import oracle.iam.identity.rolemgmt.api.RoleCategoryManager;
import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;
import oracle.iam.identity.rolemgmt.vo.Role;
import oracle.iam.identity.rolemgmt.vo.RoleCategory;
import oracle.iam.identity.rolemgmt.vo.RoleManagerResult;
import oracle.iam.platform.OIMClient;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;

public class CreateRoleWithCategory {

	public static void main(String[] args) throws Exception {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put("java.naming.provider.url", "t3://localhost:8003/oim");
    	env.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
		OIMClient oimClient = new OIMClient(env);
		oimClient.login("xelsysadm", "Welcome1");
		
		String roleName = "role3"; 
		String categoryName = "roleCat3";
		
		RoleManager roleMgr = oimClient.getService(RoleManager.class);
		RoleCategoryManager roleCategoryMgr = oimClient.getService(RoleCategoryManager.class);
		
		// find role category information
		SearchCriteria criteria = new SearchCriteria(RoleManagerConstants.ROLE_CATEGORY_NAME, categoryName,
				SearchCriteria.Operator.EQUAL);
		Set retSet = new HashSet();
		retSet.add(RoleManagerConstants.ROLE_CATEGORY_NAME);
		retSet.add(RoleManagerConstants.ROLE_CATEGORY_DESCRIPTION);

		List<RoleCategory> categories = roleCategoryMgr.search(criteria, retSet, null);
		RoleCategory roleCat = categories.get(0);
		
		RoleManagerResult roleResult = null;
		HashMap<String, Object> createAttributes = new HashMap<String, Object>();
		
		createAttributes.put(RoleManagerConstants.ROLE_NAME, roleName);
		createAttributes.put(RoleManagerConstants.ROLE_DISPLAY_NAME, roleName);
		createAttributes.put(RoleManagerConstants.ROLE_DESCRIPTION, roleName + " Description");
		createAttributes.put(RoleManagerConstants.ROLE_CATEGORY_KEY, Long.parseLong(roleCat.getEntityId()));
		
		Role role = new Role(createAttributes);

		roleResult = roleMgr.create(role);
		String entityId = roleResult.getEntityId();
		System.out.println("Created role with key = " + entityId);
		
		oimClient.logout();
		System.exit(0);
		
	}


}
