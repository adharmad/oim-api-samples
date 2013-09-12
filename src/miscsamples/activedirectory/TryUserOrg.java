package miscsamples.activedirectory;

import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import com.thortech.xl.schedule.tasks.LDAPController;


public class TryUserOrg {
    Hashtable env;
    private String baseCtx;

    public static void main(String[] args) {
        TryUserOrg recon = new TryUserOrg("com.sun.jndi.ldap.LdapCtxFactory",
                "ldap://host:389/ou=thordev,dc=exch552004,dc=com",
                "cn=Administrator,cn=Users,dc=exch552004,dc=com", "password");

        recon.reconcile("");
    }

    public TryUserOrg(String initialContextFactory, String providerUrl,
            String userDN, String password) {
        
        env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
        env.put(Context.PROVIDER_URL, providerUrl);
        env.put("java.naming.authoritative", "true");
        env.put("java.naming.security.principal", userDN);
        env.put("java.naming.security.credentials", password);
        
        baseCtx = "ou=thordev,dc=exch552004,dc=com";
    }

    public void reconcile(String rolebase) {
        try {
            DirContext ctx = new InitialDirContext(env);
            System.out.println(ctx.getNameInNamespace());
            SearchControls ctls = new SearchControls();
            ctls.setReturningObjFlag(true);
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            String query = "(objectclass=organizationalPerson)";
            NamingEnumeration list = ctx.search(rolebase, query, ctls);

            
            while (list.hasMore()) 
            {
                SearchResult searchresult = (SearchResult) list.next();
                Attributes attrs = searchresult.getAttributes();
                //System.out.println(attrs);
                
                Attribute a = attrs.get("distinguishedname");
                String dn = (String)a.get();
                
                Attribute a1 = attrs.get("name");
                String name = (String)a1.get();
                
                System.out.println(name);
                
                //System.out.println(dn);
                String myName = getOrgName(dn);
                String par = getParentOrg(dn);
                String org = LDAPController.getUsrParentOrg(dn);
                
                System.out.println(myName + " --> " + org);
                
            }
            
            
        } catch (NamingException ne) {
            System.out.println("NAmig Excpetion in LDAPRecon");
            ne.printStackTrace();
        }

    }
    
    public String getOrgName(String orgDN)
    {
        StringTokenizer stok = new StringTokenizer(orgDN, "=,\n");
        stok.nextToken();
        String firstTok = stok.nextToken().trim();
        
        return firstTok;
        
    }

    public String getParentOrg(String orgDN)
    {
        StringTokenizer stok = new StringTokenizer(orgDN, "=,\n");
        stok.nextToken();
        stok.nextToken();
        stok.nextToken();
        String firstTok = stok.nextToken().trim();
        
        return firstTok;
        
    }
}
