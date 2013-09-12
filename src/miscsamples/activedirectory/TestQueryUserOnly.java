package miscsamples.activedirectory;

import java.util.Vector;

import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchResult;

import com.thortech.xl.util.adapters.tcUtilLDAPController;


public class TestQueryUserOnly {
    public static void main(String[] args) throws Exception
    {
        String server = "host";
        String rootCtx = "ou=xim802org1,ou=xlint,dc=domainb,dc=com";
        String admin = "cn=Administrator,cn=Users,dc=domainb,dc=com";
        String password = "password";
        boolean ssl = false;

        
        // get parent hierarchy vector
        tcUtilLDAPController ad = new tcUtilLDAPController(server, 
                rootCtx, admin, password, ssl);
        
        
        Vector v = ad.search("", "(&(objectclass=organizationalPerson)(!(objectclass=computer)))");
        
        for (int i=0 ; i<v.size() ; i++)
        {
            System.out.println(v.get(i));
        }
        
        
        Vector v1 = ad.getAttributeValues("", "cn=xluser01", "objectguid");
        
        for (int i=0 ; i<v1.size() ; i++)
        {
            Object o = v1.get(i);
            System.out.println(o.getClass().getName() + " --> " + o);
            //byte[] b = (byte[])v1.get(i);
            //String s = toHexString(b);
            //System.out.println(s);
            
        }
        
        
    }
    
    
    public static String toHexString(byte bytes[])
    {
		StringBuffer retString = new StringBuffer();
		for (int i = 0; i < bytes.length; ++i)
        {
			retString.append(Integer.toHexString(0x0100 + (bytes[i] & 0x00FF)).substring(1));
	    }
		return retString.toString();
    }

}
