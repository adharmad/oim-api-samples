package iamsamples.provisioning;

import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import oracle.iam.platform.OIMClient;
import oracle.iam.provisioning.api.*;
import oracle.iam.provisioning.vo.*;


public class GetSupportedObjectclasses {


    public static void main(String[] args) throws Exception {
        Hashtable env = new Hashtable();
        env.put("java.naming.provider.url", "t3://localhost:7001/oim");
        env.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");

        OIMClient oimClient = new OIMClient(env);
        oimClient.login("xelsysadm", "Welcome1");
        
        
		Map<String,String> hmValues = new java.util.HashMap<String,String>();
		hmValues.put("host","adc2140567.us.oracle.com");
		hmValues.put("port", "22");
		hmValues.put("loginUser", "aime1");
		hmValues.put("loginUserpassword", "coolkid1");				
		hmValues.put("sudoAuthorization", "0");
		
		
//		Map<String,String> hmValues = new java.util.HashMap<String,String>();
//		hmValues.put("host", "adc2190080.us.oracle.com");
//		hmValues.put("port", "3060");
//		//hmValues.put("port", "12cjh");
//		hmValues.put("principal", "cn=oimadmin,cn=systemids,dc=us,dc=oracle,dc=com");
//		hmValues.put("credentials", "welcome1");
//		hmValues.put("baseContexts", "\"dc=us,dc=oracle,dc=com\",\"dc=oracle,dc=com\"");
//		hmValues.put("passwordAttribute", "credentials");
//		hmValues.put("ssl", "false");
		
		
        int pickCase = Integer.parseInt(args[0]);

        ApplicationService appSvc = oimClient.getService(ApplicationService.class);
        ConnectorInfoService connInfoSvc = oimClient.getService(ConnectorInfoService.class);
        
        List<String> connectorTypes = connInfoSvc.getAllInstalledConnectorTypes();
        
        System.out.println("connector types: ");
        for (String connType : connectorTypes) {
        	System.out.println(connType);
        }
        
        String connType = connectorTypes.get(0);
        ConnectorInfo provConnInfo = connInfoSvc.getAllConnectorsForType(connType).get(0);

        
		System.out.println("Connector Display name: " + provConnInfo.getConnectorDisplayName());
		oracle.iam.provisioning.vo.ConnectorConfiguration cc = provConnInfo.getConfigProperties();
		LinkedHashMap<String,oracle.iam.provisioning.vo.ConnectorConfigurationProperty> provConnConfigProperties = cc.getConfigProperties();
		List<oracle.iam.provisioning.vo.ConnectorConfigurationProperty> connConfifPropValueSetList = new LinkedList<oracle.iam.provisioning.vo.ConnectorConfigurationProperty>();

		for( String propName : provConnConfigProperties.keySet())
		{
			oracle.iam.provisioning.vo.ConnectorConfigurationProperty connConfigProp = provConnConfigProperties.get(propName);
			//System.out.println("Setting property value for "+propName+" with type "+connConfigProp.getType());
			if(hmValues.containsKey(propName))
			{
				connConfigProp.setValue(hmValues.get(propName));
				connConfifPropValueSetList.add(connConfigProp);
			}			
		}
		
        
        
		Application application = new Application ();
		application.setBundleName(provConnInfo.getConnectorKey().getBundleName());
		application.setConnectorName(provConnInfo.getConnectorKey().getConnectorName());
		application.setBundleVersion(provConnInfo.getConnectorKey().getBundleVersion());
		application.setConnectorBundleURI(provConnInfo.getConnectorBundleURI());
		application.setConnectorServer(provConnInfo.getConnectorServer());
		application.setBundleConfig(connConfifPropValueSetList);

		
		List<ApplicationObjectclass> aocList =  appSvc.getSupportedApplicationObjectclasses(application);
		
		for(oracle.iam.provisioning.vo.ApplicationObjectclass aoc : aocList)
		{
			System.out.println("-------------------------------------");
			System.out.println(" Name: "+ aoc.getName());
			System.out.println(" TargetObjectClass: "+ aoc.getTargetObjectclass());
			System.out.println(" IsContainer: "+ aoc.isContainer());
			System.out.println(" Number of attributes = " + aoc.getSchemaAttrs().size());
			
			Set<BasicAttribute> attrs = aoc.getSchemaAttrs();
			for (BasicAttribute ai : attrs) {
				System.out.println("\t-----------------------------------");
				System.out.println("\tName "+ ai.getName());
				System.out.println("\tDisplay Name "+ ai.getDisplayName());
				System.out.println("\tICF Class "+ ai.getIcfClass());
				System.out.println("\tAttribute Type "+ ai.getType());
				System.out.println("\tIs Creatable "+ ai.isCreatable());				
				System.out.println("\tIs Readable "+ ai.isReadable());
				System.out.println("\tIs Required "+ ai.isRequired());
				System.out.println("\tisReturnedByDefault "+ ai.isReturnedByDefault());
				System.out.println("\tisUpdatable "+ ai.isUpdatable());
				System.out.println("\tisPassword "+ ai.isPassword());
				System.out.println("\tisEncrypted "+ ai.isEncrypted());
				System.out.println("\t-----------------------------------");

			}
			
			System.out.println("-------------------------------------");
		}
        
        
        
        /*
        
		try{
			
			switch (pickCase){
			case 1: 
			{
				System.out.println("=============== DBUM CONNECTOR =========================");
				Map<String,String> hmValues = new java.util.HashMap<String,String>();
				String jdbcUrl = "jdbc:oracle:thin:@adc2140567.us.oracle.com"+ ":" + "5521"+ ":" + "oimdb";
				hmValues.put("jdbcUrl",jdbcUrl);
				hmValues.put("loginPassword","welcome1");
				hmValues.put("loginUser","vdec2_oim");
				hmValues.put("dbType","Oracle");
				hmValues.put("databaseName","12cj");
				testgetApplObjectClass(null,hmValues);
				break;
			}
			case 2:
			{
				System.out.println("=============== OID CONNECTOR =========================");
				Map<String,String> hmValues = new java.util.HashMap<String,String>();
				hmValues.put("host", "adc2190080.us.oracle.com");
				//hmValues.put("port", "3060");
				hmValues.put("port", "12cjh");
				hmValues.put("principal", "cn=oimadmin,cn=systemids,dc=us,dc=oracle,dc=com");
				hmValues.put("credentials", "welcome1");
				hmValues.put("baseContexts", "\"dc=us,dc=oracle,dc=com\",\"dc=oracle,dc=com\"");
				hmValues.put("passwordAttribute", "credentials");
				hmValues.put("ssl", "false");
				//hmValues.put("blockSize", "12chj");
				testgetApplObjectClass(null,hmValues);
				break;
			}
			case 3:
			{
				System.out.println("=============== LOTUS DOMINO CONNECTOR =========================");				
				Map<String,String> hmValues = new java.util.HashMap<String,String>();
				hmValues.put("registrationServer", "something");
				hmValues.put("adminPassword", "changeit");
				hmValues.put("administrationServer", "something");
				hmValues.put("userDatabaseName", "something");				
				hmValues.put("adminName", "something");
				hmValues.put("adminIdFile", "something");
				hmValues.put("mailFileAction", "12cj");
				oracle.iam.provisioning.vo.ConnectorServer cs = 
					new oracle.iam.provisioning.vo.ConnectorServer("SujaLocalMachine","130.35.46.216","8759","changeit",false,0);
				testgetApplObjectClass(cs,hmValues);
				break;
			}
			case 4:
			{
				System.out.println("=============== UNIX CONNECTOR LOCALIZATION TEST =========================");	
				Map <String, List<oracle.iam.provisioning.vo.ConnectorInfo>> localBundleDiscoveryMap = icfBundleMgr.getAllLocalConnectorBundles();
				
				//Go through map and set configuration property values in string
				oracle.iam.provisioning.vo.ConnectorInfo provConnInfo= null;
				for(String url : localBundleDiscoveryMap.keySet())
				{
					System.out.println("url path is "+url);
					List<oracle.iam.provisioning.vo.ConnectorInfo> provConnInfoListPerBundle = localBundleDiscoveryMap.get(url);
					provConnInfo = provConnInfoListPerBundle.get(0);
				}
					
				oracle.iam.provisioning.vo.ConnectorInfo updatedProvConnInfo =icfBundleMgr.updateLocaleSpecificConfigPropertyInfo(provConnInfo, Locale.FRANCE);
				
				System.out.println("Connector Display name: " + updatedProvConnInfo.getConnectorDisplayName());
				System.out.println("Deployment Host Path: " + updatedProvConnInfo.getConnectorBundleURI());

				oracle.iam.provisioning.vo.ConnectorConfiguration cc = updatedProvConnInfo.getConfigProperties();
				
				LinkedHashMap<String,oracle.iam.provisioning.vo.ConnectorConfigurationProperty> provConnConfigProperties = cc.getConfigProperties();
				System.out.println("Total Configuration properties defined are ...."+provConnConfigProperties.size());

				for( String propName : provConnConfigProperties.keySet())
				{
					oracle.iam.provisioning.vo.ConnectorConfigurationProperty provConnConfigProperty = provConnConfigProperties.get(propName);
					System.out.println("=================================================================");
					System.out.println("Property name "+propName);
					System.out.println("Property type class "+provConnConfigProperty.getType());
					System.out.println("English displayname "+provConnConfigProperty.getDisplayName());
					System.out.println("Localized displayname  ...."+provConnConfigProperty.getLocalizedDisplayName());
					System.out.println("English help message "+provConnConfigProperty.getHelpMessage());
					System.out.println("Localized help message ...."+provConnConfigProperty.getLocalizedHelpMessage());
					System.out.println("====================================================================");

				}
				
				oracle.iam.provisioning.vo.ConnectorInfo updatedProvConnInfo1 =icfBundleMgr.updateLocaleSpecificConfigPropertyInfo(provConnInfo, Locale.ITALY);
				
				System.out.println("Connector Display name: " + updatedProvConnInfo.getConnectorDisplayName());
				System.out.println("Deployment Host Path: " + updatedProvConnInfo.getConnectorBundleURI());

				oracle.iam.provisioning.vo.ConnectorConfiguration cc1 = updatedProvConnInfo1.getConfigProperties();
				
				LinkedHashMap<String,oracle.iam.provisioning.vo.ConnectorConfigurationProperty> provConnConfigProperties1 = cc1.getConfigProperties();
				System.out.println("Total Configuration properties defined are ...."+provConnConfigProperties1.size());

				for( String propName1 : provConnConfigProperties1.keySet())
				{
					oracle.iam.provisioning.vo.ConnectorConfigurationProperty provConnConfigProperty1 = provConnConfigProperties1.get(propName1);
					System.out.println("=================================================================");
					System.out.println("Property name "+propName1);
					System.out.println("English displayname "+provConnConfigProperty1.getDisplayName());
					System.out.println("Italian displayname  ...."+provConnConfigProperty1.getLocalizedDisplayName());
					System.out.println("English help message "+provConnConfigProperty1.getHelpMessage());
					System.out.println("italian help message ...."+provConnConfigProperty1.getLocalizedHelpMessage());
					System.out.println("====================================================================");

				}

				break;
			}
			case 5:
			{
				System.out.println("=============== ACTIVE DIRECTORY CONNECTOR LOCALIZATION TEST =========================");				

				oracle.iam.provisioning.vo.ConnectorServer cs = 
					new oracle.iam.provisioning.vo.
					ConnectorServer("ADMatsMachine","140.84.132.147","8759","Welcome1",false,0);
				//icfConnConfig.testRemoteConnectorFacade(hmValues,cs,"__ACCOUNT__");
				
				List<oracle.iam.provisioning.vo.ConnectorInfo> remoteConnServers = ICFIntegrationUtil.getRemoteConnectorsInConnectorServer(cs);
				oracle.iam.provisioning.vo.ConnectorInfo provConnInfo = remoteConnServers.get(0);
				
				oracle.iam.provisioning.vo.ConnectorInfo updatedProvConnInfo =icfBundleMgr.updateLocaleSpecificConfigPropertyInfo(provConnInfo, Locale.FRANCE);
				
				System.out.println("Connector Display name: " + updatedProvConnInfo.getConnectorDisplayName());
				System.out.println("Deployment Host Path: " + updatedProvConnInfo.getConnectorBundleURI());

				oracle.iam.provisioning.vo.ConnectorConfiguration cc = updatedProvConnInfo.getConfigProperties();
				
				LinkedHashMap<String,oracle.iam.provisioning.vo.ConnectorConfigurationProperty> provConnConfigProperties = cc.getConfigProperties();
				System.out.println("Total Configuration properties defined are ...."+provConnConfigProperties.size());

				for( String propName : provConnConfigProperties.keySet())
				{
					oracle.iam.provisioning.vo.ConnectorConfigurationProperty provConnConfigProperty = provConnConfigProperties.get(propName);
					System.out.println("=================================================================");
					System.out.println("Property name "+propName);
					System.out.println("Property type class "+provConnConfigProperty.getType());
					System.out.println("English displayname "+provConnConfigProperty.getDisplayName());
					System.out.println("English help message "+provConnConfigProperty.getHelpMessage());
					System.out.println("Localized displayname  ...."+provConnConfigProperty.getLocalizedDisplayName());
					System.out.println("Localized help message ...."+provConnConfigProperty.getLocalizedHelpMessage());
					System.out.println("====================================================================");

				}
				break;
			}
			case 6:
			{
				System.out.println("==================UNIX =============================");

				//Set Configuration Property Values
				Map<String,String> hmValues = new java.util.HashMap<String,String>();
				hmValues.put("host","adc2140567.us.oracle.com");
				hmValues.put("port", "22");
				hmValues.put("loginUser", "aime1");
				hmValues.put("loginUserpassword", "coolkid1");				
				hmValues.put("sudoAuthorization", "0");
				hmValues.put("rbacAuthorization", "");
				
				testgetApplObjectClass(null,hmValues);
				break;
			}
			case 7:
			{
				System.out.println("=============== ACTIVE DIRECTORY  =========================");				
				Map<String,String> hmValues = new java.util.HashMap<String,String>();
				hmValues.put("DirectoryAdminName", "adlrgadmin");
				hmValues.put("DirectoryAdminPassword", "Welcome1");
				hmValues.put("Container", "DC=adlrg,DC=us,DC=oracle,DC=com");
				//hmValues.put("DomainName", "adlrg.us.oracle.com");
				hmValues.put("DomainName", "skgjkgdfkgjdfkgjdfk");
				oracle.iam.provisioning.vo.ConnectorServer cs = 
					new oracle.iam.provisioning.vo.
					ConnectorServer("ADMatsMachine","140.84.132.147","8759","Welcome1",false,0);
				testgetApplObjectClass(cs,hmValues);
				break;
			}
		}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		*/


    }

    
    private static void getApplObjClass(oracle.iam.provisioning.vo.ConnectorServer cs,Map hmValues,ConnectorInfoService connInfSvc) throws Exception
    {
    	
    }
    
