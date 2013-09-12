package miscsamples.ctrecon;

import Thor.API.Base.tcUtilityOperationsIntf;
import Thor.API.Exceptions.tcAPIException;

import com.thortech.util.logging.Logger;
import com.thortech.xl.scheduler.tasks.SchedulerBaseTask;
import com.thortech.xl.util.logging.LoggerModules;

public class CTReconTask extends SchedulerBaseTask {
    // Private Variables
    private CTController ctCntrl;
    private CTReconTaskAttrs reconAttrs;
    private CTITRes ctItRes;
    private CTLookupMaps lookupMaps;
    public static Logger logger = Logger.getLogger(LoggerModules.XL_SCHEDULER_TASKS);


    // Execute Method
    public void execute() 
    {
        
        initialize();

        System.out.println("After initialization, IT resource params are:");
        System.out.println(ctItRes.toString());
        
        System.out.println("After initialization, Task attributes params are:");
        System.out.println(reconAttrs.toString());
        
        System.out.println("Starting execute of CT Reconciliation");
        logger.debug("Starting execution of CT reconciliation");
        
        CTReconciliation ctRecon = new CTReconciliation(this);
        
        try
        {
            ctRecon.performReconciliation(ctItRes.getTimeStamp());
            
            System.out.println("Time Stamp before updating is " + ctItRes.getTimeStamp());
            logger.debug("Time Stamp before updating is " + ctItRes.getTimeStamp());
            ctItRes.updateTimeStamp();
            
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }

        System.out.println("Ending execute of CT Reconciliation");
        
        
    }
    
    public void initialize() 
    {

        System.out.println("Starting initialize of AD Reconciliation");
        logger.debug("Starting initialize of AD reconciliation");

        reconAttrs = new CTReconTaskAttrs();
        ctItRes = new CTITRes(this);
        lookupMaps = new CTLookupMaps(this);
        
        // Get the reconciliation task attributes
        getReconTaskAttributes();

        // Get server properties
        // Initialize the AD IT Resource instance parameters
        ctItRes.initialize(reconAttrs.getServer());
        
        // Initialize the LDAP Controller
        try
        {
            ctCntrl = new CTController(ctItRes);
        } 
        catch (Exception e)
        {
            System.out.println("Exception occured while instantiating LDAPController");
            logger.fatal("Exception occured while instantiating LDAPController");
            e.printStackTrace();
        }
        
        System.out.println("Ending initialize of AD Reconciliation");
        logger.debug("Ending initialize of AD reconciliation");

    }

    // Exposing super class getUtility() method which seems to be private
    public tcUtilityOperationsIntf getUtilityOps(String utilityName) 
    {
        try 
        {
            return (tcUtilityOperationsIntf)super.getUtility(utilityName);
        } 
        catch (tcAPIException e) 
        {
            e.printStackTrace();
        }
        return null;
    }
    
    protected void getReconTaskAttributes() 
    {
        try 
        {
            // These are required fields
            reconAttrs.setServer((String) getAttribute("Server"));
            reconAttrs.setObjectName((String) getAttribute("Object"));
            reconAttrs.setKeyField((String) getAttribute("KeyField"));
            reconAttrs.setXellerateOrg((String) getAttribute("XellerateOrg"));

            // These are non-required fields
            String ufm =  (String) getAttribute("UseFieldMapping");
            String utm =  (String) getAttribute("UseTransformMapping");
            String bs = (String) getAttribute("BatchSize");
            String mva = (String) getAttribute("MultiValueAttributes");
            
            if (ufm != null && ufm.equalsIgnoreCase("true")) 
            {
                reconAttrs.setUseFieldMapping(true);
                reconAttrs.setFieldLookupCode(
                    (String) getAttribute("FieldLookupCode"));
                lookupMaps.setFieldLookupCode(reconAttrs.getFieldLookupCode());
                lookupMaps.initializeFieldMaps();
            } 

            if (utm != null && utm.equalsIgnoreCase("true")) 
            {
                reconAttrs.setUseTransformMapping(true);
                reconAttrs.setTransformLookupCode(
                    (String) getAttribute("TransformLookupCode"));
                lookupMaps.setTransformLookupCode(reconAttrs.getTransformLookupCode());
                lookupMaps.initializeTransformMaps();
            } 
            
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
        
    }
    
    /**
     * @return Returns the ctItRes.
     */
    public CTITRes getCtItRes() 
    {
        return ctItRes;
    }
    /**
     * @param ctItRes The ctItRes to set.
     */
    public void setCtItRes(CTITRes adItRes) 
    {
        this.ctItRes = ctItRes;
    }
    /**
     * @return Returns the ctCntrl.
     */
    public CTController getCTCntrl() 
    {
        return ctCntrl;
    }
    /**
     * @param ctCntrl The ctCntrl to set.
     */
    public void setLdapCntrl(CTController ctCntrl) 
    {
        this.ctCntrl = ctCntrl;
    }
    /**
     * @return Returns the lookupMaps.
     */
    public CTLookupMaps getLookupMaps() 
    {
        return lookupMaps;
    }
    /**
     * @param lookupMaps The lookupMaps to set.
     */
    public void setLookupMaps(CTLookupMaps lookupMaps) 
    {
        this.lookupMaps = lookupMaps;
    }
    /**
     * @return Returns the reconAttrs.
     */
    public CTReconTaskAttrs getReconAttrs() 
    {
        return reconAttrs;
    }
    /**
     * @param reconAttrs The reconAttrs to set.
     */
    public void setReconAttrs(CTReconTaskAttrs reconAttrs) 
    {
        this.reconAttrs = reconAttrs;
    }
    
}
