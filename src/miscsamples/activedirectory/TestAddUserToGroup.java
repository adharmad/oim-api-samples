package miscsamples.activedirectory;

import java.util.Vector;

import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

import com.thortech.xl.integration.ActiveDirectory.tcUtilADTasks;
import com.thortech.xl.util.adapters.tcUtilLDAPController;

public class TestAddUserToGroup {
    public static void main(String[] args) throws Exception
    {
        
        tcUtilADTasks tsk = new tcUtilADTasks("host", 
                "dc=exch552004,dc=com", 
                "cn=Administrator,cn=Users,dc=exch552004,dc=com", "password", false);
        System.out.println(tsk.checkConnection());
        //tsk.addUserToGroup("", "jail", "ou=testorg1, ou=helpgroup", 
        //        "gollum x. smeagol");
        
        
        boolean result;

        String hierarchy = "";
        String name = "jail";
        String userhierarchy = "ou=testorg1,ou=helpgroup";
        String user = "gollum x. smeagol";
            
        tcUtilLDAPController ADcntrl = tsk.getADController(); 
        ADcntrl.connect("");
        Vector membersAlreadyThere = tsk.getAttributeValues(hierarchy, "CN=" + name, "member");

        ADcntrl.disconnect();
        if (membersAlreadyThere == null)
          membersAlreadyThere=new Vector();
        for (int i=0; i < membersAlreadyThere.size(); i++) {
          String val=(String)membersAlreadyThere.elementAt(i);

          val=val.toUpperCase();
          System.out.println("val is: " + val);
          membersAlreadyThere.setElementAt(val, i);
        }
        ADcntrl.connect(hierarchy);
        user="cn=" + user + "," + ADcntrl.getPath(userhierarchy);
        String srchusr=user.toUpperCase();
        System.out.println("srchusr is: " + srchusr);

        for (int i=0 ; i<membersAlreadyThere.size() ; i++)
        {
            String s = (String)membersAlreadyThere.get(i);
            //System.out.println("member " + i + " is " + s);
            //System.out.println(s.compareTo(srchusr));
            if (s.compareTo(srchusr) == 0)
            {
                System.out.println("compare to is OK!");
            }
            
            if (s.equals(srchusr))
            {
                System.out.println("equals is OK!");
            }
            
        }
        
        if (membersAlreadyThere.contains(srchusr))
        {
            System.out.println("user in group");
        }
        else
        {
            System.out.println("BOOOOOOOOOOOOOO");
        }
        /*
        Attributes attrs=new BasicAttributes(true);

        attrs.put(new BasicAttribute("member", user));
        result=ADcntrl.modifyAttributesAdd("cn=" + name, attrs);
        */
        ADcntrl.disconnect();
        //System.out.println(result);

    }

}
 