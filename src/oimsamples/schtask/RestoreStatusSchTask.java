package oimsamples.schtask;

import com.thortech.xl.dataobj.tcDataSet;
import com.thortech.xl.dataobj.tcOIU;
import com.thortech.xl.scheduler.tasks.SchedulerBaseTask;
import com.thortech.xl.server.tcOrbServerObject;


public class RestoreStatusSchTask extends SchedulerBaseTask {

	public void execute() {
		String udFormName = getAttribute("UDFORM");
		long orcKey = Long.parseLong(getAttribute("ORCKEY"));
		String currentStatus = getAttribute("CURRENTSTATUS");
		
		try {
    		String udStatusField = udFormName + "_STATUS";
    		
    		tcDataSet ds = new tcDataSet();
    		String query = "select oiu.oiu_key from oiu oiu, ost ost, orc orc " +
    			"where oiu.ost_key=ost.ost_key and oiu.orc_key=orc.orc_key " + 
    			"and ost.ost_status='" + currentStatus + "' and oiu.orc_key=" + orcKey;

    		ds.setQuery(getDataBase(), query);
    		ds.executeQuery();
		
    		ds.goToRow(0);
    		
    		String oiuKey = ds.getString("oiu_key");
    		
    		tcDataSet ds1 = new tcDataSet();
			String query1 = "select obj.obj_name from obj obj, pkg pkg, orc orc " + 
				"where orc.pkg_key=pkg.pkg_key and pkg.obj_key=obj.obj_key " +
				"and orc.orc_key=" + orcKey;

			ds1.setQuery(getDataBase(), query1);
			ds1.executeQuery();
			ds1.goToRow(0);
			String objName = ds1.getString("obj_name");
			
			tcDataSet ds2 = new tcDataSet();
			String query2 = "select ost.ost_key from ost ost, obj obj, " + udFormName + " udform, orc orc "
				+ "where obj.obj_name='" + objName + "' and udform." + udStatusField + "=ost.ost_status "
				+ "and ost.obj_key=obj.obj_key and udform.orc_key=orc.orc_key "
				+ "and orc.orc_key=" + orcKey;

			ds2.setQuery(getDataBase(), query2);
			ds2.executeQuery();
			ds2.goToRow(0);
			long ostKey = ds2.getLong("ost_key");
			
			System.out.println("orc: " + orcKey + " currentStatus: " + currentStatus + " udFormName: " + udFormName);
			System.out.println("Updating oiu: " + oiuKey + " with ostKey: " + ostKey);
			
			String query3 = "update oiu set ost_key=" + ostKey + " where oiu_key=" + oiuKey;
			getDataBase().writeStatement(query3);
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}

	}

}
