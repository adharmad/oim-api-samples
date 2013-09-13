package iamsamples.tools;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import oracle.iam.platform.context.ContextAwareNumber;
import oracle.iam.platform.context.ContextAwareString;
import oracle.iam.platform.kernel.vo.EntityOrchestration;
import oracle.iam.platform.kernel.vo.Orchestration;
import oracle.iam.platform.kernel.vo.PostProcessOnlyBulkOrchestration;

public class DeserializeOrchestration {

	public static void main(String[] args) throws Exception {
		String statement1 = "select * from orchprocess where id=?";
		String statement2 = "select * from orchprocess where id=?";
		// String statement =
		// "select id, orchestration from orchprocess where entitytype='User' and operation='CREATE' order by id";
		// String statement =
		// "select orchestration from orchprocess where id in (select processid from orchevents where name='UserModifyLDAPPreProcessHandler' and status='FAILED') order by id desc";
		Connection conn = getOracleConnection();
		System.out.println("Something");

		// Read object from oracle
		PreparedStatement pstmt = conn.prepareStatement(statement1);
		pstmt.setLong(1, 1463201);
		ResultSet rs = pstmt.executeQuery();
		
		dumpRS(rs);
		

		rs.close();
		pstmt.close();
		conn.close();
		
		System.exit(0);

	}
	
	public static void dumpRS(ResultSet rs) throws Exception {
		
		InputStream is = null;
		ObjectInputStream oip = null;
		
		rs.next();
		ResultSetMetaData rsmd = rs.getMetaData();

		for (int i=0 ; i<rsmd.getColumnCount() ; i++) {
			String colName = rsmd.getColumnName(i+1);
			
			if (colName.equalsIgnoreCase("orchestration")) {
				//is = rs.getBlob(1).getBinaryStream();
				is = rs.getBinaryStream(colName);
				oip = new ObjectInputStream(is);
				
				Object o = oip.readObject();
				
				if (o instanceof PostProcessOnlyBulkOrchestration) {
				
					PostProcessOnlyBulkOrchestration object = (PostProcessOnlyBulkOrchestration) o;
				
					System.out.println("Operation = " + object.getOperation());
					System.out.println("Target = " + object.getTarget());
					System.out.println("Action Result = " + object.getActionResult());
					System.out.println("Context value = " + object.getContextVal());
					printMap("Bulk Parameters: ", object.getBulkParameters());
					printMap("Parameters: ", object.getParameters());
					printMap("Inter event data: ", object.getInterEventData());
				} else if (o instanceof EntityOrchestration) {
					EntityOrchestration object = (EntityOrchestration) o;
			
					System.out.println("EntityID = " + object.getEntityId());
					System.out.println("EntityType = " + object.getEntityType());
					System.out.println("Type = " + object.getType());
					
					System.out.print("All Entity IDs: " );
					for (int j=0 ; j<object.getAllEntityId().length ; j++) {
						System.out.print(object.getAllEntityId()[j]);
					}
					System.out.println();
					
					
				} else if (o instanceof Orchestration) {
					Orchestration object = (Orchestration) o;
					
					System.out.println("Operation = " + object.getOperation());
					System.out.println("Target = " + object.getTarget());
					//System.out.println("Action Result = " + object.getActionResult());
					System.out.println("Context value = " + object.getContextVal());
					//printMap("Bulk Parameters: ", object.getBulkParameters());
					printMap("Parameters: ", object.getParameters());
					printMap("Inter event data: ", object.getInterEventData());
					
				} else {
					System.out.println("UNKNOWN ORCHESTRATION BLOB");
				}
				

			} else {
				System.out.println(colName + " ===> " + rs.getString(colName));
			}
		}
		
		oip.close();
		is.close();
		
	}
	
	public static void printMap(String name, HashMap<String, Serializable>[] marr) {
		
		System.out.println("Dumping maps name = " + name);
		
		for (int i=0 ; i<marr.length ; i++) {
			System.out.println("Map element # " + i);
			HashMap<String, Serializable> m = marr[i];
			
			if (m == null) {
				System.out.println("Null Map found");
				continue;
			}
			Set<Entry<String, Serializable>> set = m.entrySet();
			Iterator<Entry<String, Serializable>> it = set.iterator();
		
			while (it.hasNext()) {
				Entry<String, Serializable> e = it.next();

				Object val = (Object)e.getValue();
				String v = "";
				
				if (val instanceof ContextAwareString) {
					ContextAwareString cas = (ContextAwareString)val;
					v = (String)cas.getObjectValue();
				} else if (val instanceof ContextAwareNumber) {
					ContextAwareNumber can = (ContextAwareNumber)val;
					v = String.valueOf(((Long)can.getObjectValue()));
				} else {
					v = val.toString();
				}
				
				System.out.println("\t" + e.getKey() + " ---> " + v);
				
			}
		}
	}

	public static void printMap(String name, HashMap<String, Serializable> m) {
		
		System.out.println("Dumping maps name = " + name);
		
		if (m == null) {
			System.out.println("Null Map found");
			return;
		}
		
		Set<Entry<String, Serializable>> set = m.entrySet();
		Iterator<Entry<String, Serializable>> it = set.iterator();
		
		while (it.hasNext()) {
			Entry<String, Serializable> e = it.next();
			
			Object val = (Object)e.getValue();
			String v = "";
			
			if (val instanceof ContextAwareString) {
				ContextAwareString cas = (ContextAwareString)val;
				v = (String)cas.getObjectValue();
			} else if (val instanceof ContextAwareNumber) {
				ContextAwareNumber can = (ContextAwareNumber)val;
				v = String.valueOf(((Long)can.getObjectValue()));
			} else {
				v = val.toString();
			}
			
			System.out.println("\t" + e.getKey() + " ---> " + v);
			
		}
	}
	
	
	
	public static Connection getOracleConnection() throws Exception {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@host:1521:sid";
		String username = "test";
		String password = "test";

		Class.forName(driver); // load Oracle driver
		Connection conn = DriverManager.getConnection(url, username, password);
		return conn;
	}

}
