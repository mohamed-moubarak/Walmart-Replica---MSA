package commands;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class AddtoCartCmd extends Command implements Runnable {

	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData, MongoDatabase mongodb)
			throws Exception {

		StringBuffer strbufResult;
		MongoCollection<Document> collection = mongodb.getCollection("collectionName");

		String strID;
		String itemID;
		String itemQty;
		strID = (String) mapUserData.get("userID");
		itemID = (String) mapUserData.get("itemID");
		itemQty = (String) mapUserData.get("itemQty");
		if (strID == null || strID.trim().length() == 0 || itemID == null || itemID.trim().length() == 0
				|| itemQty == null || itemQty.trim().length() == 0)
			return null;
		List<Document> cart = (List<Document>) collection.find(Filters.eq("userID", strID))
				.into(new ArrayList<Document>());

		if (cart.isEmpty()) {
			Document doc = new Document("userID", strID).append("items", new Document(itemID, itemQty));
			collection.insertOne(doc);
		} else {
			if (!((Document) cart.get(0).get("items")).containsKey(itemID)) {
				((Document) cart.get(0).get("items")).append(itemID, itemQty);
			} else {
				int old = Integer.parseInt((String) ((Document) cart.get(0).get("items")).get(itemID));
				((Document) cart.get(0).get("items")).put(itemID, old + Integer.parseInt(itemQty) + "");
			}
			collection.replaceOne(Filters.eq("userID", strID), cart.get(0));
		}

		strbufResult = makeJSONResponseEnvelope(Integer.parseInt(strID), null, null);

		return strbufResult;
	}
}
