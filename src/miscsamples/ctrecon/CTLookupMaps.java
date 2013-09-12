package miscsamples.ctrecon;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import Thor.API.tcResultSet;
import Thor.API.Operations.tcLookupOperationsIntf;

public class CTLookupMaps {

    /**
     * <code>LOOKUP_CODE</code> corresponds to the metadata in Xellerate 
     * corresponding to the "Code" field in the Lookup definition.
     */
    public static final String LOOKUP_CODE = 
        "Lookup Definition.Lookup Code Information.Code Key";

    /**
     * <code>LOOKUP_DECODE</code> corresponds to the metadata in Xellerate 
     * corresponding to the "Decode" field in the Lookup definition.
     */
    public static final String LOOKUP_DECODE = 
        "Lookup Definition.Lookup Code Information.Decode";

    /**
     * <code>fieldLookupCode</code> is the lookup code for the AD attribute -
     * XL attribute map.
     */
    private String fieldLookupCode;
    
    /**
     * <code>transformLookupCode</code> is the lookup code for the AD attribute 
     * - transformation class map.
     */
    private String transformLookupCode;
    
    /**
     * <code>fieldMap</code> contains the AD attribute - XL attribute mapping.
     */
    private Hashtable fieldMap;
    
    /**
     * <code>transformMap</code> contains the AD attribute - transform class
     * mapping.
     */
    private Hashtable transformMap;
    
    /**
     * Xellerate tcLookupOperationsIntf interface.
     */
    private tcLookupOperationsIntf lookupIntf;
    

    /**
     * Default constructor. 
     * Initializes fieldMap and transformMap hashtables
     */
    public CTLookupMaps(CTReconTask reconTask) 
    {
        super();
        
        lookupIntf = (tcLookupOperationsIntf)reconTask
            .getUtilityOps("Thor.API.Operations.tcLookupOperationsIntf");
        
        fieldMap = new Hashtable();
        transformMap = new Hashtable();
    }
    
    /**
     * Initializes the fieldMap and transformMap using Lookup APIs and 
     * getting the values from the Xellerate database.
     * 
     * The AD attributes are stored as the "keys" in both hash tables. The
     * XL attributes and the transformation class name are stored as the 
     * values. 
     */
    public void initializeFieldMaps() 
    {
        tcResultSet lookupRS = null;
        int i=0;
        
        System.out.println("inside initializeFieldMaps");
        
        try 
        {
            lookupRS = lookupIntf.getLookupValues(fieldLookupCode);
            
            for (i=0 ; i<lookupRS.getRowCount() ; i++) 
            {
                lookupRS.goToRow(i);
                fieldMap.put(lookupRS.getStringValue(LOOKUP_CODE), 
                    lookupRS.getStringValue(LOOKUP_DECODE));
                
                System.out.println(lookupRS.getStringValue(LOOKUP_CODE) + " --> " + 
                        lookupRS.getStringValue(LOOKUP_DECODE));
            }
            
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
        System.out.println("leaving initializeFieldMaps");
    }
    
    
    public void initializeTransformMaps() 
    {
        tcResultSet transformRS = null;
        int i=0;
        
        try 
        {
            transformRS = lookupIntf.getLookupValues(transformLookupCode);
            
            for (i=0 ; i<transformRS.getRowCount() ; i++) 
            {
                transformRS.goToRow(i);
                transformMap.put(transformRS.getStringValue(LOOKUP_CODE), 
                    transformRS.getStringValue(LOOKUP_DECODE));
            }
            
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }    
    
    
    
    /**
     * @return A vector containing the Active Directory fields. 
     */
    public Vector getADFieldsVector() 
    {
        Enumeration e = fieldMap.keys();
        Vector adFields = new Vector();
        
        while (e.hasMoreElements()) 
        {
            adFields.add(e.nextElement());
        }
        
        return adFields;
    }
    
    /**
     * @return An array containing the Active Directory fields. 
     */
    public String[] getADFieldsArray() 
    {
        Object[] keyArray = fieldMap.keySet().toArray();
        
        String[] adFields = new String[keyArray.length];
        
        for (int i=0 ; i<keyArray.length ; i++)
        {
            adFields[i] = (String)keyArray[i];
        }
        
        return adFields;
    }    
    
    /**
     * @return Returns the fieldLookupCode.
     */
    public String getFieldLookupCode() 
    {
        return fieldLookupCode;
    }
    
    /**
     * @param fieldLookupCode The fieldLookupCode to set.
     */
    public void setFieldLookupCode(String fieldLookupCode) 
    {
        this.fieldLookupCode = fieldLookupCode;
    }
    
    /**
     * @return Returns the fieldMap.
     */
    public Hashtable getFieldMap() 
    {
        return fieldMap;
    }
    
    /**
     * @param fieldMap The fieldMap to set.
     */
    public void setFieldMap(Hashtable fieldMap) 
    {
        this.fieldMap = fieldMap;
    }
    
    /**
     * @return Returns the lookupOps.
     */
    public tcLookupOperationsIntf getLookupIntf() 
    {
        return lookupIntf;
    }
    
    /**
     * @param lookupOps The lookupOps to set.
     */
    public void setLookupIntf(tcLookupOperationsIntf lookupOps) 
    {
        lookupIntf = lookupOps;
    }
    
    /**
     * @return Returns the transformLookupCode.
     */
    public String getTransformLookupCode() 
    {
        return transformLookupCode;
    }
    
    /**
     * @param transformLookupCode The transformLookupCode to set.
     */
    public void setTransformLookupCode(String transformLookupCode) 
    {
        this.transformLookupCode = transformLookupCode;
    }
    
    /**
     * @return Returns the transformMap.
     */
    public Hashtable getTransformMap() 
    {
        return transformMap;
    }
    
    /**
     * @param transformMap The transformMap to set.
     */
    public void setTransformMap(Hashtable transformMap) 
    {
        this.transformMap = transformMap;
    }
}
