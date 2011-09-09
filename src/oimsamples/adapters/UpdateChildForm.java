package oimsamples.adapters;

import java.util.HashMap;
import java.util.Random;

import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcFormInstanceOperationsIntf;

import com.thortech.xl.dataaccess.tcDataProvider;


public class UpdateChildForm {

	public void updateChild(tcDataProvider dp) {
        try {
        	tcFormInstanceOperationsIntf fiIntf = (tcFormInstanceOperationsIntf)tcUtilityFactory.getUtility(dp, "Thor.API.Operations.tcFormInstanceOperationsIntf");
        	Random r = new Random(System.currentTimeMillis());
        	HashMap hm = new HashMap();
        	int x = r.nextInt(1000);
        	hm.put("UD_OIACHLD_C1", "c1_val_" + x);
        	hm.put("UD_OIACHLD_C2", "c2_val_" + x);
        	
        	System.out.println("inside adapter before updating");
        	fiIntf.addProcessFormChildData(1941, 7743, hm);
        	System.out.println("inside adapter after updating");

        } catch (Exception ex) {
            ex.printStackTrace();
        } 
	}

}
