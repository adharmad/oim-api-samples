package iamsamples.mbeans;

import java.util.HashMap;
import java.util.Set;

import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;

public class SearchMBeans {

	public static void main(String[] args) throws Exception {
		String protocol = "t3";
		String jndi_root = "/jndi/";
		String wlserver = "weblogic.management.mbeanservers.runtime";
		String host = "";
		int port = 14000 ;
		String adminUsername = "xelsysadm";
		String adminPassword = "Welcome1";
		JMXServiceURL url = new JMXServiceURL(protocol, host, port, jndi_root
				+ wlserver);
		HashMap<String, Object> env = new HashMap<String, Object>();
		env.put(Context.SECURITY_PRINCIPAL, adminUsername);
		env.put(Context.SECURITY_CREDENTIALS, adminPassword);
		env.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES,
				"weblogic.management.remote");
		
		JMXConnector connector = JMXConnectorFactory.connect(url, env);
		MBeanServerConnection conn = connector.getMBeanServerConnection();

		// *:Application=oim,name=OAACGConfig,*
		String[] searchFilter = new String[] {
				"*:Application=oim,name=AppIDSeedingMBean,*",
				
		};
		
		for (int i=0 ; i<searchFilter.length ; i++) {
			String sf = searchFilter[i];
			Set<ObjectInstance> oiSet = conn.queryMBeans(new ObjectName(sf), null);
		
			for (ObjectInstance oi : oiSet) {
				System.out.println(oi.getObjectName());
			}
		}
	}

}
