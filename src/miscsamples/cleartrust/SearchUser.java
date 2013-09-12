package miscsamples.cleartrust;

import java.io.IOException;

import sirrus.api.client.*;
import sirrus.api.client.criteria.*;
import sirrus.api.client.search.*;
import sirrus.connect.ConnectionDescriptor;

public class SearchUser {
    public static final String group = "Default Administrative Group"; 
    public static final String role = "Default Administrative Role";     
    public static ConnectionDescriptor eserver = null;     
    private static APIServerProxy serverProxy = null; 
    public static Object isObject = new Object();

    public static void main(String[] args) {
        String machineName = "host";
        int port = 5601;
        int sslMode = 0;
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

			
            //          Obtain a UserSearch object.
            IUserSearch userSearch = serverProxy.searchUserObjects();

            StringCriterion sc = new StringCriterion(StringCriterion.STARTS_WITH, "gandalf");
            userSearch.setUserIDCriterion(sc);

            // Obtain the SparseData of Users matching the
            // specified Criteria.
            ISparseData usersInSearch = userSearch.search();

            // Find out how many users are in the SparseData
            int numOfUsers = 0;
            try 
            {

                IAPIObject[] retObj = usersInSearch.getByRange(0,
                        Integer.MAX_VALUE);
                numOfUsers = retObj.length;
                
                System.out.println("Number of Users returned "
                        + "by the Search: " + numOfUsers);
            } catch (Exception e) {
                e.printStackTrace();
            } 

            System.out.println("end");
        } catch (Exception e) {
            System.out.println("fail");
            e.printStackTrace();
        }
    }

    /**
     * Constructor that sets the Connection information while instaniation of
     * this Object is occurring.
     *  
     */
    public SearchUser(String machineName, int port, int sslMode, int timeout) {
        synchronized (isObject) {
            if (eserver == null) {
                System.out.println("machineName, port, sslMode,timeout "
                        + machineName + " " + port + " " + sslMode + " "
                        + timeout);
                eserver = new ConnectionDescriptor(machineName, port, sslMode,
                        timeout);
            }
        }
    }

    /**
     * Open a connection to ClearTrust. This assumes that the Connection
     * information has been set.
     * 
     * @param userId
     *            UserId to connect with
     * @param password
     *            Password information for userId
     * 
     * @return String indicating success or failure
     */
    public String openConnection(String userId, String password) {
        try {
            if (eserver == null) {
                String msg = "Connection not set. Did you call the correct Constructor or setConnection()?";
                throw new Exception(msg);
            }
            synchronized (isObject) {
                if (serverProxy == null) {
                    serverProxy = new APIServerProxy(eserver);
                    serverProxy.connect(userId, password, group, role);
                }

                IUser user = null;
                try {
                    user = (IUser) serverProxy.getUsers().getByName("admin");
                } catch (sirrus.api.client.ObjectNotFoundException onfe) {

                    // This is expected, as we want the user not to exist...
                } catch (Exception e) {
                    e.printStackTrace();
                    serverProxy = null;
                    serverProxy = new APIServerProxy(eserver);
                    serverProxy.connect(userId, password, group, role);
                }

            }
            return "RSA_OPENCONNECTION_SUCCESS";
        } catch (java.io.IOException io) {
            io.printStackTrace();
        } catch (UserNotAuthorizedException una) {
            una.printStackTrace();
        } catch (ExpiredPasswordException epe) {
            epe.printStackTrace();
            return "RSA_OPENCONNECTION_EXPIREDPASSWORD_FAIL";
        } catch (ServerTimeoutException ste) {
            ste.printStackTrace();
            return "RSA_OPENCONNECTION_TIMEOUT_FAIL";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "RSA_OPENCONNECTION_UNKNOWN_FAIL";
    }

    public APIServerProxy getConnection() {
        return serverProxy;
    }

    /**
     * Close a connection to ClearTrust. This assumes that the Connection has
     * been set.
     * 
     * @return String indicating success or failure
     */
    public void closeConnection() {
        try {
            serverProxy.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
