package iamsamples.mbeans;

import java.util.HashMap;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;

public class UnregisterCBPolicy {

	public static void main(String[] args) throws Exception {
		String protocol = "t3";
		String jndi_root = "/jndi/";
		String wlserver = "weblogic.management.mbeanservers.runtime";
		String host = "";
		int port = 7001;
		String adminUsername = "xelsysadm";
		String adminPassword = "xelsysadm";
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
				"oracle.iam:type=IAMAppRuntimeMBean,name=NotificationPolicyConfigMBean,Application=oim");

		String[] sig = new String[] { String.class.getName() };

		String[] params = new String[] { 
				"<callbackConfiguration xmlns=\"http://www.oracle.com/schema/oim/callback_config\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.oracle.com/schema/oim/callback_config\" > "
				+ "<policy>"
				+ "    <policyName>hahaha0</policyName>"
				+ "    <entityType>User</entityType>"
				+ "    <operation>Create</operation>"
				+ "    <description>Policy to create  a user in the store</description>"
				+ "    <provisioningSteps>"
				+ "        <postProcessing>"
				+ "            <asyncSteps>"
				+ "               <!--stepName>http://stadd46.us.oracle.com:7001/PostProcessingPluginRequestPortImpl/PostProcessingPluginRequestPortImpl</stepName-->"
				+ "            </asyncSteps>"
				+ "        </postProcessing>"
				+ "    </provisioningSteps>"
				+ "</policy>"
				+ "</callbackConfiguration>" };

		System.out.println("before invoking unregister ");
		Object ret = conn.invoke(name, "unregister", params, sig);

		System.out.println("ret = " + ret.toString());
	}

}
