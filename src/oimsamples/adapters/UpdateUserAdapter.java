package oimsamples.adapters;

import java.util.Hashtable;

import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcUserOperationsIntf;

import com.thortech.xl.crypto.tcCryptoUtil;
import com.thortech.xl.crypto.tcSignatureMessage;
import com.thortech.xl.util.config.ConfigurationClient;

public class UpdateUserAdapter {
	public void updateUser(String userID, String update) {
        try {
            System.out.println("JMURolesDebug001 - Entered JMU Roles");
            
            System.out.println("JMURolesDebug001a - The input value of userID is: " + userID);
            System.out.println("JMURolesDebug001b - The input value of jmuRoles is: ");
                      
            ConfigurationClient.ComplexSetting config = 
                ConfigurationClient.getComplexSettingByPath("Discovery.CoreServer");
            final Hashtable env = config.getAllSettings();
            tcSignatureMessage moSignature = 
                tcCryptoUtil.sign("xelsysadm", "PrivateKey");
            tcUtilityFactory m_utilFactory = 
                new tcUtilityFactory(env, moSignature);

            tcUserOperationsIntf userIntf = 
                (tcUserOperationsIntf)m_utilFactory.getUtility("Thor.API.Operations.tcUserOperationsIntf");

            Hashtable userHash = new Hashtable();
            userHash.put("Users.User ID", userID);
            tcResultSet allUsers = userIntf.findUsers(userHash);
                        
            allUsers.goToRow(0);
            
            Hashtable userValues = new Hashtable();
                // Initialize Access
                // HR Roles
            userValues.put("USR_UDF_JMU", update);

             userIntf.updateUser(allUsers, userValues);
             System.out.println("JMURolesDebug007 - Updated User");
             m_utilFactory.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
	}
}
