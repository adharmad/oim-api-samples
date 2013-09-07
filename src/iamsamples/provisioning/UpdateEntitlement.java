package iamsamples.provisioning;

import java.util.HashMap;
import java.util.Hashtable;

import oracle.iam.platform.OIMClient;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.provisioning.api.EntitlementService;
import oracle.iam.provisioning.vo.Entitlement;

public class UpdateEntitlement {

	public static void main(String[] args) throws Exception {

        Hashtable env = new Hashtable();
        env.put("java.naming.provider.url", "t3://localhost:8003/oim");
        env.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");

        OIMClient oimClient = new OIMClient(env);
        oimClient.login("xelsysadm", "Welcome1");

        EntitlementService entSvc = oimClient.getService(EntitlementService.class);
        
        SearchCriteria sc = new SearchCriteria(Entitlement.ENTITLEMENT_NAME,
                "4~strawberry", SearchCriteria.Operator.EQUAL);

        Entitlement ent = entSvc.findEntitlements(sc, new HashMap<String, Object>()).get(0);
        
        ent.setEntitlementValue("new display value");
        ent.setDisplayName("new display value again");
        
        entSvc.updateEntitlement(ent);
        
        oimClient.logout();
        System.exit(0);
    		
	}

}
