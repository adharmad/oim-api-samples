package miscsamples.activedirectory;

import java.util.Hashtable;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

public class TrySchema {
    Hashtable env;

    public static void main(String[] args) {
        TrySchema recon = new TrySchema("com.sun.jndi.ldap.LdapCtxFactory",
                "ldap://host:389/ou=thordev,dc=exch552004,dc=com",
                "cn=Administrator,cn=Users,dc=exch552004,dc=com", "password");

        recon.reconcile("");
    }

    public TrySchema(String initialContextFactory, String providerUrl,
            String userDN, String password) {
        env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
        env.put(Context.PROVIDER_URL, providerUrl);
        env.put("java.naming.authoritative", "true");
        env.put("java.naming.security.principal", userDN);
        env.put("java.naming.security.credentials", password);

    }

    public void reconcile(String rolebase) {
        try {
            DirContext ctx = new InitialDirContext(env);
            System.out.println(ctx.getNameInNamespace());
            SearchControls ctls = new SearchControls();
            ctls.setReturningObjFlag(true);
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            String startingUSN = "10489";
            String endIntervalUSN = "10800";
            String query = "(&(&(uSNChanged>=" + startingUSN + ")(uSNChanged<="
                    + endIntervalUSN + "))(objectclass=organizationalPerson))";
            NamingEnumeration list = ctx.search(rolebase, query, ctls);

            while (list.hasMore()) {
                SearchResult searchresult = (SearchResult) list.next();
                Attributes attrs = searchresult.getAttributes();
                //System.out.println(attrs);

                for (NamingEnumeration namingenumeration = attrs.getAll(); 
                	 namingenumeration.hasMore();) 
                {
                    Attribute a = (Attribute) namingenumeration.next();
                    //System.out.println(a);
                    DirContext c = a.getAttributeDefinition();
                    if (a.getID().equalsIgnoreCase("objectClass"))
                    {
                        System.out.println(a.getID() + " " + a.size());
                        Vector vector = new Vector();
    					for (NamingEnumeration ne = a.getAll(); 
    						 ne.hasMore(); vector.addElement(ne.next()))
    					{
    					    System.out.println(vector);
    					}
    					
    					
                    }

                }

            }
        } catch (NamingException ne) {
            System.out.println("NAmig Excpetion in LDAPRecon");
            ne.printStackTrace();
        }

    }

}
