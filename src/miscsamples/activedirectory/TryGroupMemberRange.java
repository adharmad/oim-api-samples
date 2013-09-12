package miscsamples.activedirectory;

import java.security.Provider;
import java.security.Security;
import java.util.Hashtable;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class TryGroupMemberRange {
    public static void main(String[] args) throws Exception {

        String server = "ldap://host:636/";
        String rootCtx = "dc=exch552004,dc=com";
        String admin = "cn=Administrator,cn=Users,dc=exch552004,dc=com";
        String password = "password";

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
        env.put("java.naming.authoritative", "true");
        env.put("java.naming.security.principal", admin);
        env.put("java.naming.security.credentials", password);
        env.put("java.naming.security.protocol", "ssl");

        DirContext ctx = new InitialDirContext(env);

        String hierarchy = "";
        String cnName = "CN=toyotagrp";

        //Create the search controls
        SearchControls searchCtls = new SearchControls();

        //Specify the search scope
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        //specify the LDAP search filter
        String searchFilter = "(&(objectClass=group)(CN=toyotagrp))";

        //Specify the Base for the search
        String searchBase = "";

        //initialize counter to total the group members and range values
        int totalResults = 0;
        int Start = 0;
        int Step = 10;
        int Finish = 9;
        boolean Finished = false;
        String Range;

        while (!Finished) 
        {
            // Specify the attributes to return
            Range = Start + "-" + Finish;
            String returnedAtts[] = { "member;Range=" + Range };
            searchCtls.setReturningAttributes(returnedAtts);

            //Search for objects using the filter
            NamingEnumeration answer = ctx.search(searchBase, searchFilter,
                    searchCtls);
            
            
            // Loop through the search results
			while (answer.hasMoreElements()) 
			{
				SearchResult sr = (SearchResult)answer.next();

				System.out.println(">>>" + sr.getName());

				//Print out the members
				Attributes attrs = sr.getAttributes();
				if (attrs != null) 
				{

					try 
					{
						for (NamingEnumeration ae = attrs.getAll();ae.hasMore();) 
						{
							Attribute attr = (Attribute)ae.next();


							if (attr.getID().endsWith("*")) 
							{
								Finished=true;
							}

							System.out.println("Attribute: " + attr.getID());
							for (NamingEnumeration e = attr.getAll();e.hasMore();totalResults++) 
							{

								System.out.println("   " + totalResults + ". " + e.next());
							}
						}

					}	 
					catch (NamingException e)	
					{
						System.err.println("Problem printing attributes: " + e);
					}
			
					Start = Start + Step;
					Finish = Finish + Step;

				}
			}
        }
        
        System.out.println("Total members: " + totalResults);
		ctx.close();

    }

}
