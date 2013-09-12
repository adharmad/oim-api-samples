package miscsamples.iplanetrecon;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;

import com.thortech.util.logging.Logger;
import com.thortech.xl.util.logging.LoggerModules;

import Thor.API.tcResultSet;

public class IPlanetRecon extends BaseActiveDirectoryRecon {
    public static final String ORGNAME_METADATA = "Organization Name";

    public static final String USRROLE_METADATA = "Role";

    public static final String USRTYPE_METADATA = "Xellerate Type";

    // This is the default value of USN attribute value in the Active Directory.
    // Whenever
    // this is changed by the field mapping, in the hash table containing user
    // attributes,
    // this class variable should be modified to reflect the new "key" as the
    // value of
    // USN changed attribute dictates a lot
    public static String usnAttributeName = "uSNChanged";

    public IPlanetRecon() {
        super();
    }

    public IPlanetRecon(IPlanetReconTask reconTask) {
        super(reconTask);
    }

    public void performReconciliation(String lastUSN)
            throws GenericADReconException {

        NamingEnumeration nenum = null;
        Vector vector = new Vector();
        int recordCounter = 0;
        LDAPController ldapCntrl = theReconTask.getLdapCntrl();
        int batchSize = theReconTask.getReconAttrs().getBatchSize();

        System.out.println("Beginning search of AD.");
        IPlanetReconTask.logger.debug("Beginning search of AD");

        int startingUSN = Integer.parseInt(lastUSN);
        int endIntervalUSN = startingUSN;

        try {

            String rootDSEhighestCommitedUSN = theReconTask.getLdapCntrl()
                    .getRootDSEProperty("highestCommittedUSN");

            System.out.println("\n\n\n\nhighestCommittedUSN==="
                    + rootDSEhighestCommitedUSN);
            IPlanetReconTask.logger.debug("highestCommittedUSN==="
                    + rootDSEhighestCommitedUSN);

            int highestUSNChanged = Integer.parseInt(rootDSEhighestCommitedUSN);

            // Perform organization reconciliation first
            if (theReconTask.getReconAttrs().isMaintainHierarchy())
            {
                performOrgReconciliation(startingUSN, highestUSNChanged);
            }
            
            String[] adAttrArray = null;

            if (theReconTask.getReconAttrs().isUseFieldMapping()) {
                adAttrArray = theReconTask.getLookupMaps().getADFieldsArray();
            }

            endIntervalUSN += batchSize;

            do {
                String query = "(&(&(uSNChanged>=" + startingUSN
                        + ")(uSNChanged<=" + endIntervalUSN
                        + "))(objectclass=organizationalPerson))";

                nenum = theReconTask.getLdapCntrl().searchResult("", query,
                        adAttrArray);

                if ((nenum != null) && (nenum.hasMore())) {
                    for (NamingEnumeration ne = nenum; ne.hasMore();) {
                        SearchResult searchresult = (SearchResult) ne.next();
                        recordCounter++;
                        vector.addElement(searchresult);
                        System.out.println(searchresult.getAttributes());
                    }

                    System.out.println("\n\n******SENDING TO PROCESS "
                            + vector.size() + " records, recordCounter="
                            + recordCounter);
                    processBatch(vector);
                    vector = null;
                    vector = new Vector();
                } else {
                    System.out
                            .println("\n\n******DID NOT FIND ANY RECORDS between usn= "
                                    + startingUSN
                                    + " and usn="
                                    + endIntervalUSN);
                    IPlanetReconTask.logger.debug(
                            "******DID NOT FIND ANY RECORDS between usn= "
                            + startingUSN
                            + " and usn="
                            + endIntervalUSN);
                }

                startingUSN = (endIntervalUSN + 1);
                endIntervalUSN = (startingUSN + batchSize);

            } while ((startingUSN < highestUSNChanged)
                    || ((nenum != null) && (nenum.hasMore())));

        } catch (Exception e) {
            System.out
                    .println("***************Exception occured while doing AD search.");
            IPlanetReconTask.logger.fatal("***************Exception occured while doing AD search.");
            e.printStackTrace();
        }
        System.out.println("The latestUSN before error occured="
                + theReconTask.getAdItRes().getLastUSN());
        IPlanetReconTask.logger.fatal("The latestUSN before error occured="
                + theReconTask.getAdItRes().getLastUSN());

    }

