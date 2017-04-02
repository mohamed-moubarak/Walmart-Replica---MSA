
package walmart.core;

import java.sql.DriverManager;
import java.sql.Connection;
import java.util.Properties;



public class PostgresConnection {
	private static String DB_USERNAME; // your db username
	private static String DB_PASSWORD; // your db password
	private static String DB_PORT;
	private static String DB_HOST;
	private static String DB_NAME;
	private static String DB_SCHEMA;
	private static String DB_URL;

	public static Connection connect() {
		Connection c = null;
		try {
			DB_URL = "jdbc:postgresql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
			Class.forName("org.postgresql.Driver");
			Properties props = new Properties();
			props.setProperty("user", DB_USERNAME);
			props.setProperty("password", DB_PASSWORD);
			props.setProperty("currentSchema", DB_SCHEMA);
			c = DriverManager.getConnection(DB_URL, props);
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Connected to database successfully!");

		return c;
	}

	// public static void executeThisQuery(Connection c, String query) {
	// Statement stmt = null;
	//
	// try {
	// stmt = c.createStatement();
	//
	// String sql = query;
	// stmt.executeUpdate(sql);
	// stmt.close();
	// c.commit();
	// c.close();
	//
	// } catch (Exception e) {
	// System.err.println(e.getClass().getName() + ": " + e.getMessage());
	// System.exit(0);
	// }
	// System.out.println("Records created successfully");
	// }

	public static void setDB_USERNAME(String dB_USERNAME) {
		DB_USERNAME = dB_USERNAME;
	}

	public static void setDB_PASSWORD(String dB_PASSWORD) {
		DB_PASSWORD = dB_PASSWORD;
	}

	public static void setDB_PORT(String dB_PORT) {
		DB_PORT = dB_PORT;
	}

	public static void setDB_HOST(String dB_HOST) {
		DB_HOST = dB_HOST;
	}

	public static void setDB_NAME(String dB_NAME) {
		DB_NAME = dB_NAME;
	}

	public static void setDB_SCHEMA(String dB_SCHEMA) {
		DB_SCHEMA = dB_SCHEMA;
	}

}
