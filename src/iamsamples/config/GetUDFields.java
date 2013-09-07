package iamapisamples.config;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Iterator;

import oracle.iam.configservice.api.ConfigManager;
import oracle.iam.configservice.api.Constants.Encryption;
import oracle.iam.configservice.api.Constants.Entity;
import oracle.iam.configservice.vo.AttributeDefinition;

import oracle.iam.platform.OIMClient;

public class GetUDFields {

    public static void main(String[] args) throws Exception {
        Hashtable env = new Hashtable();
        env.put("java.naming.provider.url", "t3://localhost:7001/oim");
        env.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");

        OIMClient oimClient = new OIMClient(env);
        oimClient.login("xelsysadm", "Welcome1");

        ConfigManager cfgMgr = oimClient.getService(ConfigManager.class);
        Map<String, AttributeDefinition> attrMap = cfgMgr.getAttributes(Entity.USER);

        Iterator<String> it = attrMap.keySet().iterator();
        while (it.hasNext()) {
            String attr = it.next();
            AttributeDefinition attrDef = attrMap.get(attr);

            System.out.println(attr + " ==> " + attrDef);
        }

        System.exit(0);
    }

}

