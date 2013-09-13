package iamsamples.jps;

import java.util.HashMap;

import javax.management.BadAttributeValueExpException;
import javax.management.BadBinaryOpValueExpException;
import javax.management.BadStringOperationException;
import javax.management.InvalidApplicationException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.openmbean.CompositeData;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;

import oracle.security.jps.mas.mgmt.jmx.credstore.PortableCredential;
import oracle.security.jps.mas.mgmt.jmx.credstore.PortableCredentialMap;
import oracle.security.jps.mas.mgmt.jmx.credstore.PortablePasswordCredential;

public class CreateMap {
	public static void main(String[] args) throws Exception {
		String protocol = "t3";
		String jndi_root = "/jndi/";
		String wlserver = "weblogic.management.mbeanservers.domainruntime";
		String host = "localhost";
		int port = 7001;
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

		ObjectName name = new ObjectName(
				"com.oracle.jps:type=JpsCredentialStore");

		MBeanInfo mbi = conn.getMBeanInfo(name);
		MBeanOperationInfo[] mboarr = mbi.getOperations();

		for (int i = 0; i < mboarr.length; i++) {
			MBeanOperationInfo mbo = mboarr[i];
			System.out.println(mbo.getName());
		}

		String ALIAS = "testmap1";
		String KEY = "testkey1";

		boolean containsMap = (Boolean) conn.invoke(name, "containsMap",
				new Object[] { ALIAS }, new String[] { String.class
						.getName() });

		
		PortableCredentialMap pcm = null;
		
		if (containsMap) {
			CompositeData cd = (CompositeData) conn.invoke(name,
					"getPortableCredentialMap", new Object[] { ALIAS },
					new String[] { String.class.getName() });
			
			pcm = PortableCredentialMap.from(cd);
		} else {
			pcm = new PortableCredentialMap();
		}
		
		PortablePasswordCredential pc = new PortablePasswordCredential("MYAPPID1", "password12".toCharArray());
		pcm.setPortableCredential(KEY, pc);
		
	}
}
