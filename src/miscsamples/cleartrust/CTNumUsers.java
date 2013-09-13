package miscsamples.cleartrust;

import java.io.IOException;

import sirrus.api.client.*;
import sirrus.api.client.criteria.*;
import sirrus.api.client.search.*;
import sirrus.connect.ConnectionDescriptor;

public class CTNumUsers {
	private static String group = "Default Administrative Group";

	private static String role = "Default Administrative Role";

	public static void main(String[] args) {
		APIServerProxy serverProxy = null;
		String machineName = "host";
		int port = 5601;
		int sslMode = 1;
		int timeout = 100000;
		ConnectionDescriptor eserver = new ConnectionDescriptor(machineName,
				port, sslMode, timeout);
		try {
			System.out.println("begin");
			serverProxy = new APIServerProxy(eserver);
			String userId = "admin";
			String password = "admin";
			System.out.println("begin connect");
			serverProxy.connect(userId, password, group, role);
			System.out.println("end connect");

			// Obtain a UserSearch object.
			IUserSearch userSearch = serverProxy.searchUserObjects();
			// StringCriterion sc = new StringCriterion(StringCriterion.EQUALS,
			// "Employees");
			// StringCriterion sc = new StringCriterion();
			// userSearch.setGroupCriterion(sc);
			// userSearch.setUserIDCriterion(sc);

			// Obtain the SparseData of Users matching the
			// specified Criteria.
			ISparseData usersInSearch = userSearch.search();

			// Find out how many users are in the SparseData
			int numOfUsers = 0;
			try {
				IAPIObject[] retObj = usersInSearch.getByRange(734, 735);
				// Integer.MAX_VALUE);
				numOfUsers = retObj.length;
				System.out.println("Number of Users returned "
						+ "by the Search: " + (numOfUsers));
				IUser u1 = (IUser) retObj[0];
				System.out.println(u1.getName());
				// IUser u2 = (IUser)retObj[1];
				// System.out.println(u2.getName());

			} catch (BadArgumentException e1) {
				System.out.println(e1.getMessage());
				e1.printStackTrace();
			} catch (TransportException e2) {
				System.out.println(e2.getMessage());
				e2.printStackTrace();
			} catch (java.io.IOException e3) {
				System.out.println(e3.getMessage());
				e3.printStackTrace();
			}

			// IUser user = (IUser) serverProxy.getUsers().getByName("admin");
			System.out.println("end");

		} catch (TransportException e4) {
			System.out.println("fail");
			System.out.println(e4.getMessage());
			e4.printStackTrace();
		} catch (APIException e5) {
			System.out.println(e5.getMessage());
			e5.printStackTrace();
		} catch (IOException e6) {
			System.out.println(e6.getMessage());
			e6.printStackTrace();
		}
	}
}