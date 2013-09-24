package oimsamples.itres;

import java.util.HashMap;
import java.util.Properties;

import oimsamples.util.OIMUtils;
import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class FindITResourcesByDefinition {

	public static void main(String[] args) throws Exception {

		String defName = "oidbug";
		Properties jndi = ConfigurationClient.getComplexSettingByPath(
				"Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm",
				"Welcome1");

		tcITResourceInstanceOperationsIntf itinstIntf = (tcITResourceInstanceOperationsIntf) factory
		.getUtility("Thor.API.Operations.tcITResourceInstanceOperationsIntf");
		
		HashMap map = new HashMap();
		map.put("IT Resource Type Definition.Server Type", defName);

		tcResultSet rs = itinstIntf.findITResourceInstances(map);
		OIMUtils.printResultSet(rs);
		
		for (int i=0 ; i<rs.getRowCount() ; i++) {
			rs.goToRow(i);
			long key = rs.getLongValue("IT Resource.Key");
			
			tcResultSet rs1 = itinstIntf.getITResourceInstanceParameters(key);
			OIMUtils.printResultSet(rs1);
		}

		factory.close();
		System.exit(0);
	}

}
