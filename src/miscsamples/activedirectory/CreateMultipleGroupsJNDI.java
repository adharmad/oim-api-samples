package miscsamples.activedirectory;

import java.security.Provider;
import java.security.Security;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class CreateMultipleGroupsJNDI {
	public static void main(String[] args) throws Exception {
		String server = "ldap://host:389/";
		String rootCtx = "ou=allgroups,dc=tql17ad,dc=com";
		String admin = "administrator@tql17ad.com";
		String password = "password";
		boolean ssl = false;

		Provider prov = null;

		if (ssl) {
			prov = Security
					.getProvider("com.sun.net.ssl.internal.ssl.Provider");

			if (prov == null) {
				Class moProviderClass = Class
						.forName("com.sun.net.ssl.internal.ssl.Provider");
				Provider moProvider = (Provider) moProviderClass.newInstance();
				Security.addProvider(moProvider);
			}
		}

		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, server + rootCtx);
		env.put(Context.AUTHORITATIVE, "true");
		env.put(Context.SECURITY_PRINCIPAL, admin);
		env.put(Context.SECURITY_CREDENTIALS, password);

		if (ssl) {
			env.put(Context.SECURITY_PROTOCOL, "ssl");
		}

		DirContext ctx = new InitialDirContext(env);

		System.out.println("Initial context = " + ctx);

		String prefix = "testgrp";
		for (int i = 0; i < 10000; i++) {
			Attributes attrs = new BasicAttributes(true);

			String cn = prefix + i;
			String dn = "cn=" + cn + "," + rootCtx;
			attrs.put(new BasicAttribute("objectclass", "Group"));
			attrs.put(new BasicAttribute("cn", cn));
			//attrs.put(new BasicAttribute("dn", dn));
			
			Context result = ctx.createSubcontext("cn=" + cn, attrs); 
			
			System.out.println("Created group: " + cn);
			
		}

		/*
		 * attrs.put(new BasicAttribute("objectclass", "User")); attrs.put(new
		 * BasicAttribute("sAMAccountName", cn)); //attrs.put(new
		 * BasicAttribute("cn", dn)); attrs.put(new BasicAttribute("givenName",
		 * cn)); attrs.put(new BasicAttribute("sn", "white")); //attrs.put(new
		 * BasicAttribute("displayName", "gandalf dafool")); attrs.put(new
		 * BasicAttribute("userPrincipalName", cn)); //attrs.put(new
		 * BasicAttribute("mail", "gandalf@lotr.net")); //attrs.put(new
		 * BasicAttribute("name", "gandalf dafool")); //attrs.put(new
		 * BasicAttribute("telephoneNumber", "123 456 7890")); attrs.put(new
		 * BasicAttribute("unicodePwd", new String("\"" + userPassword +
		 * "\"").getBytes("UnicodeLittleUnmarked"))); //attrs.put(new
		 * BasicAttribute("middlename", "p")); //attrs.put("userAccountControl",
		 * Integer.toString(UF_NORMAL_ACCOUNT // + UF_PASSWD_NOTREQD +
		 * UF_PASSWORD_EXPIRED + UF_ACCOUNTDISABLE));
		 * attrs.put("userAccountControl", Integer.toString(UF_NORMAL_ACCOUNT +
		 * UF_PASSWD_NOTREQD)); // attrs.put(new
		 * BasicAttribute("userAccountControl", "66048")); // attrs.put(new
		 * BasicAttribute("distinguishedName", //
		 * "cn=dharma\\,amol,ou=org3,ou=adTest1,dc=tql04domain,dc=com")); //
		 * Name composite = new CompositeName().add(dn); // Context result =
		 * ctx.createSubcontext(composite, attrs); Context result =
		 * ctx.createSubcontext("cn=" + dn, attrs); System.out.println("Created
		 * user: " + cn);
		 */

		ctx.close();

	}
}
