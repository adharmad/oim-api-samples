package iamsamples.tools;

import java.io.ByteArrayInputStream;

import java.io.ObjectInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

public class DumpOrchEventException {

	public static void main(String[] args) throws Exception {

		String dbName = "";
		String dbHost = "";
		String dbPort = "";
		String dbUser = "";
		String dbPassword = "";
		long orchEventKey = 1;

		// Connect to the database
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection con = DriverManager.getConnection("jdbc:oracle:thin:@"
				+ dbHost + ":" + dbPort + ":" + dbName, dbUser, dbPassword);

		Statement stmt = con.createStatement();
		String query = "select id, name, status, result from orchevents where id=" + orchEventKey;
		ResultSet rs = stmt.executeQuery(query);


		while (rs.next()) {

			System.out.println("id = " + rs.getString("id"));
			System.out.println("name = " + rs.getString("name"));
			System.out.println("status = " + rs.getString("status"));
			
			ByteArrayInputStream bais = new ByteArrayInputStream(rs
				.getBytes("result"));
			ObjectInputStream oip = new ObjectInputStream(bais);

			Object o = oip.readObject();
			Exception ex = (Exception)o;
			ex.printStackTrace();

			
			System.out.println(ex.getMessage());
		}

		stmt.close();
		rs.close();
		con.close();

		System.exit(0);

	}
}
