package walmart.core.commands.user;

import java.sql.Statement;
import java.util.Properties;

import walmart.core.Command;
import walmart.core.PostgresConnection;

public class CreateNewUserCommand extends Command implements Runnable {

	@Override
	public void execute() {
		PostgresConnection.setDBHost("localhost"); // set host, Default: "localhost"
		PostgresConnection.setDBName("walmartpostgreSQLDB"); // set database name
		PostgresConnection.setDBPassword(""); // set database password
		PostgresConnection.setDBPort("5432"); // set port number, Default:"5432"
		//PostgresConnection.setDB_SCHEMA("public"); // set database schema, Default(probably,not sure): public
		PostgresConnection.setDBName("postgres"); // set database username, Default: "postgres"
		dbConn = PostgresConnection.initSource();
		
		Statement stmt = null;
		try {
			dbConn.setAutoCommit(true);
			stmt = dbConn.createStatement();
			
			//This is just a static mock example, till we figure out how to make it dynamic
			String sql = "INSERT INTO public.users "
					+ "(id, email, password_hash, first_name, last_name, gender, picture_path, last_login, last_password_change, last_access, disabled, reset_password, phone, creation_time, role_id)"
					+ "VALUES (nextval('users_id_seq'::regclass), 'productsadmin@example.com', '123456789', 'products', 'admin', 'MALE', '', now(), now(), now(), false, false, '01123456789', now(), 2);";
			stmt.executeUpdate(sql);
			stmt.close();
			dbConn.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("User created successfully");
	}

}
