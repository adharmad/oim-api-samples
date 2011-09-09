package oimsamples.dm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import oimsamples.util.OIMUtils;


import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcExportOperationsIntf;

import com.thortech.xl.util.config.ConfigurationClient;
import com.thortech.xl.vo.ddm.RootObject;

public class ExportResource {
	public static void main(String[] args) throws Exception {
		String fileName = "c:/tmp/testresource1.xml";
		String type = "Resource";
		String name = "TestResource1";
		Properties jndi = ConfigurationClient.getComplexSettingByPath(
				"Discovery.CoreServer").getAllSettings();
		tcUtilityFactory factory = new tcUtilityFactory(jndi, "xelsysadm",
				"xelsysadm");

		tcExportOperationsIntf expIntf = (tcExportOperationsIntf) factory
				.getUtility("Thor.API.Operations.tcExportOperationsIntf");


		Collection col = expIntf.findObjects(type, name);
		ArrayList lstToExport = new ArrayList();
		
		Collection col1 = expIntf.retrieveChildren(col);

		Iterator it = col1.iterator();
		
		while (it.hasNext()) {
			RootObject ro = (RootObject)it.next();
			lstToExport.add(ro);
			lstToExport.addAll(ro.getChilds());
		}
		
		
		OIMUtils.printCollection(lstToExport);
		
		String xml = expIntf.getExportXML(lstToExport, "TestResource1");
		
		File f = new File(fileName);
		if (!f.exists()) {
			f.createNewFile();
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		bw.write(xml);
		bw.close();
		System.out.println(xml);
		
		factory.close();
		System.exit(0);
			
	}
}
