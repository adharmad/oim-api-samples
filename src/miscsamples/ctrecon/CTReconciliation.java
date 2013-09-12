package miscsamples.ctrecon;

import Thor.API.Operations.tcOrganizationOperationsIntf;
import Thor.API.Operations.tcReconciliationOperationsIntf;
import Thor.API.Operations.tcUserOperationsIntf;

public class CTReconciliation {
    private CTReconTask theReconTask;
    private tcOrganizationOperationsIntf orgIntf;
    private tcReconciliationOperationsIntf reconIntf;
    private tcUserOperationsIntf usrIntf;
    
    public static final String ORGNAME_METADATA = "Organization Name";
    public static final String USRROLE_METADATA = "Role";
    public static final String USRTYPE_METADATA = "Xellerate Type";


    public CTReconciliation() {
        super();
    }
    
    public CTReconciliation(CTReconTask reconTask) {
        this();
        
        theReconTask = reconTask;
        usrIntf = (tcUserOperationsIntf)theReconTask
            .getUtilityOps("Thor.API.Operations.tcUserOperationsIntf");
        orgIntf = (tcOrganizationOperationsIntf)theReconTask
            .getUtilityOps("Thor.API.Operations.tcOrganizationOperationsIntf");
        reconIntf = (tcReconciliationOperationsIntf)theReconTask
            .getUtilityOps("Thor.API.Operations.tcReconciliationOperationsIntf");
        
    }

    public void performReconciliation(String timeStamp) throws GenericCTReconException
    {
        if (theReconTask.getReconAttrs().getWhichEntity().equalsIgnoreCase("User"))
        {
            performUserReconciliation(timeStamp);
        }
        else if (theReconTask.getReconAttrs().getWhichEntity().equalsIgnoreCase("Group"))
        {
            performGroupReconciliation(timeStamp);
        }
        else
        {
            throw new GenericCTReconException("Bad Entity for Reconciliation");
        }
    }
    
    public void performUserReconciliation(String timeStamp) throws GenericCTReconException
    {
        
    }
    
    public void performGroupReconciliation(String timeStamp) throws GenericCTReconException
    {
        
    }    
    
    
    /**
     * @return Returns the theReconTask.
     */
    public CTReconTask getTheReconTask() 
    {
        return theReconTask;
    }
    
    /**
     * @param theReconTask The theReconTask to set.
     */
    public void setTheReconTask(CTReconTask theReconTask) 
    {
        this.theReconTask = theReconTask;
    }

    /**
     * @return Returns the orgIntf.
     */
    public tcOrganizationOperationsIntf getOrgIntf() 
    {
        return orgIntf;
    }
    /**
     * @param orgIntf The orgIntf to set.
     */
    public void setOrgIntf(tcOrganizationOperationsIntf orgIntf) 
    {
        this.orgIntf = orgIntf;
    }
    /**
     * @return Returns the usrIntf.
     */
    public tcUserOperationsIntf getUsrIntf() 
    {
        return usrIntf;
    }
    /**
     * @param usrIntf The usrIntf to set.
     */
    public void setUsrIntf(tcUserOperationsIntf usrIntf) 
    {
        this.usrIntf = usrIntf;
    }    

    /**
     * @return Returns the reconIntf.
     */
    public tcReconciliationOperationsIntf getReconIntf() 
    {
        return reconIntf;
    }
    
    /**
     * @param reconIntf The reconIntf to set.
     */
    public void setReconIntf(tcReconciliationOperationsIntf reconIntf) 
    {
        this.reconIntf = reconIntf;
    }

}
