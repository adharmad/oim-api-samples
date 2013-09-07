package iamsamples.provisioning;


import java.util.HashMap;
import java.util.Hashtable;

import Thor.API.tcResultSet;

import oracle.iam.platform.OIMClient;
import oracle.iam.provisioning.api.ProvisioningServiceInternal;

public class TestLegacyAPI {


    public static void main(String[] args) throws Exception {
        Hashtable env = new Hashtable();
        env.put("java.naming.provider.url", "t3://localhost:7001/oim");
        //env.put("java.naming.provider.url", "t3://adc6260270.us.oracle.com:24719/oim");
        env.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");

        OIMClient oimClient = new OIMClient(env);
        oimClient.login("xelsysadm", "Welcome1");

        ProvisioningServiceInternal provInt = oimClient.getService(ProvisioningServiceInternal.class);
        
        HashMap hm = new HashMap();
        //hm.put("IT Resource Type Definition.Server Type", "Avitek Server");
        //hm.put("IT Resource.Name", "AvitekITResource");
        
        tcResultSet rs = (tcResultSet)provInt.invokeLegacyAPI(
        	"Thor.API.Operations.tcITResourceInstanceOperationsIntf", 
        	"findITResourceInstances", 
        	new Object[] {hm});
        
        //Utils.printResultSet(rs);
        
        String[] cols = {"IT Resource.Key", "IT Resource.Name", "IT Resource Type Definition.Server Type"};
        //String[] cols = {"IT Resources.Key", "IT Resources.Name", "IT Resources Type Definition.Server Type"};

        for (int i=0 ; i<rs.getRowCount() ; i++) {
        	rs.goToRow(i);
        	for (int j=0 ; j<cols.length ; j++) {
        		System.out.println(cols[j] + " ==> " + rs.getStringValue(cols[j]));
        	}
        	System.out.println("----------------------------");
        }
       
        System.exit(0);
    }



}
