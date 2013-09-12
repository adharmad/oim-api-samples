package miscsamples.activedirectory;

import java.util.Vector;

import javax.naming.directory.Attribute;

public class OrgElem {
    
    public OrgElem(boolean isRoot)
    {
        subOrgs = new Vector();
        root = isRoot;
    }
    
    public String toString()
    {
        StringBuffer sbuf = new StringBuffer();
        
        sbuf.append(name + " --> " + parentOrg + "\n");
        
        for (int i=0 ; i<subOrgs.size() ; i++)
        {
            OrgElem elem = (OrgElem)subOrgs.elementAt(i);
            sbuf.append(elem.toString());
        }
        
        return new String(sbuf);
    }
    
    public void addToTree()
    {
        
    }
    
    /**
     * @return Returns the attr.
     */
    public Attribute getAttr() {
        return attr;
    }
    /**
     * @param attr The attr to set.
     */
    public void setAttr(Attribute attr) {
        this.attr = attr;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the subOrgs.
     */
    public Vector getSubOrgs() {
        return subOrgs;
    }
    /**
     * @param subOrgs The subOrgs to set.
     */
    public void setSubOrgs(Vector subOrgs) {
        this.subOrgs = subOrgs;
    }
    
    /**
     * @return Returns the parentOrg.
     */
    public String getParentOrg() {
        return parentOrg;
    }
    /**
     * @param parentOrg The parentOrg to set.
     */
    public void setParentOrg(String parentOrg) {
        this.parentOrg = parentOrg;
    }
    /**
     * @return Returns the root.
     */
    public boolean isRoot() {
        return root;
    }
    /**
     * @param root The root to set.
     */
    public void setRoot(boolean root) {
        this.root = root;
    }

    private String parentOrg;
    private boolean root;
    private String name;
    private Attribute attr;
    private Vector subOrgs;

}
