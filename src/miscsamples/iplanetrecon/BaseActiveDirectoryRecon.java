package miscsamples.iplanetrecon;

import Thor.API.Operations.tcOrganizationOperationsIntf;
import Thor.API.Operations.tcReconciliationOperationsIntf;
import Thor.API.Operations.tcUserOperationsIntf;

public abstract class BaseActiveDirectoryRecon {
    public BaseActiveDirectoryRecon() 
    {
        
    }
    
    public BaseActiveDirectoryRecon(IPlanetReconTask reconTask)
    {
        theReconTask = reconTask;
        usrIntf = (tcUserOperationsIntf)theReconTask
            .getUtilityOps("Thor.API.Operations.tcUserOperationsIntf");
        orgIntf = (tcOrganizationOperationsIntf)theReconTask
            .getUtilityOps("Thor.API.Operations.tcOrganizationOperationsIntf");
        reconIntf = (tcReconciliationOperationsIntf)theReconTask
        	.getUtilityOps("Thor.API.Operations.tcReconciliationOperationsIntf");
    }
    
    public abstract void performReconciliation(String lastUSN) throws GenericADReconException;
    
   
    /**
     * @return Returns the theReconTask.
     */
    public IPlanetReconTask getTheReconTask() 
    {
        return theReconTask;
    }
    
    /**
     * @param theReconTask The theReconTask to set.
     */
    public void setTheReconTask(IPlanetReconTask theReconTask) 
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
    
    protected IPlanetReconTask theReconTask;
    protected tcOrganizationOperationsIntf orgIntf;
    protected tcReconciliationOperationsIntf reconIntf;
    protected tcUserOperationsIntf usrIntf;

}
