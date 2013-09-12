package miscsamples.aumm;

import sirrus.api.client.*;
import sirrus.api.client.criteria.*;
import sirrus.api.client.search.*;
import sirrus.connect.ConnectionDescriptor;

public class CTNumUsers {
    private static String group = "Default Administrative Group"; // Administrator
                                                                  // Group

    private static String role = "Default Administrative Role"; // Administrator
                                                                // Role

    static ConnectionDescriptor eserver = null; // Connection definition
                                                // information

    static APIServerProxy serverProxy = null; // Connection Object Instance

    static Object isObject = new Object();

    public CTNumUsers() {
    }

    /**
     * Constructor that sets the Connection information while instaniation of
     * this Object is occurring.
     *  
     */
    public CTNumUsers(String machineName, int port, int sslMode, int timeout) {
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
                Logger.WriteMessage(msg);
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
            Logger
                    .WriteMessage("IOError Occurred during Connection to ClearTrust.");
            io.printStackTrace();
        } catch (UserNotAuthorizedException una) {
            Logger.WriteMessage("User unable to act as an Administrator");
            una.printStackTrace();
        } catch (ExpiredPasswordException epe) {
            Logger.WriteMessage("Expired password.");
            epe.printStackTrace();
            return "RSA_OPENCONNECTION_EXPIREDPASSWORD_FAIL";
        } catch (ServerTimeoutException ste) {
            Logger.WriteMessage("Server Timeout Connection Error");
            ste.printStackTrace();
            return "RSA_OPENCONNECTION_TIMEOUT_FAIL";
        } catch (Exception e) {
            Logger
                    .WriteMessage("Unknown Exception caught during ClearTrust Connection.");
            e.printStackTrace();
        }
        return "RSA_OPENCONNECTION_UNKNOWN_FAIL";
    }

    public APIServerProxy getConnection() {
        if (serverProxy == null) {
            Logger
                    .WriteMessage("Trying to get a Connection that does not exist.");
        }

        return serverProxy;
    }

    /**
     * Close a connection to ClearTrust. This assumes that the Connection has
     * been set.
     * 
     * @return String indicating success or failure
     */
    public void closeConnection() {
    }

    // Clean Connection to ClearTrust
    protected void finalize() {
        /*
         * // Disconnect from ClearTrust if (serverProxy != null) { try {
         * serverProxy.disconnect(); serverProxy=null; eserver = null; }
         * catch(java.io.IOException io) { io.printStackTrace(); } }
         */
    }

    static class Logger {
        static public void WriteMessage(String msg) {
            System.err.println(msg);
        }
    }

    public static void main(String[] args) {
        //String machineName = "192.168.0.55";
        String machineName = "192.168.50.37";
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
            IUserSearch userSearch =
                serverProxy.searchUserObjects();

            // Obtain the SparseData of Users matching the
            // specified Criteria.
            ISparseData usersInSearch = userSearch.search();

            // Find out how many users are in the SparseData
            int numOfUsers = 0;
            try
            {
            	IAPIObject[] retObj = usersInSearch.getByRange(0,Integer.MAX_VALUE);
            	numOfUsers = retObj.length;
                System.out.println ("Number of Users returned "
                                    + "by the Search: "
                                    + (numOfUsers));
            }catch(BadArgumentException e){
                e.printStackTrace();
            }catch(TransportException e){
                e.printStackTrace();
            }catch(java.io.IOException e){
                e.printStackTrace();
            }
            
            //IUser user = (IUser) serverProxy.getUsers().getByName("admin");
            System.out.println("end");
        } catch (Exception e) {
            System.out.println("fail");
            e.printStackTrace();
        }
    }
}