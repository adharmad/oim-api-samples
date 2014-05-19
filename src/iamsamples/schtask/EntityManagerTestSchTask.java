package iamsamples.schtask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import oracle.iam.platform.Platform;
import oracle.iam.platform.entitymgr.EntityManager;
import oracle.iam.platform.entitymgr.MissingRequiredAttributeException;
import oracle.iam.platform.entitymgr.vo.Entity;
import oracle.iam.platform.tx.OIMTransactionCallbackWithoutResult;
import oracle.iam.platform.tx.OIMTransactionManager;
import oracle.iam.platform.tx.OIMTransactionResult;
import oracle.iam.platform.utils.SpringBeanFactory;
import oracle.iam.scheduler.vo.TaskSupport;

public class EntityManagerTestSchTask extends TaskSupport {
	EntityManager mgr;
	String entityType;
	
	@Override
	public void execute(HashMap attributes) throws Exception {
		entityType = (String)attributes.get("ENTITY_TYPE");
		mgr = Platform.getService(EntityManager.class);
		
		System.out.println("entity type = " + entityType);
		System.out.println("before createEntity call");
		createEntity();
		System.out.println("after createEntity call");
		
	}

	public void createEntity() throws Exception {

		System.out.println("Creating an entity...");

		OIMTransactionManager txManager = Platform.getOIMTransactionManager();
	    txManager.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

	    /*
		PlatformTransactionManager txMgr = (PlatformTransactionManager) SpringBeanFactory
				.getBean("txManager");
		DefaultTransactionDefinition defaultTxDef = new DefaultTransactionDefinition();
		defaultTxDef.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus txStatus = txMgr
				.getTransaction(defaultTxDef);
		*/
	    

		OIMTransactionResult txResult = txManager.oimExecute(new OIMTransactionCallbackWithoutResult() {
            @Override
            public void processWithoutResult(TransactionStatus txStatus) throws Throwable {
            	
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
        		// date fields
        		// attrs.put("CREATE_DATE","something");
        		// attrs.put("LAST_UPDATE_DATE","something");
        		// attrs.put("RECON_LAST_READ_FRM_APPLN","something");
        		Calendar c = Calendar.getInstance();
        		c.set(2008, 1, 17, 0, 0, 0);

        		attrs.put("startDate", new java.sql.Date(c.getTimeInMillis()));
        		// attrs.put("endDate",d);
        		// number fields
        		// attrs.put("RECON_SITUATION","something");
        		attrs.put("employeeNo", new Integer(12345));
            	
        		Entity e = null;
        		try {
        			
        			System.out.println("here1---------");
        			e = mgr.createEntity(entityType, attrs);
        			System.out.println("here2---------");
        			//txManager.commit(txStatus);
        			System.out.println("here3---------");
        		} catch (MissingRequiredAttributeException ex) {
        			System.out.println("here4---------");
        			ex.printStackTrace();
        			//txManager.rollback(txStatus);
        		}
        		
        		System.out.println("Created entity id = " + e.getID());
            	
        }});
        Throwable t = txResult.getException();
        if(t != null) {
            if(t instanceof Exception) {
                throw new Exception(t);
            }
        }		
		
	}
	
	
	@Override
	public HashMap getAttributes() {
		return null;
	}

	@Override
	public void setAttributes() {
		
	}

}
