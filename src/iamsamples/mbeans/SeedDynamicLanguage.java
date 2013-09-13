package iamsamples.mbeans;

import java.util.HashMap;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;

public class SeedDynamicLanguage {

	public static void main(String[] args) throws Exception {
		String protocol = "t3"; 
		String jndi_root = "/jndi/";
		String wlserver = "weblogic.management.mbeanservers.runtime";
		String host = "";
		int port = 7001;
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
		
		System.out.println("got mbean server connection");
		
		ObjectName name = new ObjectName(
				"oracle.iam:type=IAMAppRuntimeMBean,name=MLSLanguageSeedingMXBean,Application=oim");
		
		String[] sig = new String[] { 
				String.class.getName()
		};

		
		String[] params = new String[] { 
				"abcd"
		};
			
		System.out.println("before invoking register ");
		Object ret = conn.invoke(name, "seedDynamicMLSLanguage", params, sig);
		System.out.println(ret.getClass().getName());
		
		System.out.println("ret = " + ret.toString());
	}


}
