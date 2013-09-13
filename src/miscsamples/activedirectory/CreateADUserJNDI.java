package drivers.activedirectory;

import java.security.Provider;
import java.security.Security;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class CreateADUserJNDI {
	public static void main(String[] args) throws Exception {

		int UF_ACCOUNTDISABLE = 0x0002;
		int UF_PASSWD_NOTREQD = 0x0020;
		int UF_PASSWD_CANT_CHANGE = 0x0040;
		int UF_NORMAL_ACCOUNT = 0x0200;
		int UF_DONT_EXPIRE_PASSWD = 0x10000;
		int UF_PASSWORD_EXPIRED = 0x800000;

		String server = "ldap://192.168.50.110:389/";
		String rootCtx = "ou=coke,ou=adTest1,dc=tql17ad,dc=com";
		String admin = "administrator@tql17ad.com";
		String password = "dead_line";
		boolean ssl = false;
		String userPassword = "P@ssw0rd1";

		Provider prov = Security
				.getProvider("com.sun.net.ssl.internal.ssl.Provider");

		if (prov == null) {
			Class moProviderClass = Class
					.forName("com.sun.net.ssl.internal.ssl.Provider");
			Provider moProvider = (Provider) moProviderClass.newInstance();
			Security.addProvider(moProvider);
		}

		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, server + rootCtx);
		env.put(Context.AUTHORITATIVE, "true");
		env.put(Context.SECURITY_PRINCIPAL, admin);
		env.put(Context.SECURITY_CREDENTIALS, password);
		//env.put(Context.SECURITY_PROTOCOL, "ssl");

		DirContext ctx = new InitialDirContext(env);

		System.out.println("Initial context = " + ctx);

		Attributes attrs = new BasicAttributes(true);

		// String cn = "cn=dharma\\\\,amol";
		String cn = "gandalf1";
		String dn = "gandalf1";

		attrs.put(new BasicAttribute("objectclass", "User"));
		attrs.put(new BasicAttribute("sAMAccountName", cn));
		//attrs.put(new BasicAttribute("cn", dn));
		attrs.put(new BasicAttribute("givenName", cn));
		attrs.put(new BasicAttribute("sn", "white"));
		//attrs.put(new BasicAttribute("displayName", "gandalf dafool"));
		attrs.put(new BasicAttribute("userPrincipalName", cn));
		//attrs.put(new BasicAttribute("mail", "gandalf@lotr.net"));
		//attrs.put(new BasicAttribute("name", "gandalf dafool"));
		//attrs.put(new BasicAttribute("telephoneNumber", "123 456 7890"));
		//attrs.put(new BasicAttribute("unicodePwd", new String("\""
		//		+ userPassword + "\"").getBytes("UnicodeLittleUnmarked")));
		//attrs.put(new BasicAttribute("middlename", "p"));
		//attrs.put("userAccountControl", Integer.toString(UF_NORMAL_ACCOUNT
		//		+ UF_PASSWD_NOTREQD + UF_PASSWORD_EXPIRED + UF_ACCOUNTDISABLE));
		attrs.put("userAccountControl", Integer.toString(UF_NORMAL_ACCOUNT + UF_PASSWD_NOTREQD));

		// attrs.put(new BasicAttribute("userAccountControl", "66048"));
		// attrs.put(new BasicAttribute("distinguishedName",
		// "cn=dharma\\,amol,ou=org3,ou=adTest1,dc=tql04domain,dc=com"));

		// Name composite = new CompositeName().add(dn);
		// Context result = ctx.createSubcontext(composite, attrs);
		Context result = ctx.createSubcontext("cn=" + dn, attrs);
		System.out.println("Created user: " + cn);

		ctx.close();

	}
}
