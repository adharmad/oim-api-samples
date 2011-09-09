package oimsamples.login;

import java.util.HashMap;
import java.util.Properties;

import oimsamples.util.OIMUtils;


import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcGroupOperationsIntf;

import com.thortech.xl.crypto.tcCryptoUtil;
import com.thortech.xl.crypto.tcSignatureMessage;
import com.thortech.xl.util.config.ConfigurationClient;

public class SignatureLogin {
	public static void main(String[] args) throws Exception
	{
		Properties jndi = ConfigurationClient.getComplexSettingByPath(
		"Discovery.CoreServer").getAllSettings();

		tcSignatureMessage signedMsg = tcCryptoUtil.sign("xelsysadm", "PrivateKey");
		tcUtilityFactory factory = new tcUtilityFactory(jndi, signedMsg);
		tcGroupOperationsIntf grpIntf = (tcGroupOperationsIntf)factory.getUtility(
			"Thor.API.Operations.tcGroupOperationsIntf");
		System.out.println("signature login complete");
		
		tcResultSet rs = grpIntf.findGroups(new HashMap());
		OIMUtils.printResultSet(rs);

		factory.close();
		System.out.println("logout complete");
		System.exit(0);
	}
}
