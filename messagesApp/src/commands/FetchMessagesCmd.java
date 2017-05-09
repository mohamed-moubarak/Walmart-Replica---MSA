package commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class FetchMessagesCmd extends Command implements Runnable {

	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {

		StringBuffer strbufResult;
		CallableStatement sqlProc;
		String strID;
		strID = (String) mapUserData.get("userID");

		if (strID == null || strID.trim().length() == 0)
			return null;

		// sqlProc = connection.prepareCall("{?=call addUserSimple(?, ?, ?,
		// ?)}");
		//
		// sqlProc.registerOutParameter(1, Types.INTEGER);
		// sqlProc.setString(2, strID);
		// sqlProc.execute();

		MongoCollection<Document> messages = mongoDB.getCollection("messages");

		FindIterable<Document> result = messages.find(new Document("receiverID", strID));
		Iterator<Document> iter = result.iterator();

		String jsonData = "";

		while (iter.hasNext()) {
			Document doc = iter.next();
			jsonData += '{';
			jsonData += "\"senderID\":";
			jsonData += '"' + doc.getString("senderID") + '"' + ',';
			jsonData += "\"receiverID\":";
			jsonData += '"' + doc.getString("receiverID") + '"' + ',';
			jsonData += "\"message\":";
			jsonData += '"' + doc.getString("message") + '"' + ',';
			jsonData += "},";
		}

		System.out.println("AAAA" + jsonData);

		StringBuffer strBuf = new StringBuffer();
		strBuf.append(jsonData);

		strbufResult = makeJSONResponseEnvelope(200, null, strBuf);
		// sqlProc.close();

		return strbufResult;
	}
}
