package miscsamples.activedirectory;

import java.util.HashMap;
import java.util.Vector;

import com.thortech.xl.integration.ActiveDirectory.tcUtilADTasks;

public class TestCreateUser {
	public static void main(String[] args) throws Exception {
		try {
			tcUtilADTasks ad = new tcUtilADTasks("host",
					"ou=miniluv,dc=tql04ad,dc=com",
					"cn=Administrator,cn=Users,dc=tql04ad,dc=com", "password",
					true);

			HashMap hm = new HashMap();

			hm.put("cn", "test1");
			hm.put("userPrincipalName", "test1");
			hm.put("sAMAccountName", "test1");
			hm.put("givenName", "testfirst1");
			hm.put("sn", "testlast1");
			hm.put("middlename", "p");
			hm.put("displayName", "testfirst1 testlast1");
			hm.put("unicodePwd", "password1");

			ad.fielddatamap = hm;

			String path = getParentHierarchyString("temporg1");

			System.out.println(path);

			ad.createUser(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getParentHierarchyString(String psOrgName) {
		System.out.println("psOrgName   " + psOrgName);
		Vector moResult = new Vector();
		String[] orgarr = { "OU", psOrgName };
		moResult.add("OU");
		moResult.add(orgarr);
		System.out.println("msResult   ");
		System.out.println(moResult);
		String msResult = getPathFromVector(moResult);
		System.out.println("msResult   " + msResult);
		return msResult;
	}

	public static String getPathFromVector(Vector hierarchy) {
		// System.out.println("hierarchy "+hierarchy.toString());
		StringBuffer hierString = new StringBuffer();

		for (int i = 0; i < hierarchy.size(); i += 2) {
			if (i > 0) {
				hierString.append(",");
			}

			String[] elem = (String[]) hierarchy.elementAt(i + 1);
			hierString.append(elem[0]);
			hierString.append("=");
			hierString.append(elem[1]);
		}

		// System.out.println("hierString.toString() "+hierString.toString());
		return hierString.toString();
	}

}
