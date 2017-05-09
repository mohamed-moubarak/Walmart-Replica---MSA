package commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class createTransactionCmd extends Command implements Runnable {

	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData, MongoDatabase mongodb)
			throws Exception {

		MongoCollection<Document> collection = mongodb.getCollection("collectionName");

		CallableStatement sqlProc;
		StringBuffer strbufResult = null, strbufResponseJSON;
		String userID = ((String) mapUserData.get("userID"));
		if (userID == null || userID.trim().length() == 0)
			return null;

		List<Document> cart = (List<Document>) collection.find(Filters.eq("userID", userID))
				.into(new ArrayList<Document>());
		Document doc = ((Document) cart.get(0).get("items"));
		Collection<Object> x = doc.values();
		String[] a = doc.toString().substring(10, doc.toString().length() - 2).replaceAll("\\s", "").split(",");
		for (int i = 0; i < a.length; i++) {
			String[] b = a[i].split("=");
			sqlProc = connection.prepareCall("{?=call decreasestock(?,?)}");
			sqlProc.registerOutParameter(1, Types.INTEGER);
			sqlProc.setInt(2, Integer.parseInt(b[0]));
			sqlProc.setInt(3, Integer.parseInt(b[1]));
			sqlProc.execute();
			int ID = sqlProc.getInt(1);
			sqlProc.close();
		}
		strbufResult = makeJSONResponseEnvelope(Integer.parseInt(userID), null, null);
		collection.deleteOne(Filters.eq("userID", userID));
		return strbufResult;

	}
}
