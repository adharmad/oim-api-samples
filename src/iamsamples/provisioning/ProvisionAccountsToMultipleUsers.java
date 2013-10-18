package iamsamples.provisioning;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.OIMClient;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.provisioning.api.ApplicationInstanceService;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.AccountData;
import oracle.iam.provisioning.vo.ApplicationInstance;

public class ProvisionAccountsToMultipleUsers {

	public static void main(String[] args) throws Exception {

        Hashtable env = new Hashtable();
        env.put("java.naming.provider.url", "t3://localhost:8003/oim");
        env.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");

        OIMClient oimClient = new OIMClient(env);
        oimClient.login("xelsysadm", "Welcome1");

        ApplicationInstanceService aiSvc = oimClient.getService(ApplicationInstanceService.class);
        ProvisioningService provSvc = oimClient.getService(ProvisioningService.class);
        UserManager usrMgr = oimClient.getService(UserManager.class);
        
        String appInstanceName = "prov2";
        
        // Find the user
        SearchCriteria criteria = new SearchCriteria("User Login",
                "KARPOV", SearchCriteria.Operator.BEGINS_WITH);
        Set retSet = new HashSet();
        retSet.add("usr_key");
        retSet.add("User Login");
        retSet.add("First Name");
        retSet.add("Last Name");

        List<User> users = usrMgr.search(criteria, retSet, null);
        
        for (User u : users) {
        	ApplicationInstance ai = aiSvc.findApplicationInstanceByName(appInstanceName);

        	HashMap<String, Object> parentData = new HashMap<String, Object>();
        	AccountData accountData = new AccountData(ai.getAccountForm().getFormKey() + "", "", parentData);
        	Account account = new Account(ai, accountData);
        	
        	System.out.println("Provisioning app instance " + appInstanceName + " to user " + u.getEntityId());
        	provSvc.provision(u.getEntityId(), account);
        }

        oimClient.logout();
        System.exit(0);
    		
	}
}