    public void processBatch(Vector bulkVector) {
        String lastUSN = new String();

        System.out.println("Starting processBatch()");
        IPlanetReconTask.logger.debug("Starting processBatch()");

        try {

            int msChangeNumber = bulkVector.size();

            for (int i = 0; i < msChangeNumber; i++) {
                SearchResult sr = (SearchResult) bulkVector.elementAt(i);
                String msRDN = sr.getName();

                Attributes attr = sr.getAttributes();

                System.out.println("For user number " + i);
                System.out.println("Attributes are: " + attr);
                IPlanetReconTask.logger.debug("Reconciling user with attributes: " + attr);

                Hashtable usrHash = getHashtableFromAttributes(attr);

                System.out.println("Hashtable is: " + usrHash);
                IPlanetReconTask.logger.debug("User hashtable is: " + usrHash);

                lastUSN = processUserChange(usrHash);

                if (Integer.valueOf(lastUSN).intValue() > Integer.valueOf(
                        theReconTask.getAdItRes().getLastUSN()).intValue()) {
                    theReconTask.getAdItRes().setLastUSN(new String(lastUSN));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Leaving processBatch()");
        IPlanetReconTask.logger.debug("Leaving processBatch()");

    }

    private String processUserChange(Hashtable usrAttrs) throws Exception {

        System.out.println("USN key = " + usnAttributeName);

        System.out.println("USN value = " + usrAttrs.get(usnAttributeName));
        IPlanetReconTask.logger.debug("USN value = " + usrAttrs.get(usnAttributeName));

        String result = (new Integer((Integer.valueOf(
                (String) usrAttrs.get(usnAttributeName)).intValue() - 1)))
                .toString();

        // Modify the usrAttrs hashtable and ensure that there are no
        // multi-valued
        // fields in it.
        Hashtable multiValAttrs = modifyUserAttrs(usrAttrs);

        try {
            long rceKey;

            String uid = (String) usrAttrs.get("uid");

            // Add recon even here
            // Recon event can be "regular" or delete recon event

            if (!theReconTask.getReconAttrs().isDeleteRecon()) 
            {
                if (multiValAttrs != null) 
                {
                    // Multi valued attributes present
                    rceKey = reconIntf.createReconciliationEvent(theReconTask
                            .getReconAttrs().getObjectName(), usrAttrs, false);

                    Enumeration e = multiValAttrs.keys();

                    while (e.hasMoreElements()) 
                    {
                        String key = (String) e.nextElement();
                        Vector v = (Vector) multiValAttrs.get(key);

                        for (int i = 0; i < v.size(); i++) {
                            HashMap hm = new HashMap();
                            hm.put(key, v.elementAt(i));
                            System.out.println("adding multiAttribute data: "
                                    + hm);
                            reconIntf.addMultiAttributeData(rceKey, key, hm);
                        }
                    }

                    reconIntf.finishReconciliationEvent(rceKey);
                } 
                else 
                {
                    // Multi valued attributes absent
                    rceKey = reconIntf.createReconciliationEvent(theReconTask
                            .getReconAttrs().getObjectName(), usrAttrs, true);
                }
            } 
            else 
            {
                // Delete reconciliation
                rceKey = reconIntf.createDeleteReconciliationEvent(theReconTask
                        .getReconAttrs().getObjectName(), usrAttrs);
            }

            System.out.println("Created reconciliation event " + rceKey
                    + " for user " + uid);
            IPlanetReconTask.logger.debug("Created reconciliation event " + rceKey
                    + " for user " + uid);

        } catch (Exception e) {
            System.out.println("\n\nError within PROCESSUSERCHANGE");
            IPlanetReconTask.logger.fatal("Error within PROCESSUSERCHANGE");
            
            e.printStackTrace();
        }

        return result;
    }

    public void performOrgReconciliation(int startingUSN, int highestUSNChanged) {
        int batchSize = theReconTask.getReconAttrs().getBatchSize();
        System.out.println("Beginning org reconciliation");
        IPlanetReconTask.logger.debug("Beginning org reconciliation");
        

        NamingEnumeration nenum = null;
        long rceKey = 0;
        
        try {
            String query = "(&(&(uSNChanged>=" + startingUSN + ")(uSNChanged<="
                    + highestUSNChanged + "))(objectclass=organizationalUnit))";

            
            nenum = theReconTask.getLdapCntrl().searchResult("", query, null);
            
            IPOrgElem tree = new IPOrgElem(true);
            tree.setName(LDAPController.getOrgName(
                    theReconTask.getLdapCntrl().getRootContext()));
            
            Vector tmpVector = new Vector();
            tmpVector.add(tree);

            if ((nenum != null) && (nenum.hasMore())) 
            {
                for (NamingEnumeration ne = nenum; ne.hasMore();) 
                {
                    SearchResult searchresult = (SearchResult) ne.next();
                    
                    Attributes attr = searchresult.getAttributes();
                    System.out.println("Attributes are: " + attr);
                    IPlanetReconTask.logger.debug("Org attributes are: " + attr);

                    Hashtable orgHash = getOrgHashtableFromAttributes(attr);

                    System.out.println("Hashtable is: " + orgHash);
                    IPlanetReconTask.logger.debug("Org Hash is: " + orgHash);
                    
                    String orgName = (String)orgHash.get("name");
                    String parentName = (String)orgHash.get("parent");
                    
                    // Create an IPOrgElem which is not the "root
                    IPOrgElem o = new IPOrgElem(false);
                    o.setName(orgName);
                    o.setParentOrg(parentName);
                    tmpVector.add(o);
                    
                }
                
                // Ensure that we have the actual Organization tree before we
                // start creating organizations
                for (int i=0 ; i<tmpVector.size() ; i++)
                {
                    IPOrgElem elem = (IPOrgElem)tmpVector.elementAt(i);
                    
                    for (int j=1 ; j<tmpVector.size() ; j++)
                    {
                        IPOrgElem elem2 = (IPOrgElem) tmpVector.elementAt(j);
                        if (!elem2.isRoot() && elem2.getParentOrg().equals(elem.getName()))
                        {
                            System.out.println(elem2.getName() + " " + elem.getName());
                            elem.getSubOrgs().add(elem2);
                        }
                    }
                }

                System.out.println("Tree is as follows: ");
                System.out.println(tree.toString());
                IPlanetReconTask.logger.debug("Organization tree is as follows: ");
                IPlanetReconTask.logger.debug(tree.toString());
                
                
                createOrgHierarchy(tree);
                
            } else {
                System.out
                        .println("\n\n******DID NOT FIND ANY ORGANIZATION RECORDS between usn= "
                                + startingUSN + " and usn=" + highestUSNChanged);
                IPlanetReconTask.logger.debug("******DID NOT FIND ANY ORGANIZATION RECORDS between usn= "
                        + startingUSN + " and usn=" + highestUSNChanged);
            }

        } catch (Exception e) {
            System.out
                    .println("Error occured while Organization reconciliation");
            IPlanetReconTask.logger.fatal("Error occured while Organization reconciliation");
            e.printStackTrace();
        }

        System.out.println("Ending org reconciliation");
        IPlanetReconTask.logger.debug("Ending org reconciliation");

    }
    
    public void createOrgHierarchy(IPOrgElem tree)
    {
        String orgName = tree.getName();
        String parent = tree.getParentOrg();
        
        // Check if the organization exists in xellerate
        HashMap orgMap = new HashMap();
        orgMap.put("Organizations.Organization Name", orgName);
        tcResultSet orgRS = null;
        boolean orgExists = false;
        long orgKey = 0;
        
        try
        {
            orgRS = orgIntf.findOrganizations(orgMap);
            
            if (orgRS.isEmpty()) 
            {
                orgExists = false;
            } 
            else if (orgRS.getRowCount() > 1) 
            {
                throw new Exception("Multiple organizations found");
            }
            else 
            {
                orgRS.goToRow(0);
                String val = orgRS.getStringValue("Organizations.Organization Name");
                orgKey = orgRS.getLongValue("Organizations.Key");
                if (val.equals(orgName))
                {
                    orgExists = true;
                }
            }
            
            // If org exists, update it, otherwise create it
            if (orgExists)
            {
                orgMap.put("Organizations.Type", "Company");
                if (!tree.isRoot())
                {
                    //orgMap.put("Organizations.Parent Name", parent);
                    long parentKey = getOrgKey(parent);
                    orgMap.put("Organizations.Parent Key", parentKey + "");
                }
                orgIntf.updateOrganization(orgKey, orgMap);
                System.out.println("Organization " + orgName + " with key " +
                    orgKey + " updated");
            }
            else
            {
                orgMap.put("Organizations.Type", "Company");
                if (!tree.isRoot())
                {
                    //orgMap.put("Organizations.Parent Name", parent);
                    long parentKey = getOrgKey(parent);
                    orgMap.put("Organizations.Parent Key", parentKey + "");                    
                }
                orgKey = orgIntf.createOrganization(orgMap);
                System.out.println("Organization " + orgName + " with key " +
                    orgKey + " created");
            }
            
            // Now go ahead and update all the child organizations
            Vector subOrgs = tree.getSubOrgs();
            if (subOrgs == null || subOrgs.size() == 0)
            {
                return;
            }
            else
            {
                for (int i=0 ; i<subOrgs.size() ; i++)
                {
                    IPOrgElem orgElem = (IPOrgElem)subOrgs.elementAt(i);
                    createOrgHierarchy(orgElem);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    public long getOrgKey(String orgName)
    {
        HashMap orgMap = new HashMap();
        orgMap.put("Organizations.Organization Name", orgName);
        tcResultSet orgRS = null;
        long orgKey = 0;
        
        try
        {
            orgRS = orgIntf.findOrganizations(orgMap);
            
            if (orgRS.isEmpty() || (orgRS.getRowCount() > 1)) 
            {
                throw new Exception("Error in findOrganizations().");
            } 
            else 
            {
                orgRS.goToRow(0);
                String val = orgRS.getStringValue("Organizations.Organization Name");
                if (val.equals(orgName))
                {
                    orgKey = orgRS.getLongValue("Organizations.Key");
                    return orgKey;
                }
            }        
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return orgKey;
    }
    
    public Hashtable getOrgHashtableFromAttributes(Attributes attributes) 
   		throws Exception 
    {
        // Get the hashtable from attributes
        Hashtable orgAttrs = theReconTask.getLdapCntrl()
                .getHashtableFromAttributes(attributes);
        
        Hashtable retAttrs = new Hashtable();
        
        // Get the organization name
        retAttrs.put("name", orgAttrs.get("name"));
        
        // Get the parent organization name
        String dn = (String)orgAttrs.get("distinguishedName");
        System.out.println("distinguished name -> " + dn);
        String parent = LDAPController.getParentOrg(dn);
        retAttrs.put("parent", parent);
        retAttrs.put("distinguishedName", dn);
        
        // Hard code the type
        retAttrs.put("type", "Company");
        
        return retAttrs;

    }

    /**
     * Return a hash table containing multi valued attributes. Also remove them
     * from the usrAttrs hashtable
     * 
     * @param usrAttrs
     * @return
     */
    public Hashtable modifyUserAttrs(Hashtable usrAttrs) {
        Hashtable multiValAttrs = new Hashtable();

        Vector v = theReconTask.getReconAttrs().getMultiValAttrs();

        if (v != null) {

            for (int i = 0; i < v.size(); i++) {
                String attr = (String) v.elementAt(i);
                Vector val = (Vector) usrAttrs.get(attr);

                multiValAttrs.put(attr, val);
                usrAttrs.remove(attr);
            }

            return multiValAttrs;
        } else {
            return null;
        }
    }

    /**
     * This function works in teh following way -> o If useFieldMapping = false -
     * Call getHashtableFromAttributes() method of LDAPController o If
     * useFieldMapping = true - Call getHashtableFromAttributes() method of
     * LDAPController - Modify the hashtable and using fieldMaps from Lookup
     * tables o If useTransformMapping = false - Return the hashtable obtained
     * above "as is" o If useTransformMapping = true - Transform all the values
     * of in the hashtable using transformation specific to the key. It is the
     * user's responsibility to ensure that the right keys in the hashtable are
     * provided in the transform map. - Return the hash table with the values
     * transformed
     * 
     * Also, we need to add a few fields that are "required" by Xellerate. They
     * are: - organization name - xellerate type - role
     * 
     * @param attributes
     * @return
     * @throws Exception
     */
    private Hashtable getHashtableFromAttributes(Attributes attributes)
            throws Exception {
        // Get the hashtable from attributes
        Hashtable usrAttrs = theReconTask.getLdapCntrl()
                .getHashtableFromAttributes(attributes);

        // We put these in by default. Later on, these can be modified by either
        // the field mapping or transform mapping
        if (theReconTask.getReconAttrs().isProcessOrgs() || theReconTask.getReconAttrs().isMaintainHierarchy()) 
        {
            String dn = (String)usrAttrs.get("distinguishedName");
            String orgName = LDAPController.getUsrParentOrg(dn);
            usrAttrs.put(ORGNAME_METADATA, orgName);
            
            // Process the single organization only if processOrgs is set. Otherwise if its
            // maintainHierarchy, it will be handled in organization reconciliation.
            if (theReconTask.getReconAttrs().isProcessOrgs()) 
            {
                processSingleOrg(orgName);
            }
        }
        else
        {
            usrAttrs.put(ORGNAME_METADATA, theReconTask.getReconAttrs()
                    .getXellerateOrg());            
        }
        
        usrAttrs.put(USRROLE_METADATA, "Full-Time");
        usrAttrs.put(USRTYPE_METADATA, "End-User");

        if (theReconTask.getReconAttrs().isUseFieldMapping()) {
            Hashtable fieldHash = theReconTask.getLookupMaps().getFieldMap();
            Hashtable tmpAttrs = new Hashtable();

            Enumeration keyEnum = fieldHash.keys();

            while (keyEnum.hasMoreElements()) {
                String key = (String) keyEnum.nextElement();
                String newKey = (String) fieldHash.get(key);
                String value = null;
                Vector valVector = null;

                if (theReconTask.getReconAttrs().isMultiValAttribute(newKey)) {
                    valVector = (Vector) usrAttrs.get(key);
                    tmpAttrs.put(newKey, value);
                } else {
                    value = (String) usrAttrs.get(key);
                    tmpAttrs.put(newKey, value);
                }

                System.out.println("OldKey -> " + key + " New Key -> " + newKey
                        + " Value " + value);

                // keep track of where we are storing the Last Checked USN
                // attribute
                if (key.equals(usnAttributeName)) {
                    usnAttributeName = newKey;
                }
            }

            usrAttrs = tmpAttrs;
        }

        // Note:
        // Should add code to modify multi-valued attributes
        if (theReconTask.getReconAttrs().isUseTransformMapping()) {
            Hashtable transformHash = theReconTask.getLookupMaps()
                    .getTransformMap();
            Enumeration keyEnum = transformHash.keys();

            while (keyEnum.hasMoreElements()) {
                String key = (String) keyEnum.nextElement();
                String className = (String) transformHash.get(key);
                String oldValue = (String) usrAttrs.get(key);

                Class cls = Class.forName(className);
                Object o = cls.newInstance();
                Method m = cls.getMethod("transform",
                        new Class[] { java.lang.String.class });
                Object o1 = m.invoke(o, new Object[] { oldValue });

                String newValue = (String) o1;

                System.out
                        .println("key = " + key + "Class name = " + className);

                System.out.println("key -> " + key + " OldValue -> " + oldValue
                        + " NewValue " + newValue);

                usrAttrs.put(key, newValue);
            }
        }

        return usrAttrs;
    }

    
    /**
     * Check if the organization exists in xellerate and if it does not, create it
     * 
     */
    public void processSingleOrg(String orgName)
    {
        // Check if organization exists
        HashMap orgMap = new HashMap();
        orgMap.put("Organizations.Organization Name", orgName);
        tcResultSet orgRS = null;
        boolean found = false;
        
        try
        {
            orgRS = orgIntf.findOrganizations(orgMap);
            
            if (orgRS.isEmpty()) 
            {
                found = false;
            } 
            else if (orgRS.getRowCount() > 1) 
            {
                throw new Exception("Multiple organizations found");
            }
            else 
            {
                orgRS.goToRow(0);
                String val = orgRS.getStringValue("Organizations.Organization Name");
                if (val.equals(orgName))
                {
                    found = true;
                }
            }
            
            if (!found)
            {
                orgMap.put("Organizations.Type", "Company");
                long orgKey = orgIntf.createOrganization(orgMap);
                System.out.println("Organization " + orgName + " with key " +
                    orgKey + " created");
            }     
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
