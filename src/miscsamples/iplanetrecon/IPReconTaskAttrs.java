package miscsamples.iplanetrecon;

import java.util.StringTokenizer;
import java.util.Vector;

public class IPReconTaskAttrs {
    public IPReconTaskAttrs() {
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
        deleteRecon = false;
        maintainHierarchy = false;
        processOrgs = false;
        batchSize = 10;
        multiValAttrs = null;
    }
    
    public String toString()
    {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("Server: " + server + "\n");
        sbuf.append("Object: " + objectName + "\n");
        sbuf.append("KeyField: " + keyField + "\n");
        sbuf.append("Xellerate Organization: " + xellerateOrg + "\n");
        sbuf.append("deleteRecon: " + deleteRecon + "\n");
        sbuf.append("Maintain Hierarchy: " + maintainHierarchy + "\n");
        sbuf.append("processOrgs: " + processOrgs + "\n");
        sbuf.append("Batch Size: " + batchSize + "\n");
        sbuf.append("Use Field Mappings: " + useFieldMapping + "\n");
        sbuf.append("Field Lookup Code: " + fieldLookupCode + "\n");
        sbuf.append("Use Transform Mappings: " + useTransformMapping + "\n");
        sbuf.append("Transform Lookup Code: " + transformLookupCode + "\n");
        sbuf.append("MultiValue Attributes: " + multiValAttrs + "\n");
        
        return new String(sbuf);
    }

    /**
     * mva is a string that contains comma-delimited attributes (from active 
     * directory) that are "multi-valued" and need to be reconciled into
     * Xellerate.
     * 
     * We use a string tokenizer and parse it adding the tokens into multiValAttrs
     * Vector.
     * 
     * @param mva
     */
    public void parseAndSetMultiValAttrs(String mva)
    {
        // The vector is created here because testing whether it is null is 
        // a way of "knowing" that there are no multi-valued attributes 
        multiValAttrs = new Vector();
        
        StringTokenizer stok = new StringTokenizer(mva, ",\n");
        
        while (stok.hasMoreTokens())
        {
            multiValAttrs.add(stok.nextToken());
        }
        
    }
    
    /**
     * Checks if this attribute is in the multiValAttrs Vector
     * 
     * @param attr
     * @return
     */
    public boolean isMultiValAttribute(String attr)
    {
        if (multiValAttrs == null)
        {
            return false;
        }
        else if (multiValAttrs.contains(attr))
        {
            return true;
        } 
        else
        {
            return false;
        }
    }
    
    /**
     * @return Returns the batchSize.
     */
    public int getBatchSize() {
        return batchSize;
    }
    
    /**
     * @param batchSize The batchSize to set.
     */
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
    
    /**
     * @return Returns the deleteRecon.
     */
    public boolean isDeleteRecon() {
        return deleteRecon;
    }
    
    /**
     * @param deleteRecon The deleteRecon to set.
     */
    public void setDeleteRecon(boolean deleteRecon) {
        this.deleteRecon = deleteRecon;
    }
    
    /**
     * @return Returns the keyField.
     */
    public String getKeyField() {
        return keyField;
    }
    
    /**
     * @param keyField The keyField to set.
     */
    public void setKeyField(String keyField) {
        this.keyField = keyField;
    }
    
    /**
     * @return Returns the maintainHierarchy.
     */
    public boolean isMaintainHierarchy() {
        return maintainHierarchy;
    }
    
    /**
     * @param maintainHierarchy The maintainHierarchy to set.
     */
    public void setMaintainHierarchy(boolean maintainHierarchy) {
        this.maintainHierarchy = maintainHierarchy;
    }
    
    /**
     * @return Returns the objectName.
     */
    public String getObjectName() {
        return objectName;
    }
    
    /**
     * @param objectName The objectName to set.
     */
    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
    
    /**
     * @return Returns the processOrgs.
     */
    public boolean isProcessOrgs() {
        return processOrgs;
    }
    
    /**
     * @param processOrgs The processOrgs to set.
     */
    public void setProcessOrgs(boolean processOrgs) {
        this.processOrgs = processOrgs;
    }
    
    /**
     * @return Returns the server.
     */
    public String getServer() {
        return server;
    }
    
    /**
     * @param server The server to set.
     */
    public void setServerName(String serverName) {
        this.server = serverName;
    }
    
    /**
     * @return Returns the xellerateOrg.
     */
    public String getXellerateOrg() {
        return xellerateOrg;
    }
    
    /**
     * @param xellerateOrg The xellerateOrg to set.
     */
    public void setXellerateOrg(String xellerateOrg) {
        this.xellerateOrg = xellerateOrg;
    }


    /**
     * @return Returns the fieldLookupCode.
     */
    public String getFieldLookupCode() {
        return fieldLookupCode;
    }
    
    /**
     * @param fieldLookupCode The fieldLookupCode to set.
     */
    public void setFieldLookupCode(String fieldLookupCode) {
        this.fieldLookupCode = fieldLookupCode;
    }
    
    /**
     * @return Returns the transformLookupCode.
     */
    public String getTransformLookupCode() {
        return transformLookupCode;
    }
    
    /**
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
     * @param useTransformMapping The useTransformMapping to set.
     */
    public void setUseTransformMapping(boolean useTransformMapping) {
        this.useTransformMapping = useTransformMapping;
    }    

    /**
     * @return Returns the multiValAttrs.
     */
    public Vector getMultiValAttrs() {
        return multiValAttrs;
    }
    /**
     * @param multiValAttrs The multiValAttrs to set.
     */
    public void setMultiValAttrs(Vector multiValAttrs) {
        this.multiValAttrs = multiValAttrs;
    }
    
    private String server;    
    private String objectName;
    private boolean useFieldMapping;
    private boolean useTransformMapping;
    private String fieldLookupCode;
    private String transformLookupCode;
    private String keyField;
    private String xellerateOrg;
    private boolean deleteRecon;
    private boolean maintainHierarchy;
    private boolean processOrgs;
    private int batchSize;
    private Vector multiValAttrs;
}
