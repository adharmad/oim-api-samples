package iamsamples.entitymgr;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

//import junit.framework.TestCase;
import oracle.iam.platform.Platform;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import oracle.iam.platform.entitymgr.DuplicateAttributeDefinitionException;
import oracle.iam.platform.entitymgr.EntityManager;
import oracle.iam.platform.entitymgr.EntityManagerConfig;
import oracle.iam.platform.entitymgr.MissingRequiredAttributeException;
import oracle.iam.platform.entitymgr.NoSuchEntityException;
import oracle.iam.platform.entitymgr.UnknownAttributeException;
import oracle.iam.platform.entitymgr.spi.entity.Repository;
import oracle.iam.platform.entitymgr.vo.AttributeDefinition;
import oracle.iam.platform.entitymgr.vo.Entity;
import oracle.iam.platform.entitymgr.vo.EntityMetadata;
import oracle.iam.platform.entitymgr.vo.FieldDefinition;
import oracle.iam.platform.entitymgr.vo.Schema;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.platform.utils.MDSManager;
import oracle.iam.platform.utils.SpringBeanFactory;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class CreateEntityDefinition { //extends TestCase {

	EntityManager mgr;
	EntityManagerConfig mgrConfig;
	String entityType;
	String childEntityType;
	String childAttributeName;
	MDSManager mdsManager;
	String id;
	
	public CreateEntityDefinition(String entityName) {
		entityType = entityName;
	}
	
	public static void main(String[] args) throws Exception {
		CreateEntityDefinition ced = new CreateEntityDefinition("testentity1");
		ced.setUp();
		//ced.createEntityMetadata();
	}

	public void createEntityMetadata() throws Exception {
		addEntityMetadata();
		getEntityMetadata(entityType);
		createEntity();
	}
	
	public void setUp() {
		mgr = Platform.getService(EntityManager.class);
		mgrConfig = mgr.getEntityManagerConfig();
		mdsManager = new MDSManager();
	}

	public void testEntityMetadataOperations() throws Exception {
		addChildEntityMetadata();
		addEntityMetadata();
		addChildEntityAttribute();
		getEntityMetadata(childEntityType);
		getEntityMetadata(entityType);
		modifyEntityMetadata();
		createEntity();
		modifyEntity();
		searchEntity();
		deleteEntity();
		removeEntityMetadata(entityType);
		removeEntityMetadata(childEntityType);

	}



	private void addChildEntityAttribute() throws NoSuchEntityException,
			DuplicateAttributeDefinitionException, UnknownAttributeException {
		childAttributeName = "RESP";

		HashMap<String, Map<String, String>> metadataAttachments = new HashMap<String, Map<String, String>>();
		Map<String, String> prop = new HashMap<String, String>();
		prop.put("entitlement", "true");
		metadataAttachments.put("properties", prop);

		AttributeDefinition attributeDefinition = new AttributeDefinition(
				childAttributeName, childEntityType, null, false, true, false,
				null, "Basic", new HashMap<String, Map<String, String>>());
		mgrConfig.addChildEntityAttributeDefinition(entityType,
				attributeDefinition);

	}

	private void modifyEntityMetadata() throws Exception {
		AttributeDefinition attributeDefinition = new AttributeDefinition(
				"udf1", "string", null, false, true, false, null, "Basic",
				new HashMap<String, Map<String, String>>());
		mgrConfig.addEntityAttributeDefinition(entityType, attributeDefinition);
	}

	public void addChildEntityMetadata() throws Exception {

		childEntityType = java.util.UUID.randomUUID().toString()
				.replaceAll("-", "").toUpperCase();

		EntityMetadata entity = new EntityMetadata(childEntityType, true);
		entity.setRepositoryInstance("OperationalDB");
		entity.setProviderType("RDBMSChildDataProvider");

		Map<String, List<String>> providerParams = new HashMap<String, List<String>>();
		providerParams.put("table",
				Arrays.asList(new String[] { "APPLICATION_SUBOBJECT" }));
		providerParams.put("id_column", Arrays.asList(new String[] { "ID" }));
		providerParams.put("id_type", Arrays.asList(new String[] { "guid" }));
		providerParams.put("parent_id_column",
				Arrays.asList(new String[] { "PARENT_ID" }));
		providerParams.put("optimistic_locking",
				Arrays.asList(new String[] { "false" }));
		providerParams.put("flex_searchable_string_prefix",
				Arrays.asList(new String[] { "I_VC" }));
		providerParams.put("flex_non_searchable_string_prefix",
				Arrays.asList(new String[] { "U_VC" }));
		providerParams.put("flex_searchable_date_prefix",
				Arrays.asList(new String[] { "I_DT" }));
		providerParams.put("flex_non_searchable_date_prefix",
				Arrays.asList(new String[] { "U_DT" }));
		providerParams.put("flex_searchable_number_prefix",
				Arrays.asList(new String[] { "I_NUM" }));
		providerParams.put("flex_non_searchable_number_prefix",
				Arrays.asList(new String[] { "U_NUM" }));
		providerParams.put("flex_blob_prefix",
				Arrays.asList(new String[] { "U_BLOB" }));

		entity.setProviderParameters(providerParams);
		entity.setContainer(false);

		HashMap<String, Map<String, String>> metadataAttachments = new HashMap<String, Map<String, String>>();
		AttributeDefinition attrDef = null;
		FieldDefinition fieldDef = null;

		// parameters: name, type, description, isRequired, isSearchable, isMLS,
		// defaultValue, groupName, metadataAttachments

		attrDef = new AttributeDefinition("responsibility", "string", null,
				true, true, false, null, "Basic", metadataAttachments);
		entity.addAttribute(attrDef);
		
		attrDef = new AttributeDefinition("startDate", "date", null, true,
				true, false, null, "Basic", metadataAttachments);
		entity.addAttribute(attrDef);
		
		attrDef = new AttributeDefinition("endDate", "date", null, true, false,
				false, null, "Basic", metadataAttachments);
		entity.addAttribute(attrDef);

		String xmlString = getStringfromDoc(entity);

		try {
			mgrConfig.createEntityMetadata(entity);
			System.out.println("Created child entity type: " + childEntityType);
		} catch (Exception e) {
			//fail("Unexpected exception: " + getStackTrace(e));
			e.printStackTrace();
		}
	}

	public void addEntityMetadata() throws Exception {

		//entityType = java.util.UUID.randomUUID().toString().replaceAll("-", "")
		//		.toUpperCase();

		EntityMetadata entity = new EntityMetadata(entityType, false);
		entity.setRepositoryInstance("OperationalDB");
		entity.setProviderType("RDBMSDataProvider");

		Map<String, List<String>> providerParams = new HashMap<String, List<String>>();
		providerParams.put("table",
				Arrays.asList(new String[] { "APPLICATION_OBJECT" }));
		providerParams.put("id_column", Arrays.asList(new String[] { "ID" }));
		providerParams.put("id_type", Arrays.asList(new String[] { "guid" }));
		providerParams.put("optimistic_locking",
				Arrays.asList(new String[] { "false" }));
		providerParams.put("flex_searchable_string_prefix",
				Arrays.asList(new String[] { "I_VC" }));
		providerParams.put("flex_non_searchable_string_prefix",
				Arrays.asList(new String[] { "U_VC" }));
		providerParams.put("flex_searchable_date_prefix",
				Arrays.asList(new String[] { "I_DT" }));
		providerParams.put("flex_non_searchable_date_prefix",
				Arrays.asList(new String[] { "U_DT" }));
		providerParams.put("flex_searchable_number_prefix",
				Arrays.asList(new String[] { "I_NUM" }));
		providerParams.put("flex_non_searchable_number_prefix",
				Arrays.asList(new String[] { "U_NUM" }));
		providerParams.put("flex_blob_prefix",
				Arrays.asList(new String[] { "U_BLOB" }));

		entity.setProviderParameters(providerParams);
		entity.setContainer(false);

		HashMap<String, Map<String, String>> metadataAttachments = new HashMap<String, Map<String, String>>();
		Map<String, String> prop = new HashMap<String, String>();
		prop.put("isEncrypted", "false");
		metadataAttachments.put("properties", prop);
		AttributeDefinition attrDef = null;
		FieldDefinition fieldDef = null;

		// parameters: name, type, description, isRequired, isSearchable, isMLS,
		// defaultValue, groupName, metadataAttachments

		attrDef = new AttributeDefinition("givenName", "string", null, true,
				true, false, null, "Basic", metadataAttachments);
		entity.addAttribute(attrDef);
		
		attrDef = new AttributeDefinition("lastName", "string", null, true,
				true, false, null, "Basic", metadataAttachments);
		entity.addAttribute(attrDef);
		
		attrDef = new AttributeDefinition("email", "string", null, true,
				true, false, null, "Basic", metadataAttachments);
		entity.addAttribute(attrDef);
		
		attrDef = new AttributeDefinition("startDate", "date", null, false,
				true, false, null, "Basic", metadataAttachments);
		entity.addAttribute(attrDef);
		
		attrDef = new AttributeDefinition("endDate", "date", null, false,
				true, false, null, "Basic", metadataAttachments);
		entity.addAttribute(attrDef);
		
		attrDef = new AttributeDefinition("employeeNo", "number", null, true,
				true, false, null, "Basic", metadataAttachments);
		entity.addAttribute(attrDef);


		attrDef = new AttributeDefinition("__UID__", "string", null, true,
				true, false, null, "Basic", metadataAttachments);
		entity.addAttribute(attrDef);
		fieldDef = new FieldDefinition("AO_UID", "string",true);
		entity.addField(fieldDef);
		entity.addAttributeMapping("__UID__", "AO_UID");
		
		attrDef = new AttributeDefinition("__NAME__", "string", null, true,
				true, false, null, "Basic", metadataAttachments);
		entity.addAttribute(attrDef);
		fieldDef = new FieldDefinition("AO_NAME", "string",true);
		entity.addField(fieldDef);
		entity.addAttributeMapping("__NAME__", "AO_NAME");

		/*
		 * attrDef = new AttributeDefinition(childEntityType, childEntityType,
		 * null, false, true, false, null, "Basic", metadataAttachments);
		 * entity.addChildEntityAttribute(attrDef);
		 */

		String xmlString = getStringfromDoc(entity);

		try {
			mgrConfig.createEntityMetadata(entity);
			System.out.println("Created entity type: " + entityType);

		} catch (Exception e) {
			//fail("Unexpected exception: " + getStackTrace(e));
			e.printStackTrace();
		}
	}

	public void getEntityMetadata(String entityType) {
		EntityMetadata entityMetaData;
		try {
			entityMetaData = mgrConfig.getEntityMetadata(entityType);
			//assertNotNull(entityMetaData);
			System.out.println(getStringfromDoc(entityMetaData));
			if (!entityMetaData.isChildEntity()) {
				//assertTrue(entityMetaData
				//		.getChildEntityAttribute(childAttributeName).getType()
				//		.equals(childEntityType));
			}
		} catch (Exception e) {
			//fail("Unexpected exception: " + getStackTrace(e));
			e.printStackTrace();
		}

	}

	public void createEntity() throws Exception {

		System.out.println("Creating an entity...");
		PlatformTransactionManager txMgr = (PlatformTransactionManager) SpringBeanFactory
				.getBean("txManager");
		TransactionStatus txStatus = txMgr
				.getTransaction(new DefaultTransactionDefinition());
		HashMap<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("APP_OC_ID", entityType);
		attrs.put("__UID__", UUID.randomUUID().toString());
		attrs.put("__NAME__", "john.doe");
		attrs.put("AO_OWNER", UUID.randomUUID().toString());
		attrs.put("AO_ACCOUNT_TYPE", "what type?");
		attrs.put("AO_STATUS", "Provisioned");
		attrs.put("PROV_POLICY_ID", "1");
		attrs.put("PROV_REQUEST_ID", "2");
		attrs.put("PROVISIONED_BY", UUID.randomUUID().toString());
		attrs.put("PROVISIONED_BY_MECH", "Direct Provisioned");
		attrs.put("CREATED_BY", UUID.randomUUID().toString());
		// attrs.put("LAST_UPDATED_BY","something");
		attrs.put("givenName", "John");
		attrs.put("lastName", "Doe");
		attrs.put("email", "john.doe@oracle.com");
		attrs.put("udf1", "user defined field1");
		// date fields
		// attrs.put("CREATE_DATE","something");
		// attrs.put("LAST_UPDATE_DATE","something");
		// attrs.put("RECON_LAST_READ_FRM_APPLN","something");
		Calendar c = Calendar.getInstance();
		c.set(2008, 1, 17, 0, 0, 0);
		Date d = c.getTime();

		attrs.put("startDate", d);
		// attrs.put("endDate",d);
		// number fields
		// attrs.put("RECON_SITUATION","something");
		attrs.put("employeeNo", new Long(12345));

		Entity e = null;
		try {
			e = mgr.createEntity(entityType, attrs);
		} catch (MissingRequiredAttributeException ex) {
			ex.printStackTrace();
		}

		//assertTrue(e != null);
		//assertTrue(e.getID() != null);
		//assertTrue(e.getAttribute("givenName").equals("John"));
		//assertTrue(e.getAttribute("udf1").equals("user defined field1"));

		id = e.getID();

		System.out.println(e);

		txMgr.commit(txStatus);

	}
	
	public void deleteEntity() {
		System.out.println("Deleting an entity...");
		PlatformTransactionManager txMgr = (PlatformTransactionManager) SpringBeanFactory
				.getBean("txManager");
		TransactionStatus txStatus = txMgr
				.getTransaction(new DefaultTransactionDefinition());
	
		try {
			mgr.deleteEntity(entityType, id, true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	
		txMgr.commit(txStatus);
		
		
	}

	public void modifyEntity() throws Exception {

		System.out.println("Modifying an entity...");
		PlatformTransactionManager txMgr = (PlatformTransactionManager) SpringBeanFactory
				.getBean("txManager");
		TransactionStatus txStatus = txMgr
				.getTransaction(new DefaultTransactionDefinition());
		HashMap<String, Object> attrs = new HashMap<String, Object>();

		attrs.put("givenName", "John2");
		attrs.put("lastName", "Doe2");
		attrs.put("email", "john2.doe2@oracle.com");

		Calendar c = Calendar.getInstance();
		c.set(2009, 1, 17, 0, 0, 0);
		Date d = c.getTime();

		attrs.put("startDate", d);
		attrs.put("endDate", d);
		attrs.put("employeeNo", new Long(123456));

		Entity e = null;
		try {
			e = mgr.modifyEntity(entityType, id, attrs);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		//assertTrue(e != null);
		//assertTrue(e.getID() != null);
		//assertTrue(e.getAttribute("givenName").equals("John2"));
		//assertTrue(e.getAttribute("email").equals("john2.doe2@oracle.com"));

		System.out.println(e);

		txMgr.commit(txStatus);

	}

	public void searchEntity() throws Exception {
		System.out.println("Searching for an entity...");

		SearchCriteria sc1 = new SearchCriteria("givenName", "John2",
				SearchCriteria.Operator.EQUAL);
		Calendar c = Calendar.getInstance();
		c.set(2008, 1, 1, 0, 0, 0);
		Date d = c.getTime();
		SearchCriteria sc2 = new SearchCriteria("endDate", d,
				SearchCriteria.Operator.GREATER_THAN);
		SearchCriteria sc = new SearchCriteria(sc1, sc2,
				SearchCriteria.Operator.AND);

		List<Entity> es = mgr.findEntities(entityType, sc, null, 1, 100,
				"givenName", null);

		//assertTrue(es != null);
		//assertTrue(es.size() == 1);
		//assertTrue(es.get(0).getAttribute("givenName").equals("John2"));
		//assertTrue(es.get(0).getAttribute("email")
		//.equals("john2.doe2@oracle.com"));

	}

	public String getStackTrace(Throwable e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	public void removeEntityMetadata(String entityType) {
		try {
			mgrConfig.removeEntityMetadata(entityType);
			System.out.println("Removed " + entityType);
		} catch (Throwable e) {
			//fail("Unexpected exception: " + getStackTrace(e));
			e.printStackTrace();
		}
	}

	private String getStringfromDoc(EntityMetadata entityMetaData)
			throws Exception {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(entityMetaData.generateXml());
		StringWriter xmlStringWriter = new StringWriter();
		StreamResult result = new StreamResult(xmlStringWriter);
		transformer.transform(source, result);
		xmlStringWriter.close();
		return xmlStringWriter.toString();
	}

}
