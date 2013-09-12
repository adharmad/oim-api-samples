package miscsamples.activedirectory;

import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class TryObjectGUID {
    Hashtable env;
    private String baseCtx;

    public static void main(String[] args) {
        TryObjectGUID test = new TryObjectGUID("com.sun.jndi.ldap.LdapCtxFactory",
                "ldap://host:389/ou=xim802org1,ou=xlint,dc=domainb,dc=com",
                "cn=Administrator,cn=Users,dc=domainb,dc=com", "password");

        test.getobject("");
    }

    public TryObjectGUID(String initialContextFactory, String providerUrl,
            String userDN, String password) {
        
        env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
        env.put(Context.PROVIDER_URL, providerUrl);
        env.put("java.naming.authoritative", "true");
        env.put("java.naming.security.principal", userDN);
        env.put("java.naming.security.credentials", password);
        env.put("java.naming.ldap.attributes.binary","objectGUID");
        
        baseCtx = "ou=xim802org1,ou=xlint,dc=domainb,dc=com";
    }

    public void getobject(String rolebase) {
        try {
            DirContext ctx = new InitialDirContext(env);
            System.out.println(ctx.getNameInNamespace());
            SearchControls ctls = new SearchControls();
            ctls.setReturningObjFlag(true);
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            String query = "(cn=XLUSER01)";
            NamingEnumeration list = ctx.search(rolebase, query, ctls);

            
            while (list.hasMore()) 
            {
                SearchResult searchresult = (SearchResult) list.next();
                Attributes attrs = searchresult.getAttributes();
                //System.out.println(attrs);
                
                //Attribute a = attrs.get("objectGUID");
                byte[] b = (byte[])attrs.get("objectGUID").get();
                //String o = (String)a.get();
                //System.out.println(o);
                //byte[] b = o.getBytes();
                System.out.println(b.length);
                for (int i=0 ; i<b.length ; i++)
                {
                    System.out.println(b[i]);
                }
                
                System.out.println(byteToString(b));
                System.out.println(toHexString(b));
                
            }
            
            
        } catch (NamingException ne) {
            System.out.println("NAmig Excpetion in LDAPRecon");
            ne.printStackTrace();
        }

    }
    
    public String byteToString(byte[] b) {


        System.out.println("Inside byteToString()");
        System.out.println("Byte array length is: " + b.length);
        System.out.println("Byte Array is: ");
        for (int i=0 ; i<b.length ; i++)
        {
            System.out.print(" " + b[i]);
        }
        System.out.println("");
        
        if (b == null) {
            throw new IllegalArgumentException("b is null reference.");
        }
        /*
        if (b.length != 16) {
            throw new IllegalArgumentException("b is not 16 bytes long.");
        }
        */
        
        StringBuffer sb = new StringBuffer();
        for (int i=0 ; i<b.length ; i++)
        {
            sb.append(toHex(b[i]));
        }
        
        return new String(sb);
    }

    protected String toHex(byte value) {

        StringBuffer t = new StringBuffer(Integer
                .toHexString(value & 0x000000FF));
        while (t.length() < 2) {
            t.insert(0, "0");
        }
        return t.toString();
    } 
    
    public String toHexString(byte bytes[])
	{
		StringBuffer retString = new StringBuffer();
		for (int i = 0; i < bytes.length; ++i)
		{
			retString.append(
				Integer.toHexString(0x0100 + (bytes[i] & 0x00FF)).substring(1));
		}
		return retString.toString();
	}

    
    
}
