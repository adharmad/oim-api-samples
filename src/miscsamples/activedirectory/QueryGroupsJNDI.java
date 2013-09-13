package miscsamples.activedirectory;

import java.security.Provider;
import java.security.Security;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import com.sun.jndi.ldap.ctl.PagedResultsControl;
import com.sun.jndi.ldap.ctl.PagedResultsResponseControl;

public class QueryGroupsJNDI {
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

		// DirContext ctx = new InitialDirContext(env);
		LdapContext ctx = new InitialLdapContext(env, null);
		SearchControls searchCtls = new SearchControls();

		// Specify the attributes to return
		//String returnedAtts[] = { "name", "cn" };
		String returnedAtts[] = {"objectGUID", "cn", "whenChanged"};
		searchCtls.setReturningAttributes(returnedAtts);

		// Specify the search scope
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		int pageSize = 100;
		byte[] cookie = null;

		
		//***************change************
		Control[] ctls = new Control[] { new PagedResultsControl(pageSize) };
		ctx.setRequestControls(ctls);
		//***************change************

		int totalResults = 0;
		System.out.println("Initial context = " + ctx);

		do {
			NamingEnumeration nenum = ctx.search("", "(&(whenChanged>=19000101000000.0Z)(objectclass=group))",
					searchCtls);

			// loop through the results in each page
			while (nenum != null && nenum.hasMoreElements()) {
				SearchResult sr = (SearchResult) nenum.next();

				// print out the name
				System.out.println("name: " + sr.getName());
				// increment the counter
				totalResults++;
			}

			// examine the response controls
			cookie = parseControls(ctx.getResponseControls());

			System.out.println("cookie = " + cookie + " length = " + cookie.length);
			
			// pass the cookie back to the server for the next page
			ctx.setRequestControls(new Control[] { new PagedResultsControl(
					pageSize, cookie, Control.CRITICAL) });

		} while ((cookie != null) && (cookie.length != 0));

		
		ctx.close();
		System.out.println("Total entries: " + totalResults);
		
		// NamingEnumeration nenum = ctx.search("", "(objectclass=group)",
		// null);
		/*
		 * NamingEnumeration nenum = ctx.search("", "(objectclass=group)",
		 * searchCtls);
		 * 
		 * int cnt = 0; for (NamingEnumeration ne = nenum; ne.hasMoreElements();
		 * cnt++) { SearchResult sr = (SearchResult)ne.nextElement();
		 * System.out.println(sr.getName()); }
		 * 
		 * System.out.println(cnt);
		 */
	}

	public static byte[] parseControls(Control[] controls)
			throws NamingException {

		byte[] cookie = null;

		if (controls != null) {

			for (int i = 0; i < controls.length; i++) {
				System.out.println("controls[" + i + "] = " + controls.getClass().getName());
				if (controls[i] instanceof PagedResultsResponseControl) {
					PagedResultsResponseControl prrc = (PagedResultsResponseControl) controls[i];
					cookie = prrc.getCookie();
					System.out.println(">>Next Page \n");
				}
			}
		}

		return (cookie == null) ? new byte[0] : cookie;
	}

}