    /*
	private static void testgetApplObjectClass(oracle.iam.provisioning.vo.ConnectorServer cs,Map hmValues) throws Exception
	{
		oracle.iam.provisioning.vo.ConnectorInfo provConnInfo=null;
		if (cs == null)
		{
			oracle.iam.provisioning.icfintegration.ICFBundleManager icfBundleMgr = new oracle.iam.provisioning.icfintegration.ICFBundleManager();
			Map <String, List<oracle.iam.provisioning.vo.ConnectorInfo>> localBundleDiscoveryMap = icfBundleMgr.getAllLocalConnectorBundles();
			
			for(String url : localBundleDiscoveryMap.keySet())
			{
				List<oracle.iam.provisioning.vo.ConnectorInfo> provConnInfoListPerBundle = localBundleDiscoveryMap.get(url);
				provConnInfo = provConnInfoListPerBundle.get(0);
			}
		}
		else{
			List<oracle.iam.provisioning.vo.ConnectorInfo> remoteConnServers = ICFIntegrationUtil.getRemoteConnectorsInConnectorServer(cs);
			provConnInfo = remoteConnServers.get(0);
		}
		System.out.println("Connector Display name: " + provConnInfo.getConnectorDisplayName());
		
		oracle.iam.provisioning.vo.ConnectorConfiguration cc = provConnInfo.getConfigProperties();
		
		LinkedHashMap<String,oracle.iam.provisioning.vo.ConnectorConfigurationProperty> provConnConfigProperties = cc.getConfigProperties();

		List<oracle.iam.provisioning.vo.ConnectorConfigurationProperty> connConfifPropValueSetList = new LinkedList<oracle.iam.provisioning.vo.ConnectorConfigurationProperty>();

		for( String propName : provConnConfigProperties.keySet())
		{
			oracle.iam.provisioning.vo.ConnectorConfigurationProperty connConfigProp = provConnConfigProperties.get(propName);
			if(hmValues.containsKey(propName))
			{
				//System.out.println("Setting property value for "+propName+" with value "+hmValues.get(propName));
				connConfigProp.setValue(hmValues.get(propName));
				connConfifPropValueSetList.add(connConfigProp);
			}
		}

		//Create Application
		oracle.iam.provisioning.vo.Application application = new oracle.iam.provisioning.vo.Application ();
		application.setBundleName(provConnInfo.getConnectorKey().getBundleName());
		application.setConnectorName(provConnInfo.getConnectorKey().getConnectorName());
		application.setBundleVersion(provConnInfo.getConnectorKey().getBundleVersion());
		application.setConnectorBundleURI(provConnInfo.getConnectorBundleURI());
		application.setConnectorServer(provConnInfo.getConnectorServer());
		application.setBundleConfig(connConfifPropValueSetList);
		
		oracle.iam.provisioning.icfintegration.ICFConnectorConfigManager icfConnConfigManager = new oracle.iam.provisioning.icfintegration.ICFConnectorConfigManager();
		List<oracle.iam.provisioning.vo.ApplicationObjectclass> aocList = icfConnConfigManager.getApplicationObjectClass(application, false);
		
		for(oracle.iam.provisioning.vo.ApplicationObjectclass aoc : aocList)
		{
			System.out.println("-------------------------------------");
			System.out.println(" Name: "+ aoc.getName());
			System.out.println(" TargetObjectClass: "+ aoc.getTargetObjectclass());
			System.out.println(" IsContainer: "+ aoc.isContainer());
			System.out.println("-------------------------------------");
		}

		
	}
	*/

}
