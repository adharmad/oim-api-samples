package iamsamples.request;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import oracle.iam.api.OIMService;
import oracle.iam.platform.OIMClient;
import oracle.iam.platform.utils.vo.OIMType;
import oracle.iam.request.vo.Beneficiary;
import oracle.iam.request.vo.RequestBeneficiaryEntity;
import oracle.iam.request.vo.RequestBeneficiaryEntityAttribute;
import oracle.iam.request.vo.RequestConstants;
import oracle.iam.request.vo.RequestData;
import oracle.iam.vo.OperationResult;

public class CreateTemporalEntitlementProvisioningRequest {
    public static void main(String[] args) throws Exception {
        Hashtable env = new Hashtable();
        env.put("java.naming.provider.url", "t3://host:port/oim");
        env.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");

        OIMClient oimClient = new OIMClient(env);
        oimClient.login("xelsysadm", "Welcome1");

        OIMService unifiedService = oimClient.getService(OIMService.class);
        
        RequestData requestData = new RequestData();

        List<RequestBeneficiaryEntity> entities = new ArrayList<RequestBeneficiaryEntity>();
        
        String[] entityKeys = {"25"};
        RequestBeneficiaryEntity requestEntity = null;
        
        for (int i=0 ; i<entityKeys.length ; i++) {
        	requestEntity = new RequestBeneficiaryEntity();
        	requestEntity.setRequestEntityType(OIMType.Entitlement);
        	requestEntity.setEntitySubType("nidFileshare");
        	requestEntity.setEntityKey(entityKeys[i]); 
        	requestEntity.setOperation(RequestConstants.MODEL_PROVISION_ENTITLEMENT_OPERATION);
        	
            List<RequestBeneficiaryEntityAttribute> attrs = new ArrayList<RequestBeneficiaryEntityAttribute>();
            RequestBeneficiaryEntityAttribute attr = null; 

            attr = new RequestBeneficiaryEntityAttribute("Sunrise", new Date(113,10,1), RequestBeneficiaryEntityAttribute.TYPE.Date);
            attrs.add(attr);
            
            attr = new RequestBeneficiaryEntityAttribute("Sunset", new Date(114,10,1), RequestBeneficiaryEntityAttribute.TYPE.Date);
            attrs.add(attr);        
            
            requestEntity.setEntityData(attrs);        	
        	
        	entities.add(requestEntity);
        }
        
        Beneficiary beneficiary = new Beneficiary();
        
        String userKey = "14"; 
        beneficiary.setBeneficiaryKey(userKey);
        beneficiary.setBeneficiaryType(Beneficiary.USER_BENEFICIARY);        
        beneficiary.setTargetEntities(entities);
        
        List<Beneficiary> beneficiaries = new ArrayList<Beneficiary>();
        beneficiaries.add(beneficiary);
        requestData.setBeneficiaries(beneficiaries);
        
        OperationResult result = unifiedService.doOperation(requestData, OIMService.Intent.REQUEST);

        System.out.println("result = " + result.toString());
        
        System.exit(0);
    }



}
