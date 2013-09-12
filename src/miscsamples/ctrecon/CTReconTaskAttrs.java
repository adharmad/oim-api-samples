package miscsamples.ctrecon;

public class CTReconTaskAttrs {

    private String server;    
    private String objectName;
    private boolean useFieldMapping;
    private boolean useTransformMapping;
    private String fieldLookupCode;
    private String transformLookupCode;
    private String keyField;
    private String xellerateOrg;
    private String whichEntity;
    
    //private boolean deleteRecon;
    //private boolean maintainHierarchy;
    //private boolean processOrgs;
    //private int batchSize;
    //private Vector multiValAttrs;
    
    public CTReconTaskAttrs() {
        super();
        
        // default values
        server = "";    
        objectName = "";
        useFieldMapping = false;
        useTransformMapping = false;
        fieldLookupCode = "";
        transformLookupCode = "";
        keyField = "";
        xellerateOrg = "";
        whichEntity = "";
    }
    
    public String toString()
    {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("Server: " + server + "\n");
        sbuf.append("Object: " + objectName + "\n");
        sbuf.append("KeyField: " + keyField + "\n");
        sbuf.append("Xellerate Organization: " + xellerateOrg + "\n");
        sbuf.append("Use Field Mappings: " + useFieldMapping + "\n");
        sbuf.append("Field Lookup Code: " + fieldLookupCode + "\n");
        sbuf.append("Use Transform Mappings: " + useTransformMapping + "\n");
        sbuf.append("Transform Lookup Code: " + transformLookupCode + "\n");
        sbuf.append("Which Entity: " + whichEntity + "\n");
        
        return new String(sbuf);
    }    
    
    /**
     * @return Returns the fieldLookupCode.
     */
    public String getFieldLookupCode() {
        return fieldLookupCode;
    }
    
    /**
     * Sets the value of the fieldLookupCode.
     * 
     * @param fieldLookupCode The fieldLookupCode to set.
     */
    public void setFieldLookupCode(String fieldLookupCode) {
        this.fieldLookupCode = fieldLookupCode;
    }
    
    /**
     * @return Returns the keyField.
     */
    public String getKeyField() {
        return keyField;
    }
    
    /**
     * Sets the value of the keyField.
     * 
     * @param keyField The keyField to set.
     */
    public void setKeyField(String keyField) {
        this.keyField = keyField;
    }
    
    /**
     * @return Returns the objectName.
     */
    public String getObjectName() {
        return objectName;
    }
    
    /**
     * Sets the value of the objectName.
     * 
     * @param objectName The objectName to set.
     */
    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
    
    /**
     * @return Returns the server.
     */
    public String getServer() {
        return server;
    }
    
    /**
     * Sets the value of the server.
     * 
     * @param server The server to set.
     */
    public void setServer(String server) {
        this.server = server;
    }
    
    /**
     * @return Returns the transformLookupCode.
     */
    public String getTransformLookupCode() {
        return transformLookupCode;
    }
    
    /**
     * Sets the value of the transformLookupCode.
     * 
     * @param transformLookupCode The transformLookupCode to set.
     */
    public void setTransformLookupCode(String transformLookupCode) {
        this.transformLookupCode = transformLookupCode;
    }
    
    /**
     * @return Returns the useFieldMapping.
     */
    public boolean isUseFieldMapping() {
        return useFieldMapping;
    }
    
    /**
     * Sets the value of the useFieldMapping.
     * 
     * @param useFieldMapping The useFieldMapping to set.
     */
    public void setUseFieldMapping(boolean useFieldMapping) {
        this.useFieldMapping = useFieldMapping;
    }
    
    /**
     * @return Returns the useTransformMapping.
     */
    public boolean isUseTransformMapping() {
        return useTransformMapping;
    }
    
    /**
     * Sets the value of the useTransformMapping.
     * 
     * @param useTransformMapping The useTransformMapping to set.
     */
    public void setUseTransformMapping(boolean useTransformMapping) {
        this.useTransformMapping = useTransformMapping;
    }
    
    /**
     * @return Returns the whichEntity.
     */
    public String getWhichEntity() {
        return whichEntity;
    }
    
    /**
     * Sets the value of the whichEntity.
     * 
     * @param whichEntity The whichEntity to set.
     */
    public void setWhichEntity(String whichEntity) {
        this.whichEntity = whichEntity;
    }
    
    /**
     * @return Returns the xellerateOrg.
     */
    public String getXellerateOrg() {
        return xellerateOrg;
    }
    
    /**
     * Sets the value of the xellerateOrg.
     * 
     * @param xellerateOrg The xellerateOrg to set.
     */
    public void setXellerateOrg(String xellerateOrg) {
        this.xellerateOrg = xellerateOrg;
    }
}
