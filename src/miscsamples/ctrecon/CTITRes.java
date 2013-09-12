package miscsamples.ctrecon;

import java.util.HashMap;
import java.util.Hashtable;

import Thor.API.tcResultSet;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;

public class CTITRes {
    private String ctAdminGroup;
    private String ctAdminRole;
    private String ctAdminUserId;
    private String ctAdminPassword;
    private String caFileLocation;
    private String caPassword;
    private String defaultUserGroup;
    private String keyAlias;
    private String ksFileLocation;
    private String ksPassword;
    private String serverAddress;
    private String serverPort;
    private String privatePassword;
    private String sslMode;
    private String timeOut;
    private String timeStamp;
    
    private long svrKey;
    private CTReconTask theReconTask;
    private tcITResourceInstanceOperationsIntf itIntf;
    
    public static final String COL_NAME = "IT Resources Type Parameter.Name";
    public static final String COL_VAL = "IT Resources Type Parameter Value.Value";

    public static final int CLEAR = 0;
    public static final int SSL_AUTH = 1;
    public static final int SSL_CERT = 2;
    
    public CTITRes() 
    {
        super();
    }
    
    public CTITRes(String adminGroup, String adminRole, String adminUser, String adminPassword,
            String caFileLocation, String caPassword, String defaultUserGroup, String keyAlias,
            String ksFileLocation, String ksPassword, String serverAddress, String serverPort,
            String privatePassword, String sslMode, String timeOut, String timeStamp)
    {
        this.ctAdminGroup = adminGroup;
        this.ctAdminRole = adminRole;
        this.ctAdminUserId = adminUser;
        this.ctAdminPassword = adminPassword;
        this.caFileLocation = caFileLocation;
        this.caPassword = caPassword;
        this.defaultUserGroup = defaultUserGroup;
        this.keyAlias = keyAlias;
        this.ksFileLocation = ksFileLocation;
        this.ksPassword = ksPassword;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.privatePassword = privatePassword;
        this.sslMode = sslMode;
        this.timeOut = timeOut;
        this.timeStamp = timeStamp;
    }
    
    public CTITRes(CTReconTask reconTask) 
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
                
                if (key.trim().equals("CTAdmin Group"))
                {
                    ctAdminGroup = set.getStringValue(COL_VAL);
                } 
                else if (key.trim().equals("CTAdmin Role"))
                {
                    ctAdminRole = set.getStringValue(COL_VAL);
                }
                else if (key.trim().equals("CTAdminPassword"))
                {
                    ctAdminPassword = set.getStringValue(COL_VAL);
                }
                else if (key.trim().equals("CTAdminUserId"))
                {
                    ctAdminUserId = set.getStringValue(COL_VAL);
                }
                else if (key.trim().equals("CaFileLocation"))
                {
                    caFileLocation = set.getStringValue(COL_VAL);
                }
                else if (key.trim().equals("CaPassword"))
                {
                    caPassword = set.getStringValue(COL_VAL);
                }
                else if (key.trim().equals("Default User Group"))
                {
                    defaultUserGroup = set.getStringValue(COL_VAL);
                }
                else if (key.trim().equals("KeyAlias"))
                {
                    keyAlias = set.getStringValue(COL_VAL);
                }
                else if (key.trim().equals("KsFileLocation"))
                {
                    ksFileLocation = set.getStringValue(COL_VAL);
                }
                else if (key.trim().equals("KsPassword"))
                {
                    ksPassword = set.getStringValue(COL_VAL);
                }
                else if (key.trim().equals("MachineName/IPAddress"))
                {
                    serverAddress = set.getStringValue(COL_VAL);
                }
                else if (key.trim().equals("Port"))
                {
                    serverPort = set.getStringValue(COL_VAL);
                }
                else if (key.trim().equals("PrivatePassword"))
                {
                    privatePassword = set.getStringValue(COL_VAL);
                }
                else if (key.trim().equals("SSLMode"))
                {
                    sslMode = set.getStringValue(COL_VAL);
                }
                else if (key.trim().equals("TimeOut"))
                {
                    timeOut = set.getStringValue(COL_VAL);
                }
                else if (key.trim().equals("TimeStamp"))
                {
                    timeStamp = set.getStringValue(COL_VAL);
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
        sbuf.append("MachineName/IPAddress: " + serverAddress+ "\n");
        sbuf.append("Port: " + serverPort+ "\n");
        sbuf.append("CT Admin Group: " + ctAdminGroup+ "\n");
        sbuf.append("CT Admin Role: " + ctAdminRole+ "\n");
        sbuf.append("CT Admin UserId: " + ctAdminUserId+ "\n");        
        sbuf.append("CT Admin Password: " + ctAdminPassword+ "\n");
        sbuf.append("CaFileLocation: " + caFileLocation+ "\n");
        sbuf.append("CaPassword: " + caPassword+ "\n");
        sbuf.append("KsFileLocation: " + ksFileLocation+ "\n");
        sbuf.append("KsPassword: " + ksPassword+ "\n");
        sbuf.append("Default User Group: " + defaultUserGroup+ "\n");
        sbuf.append("KeyAlias: " + keyAlias+ "\n");
        sbuf.append("PrivatePassword: " + privatePassword+ "\n");
        sbuf.append("SSLMode: " + sslMode+ "\n");
        sbuf.append("TimeOut: " + timeOut+ "\n");
        sbuf.append("TimeStamp: " + timeStamp+ "\n");
        
        return new String(sbuf);
    }
    
