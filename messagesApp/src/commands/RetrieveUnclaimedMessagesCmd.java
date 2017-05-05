package commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class RetrieveUnclaimedMessagesCmd extends Command implements Runnable {

	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {

		StringBuffer strbufResult;
		CallableStatement sqlProc;
		String strID;
		strID = (String) mapUserData.get("adminID");

		if (strID == null || strID.trim().length() == 0)
			return null;

		MongoCollection<Document> messages = mongoDB.getCollection("messages");

		System.out.println("before");

		FindIterable<Document> result = messages.find(new Document("receiverID", "null"));

		System.out.println("after");

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

		// if (!EmailVerifier.verify(strEmail))
		// return null;

		// sqlProc = connection.prepareCall("{?=call addUserSimple(?, ?, ?,
		// ?)}");
		//
		// sqlProc.registerOutParameter(1, Types.INTEGER);
		// sqlProc.setString(2, strEmail);
		// sqlProc.setString(3, strPassword);
		// sqlProc.setString(4, strFirstName);
		// sqlProc.setString(5, strLastName);
		//
		// sqlProc.execute();
		strbufResult =

				makeJSONResponseEnvelope(200, null, strBuf);
		// sqlProc.close();

		return strbufResult;
	}
}
