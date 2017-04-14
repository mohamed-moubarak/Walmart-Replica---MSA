package commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

public class SendMessageCmd extends Command implements Runnable {

	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {

		StringBuffer strbufResult;
		CallableStatement sqlProc;
		String strID, strMessage, strReceiverID;
		strID = (String) mapUserData.get("userID");
		strReceiverID = (String) mapUserData.get("receiverID");
		strMessage = (String) mapUserData.get("message");

		if (strID == null || strID.trim().length() == 0 || strMessage == null || strMessage.trim().length() == 0)
			return null;

		MongoCollection<Document> messages = mongoDB.getCollection("messages");

		Map<String, Object> documentmap = new HashMap<String, Object>();
		documentmap.put("senderID", strID);
		documentmap.put("message", strMessage);
		documentmap.put("receiverID", strReceiverID);

		messages.insertOne(new Document(documentmap));

		// if (!EmailVerifier.verify(strEmail))
		// return null;

		// sqlProc = connection.prepareCall("{?=call editInfo(?, ?, ?, ?, ?,
		// ?, ?)}");
		//
		// sqlProc.registerOutParameter(1, Types.INTEGER);
		// sqlProc.setString(2, strEmail);
		// sqlProc.setString(3, strPassword);
		// sqlProc.setString(4, strnewPassword);
		// sqlProc.setString(5, strFirstName);
		// sqlProc.setString(6, strLastName);
		// sqlProc.setString(7, strPicturePath);
		// sqlProc.setString(8, strGender);
		//
		// sqlProc.execute();
		strbufResult = makeJSONResponseEnvelope(200, null, null);
		// sqlProc.close();

		return strbufResult;
	}
}
