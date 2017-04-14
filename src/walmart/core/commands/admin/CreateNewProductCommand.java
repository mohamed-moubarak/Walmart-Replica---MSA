package walmart.core.commands.admin;

import walmart.core.Command;
import walmart.core.PostgresConnection;

public class CreateNewProductCommand extends Command implements Runnable{

	@Override
	public void execute() {
		PostgresConnection.setDB_HOST("localhost"); // set host, Default: "localhost"
		PostgresConnection.setDB_NAME("walmartpostgreSQLDB"); // set database name
		PostgresConnection.setDB_PASSWORD(""); // set database password
		PostgresConnection.setDB_PORT("5432"); // set port number, Default:"5432"
		PostgresConnection.setDB_SCHEMA("public"); // set database schema, Default(probably,not sure): public
		PostgresConnection.setDB_USERNAME("postgres"); // set database username, Default: "postgres"
		dbConn = PostgresConnection.connect();
		
		try {
			dbConn.setAutoCommit(true);
            proc = dbConn.prepareCall("{call addProduct(?)}");
			//This is just a static mock example, till we figure out how to make it dynamic
            proc.setInt(1, 500); // 1 refers to the number of placeholder
            					// Placeholder is the ? sign
            					// So if the stored procedure is expecting 2 params, which are both integers, it will be presented as follows
            					// proc = dbConn.prepareCall("{call mockStoredProc(?, ?)}");
            					// proc.setInt(1, 20);
            					// proc.setInt(2, 30);
            
            proc.execute();

			dbConn.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Product created successfully");
		
	}
	
}
