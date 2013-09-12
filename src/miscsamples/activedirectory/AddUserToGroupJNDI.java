package miscsamples.activedirectory;

import java.security.Provider;
import java.security.Security;
import java.util.Hashtable;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class AddUserToGroupJNDI {
    public static void main(String[] args) throws Exception
    {

        String server = "ldap://host:636/";
        String rootCtx = "dc=exch552004,dc=com";
        String admin = "cn=Administrator,cn=Users,dc=exch552004,dc=com";
        String password = "password";

        Provider prov = Security.getProvider(
                    "com.sun.net.ssl.internal.ssl.Provider");

        if (prov == null) {
            Class moProviderClass = Class.forName(
            	"com.sun.net.ssl.internal.ssl.Provider");
            Provider moProvider = (Provider) moProviderClass.newInstance();
            Security.addProvider(moProvider);
        }

        Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, server + rootCtx);
		env.put("java.naming.authoritative","true");
		env.put("java.naming.security.principal", admin);
		env.put("java.naming.security.credentials", password);
		env.put("java.naming.security.protocol","ssl");
        
		DirContext ctx = new InitialDirContext(env);

        String hierarchy = "";
        String name = "jail";
        String cnName = "CN=" + name;
        String userhierarchy = "ou=testorg1,ou=helpgroup";
        String user = "gollum x. smeagol";

        Vector values = null;

        try {
            
            Attributes answer = null;
            if (!hierarchy.equals("")) {
                answer = ctx.getAttributes(cnName + "," + hierarchy);
            } else {
                answer = ctx.getAttributes(cnName);
            }
            
            Attribute attrib = answer.get("member");

            int num = attrib.size();
            
            System.out.println("num = " + num);

            values = new Vector();
            for (int i = 0; i < num; i++) {
                values.addElement(attrib.get(i));
                Object o = attrib.get(i);
                System.out.println(o);
            }

            //System.out.println("\n: "+ name + "\n" + answer.get(attrname));
        } catch (NamingException e) {
            System.err.println("Problem retrieving attribute values: " + e);
        }
        
        if (values == null)
            values = new Vector();
        for (int i=0; i < values.size(); i++) {
          String val=(String)values.elementAt(i);

          val=val.toUpperCase();
          System.out.println("val is: " + val);
          values.setElementAt(val, i);
        }
        
        user = "cn=" + user + "," + userhierarchy + "," + rootCtx;
        String srchusr=user.toUpperCase();
        System.out.println("srchusr is: " + srchusr);

        for (int i=0 ; i<values.size() ; i++)
        {
            String s = (String)values.get(i);
            System.out.println("member " + i + " is " + s);
            System.out.println(s.compareTo(srchusr));
            if (s.compareTo(srchusr) == 0)
            {
                System.out.println("compare to is OK!");
            }
            
            if (s.equals(srchusr))
            {
                System.out.println("equals is OK!");
            }
            
        }
        
        if (values.contains(srchusr))
        {
            System.out.println("user in group");
        }
        else
        {
            System.out.println("BOOOOOOOOOOOOOO");
        }
        
        Attributes attrs=new BasicAttributes(true);
        attrs.put(new BasicAttribute("member", user));
        
        try {
            ctx.modifyAttributes(cnName, DirContext.ADD_ATTRIBUTE, attrs);
            System.out.println("attributes added: " + cnName);

        } catch (NamingException e) {
            System.err.println("Problem modifying object: " + e);
            e.printStackTrace();
        }
        
        try {
            ctx.close();
        } catch (NamingException e) {
            System.err.println("Problem while disconnecting: " + e);
        }
    }
}
