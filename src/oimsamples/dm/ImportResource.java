package oimsamples.dm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collection;
import java.util.Properties;

import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcImportOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;

public class ImportResource {

	public static void main(String[] args) throws Exception {
		String fileName = "c:/tmp/testresource1.xml";
		Properties jndi = ConfigurationClient.getComplexSettingByPath(
				"Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm",
				"xelsysadm");

		tcImportOperationsIntf impIntf = (tcImportOperationsIntf) factory
				.getUtility("Thor.API.Operations.tcImportOperationsIntf");

		
		System.out.println("Acquiring lock");
		impIntf.acquireLock(true);
		
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		StringBuffer sbuf = new StringBuffer();
		String line = null;
		
		while ((line = br.readLine()) != null) {
			sbuf.append(line);
		}
		
		Collection col = impIntf.addXMLFile(fileName, sbuf.toString());
		impIntf.performImport(col);
		
		factory.close();
		System.exit(0);
			
	}

}
