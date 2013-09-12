package miscsamples.iplanetrecon;

import java.util.HashMap;
import java.util.Hashtable;

import Thor.API.tcResultSet;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;

public class IPITRes {
    public IPITRes() 
    {
        super();
    }
    
    public IPITRes(IPlanetReconTask reconTask) 
    {
        this();
        this.theReconTask = reconTask;
        
        itIntf = (tcITResourceInstanceOperationsIntf)theReconTask
        	.getUtilityOps("Thor.API.Operations.tcITResourceInstanceOperationsIntf");
    }
    
    public void initialize(String itResName) 
    {
        // Get the IT resource key
        try 
        {
            Hashtable map = new Hashtable();
            map.put("IT Resources.Name", itResName);
            tcResultSet set = itIntf.findITResourceInstances(map);
            set.goToRow(0);

            // Set the IT resource parameters
            svrKey = set.getLongValue("IT Resources.Key");
            
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
        // Now Get the IT resource parameter values
        try 
        {
            
            tcResultSet set = itIntf.getITResourceInstanceParameters(svrKey);
            
            for (int i=0 ; i<set.getRowCount() ; i++)
            {
                set.goToRow(i);
               
                String key = set.getStringValue(COL_NAME);
                
                if (key.trim().equals("Server Address"))
                {
                    serverAddress = set.getStringValue(COL_VAL);
                } 
                else if (key.trim().equals("Root Context"))
                {
                    rootContext = set.getStringValue(COL_VAL);
                }
                else if (key.trim().equals("Admin FQDN"))
                {
                    adminFQDN = set.getStringValue(COL_VAL);
                }
                else if (key.trim().equals("Admin Password"))
                {
                    password = set.getStringValue(COL_VAL);
                }
                else if (key.trim().equals("Admin Login"))
                {
                    adminLogin = set.getStringValue(COL_VAL);
                }
                else if (key.trim().equals("Last Checked USN"))
                {
                    lastUSN = set.getStringValue(COL_VAL);
                }
                else if (key.trim().equals("Domain"))
                {
                    domain = set.getStringValue(COL_VAL);
                }
            }
            
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
    }
    
    public String toString()
    {
        StringBuffer sbuf = new StringBuffer();
        
        sbuf.append("Key: " + svrKey + "\n");
        sbuf.append("Server Address: " + serverAddress+ "\n");
        sbuf.append("Root Context: " + rootContext+ "\n");
        sbuf.append("Admin FQDN: " + adminFQDN+ "\n");
        sbuf.append("Admin Login: " + adminLogin+ "\n");        
        sbuf.append("Admin Password: " + password+ "\n");
        sbuf.append("Last Checked USN: " + lastUSN+ "\n");
        sbuf.append("Domain: " + domain+ "\n");
        
        return new String(sbuf);
    }
    
    public void updateLastUSN(String lastUSNChanged)
    {
        HashMap map = new HashMap();
        map.put("Last Checked USN", lastUSNChanged);

        try 
        {
            itIntf.updateITResourceInstanceParameters(svrKey, map);
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // This method is called when the last usn is already changed in this class
    // instance
    public void updateLastUSN()
    {
        HashMap map = new HashMap();
        map.put("Last Checked USN", lastUSN);

        try 
        {
            itIntf.updateITResourceInstanceParameters(svrKey, map);
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }    
    
    
    /**
     * @return Returns the adminFQDN.
     */
    public String getAdminFQDN() 
    {
        return adminFQDN;
    }
    /**
     * @param adminFQDN The adminFQDN to set.
     */
    public void setAdminFQDN(String adminFQDN) 
    {
        this.adminFQDN = adminFQDN;
    }
    /**
     * @return Returns the adminLogin.
     */
    public String getAdminLogin() 
    {
        return adminLogin;
    }
    /**
     * @param adminLogin The adminLogin to set.
     */
    public void setAdminLogin(String adminLogin) 
    {
        this.adminLogin = adminLogin;
    }
    /**
     * @return Returns the domain.
     */
    public String getDomain() 
    {
        return domain;
    }
    /**
     * @param domain The domain to set.
     */
    public void setDomain(String domain) 
    {
        this.domain = domain;
    }
    /**
     * @return Returns the password.
     */
    public String getPassword() 
    {
        return password;
    }
    /**
     * @param password The password to set.
     */
    public void setPassword(String password) 
    {
        this.password = password;
    }
    /**
     * @return Returns the rootContext.
     */
    public String getRootContext() 
    {
        return rootContext;
    }
    /**
     * @param rootContext The rootContext to set.
     */
    public void setRootContext(String rootContext) 
    {
        this.rootContext = rootContext;
    }
    /**
     * @return Returns the serverAddress.
     */
    public String getServerAddress() 
    {
        return serverAddress;
    }
    /**
     * @param serverAddress The serverAddress to set.
     */
    public void setServerAddress(String serverAddress) 
    {
        this.serverAddress = serverAddress;
    }
    /**
     * @return Returns the svrKey.
     */
    public long getSvrKey() 
    {
        return svrKey;
    }
    /**
     * @param svrKey The svrKey to set.
     */
    public void setSvrKey(long svrKey) 
    {
        this.svrKey = svrKey;
    }

    /**
     * @return Returns the lastUSN.
     */
    public String getLastUSN() 
    {
        return lastUSN;
    }

    /**
     * @param lastUSN The lastUSN to set.
     */
    public void setLastUSN(String lastUSN) 
    {
        this.lastUSN = lastUSN;
    }
    
    private String adminFQDN;
    private String adminLogin;
    private String password;
    private String domain;
    private String rootContext;
    private String serverAddress;
    private String lastUSN;
    private long svrKey;
    private IPlanetReconTask theReconTask;
    private tcITResourceInstanceOperationsIntf itIntf;
    
    public static final String COL_NAME = "IT Resources Type Parameter.Name";
    public static final String COL_VAL = "IT Resources Type Parameter Value.Value";

    
}
