package oimsamples.adapters;

import java.util.HashMap;

import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcRequestOperationsIntf;

import com.thortech.xl.dataaccess.tcDataProvider;
import com.thortech.xl.dataobj.tcDataSet;

public class SleepAndUpdateRequest {

	public void sleep(tcDataProvider dp, long orcKey, long sec) throws Exception {
		
		System.out.println("------------------------------------------------------");
		
		tcRequestOperationsIntf reqIntf = (tcRequestOperationsIntf)tcUtilityFactory.getUtility(dp, "Thor.API.Operations.tcRequestOperationsIntf");
		String reqKey = "";
		
		tcDataSet ds = new tcDataSet();
		ds.setQuery(dp, "select req_key from orc where orc_key=" + orcKey);
		ds.executeQuery();
		if (ds.getRowCount() == 1) {
			ds.goToRow(0);
			reqKey = ds.getString("req_key");
			System.out.println("req_key = " + reqKey);
			
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("Requests.Note", "hello world from provisioning" + reqKey);
			
			System.out.println("updating request");
			System.out.println("ADAPTER:: NOT UPDATING REQUEST");
			//reqIntf.updateRequest(Long.parseLong(reqKey), hm);
			System.out.println("updated request");
			
		} else {
			System.out.println("no or multiple requests found!!");
		}
		
		try {
			System.out.println("Thread: " + Thread.currentThread().getName() + " sleeping");
			Thread.sleep(sec * 1000);
			System.out.println("Thread: " + Thread.currentThread().getName() + " awoke");
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		
		System.out.println("------------------------------------------------------");
	}
}
