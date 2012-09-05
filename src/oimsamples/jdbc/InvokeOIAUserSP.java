package oimsamples.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;

public class InvokeOIAUserSP {
	public static void main(String[] args) throws Exception {
		String dbURL = "jdbc:oracle:thin:@host:port:DBNAME";
		String dbUser = "user";
		String dbPassword = "password";

		// Connect to the database
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
		
		//CallableStatement cs = con.prepareCall("{ call OIM_PKG_OIA_INTEGRATION.OIM_SP_OIA_POPULATE_USER_DATA( ?,?,?,? ) }");
		CallableStatement cs = con.prepareCall("{ call OIM_PKG_OIA_INTEGRATION.OIM_SP_OIA_POPULATE_ENT_DATA( ?,?,?,? ) }");
		cs.setDate(1, null);
		cs.registerOutParameter(2, java.sql.Types.NUMERIC);
		cs.registerOutParameter(3, java.sql.Types.NUMERIC);
		cs.registerOutParameter(4, java.sql.Types.VARCHAR);
		
		cs.execute();
		cs.close();
		con.close();
	}
}
