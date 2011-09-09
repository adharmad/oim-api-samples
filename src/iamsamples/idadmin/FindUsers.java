package iamsamples.idadmin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.List;
import java.util.Set;

import oracle.iam.identity.usermgmt.vo.UserManagerResult;

import oracle.iam.platform.OIMClient;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.identity.usermgmt.vo.User;

public class FindUsers {
    public static void main(String[] args) throws Exception {
        Hashtable env = new Hashtable();
        env.put("java.naming.provider.url", "t3://localhost:7001/oim");
        env.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");

        OIMClient oimClient = new OIMClient(env);
        oimClient.login("xelsysadm", "Welcome1");

        UserManager usrMgr = oimClient.getService(UserManager.class);

        SearchCriteria criteria = new SearchCriteria("User Login",
            "FOO1", SearchCriteria.Operator.EQUAL);
        Set retSet = new HashSet();
        retSet.add("usr_key");
        retSet.add("User Login");
        retSet.add("First Name");
        retSet.add("Last Name");
        retSet.add("test1");
        retSet.add("custom1");

        List<User> users = usrMgr.search(criteria, retSet, null);
        
        for(User user : users) {
            System.out.println("********LIST********"+user.getAttributeNames());
            
            Long usrKey = (Long)user.getAttribute("usr_key");
            String usrLogin = (String)user.getAttribute("User Login");
            String fn = (String)user.getAttribute("First Name");
            String ln = (String)user.getAttribute("Last Name");
            String test1 = (String)user.getAttribute("test1");
            String custom1 = (String)user.getAttribute("custom1");

            System.out.println(usrKey + " " + usrLogin + " " + fn + " " + ln + " " + test1 + " " + custom1);
        }

        System.exit(0);
    }
}
