package iamsamples.provisioning;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.List;
import java.util.Set;

import oracle.iam.provisioning.vo.*;
import oracle.iam.provisioning.api.*;

import oracle.iam.platform.OIMClient;
import oracle.iam.platform.Platform;
import oracle.iam.platform.context.*;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

public class FindAppInstances {

    public static void main(String[] args) throws Exception {
        Hashtable env = new Hashtable();
        env.put("java.naming.provider.url", "t3://localhost:8003/oim");
        env.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");

        OIMClient oimClient = new OIMClient(env);
        oimClient.login("xelsysadm", "Welcome1");


        /*
        HashMap ctxValues = new HashMap();
        ctxValues.put("this", "is a string");
        ctxValues.put("that", "is another string");
        ctxValues.put("here", "you go");

        ContextManager.pushContext("hello_world", ContextManager.ContextTypes.REQUEST, "testing"); //, ctxValues);
        ContextManager.setValue("request_key", new ContextAwareString("123"));
        ContextManager.setValue("user_id", new ContextAwareString("456"));
        ContextManager.setValue("role_name", new ContextAwareString("foo"));
        */
        
        ApplicationInstanceService aiSvc = oimClient.getService(ApplicationInstanceService.class);

        //SearchCriteria criteria = new SearchCriteria(ApplicationInstance.APPINST_NAME,
        //    "simapp1", SearchCriteria.Operator.EQUALS);

        ApplicationInstance ai = aiSvc.findApplicationInstanceByName("discon1");

        //List<ApplicationInstance> aiLst = aiSvc.findApplicationInstance(criteria, new HashMap());

        //ContextManager.popContext();
        
        //for(ApplicationInstance ai : aiLst) {

        String aiName = ai.getApplicationInstanceName();
        String roName = ai.getObjectName();
        String itresName = ai.getItResourceName();
        String dispName = ai.getDisplayName();
        String desc = ai.getDescription();
        boolean softDel = ai.isSoftDelete();
        String type = ai.getType().toString();

        System.out.println("ai name = " + aiName);
        System.out.println("ro name = " + roName);
        System.out.println("itres name = " + itresName);
        System.out.println("disp name = " + dispName);
        System.out.println("desc = " + desc);
        System.out.println("softdel = " + softDel);
        System.out.println("type = " + type);

        FormInfo pForm = ai.getAccountForm();
        List<FormInfo> cForms = ai.getChildForms();

            
        //}

        oimClient.logout();

        System.exit(0);
    }


    public static void printFormInfo(FormInfo f) {
        System.out.println("form name = " + f.getName());
        System.out.println("form desc = " + f.getDescription());
        System.out.println("latest vers = " + f.getLatestVersion());
        System.out.println("active vers = " + f.getActiveVersion());

        List<FormField> fields = f.getFormFields();

        for (FormField ff : fields) {
        }

    }

}
