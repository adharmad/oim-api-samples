package iamsamples.util;


import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.thortech.xl.dataobj.tcDataSet;
import com.thortech.xl.vo.AccessPolicyResourceData;

import Thor.API.tcResultSet;
import Thor.API.Operations.tcFormDefinitionOperationsIntf;
import Thor.API.Operations.tcGroupOperationsIntf;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;
import Thor.API.Operations.tcObjectOperationsIntf;
import Thor.API.Operations.tcUserOperationsIntf;

public class Utils {

	public static long getUserKey(tcUserOperationsIntf usrIntf, String usrLogin) {
		long usrKey = 0;
		try {
			HashMap hm = new HashMap();
			hm.put("Users.User ID", usrLogin);
			tcResultSet rs = usrIntf.findUsers(hm);
			rs.goToRow(0);
			usrKey = rs.getLongValue("Users.Key");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return usrKey;
	}

	public static long getGroupKey(tcGroupOperationsIntf grpIntf, String grpName) {
		long usrKey = 0;
		try {
			HashMap hm = new HashMap();
			hm.put("Groups.Group Name", grpName);
			tcResultSet rs = grpIntf.findGroups(hm);
			rs.goToRow(0);
			usrKey = rs.getLongValue("Groups.Key");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return usrKey;
	}

	public static long getObjectKey(tcObjectOperationsIntf objIntf,
			String objName) {
		long objKey = 0;
		try {
			HashMap hm = new HashMap();
			hm.put("Objects.Name", objName);
			tcResultSet rs = objIntf.findObjects(hm);
			rs.goToRow(0);
			objKey = rs.getLongValue("Objects.Key");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return objKey;
	}

	public static long getFormDefKey(tcFormDefinitionOperationsIntf fdIntf,
			String formName) {
		long childDefKey = 0;
		try {
			HashMap hm = new HashMap();
			hm.put("Structure Utility.Table Name", formName);
			tcResultSet rs = fdIntf.findForms(hm);
			rs.goToRow(0);
			childDefKey = rs.getLongValue("Structure Utility.Key");
			// printResultSet(rs);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return childDefKey;
	}
	
	public static long getITResKey(tcITResourceInstanceOperationsIntf itinstIntf,
			String itresName) {
		long itinstKey = 0;
		try {
			HashMap hm = new HashMap();
			hm.put("IT Resources.Name", itresName);
			tcResultSet rs = itinstIntf.findITResourceInstances(hm);
			rs.goToRow(0);
			itinstKey = rs.getLongValue("IT Resources.Key");
			// printResultSet(rs);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return itinstKey;
	}	
	

	public static long getProcessInstanceKey(tcUserOperationsIntf usrIntf,
			long usrKey, long oiuKey) {
		long orcKey = 0;
		try {
			tcResultSet rs = usrIntf.getObjects(usrKey);

			for (int i = 0; i < rs.getRowCount(); i++) {
				rs.goToRow(i);
				long thisOiuKey = rs
						.getLongValue("Users-Object Instance For User.Key");

				if (thisOiuKey == oiuKey) {
					return rs.getLongValue("Process Instance.Key");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return orcKey;
	}

	public static long getObjectInstanceKey(tcUserOperationsIntf usrIntf,
			long usrKey, long oiuKey) {
		long obiKey = 0;
		try {
			tcResultSet rs = usrIntf.getObjects(usrKey);

			for (int i = 0; i < rs.getRowCount(); i++) {
				rs.goToRow(i);
				long thisOiuKey = rs
						.getLongValue("Users-Object Instance For User.Key");

				if (thisOiuKey == oiuKey) {
					return rs.getLongValue("Object Instance.Key");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obiKey;
	}

	public static void printResultSet(tcResultSet rs) throws Exception {

		System.out.println("COUNT = " + rs.getRowCount() + "\n\n");
		String[] cols = rs.getColumnNames();

		for (int i = 0; i < rs.getRowCount(); ++i) {
			rs.goToRow(i);

			for (int j = 0; j < cols.length; j++) {
				if (cols[j].indexOf("Row Version") == -1) {
					System.out.println(cols[j] + "\t\t:"
							+ rs.getStringValue(cols[j]));
				}
			}
			System.out.println();
		}
	}

	public static void printCollection(Collection col) throws Exception {

		Iterator it = col.iterator();

		while (it.hasNext()) {
			Object obj = it.next();
			System.out.println(obj);
		}
	}

	public static Map mapFromRS(tcResultSet rs) throws Exception {

		HashMap map = new HashMap();
		String[] cols = rs.getColumnNames();

		// for (int i = 0; i < rs.getRowCount(); ++i) {
		rs.goToRow(0);

		for (int j = 0; j < cols.length; j++) {
			String col = cols[j];
			map.put(col, rs.getStringValue(col));

		}

		// }
		return map;
	}

	public static void printMap(Map m) throws Exception {

		Iterator it = m.entrySet().iterator();

		while (it.hasNext()) {
			Entry e = (Entry) it.next();
			System.out.println(e.getKey() + " ==> " + e.getValue());
		}
	}
	
	public static void printAPRD(AccessPolicyResourceData aprd) throws Exception {
		System.out.println("Object name = " + aprd.getObjectName());
		System.out.println("Object key = " + aprd.getObjectKey());
		
		System.out.println("Form name = " + aprd.getFormName());
		System.out.println("Form key = " + aprd.getFormDefinitionKey());
		System.out.println("Form type = " + aprd.getFormType());
		
		HashMap hm = aprd.getFormData();
		
		System.out.println("Form data is: ");
		printMap(hm);
		
		return;

	}

	public static long[] toNativeLongArray(Long[] lArr) {
		long[] arr = new long[lArr.length];
		for (int i=0 ; i<arr.length ; i++) {
			arr[i] = lArr[i].longValue();
		}
		
		return arr;
	}
	
	public static long[] toNativeLongArray(String[] lArr) {
		long[] arr = new long[lArr.length];
		for (int i=0 ; i<arr.length ; i++) {
			arr[i] = Long.parseLong(lArr[i]);
		}
		
		return arr;
	}
	
}

