package oimsamples.attestation;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.AttestationDefinitionOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;
import com.thortech.xl.vo.AttestationProcessDefinitionVO;
import com.thortech.xl.vo.AttestationUserScopeVO;

public class UpdateAttestationProcess {


	public static void main(String[] args) throws Exception {

		String attProcessName = "foobar1";
		String newUserLogin = "MOBY_DUCK";
		
		Properties jndi = ConfigurationClient.getComplexSettingByPath("Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm", "xelsysadm");
		AttestationDefinitionOperationsIntf attDefIntf = (AttestationDefinitionOperationsIntf)factory.getUtility("Thor.API.Operations.AttestationDefinitionOperationsIntf");
		
		Map hm = new HashMap();
		hm.put("Attestation Definition.Process Name", attProcessName);

		tcResultSet rs = attDefIntf.findAttestationProcesses(hm);
		rs.goToRow(0);
		//OIMUtils.printResultSet(rs);
		
		long attProcKey = rs.getLongValue("Attestation Definition.Key");
		AttestationProcessDefinitionVO attVO = attDefIntf.getAttestationProcessDefinition(attProcKey);
		ArrayList userScopeList = attVO.getUserScope();
		
		// Assuming a single element here
		AttestationUserScopeVO userScope = (AttestationUserScopeVO)userScopeList.get(0);
		userScope.setValueList(newUserLogin);
		
		attDefIntf.updateAttestationDefinition(attProcKey, attVO);
		
		System.out.println("updated!");
		
		factory.close();
		System.exit(0);
	}
	
}
