package oimsamples.request;

import java.util.Date;
import java.util.Properties;

import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcRequestOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class TestFindRequestAPI {

	public static void main(String[] args) throws Exception
	{
		Properties p = ConfigurationClient.getComplexSettingByPath(
				"Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(p, 
				"xelsysadm", "xelsysadm");
		tcRequestOperationsIntf reqOps = (tcRequestOperationsIntf)factory.getUtility(
				"Thor.API.Operations.tcRequestOperationsIntf");
		
		// Find requests in completed status
		String[] statuses = {"C"};
		Date date = new Date();
		
		tcResultSet rs = reqOps.findRequestsByStatusBeforeDate(statuses, date);
		
		int rsCount = rs.getRowCount();
		for (int i=0 ; i<rsCount ; i++)
		{
			rs.goToRow(i);
			String[] columns = rs.getColumnNames();
			int colCount = columns.length;
			
			for (int j=0 ; j<colCount ; j++)
			{
				String column = columns[j];
				String val = rs.getStringValue(column);
				System.out.println(column + " --> " + val);
			}
		}
		
		System.out.println("logged in");
		System.exit(0);
		
	}	
	
}
