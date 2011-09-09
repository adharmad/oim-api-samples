package oimsamples.attestation;

import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

import oimsamples.util.OIMUtils;


import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.AttestationOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class FindPendingAttestationTasksForUser {


	public static void main(String[] args) throws Exception {

		long attProcessDefKey = 41;
		
		Properties jndi = ConfigurationClient.getComplexSettingByPath("Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm", "xelsysadm");
		AttestationOperationsIntf attIntf = (AttestationOperationsIntf)factory.getUtility("Thor.API.Operations.AttestationOperationsIntf");
		
		tcResultSet rs = attIntf.getAttestationProcessExecutionHistory(attProcessDefKey);
		//OIMUtils.printResultSet(rs);
		
		// GEt all the attestation requests for the process definition
		Vector attestationRequests = new Vector();
		
		for (int i=0 ; i<rs.getRowCount() ; i++) {
			rs.goToRow(i);
			attestationRequests.add(rs.getStringValue("Attestation Requests.Key"));
		}
		
		// For all attestation requests, get the tasks and filter out by the user key
		for (int i=0 ; i<attestationRequests.size() ; i++) {
			long attReqKey = Long.parseLong((String)attestationRequests.get(i));
			tcResultSet rs1 = attIntf.getAttestationRequestDetails(attReqKey, new HashMap(), 1, 100);
			
			for (int j=0 ; j<rs1.getRowCount() ; j++) {
				rs1.goToRow(j);
				String usrKey = rs1.getStringValue("Provisioned User.Key");
				String objName = rs1.getStringValue("Objects.Name");
				String attTaskKey = rs1.getStringValue("Attestation Tasks.Key");
				
				
				// you can filter out here based on user login, resource object name etc.
				System.out.println("atttaskkey = " + attTaskKey + " for user=" + usrKey + " for object=" + objName);
			}
			
		}
		
		factory.close();
		System.exit(0);
	}
	


}
