package commands;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.util.JSON;

public class GetPromotionsCmd extends Command implements Runnable {

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

		// get image file by it's filename
		List<GridFSDBFile> imagesForOutput = gfsPhoto.find((DBObject) JSON
				.parse("{ promotion : true, active : true }"));
//		System.out.println(imagesForOutput);
//		for (int i = 0; i < imagesForOutput.size(); i++) {
////			 save it into a new image file
//			imagesForOutput.get(i).writeTo("JavaWebHostingNew_" + i + ".png");
//
//		}
		
//		// remove the image file from mongoDB
//		gfsPhoto.remove(gfsPhoto.findOne(newFileName));

//		System.out.println("Done");
		
		
		
		StringBuffer strbufResult;
		CallableStatement sqlProc;
//		String strID;
//		strID = (String) mapUserData.get("userID");
//
//		if (strID == null || strID.trim().length() == 0)
//			return null;

		// sqlProc = connection.prepareCall("{?=call addUserSimple(?, ?, ?,
		// ?)}");
		//
		// sqlProc.registerOutParameter(1, Types.INTEGER);
		// sqlProc.setString(2, strID);
		// sqlProc.execute();

//		MongoCollection<Document> messages = mongoDB.getCollection("messages");
//
//		FindIterable<Document> result = messages.find(new Document("receiverID", strID));
//		Iterator<GridFSDBFile> iter = imagesForOutput.iterator();

		String jsonData = "";
		System.out.println(imagesForOutput);
//		while (iter.hasNext()) {
//			jsonData += '{';
			jsonData += "\"promotions\":";
			jsonData += imagesForOutput;
//			jsonData += "}";
//		}

		System.out.println("AAAA" + jsonData);

		StringBuffer strBuf = new StringBuffer();
		strBuf.append(jsonData);

		strbufResult =

				makeJSONResponseEnvelope(200, null, strBuf);
		// sqlProc.close();

		return strbufResult;
	}
}
