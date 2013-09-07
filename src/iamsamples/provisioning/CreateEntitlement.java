package iamsamples.provisioning;

import java.util.Hashtable;
import java.util.List;

import oracle.iam.platform.OIMClient;
import oracle.iam.provisioning.api.EntitlementService;
import oracle.iam.provisioning.vo.Entitlement;


public class CreateEntitlement {
	public static void main(String[] args) throws Exception {

        Hashtable env = new Hashtable();
        env.put("java.naming.provider.url", "t3://localhost:8003/oim");
        env.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");

        OIMClient oimClient = new OIMClient(env);
        oimClient.login("xelsysadm", "Welcome1");

        EntitlementService entSvc = oimClient.getService(EntitlementService.class);
        
        Entitlement ent = new Entitlement();
        //ent.setEntitlementKey(8);
        ent.setObjectKey(4);
        ent.setFormKey(9);
        ent.setFormFieldKey(29);
        ent.setItResourceKey(4);
        ent.setEntitlementCode("4~strawberry");
        ent.setEntitlementValue("BASIC1~strawberry");
        ent.setDisplayName("BASIC1~strawberry");
        ent.setDescription("BASIC1~strawberry");
        ent.setLookupValueKey(2479);
        ent.setValid(true);
        
        entSvc.addEntitlement(ent);
        
        oimClient.logout();

        System.exit(0);
    		
	}
}
