package iamsamples.idadmin;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import oracle.iam.identity.rolemgmt.api.RoleCategoryManager;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;
import oracle.iam.identity.rolemgmt.vo.RoleCategory;
import oracle.iam.platform.OIMClient;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;

public class SearchRoleCategory {
	public static void main(String[] args) throws Exception {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put("java.naming.provider.url", "t3://localhost:8003/oim");
    	env.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
		OIMClient oimClient = new OIMClient(env);
		oimClient.login("xelsysadm", "Welcome1");

		RoleCategoryManager roleCategoryMgr = oimClient.getService(RoleCategoryManager.class);

		SearchCriteria criteria = new SearchCriteria(RoleManagerConstants.ROLE_CATEGORY_NAME, "role",
				SearchCriteria.Operator.BEGINS_WITH);
		Set retSet = new HashSet();
		retSet.add(RoleManagerConstants.ROLE_CATEGORY_NAME);
		retSet.add(RoleManagerConstants.ROLE_CATEGORY_DESCRIPTION);

		List<RoleCategory> categories = roleCategoryMgr.search(criteria, retSet, null);

		for (RoleCategory roleCat : categories) {

			System.out.println(roleCat.getEntityId() + " :: " + roleCat.getName() + " - " + roleCat.getDescription());
		}

		System.exit(0);
	}
}
