package miscsamples.activedirectory;

import java.util.Vector;

import com.thortech.xl.integration.ActiveDirectory.tcUtilADTasks;
import com.thortech.xl.util.adapters.tcUtilLDAPController;

public class AddUserToGroupAdapter {
	public static void main(String[] args) throws Exception {

		String server = "host";
		String rootCtx = "dc=exch552004,dc=com";
		String admin = "cn=Administrator,cn=Users,dc=exch552004,dc=com";
		String password = "password";
		boolean ssl = true;

		tcUtilADTasks tsk = new tcUtilADTasks(server, rootCtx, admin, password,
				ssl);
		System.out.println(tsk.checkConnection());
		// tsk.addUserToGroup("", "jail", "ou=testorg1, ou=helpgroup",
		// "gollum x. smeagol");

		// String hierarchy = "";
		String name = "jail";
		// String userhierarchy = "ou=testorg1,ou=helpgroup";
		String user = "gollum x. smeagol";

		// tcUtilLDAPController ad = tsk.getADController();

		// get group RDN adapter
		Vector v = tsk.getGroup("", name);
		System.out.println(v);

		// get empty vector
		Vector empty = new Vector();

		// get first element
		String s = (String) v.elementAt(0);
		System.out.println("s --> " + s);

		// get parent hierarchy vector
		tcUtilLDAPController ad = new tcUtilLDAPController(server, rootCtx,
				admin, password, ssl);
		Vector v1 = ad.getParentHierarchyVector(s, empty);
		System.out.println("v1 --> " + v1);

		// get path from vector
		String s1 = ad.getPathFromVector(v1);
		System.out.println("s1 --> " + s1);

		// get user RDN
		String s2 = tsk.getObjectByUSNCreated("40976");
		System.out.println("s2 --> " + s2);

		// get user hierarchy
		String s3 = tsk.getParentHierarchyDN(s2, "");
		System.out.println("s3 --> " + s3);

		// add user to group
		boolean b = tsk.addUserToGroup(s1, name, s3, user);
		System.out.println("b --> " + b);

	}
}
