package oimsamples.itres;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

import oimsamples.util.OIMUtils;

import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcITResourceDefinitionOperationsIntf;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class CreateITResourceInstances {



	public static void main(String[] args) throws Exception {

		String name = "mytype";
		Properties jndi = ConfigurationClient.getComplexSettingByPath(
				"Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm",
				"xelsysadm");

		tcITResourceInstanceOperationsIntf itinstIntf = (tcITResourceInstanceOperationsIntf) factory
		.getUtility("Thor.API.Operations.tcITResourceInstanceOperationsIntf");
		tcITResourceDefinitionOperationsIntf itdefIntf = (tcITResourceDefinitionOperationsIntf) factory
		.getUtility("Thor.API.Operations.tcITResourceDefinitionOperationsIntf");
		
		// Find the definition of the type you want to create
		HashMap map = new HashMap();
		map.put("IT Resources Type Definition.Server Type", name);

		tcResultSet rs = itdefIntf.getITResourceDefinition(map);
		
		// assuming single row since search is by exact type definition name
		rs.goToRow(0);
		long itdefKey = rs.getLongValue("IT Resources Type Definition.Key");
		
		tcResultSet rs1 = itdefIntf.getITResourceDefinitionParameters(itdefKey);
		
		//OIMUtils.printResultSet(rs1);
		
		String[] paramNames = new String[rs1.getRowCount()];
		
		for (int i=0 ; i<rs1.getRowCount() ; i++) {
			rs1.goToRow(i);
			paramNames[i] = rs1.getStringValue("IT Resource Type Definition.IT Resource Type Parametr.Field Name");
		}
		
		String firstFieldVal = "myFirstField";
		String secondFieldVal = "mySecondField";
		String thirdFieldVal = "myThirdField";
		
		String instName = "myinstance5";
		
		// create a new it resource instance
		HashMap itInstMap = new HashMap();
		itInstMap.put("IT Resource.Name", instName);
		itInstMap.put("IT Resources Type Definition.Key", itdefKey+"");
		
		long instKey = itinstIntf.createITResourceInstance(itInstMap);

		for(int i=0 ; i<paramNames.length ; i++) {
		
			String paramName = paramNames[i];
			String val = "";
			
			HashMap map1 = new HashMap();
			
			if (paramName.equalsIgnoreCase("firstField")) { 
				val=  firstFieldVal;
			} else if (paramName.equalsIgnoreCase("secondField")) {
				val=  secondFieldVal;
			} else if (paramName.equalsIgnoreCase("thirdField")) {
				val=  thirdFieldVal;
			}
			map1.put(paramName, val);
			
			OIMUtils.printMap(map1);
			
			itinstIntf.updateITResourceInstanceParameters(instKey, map1);
		}
		
		factory.close();
		System.exit(0);
	}


}