    public void updateTimeStamp(String ts)
    {
        HashMap map = new HashMap();
        map.put("TimeStamp", ts);

        try 
        {
            itIntf.updateITResourceInstanceParameters(svrKey, map);
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // This method is called when the time stamp is already changed in this class
    // instance
    public void updateTimeStamp()
    {
        HashMap map = new HashMap();
        map.put("Last Checked USN", timeStamp);

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
     * @return Returns the caFileLocation.
     */
    public String getCaFileLocation() {
        return caFileLocation;
    }
    
    /**
     * Sets the value of the caFileLocation.
     * 
     * @param caFileLocation The caFileLocation to set.
     */
    public void setCaFileLocation(String caFileLocation) {
        this.caFileLocation = caFileLocation;
    }
    
    /**
     * @return Returns the caPassword.
     */
    public String getCaPassword() {
        return caPassword;
    }
    
    /**
     * Sets the value of the caPassword.
     * 
     * @param caPassword The caPassword to set.
     */
    public void setCaPassword(String caPassword) {
        this.caPassword = caPassword;
    }
    
    /**
     * @return Returns the ctAdminGroup.
     */
    public String getCtAdminGroup() {
        return ctAdminGroup;
    }
    
    /**
     * Sets the value of the ctAdminGroup.
     * 
     * @param ctAdminGroup The ctAdminGroup to set.
     */
    public void setCtAdminGroup(String ctAdminGroup) {
        this.ctAdminGroup = ctAdminGroup;
    }
    
    /**
     * @return Returns the ctAdminPassword.
     */
    public String getCtAdminPassword() {
        return ctAdminPassword;
    }
    
    /**
     * Sets the value of the ctAdminPassword.
     * 
     * @param ctAdminPassword The ctAdminPassword to set.
     */
    public void setCtAdminPassword(String ctAdminPassword) {
        this.ctAdminPassword = ctAdminPassword;
    }
    
    /**
     * @return Returns the ctAdminRole.
     */
    public String getCtAdminRole() {
        return ctAdminRole;
    }
    
    /**
     * Sets the value of the ctAdminRole.
     * 
     * @param ctAdminRole The ctAdminRole to set.
     */
    public void setCtAdminRole(String ctAdminRole) {
        this.ctAdminRole = ctAdminRole;
    }
    
    /**
     * @return Returns the ctAdminUserId.
     */
    public String getCtAdminUserId() {
        return ctAdminUserId;
    }
    
    /**
     * Sets the value of the ctAdminUserId.
     * 
     * @param ctAdminUserId The ctAdminUserId to set.
     */
    public void setCtAdminUserId(String ctAdminUserId) {
        this.ctAdminUserId = ctAdminUserId;
    }
    
    /**
     * @return Returns the defaultUserGroup.
     */
    public String getDefaultUserGroup() {
        return defaultUserGroup;
    }
    
    /**
     * Sets the value of the defaultUserGroup.
     * 
     * @param defaultUserGroup The defaultUserGroup to set.
     */
    public void setDefaultUserGroup(String defaultUserGroup) {
        this.defaultUserGroup = defaultUserGroup;
    }
    
    /**
     * @return Returns the keyAlias.
     */
    public String getKeyAlias() {
        return keyAlias;
    }
    
    /**
     * Sets the value of the keyAlias.
     * 
     * @param keyAlias The keyAlias to set.
     */
    public void setKeyAlias(String keyAlias) {
        this.keyAlias = keyAlias;
    }
    
    /**
     * @return Returns the ksFileLocation.
     */
    public String getKsFileLocation() {
        return ksFileLocation;
    }
    
    /**
     * Sets the value of the ksFileLocation.
     * 
     * @param ksFileLocation The ksFileLocation to set.
     */
    public void setKsFileLocation(String ksFileLocation) {
        this.ksFileLocation = ksFileLocation;
    }
    
    /**
     * @return Returns the ksPassword.
     */
    public String getKsPassword() {
        return ksPassword;
    }
    
    /**
     * Sets the value of the ksPassword.
     * 
     * @param ksPassword The ksPassword to set.
     */
    public void setKsPassword(String ksPassword) {
        this.ksPassword = ksPassword;
    }
    
    /**
     * @return Returns the privatePassword.
     */
    public String getPrivatePassword() {
        return privatePassword;
    }
    
    /**
     * Sets the value of the privatePassword.
     * 
     * @param privatePassword The privatePassword to set.
     */
    public void setPrivatePassword(String privatePassword) {
        this.privatePassword = privatePassword;
    }
    
    /**
     * @return Returns the serverAddress.
     */
    public String getServerAddress() {
        return serverAddress;
    }
    
    /**
     * Sets the value of the serverAddress.
     * 
     * @param serverAddress The serverAddress to set.
     */
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }
    
    /**
     * @return Returns the serverPort.
     */
    public String getServerPort() {
        return serverPort;
    }
    
    /**
     * Sets the value of the serverPort.
     * 
     * @param serverPort The serverPort to set.
     */
    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }
    /**
     * @return Returns the sslMode.
     */
    public String getSslMode() {
        return sslMode;
    }
    
    /**
     * Sets the value of the sslMode.
     * 
     * @param sslMode The sslMode to set.
     */
    public void setSslMode(String sslMode) {
        this.sslMode = sslMode;
    }
    
    /**
     * @return Returns the timeOut.
     */
    public String getTimeOut() {
        return timeOut;
    }
    
    /**
     * Sets the value of the timeOut.
     * 
     * @param timeOut The timeOut to set.
     */
    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }
    
    /**
     * @return Returns the timeStamp.
     */
    public String getTimeStamp() {
        return timeStamp;
    }
    
    /**
     * Sets the value of the timeStamp.
     * 
     * @param timeStamp The timeStamp to set.
     */
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
