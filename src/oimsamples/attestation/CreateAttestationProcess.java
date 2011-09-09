package oimsamples.attestation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import Thor.API.tcUtilityFactory;
import Thor.API.Operations.AttestationDefinitionOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;
import com.thortech.xl.vo.AttestationProcessDefinitionVO;
import com.thortech.xl.vo.AttestationResourceScopeVO;
import com.thortech.xl.vo.AttestationUserScopeVO;

public class CreateAttestationProcess {

	public static void main(String[] args) throws Exception {

		String attProcessName = "foobar1";
		
		Properties jndi = ConfigurationClient.getComplexSettingByPath("Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm", "xelsysadm");
		AttestationDefinitionOperationsIntf attDefIntf = (AttestationDefinitionOperationsIntf)factory.getUtility("Thor.API.Operations.AttestationDefinitionOperationsIntf");

		AttestationProcessDefinitionVO attVO = new AttestationProcessDefinitionVO();
		attVO.setProcessName(attProcessName);
		attVO.setProcessCode(attProcessName + " code");
		attVO.setProcessDescription(attProcessName + " descrition");
		attVO.setProcessOwner("SYSTEM ADMINISTRATORS");
		attVO.setProcessGracePeriod(0);
		attVO.setProcessScheduleType(AttestationProcessDefinitionVO.ONCE);
		attVO.setProcessScheduleFrequency(0);
		attVO.setReviewerType("USER.MANAGER");
		attVO.setProcessGracePeriod(1);
		attVO.setProcessSendMailToOwner(true);
		Calendar c = GregorianCalendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_MONTH, 1);
		//java.sql.Date tomorrow = new java.sql.Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		Date tomorrow = c.getTime();
		//attVO.setProcessNextStartTime(tomorrow);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String tomo = sdf.format(tomorrow);
		System.out.println(tomo);
		attVO.setProcessStartTime(tomo);
		
		AttestationUserScopeVO userScope = new AttestationUserScopeVO();
		userScope.setAttributeName("Users.User ID");
		userScope.setCondition("IsExactly");
		userScope.setValueList("HELLO_WORLD");
		ArrayList userScopeList = new ArrayList();
		userScopeList.add(userScope);
		
		attVO.setUserScope(userScopeList);
		
		AttestationResourceScopeVO resScope = new AttestationResourceScopeVO();
		resScope.setAttributeName("Objects.Name");
		resScope.setCondition("Contains");
		resScope.setValueList("fooboo");
		ArrayList resScopeList = new ArrayList();
		resScopeList.add(resScope);
		
		attVO.setResourceScope(resScopeList);
		
		long procKey = attDefIntf.createAttestationDefinition(attVO);
		System.out.println("Created attestation process with key = " + procKey); 
		
		factory.close();
		System.exit(0);
	}

}
