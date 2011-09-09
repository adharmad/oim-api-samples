package oimsamples.eventhandlers;

import java.util.HashMap;
import java.util.Properties;

import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcRequestOperationsIntf;

import com.thortech.xl.client.events.tcBaseEvent;
import com.thortech.xl.crypto.tcCryptoUtil;
import com.thortech.xl.crypto.tcSignatureMessage;
import com.thortech.xl.dataaccess.tcDataProvider;
import com.thortech.xl.dataobj.tcDataObj;
import com.thortech.xl.dataobj.tcDataSet;
import com.thortech.xl.util.config.ConfigurationClient;



public class SchItemPreUpdate extends tcBaseEvent {
    public SchItemPreUpdate() {
        setEventName("SchItemPreUpdate");
    }
    protected void implementation() throws Exception {
    	
    	System.out.println("Executing SchItemPreUpdate");
    	System.out.println("==========================================");
		Properties jndi = ConfigurationClient.getComplexSettingByPath(
		"Discovery.CoreServer").getAllSettings();

		tcSignatureMessage signedMsg = tcCryptoUtil.sign("sa", "PrivateKey");
		tcUtilityFactory factory = new tcUtilityFactory(jndi, signedMsg);
    	
    	
    	tcDataProvider dp = getDataBase();
    	tcDataObj dob = getDataObject();
    	
    	String orcKey = dob.getString("orc_key").trim();
    	String schStatus = dob.getString("sch_status").trim();
    	
    	System.out.println("SchItemPreUpdate orc_key = " + orcKey);
    	System.out.println("SchItemPreUpdate sch_status = " + schStatus);
    	
		//tcRequestOperationsIntf reqIntf = (tcRequestOperationsIntf)tcUtilityFactory.getUtility(dp, "Thor.API.Operations.tcRequestOperationsIntf");
    	tcRequestOperationsIntf reqIntf = (tcRequestOperationsIntf)factory.getUtility("Thor.API.Operations.tcRequestOperationsIntf");
    	
		String reqKey = "";
		
		if (!"".equals(orcKey) && "C".equalsIgnoreCase(schStatus)) {
			tcDataSet ds = new tcDataSet();
			ds.setQuery(dp, "select req_key, pkg.pkg_name from orc, tos, pkg where orc_key=" + orcKey
					+ " and orc.tos_key=tos.tos_key and tos.pkg_key=pkg.pkg_key "
					+ " and pkg.pkg_type='Provisioning'");
			ds.executeQuery();
			if (ds.getRowCount() == 1) {
				ds.goToRow(0);
				reqKey = ds.getString("req_key").trim();
				System.out.println("req_key = " + reqKey);
				
				if (!"".equals(reqKey)) {
					HashMap<String, String> hm = new HashMap<String, String>();
					hm.put("Requests.Note", "ram ramfrom provisioning " + reqKey);
			
					System.out.println("SchItemPreUpdate updating request");
					System.out.println("EVENT HANDLER:: NOT UPDATING REQUEST");
					//reqIntf.updateRequest(Long.parseLong(reqKey), hm);
					System.out.println("SchItemPreUpdate updated request");
				}
			
			} else {
				System.out.println("SchItemPreUpdate no or multiple requests found!!");
			}
		}
    	
    	System.out.println("==========================================");
    }
}
