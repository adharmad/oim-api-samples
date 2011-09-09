package iamsamples.mbeans;

import java.util.HashMap;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;

public class SeedFusionAPPID {

	public static void main(String[] args) throws Exception {
		String protocol = "t3"; 
		String jndi_root = "/jndi/";
		String wlserver = "weblogic.management.mbeanservers.runtime";
		String host = "dadvmn0695.us.oracle.com";
		int port = 7003;
		String adminUsername = "weblogic";
		String adminPassword = "weblogic1";
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
		
		ObjectName name = new ObjectName("*:name=APPIDSeedingMBean,type=IAMAppRuntimeMBean,Application=oim,*");
		 
		String[] sig = new String[] {
			//String.class.getName(), String.class.getName()
				String.class.getName()
		};

		
		String[] params = new String[] {
				"HELLO_APPID_3"//, "Welcome1"
		};
			
		System.out.println("before invoking register ");
		Object ret = conn.invoke(name, "seedFusionAPPID", params, sig);
		System.out.println(ret.getClass().getName());
		
		System.out.println("ret = " + ret.toString());
	}

}
