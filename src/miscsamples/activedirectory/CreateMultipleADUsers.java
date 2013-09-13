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

public class CreateMultipleADUsers {
	
	public static void main(String[] args) throws Exception {
		int UF_ACCOUNTDISABLE = 0x0002;
		int UF_PASSWD_NOTREQD = 0x0020;
		int UF_PASSWD_CANT_CHANGE = 0x0040;
		int UF_NORMAL_ACCOUNT = 0x0200;
		int UF_DONT_EXPIRE_PASSWD = 0x10000;
		int UF_PASSWORD_EXPIRED = 0x800000;

		String server = "ldap://host:389/";
		String rootCtx = "ou=myusers,dc=tql17ad,dc=com";
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

		String prefix = "samba";
		for (int i = 0; i < 2100; i++) {
			Attributes attrs = new BasicAttributes(true);

			
			String cn = prefix + i;
			//String dn = "cn=" + cn + "," + rootCtx;
			attrs.put(new BasicAttribute("objectclass", "User"));
			attrs.put(new BasicAttribute("sAMAccountName", cn)); 
			attrs.put(new BasicAttribute("cn", cn));
			attrs.put(new BasicAttribute("givenName", "samba"));
			attrs.put(new BasicAttribute("sn", "user"));
			attrs.put(new BasicAttribute("userPrincipalName", cn));
			attrs.put("userAccountControl", Integer.toString(UF_NORMAL_ACCOUNT + UF_PASSWD_NOTREQD));
			
			Context result = ctx.createSubcontext("cn=" + cn, attrs); 
			
			System.out.println("Created user: " + cn);			
		}
		ctx.close();

	}
}
