package iamsamples.mbeans;

import java.util.HashMap;

import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;

public class PolicyConfig1 {
	public static void main(String[] args) throws Exception {
		String protocol = "t3";
		String jndi_root = "/jndi/";
		//String wlserver = "weblogic.management.mbeanservers.domainruntime";
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
		
		ObjectName name = new ObjectName("oracle.iam:type=IAMAppRuntimeMBean,name=RoleCategorySeedMBean,Application=oim,ApplicationVersion=11.1.1.3.0");

		MBeanInfo mbi = conn.getMBeanInfo(name);
		MBeanOperationInfo[] mboarr = mbi.getOperations();

		for (int i = 0; i < mboarr.length; i++) {
			MBeanOperationInfo mbo = mboarr[i];
			System.out.println(mbo.getName());
			String retType = mbo.getReturnType();
			System.out.println("\t" + retType);
			MBeanParameterInfo[] params = mbo.getSignature();
			for (int j=0 ; j<params.length ; j++) {
				MBeanParameterInfo p = params[j];
				System.out.println("\t" + p.getName() + " " + p.getType() + " " + p.getClass());
			}
		}
		
	}
}
