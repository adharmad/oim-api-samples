package oimsamples.attestation;


import java.util.Properties;

import Thor.API.tcUtilityFactory;

import Thor.API.Operations.AttestationOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class TriggerAttestationProcess {

	public static void main(String[] args) throws Exception {

		String attProcessName = "foobar1";
		
		Properties jndi = ConfigurationClient.getComplexSettingByPath("Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm", "xelsysadm");
		//AttestationDefinitionOperationsIntf attDefIntf = (AttestationDefinitionOperationsIntf)factory.getUtility("Thor.API.Operations.AttestationDefinitionOperationsIntf");
		AttestationOperationsIntf attIntf = (AttestationOperationsIntf)factory.getUtility("Thor.API.Operations.AttestationOperationsIntf");
		
		attIntf.initiateAttestationProcess(attProcessName);
		System.out.println("done!");
		
		factory.close();
		System.exit(0);
	}
	

}
