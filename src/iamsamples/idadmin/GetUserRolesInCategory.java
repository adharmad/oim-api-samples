package iamsamples.idadmin;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import oracle.iam.identity.rolemgmt.api.RoleCategoryManager;
import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;
import oracle.iam.identity.rolemgmt.vo.Role;
import oracle.iam.identity.rolemgmt.vo.RoleCategory;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.OIMClient;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;

public class GetUserRolesInCategory {

	public static void main(String[] args) throws Exception {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put("java.naming.provider.url", "t3://localhost:8003/oim");
    	env.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
		OIMClient oimClient = new OIMClient(env);
		oimClient.login("xelsysadm", "Welcome1");
		
		RoleManager roleMgr = oimClient.getService(RoleManager.class);
        UserManager usrMgr = oimClient.getService(UserManager.class);
        RoleCategoryManager roleCategoryMgr = oimClient.getService(RoleCategoryManager.class);
        
        String usrLogin = "HIGGS";
        String roleCategoryPattern = "roleCat";
        Set<Role> matchingRoles = new HashSet<Role>();

        SearchCriteria criteria = new SearchCriteria("User Login",
            usrLogin, SearchCriteria.Operator.EQUAL);
        Set retSet = new HashSet();
        retSet.add("usr_key");
        retSet.add("User Login");
        retSet.add("First Name");
        retSet.add("Last Name");

        List<User> users = usrMgr.search(criteria, retSet, null);
        User user = users.get(0);
		
		List<Role> userRoles = roleMgr.getUserMemberships(user.getEntityId(), false);
		
		for (Role role : userRoles) {
			Long catKey = (Long)role.getAttributes().get(RoleManagerConstants.ROLE_CATEGORY_KEY);
			RoleCategory roleCat = roleCategoryMgr.getDetails(catKey.toString(), new HashSet<String>());
			if (roleCat.getName().startsWith(roleCategoryPattern)) {
				matchingRoles.add(role);
			}
		}
		
		Iterator<Role> it = matchingRoles.iterator();
		while (it.hasNext()) {
			Role r = it.next();
			System.out.println(r.getName());
		}
		
		oimClient.logout();
		System.exit(0);
		
	}
}
