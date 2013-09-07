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

public class DeleteUDF {

    public static void main(String[] args) throws Exception {
        String udf = args[0].trim();

        Hashtable env = new Hashtable();
        env.put("java.naming.provider.url", "t3://localhost:7001/oim");
        env.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");

        OIMClient oimClient = new OIMClient(env);
        oimClient.login("xelsysadm", "Welcome1");

        ConfigManager cfgMgr = oimClient.getService(ConfigManager.class);
        cfgMgr.deleteAttribute(Entity.USER, udf);

        System.exit(0);
    }

}

