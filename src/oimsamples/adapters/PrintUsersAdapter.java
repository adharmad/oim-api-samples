package oimsamples.adapters;

import java.util.HashMap;
import java.util.Hashtable;

import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcUserOperationsIntf;

import com.thortech.xl.crypto.tcCryptoUtil;
import com.thortech.xl.crypto.tcSignatureMessage;
import com.thortech.xl.dataaccess.tcDataProvider;
import com.thortech.xl.util.config.ConfigurationClient;

public class PrintUsersAdapter {

	public void printUsers(tcDataProvider dp) {
        try {
        	
        	tcUserOperationsIntf usrIntf = (tcUserOperationsIntf)tcUtilityFactory.getUtility(dp, "Thor.API.Operations.tcUserOperationsIntf");

        	/*
            ConfigurationClient.ComplexSetting config = 
                ConfigurationClient.getComplexSettingByPath("Discovery.CoreServer");
            final Hashtable env = config.getAllSettings();
            tcSignatureMessage moSignature = 
                tcCryptoUtil.sign("xelsysadm", "PrivateKey");
            tcUtilityFactory m_utilFactory = 
                new tcUtilityFactory(env, moSignature);

            tcUserOperationsIntf userIntf = 
                (tcUserOperationsIntf)m_utilFactory.getUtility("Thor.API.Operations.tcUserOperationsIntf");
			*/
        	
            tcResultSet rs = usrIntf.findUsersFiltered(new HashMap(), new String[] {"Users.User ID", "Users.First Name", "Users.Last Name"});
            
            for (int i=0 ; i<rs.getRowCount() ; i++) {
            	rs.goToRow(i);
            	System.out.println(rs.getStringValue("Users.User ID") + " " + rs.getStringValue("Users.First Name") + rs.getStringValue("Users.Last Name"));
            }
                        
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
	}

}
