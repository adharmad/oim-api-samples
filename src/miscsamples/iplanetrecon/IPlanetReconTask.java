package miscsamples.iplanetrecon;

import Thor.API.Base.tcUtilityOperationsIntf;
import Thor.API.Exceptions.tcAPIException;

import com.thortech.util.logging.Logger;
import com.thortech.xl.scheduler.tasks.SchedulerBaseTask;
import com.thortech.xl.util.logging.LoggerModules;

public class IPlanetReconTask extends SchedulerBaseTask {

    // Execute Method
    public void execute() 
    {
        
        initialize();

        System.out.println("After initialization, IT resource params are:");
        System.out.println(adItRes.toString());
        
        System.out.println("After initialization, Task attributes params are:");
        System.out.println(reconAttrs.toString());
        
        System.out.println("Starting execute of AD Reconciliation");
        logger.debug("Starting execution of AD reconciliation");
        
        IPlanetRecon adRecon = new IPlanetRecon(this);
        try
        {
            adRecon.performReconciliation(adItRes.getLastUSN());
            
            System.out.println("last USN before updating is " + adItRes.getLastUSN());
            logger.debug("last USN before updating is " + adItRes.getLastUSN());
            adItRes.updateLastUSN();
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }

        System.out.println("Ending execute of AD Reconciliation");
        
        
    }
    
    public void initialize() 
    {

        System.out.println("Starting initialize of AD Reconciliation");
        logger.debug("Starting initialize of AD reconciliation");

        reconAttrs = new IPReconTaskAttrs();
        adItRes = new IPITRes(this);
        lookupMaps = new IPLookupMaps(this);
        
        // Get the reconciliation task attributes
        getReconTaskAttributes();

        // Get server properties
        // Initialize the AD IT Resource instance parameters
        adItRes.initialize(reconAttrs.getServer());
        
        // Initialize the LDAP Controller
        try
        {
            ldapCntrl = new LDAPController(adItRes, false);
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
            reconAttrs.setServerName((String) getAttribute("Server"));
            reconAttrs.setObjectName((String) getAttribute("Object"));
            reconAttrs.setKeyField((String) getAttribute("KeyField"));
            reconAttrs.setXellerateOrg((String) getAttribute("XellerateOrg"));
            String dr = (String) getAttribute("DeleteRecon");
            String mh = (String) getAttribute("MaintainHierarchy");
            String po = (String) getAttribute("ProcessOrgs");

            if (dr.equalsIgnoreCase("true")) 
            {
                reconAttrs.setDeleteRecon(true);
            } 
            
            if (mh.equalsIgnoreCase("true")) 
            {
                reconAttrs.setMaintainHierarchy(true);
            } 

            if (po.equalsIgnoreCase("true")) 
            {
                reconAttrs.setProcessOrgs(true);
            }            
            
            
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
            
            if (bs != null && !bs.trim().equals("")) 
            {
                reconAttrs.setBatchSize(Integer.parseInt(bs.trim()));
            }
            
            if (mva != null && !mva.trim().equals(""))
            {
                reconAttrs.parseAndSetMultiValAttrs(mva);
            }
            
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
        
    }
    
    /**
     * @return Returns the adItRes.
     */
    public IPITRes getAdItRes() 
    {
        return adItRes;
    }
    /**
     * @param adItRes The adItRes to set.
     */
    public void setAdItRes(IPITRes adItRes) 
    {
        this.adItRes = adItRes;
    }
    /**
     * @return Returns the ldapCntrl.
     */
    public LDAPController getLdapCntrl() 
    {
        return ldapCntrl;
    }
    /**
     * @param ldapCntrl The ldapCntrl to set.
     */
    public void setLdapCntrl(LDAPController ldapCntrl) 
    {
        this.ldapCntrl = ldapCntrl;
    }
    /**
     * @return Returns the lookupMaps.
     */
    public IPLookupMaps getLookupMaps() 
    {
        return lookupMaps;
    }
    /**
     * @param lookupMaps The lookupMaps to set.
     */
    public void setLookupMaps(IPLookupMaps lookupMaps) 
    {
        this.lookupMaps = lookupMaps;
    }
    /**
     * @return Returns the reconAttrs.
     */
    public IPReconTaskAttrs getReconAttrs() 
    {
        return reconAttrs;
    }
    /**
     * @param reconAttrs The reconAttrs to set.
     */
    public void setReconAttrs(IPReconTaskAttrs reconAttrs) 
    {
        this.reconAttrs = reconAttrs;
    }
    
    // Private Variables
    private LDAPController ldapCntrl;
    private IPReconTaskAttrs reconAttrs;
    private IPITRes adItRes;
    private IPLookupMaps lookupMaps;
    public static Logger logger = Logger.getLogger(LoggerModules.XL_SCHEDULER_TASKS);

}
