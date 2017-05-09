package commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.*;

import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.util.JSON;

public class GetAllProductsCmd extends Command implements Runnable {

	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {
		Mongo mongo = new Mongo("localhost", 27017);
		DB db = mongo.getDB("imagedb");

		// create a "photo" namespace
		GridFS gfsPhoto = new GridFS(db, "photo");

		// print the result
		DBCursor cursor = gfsPhoto.getFileList();
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}
		
		StringBuffer strbufResult;
		PreparedStatement preparedStatement;
		ResultSet resultSet;

		preparedStatement = connection.prepareStatement("select * from getProducts()");

		resultSet = preparedStatement.executeQuery();
		ResultSetMetaData rsmd = resultSet.getMetaData();
		int numColumns = rsmd.getColumnCount();
		String[] columnNames = new String[numColumns+1];
		for (int i = 0; i < columnNames.length - 1; i++) {
			columnNames[i] = rsmd.getColumnLabel(i + 1);
		}
		columnNames[numColumns] = "images";
		
		JSONArray result = new JSONArray();
		while (resultSet.next()) {
			Map<String, Object> row = new HashMap<String, Object>();
			for (int i = 0; i < columnNames.length - 1; i++) {
				String columnName = columnNames[i];
				row.put(columnName, resultSet.getObject(columnName));
			}
			row.put("images", gfsPhoto.find((DBObject) JSON
				.parse("{ PID : " + resultSet.getObject("id") + "}")));
			System.out.println(row.get("images"));
			result.put(row);
		}
		System.out.println(result);
		strbufResult = makeJSONResponseEnvelope(200, null, new StringBuffer("\"all_products\": " + result));

		preparedStatement.close();

		return strbufResult;
	}
}