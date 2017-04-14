import java.io.*;
import java.sql.*;
import java.util.*;
import java.math.*;
import java.lang.reflect.*;
import java.util.concurrent.*;

import org.bson.Document;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.zaxxer.hikari.HikariDataSource;

public class Dispatcher {

	protected Hashtable _htblCommands;
	protected ExecutorService _threadPoolCmds;
	protected HikariDataSource _hikariDataSource;

	public Dispatcher() {
	}

	abstract class Command {

		protected HikariDataSource _hikariDataSource;
		protected ClientHandle _clientHandle;
		protected ClientRequest _clientRequest;
		protected MongoDatabase mongoDB;

		protected ArrayList<String> _arrColsToKeep;

		public void init(HikariDataSource hikariDataSource, ClientHandle clientHandle, ClientRequest clientRequest) {
			_hikariDataSource = hikariDataSource;
			_clientRequest = clientRequest;
			_clientHandle = clientHandle;
		}

		public void run() {
			mongoDB = connectMongo();
			Connection connection = null;
			try {
				Map<String, Object> map;
				StringBuffer strbufResponse;
				connection = _hikariDataSource.getConnection();
				// connection = null;
				map = _clientRequest.getData();
				strbufResponse = execute(connection, map);

				if (strbufResponse != null)
					_clientHandle.passResponsetoClient(strbufResponse);
				else
					_clientHandle.terminateClientRequest();
			} catch (Exception exp) {
				System.err.println(exp.toString());
				_clientHandle.terminateClientRequest();
			} finally {
				closeConnectionQuietly(connection);
			}
		}

		protected void closeConnectionQuietly(Connection connection) {
			try {
				if (connection != null)
					connection.close();
			} catch (Exception exp) {
				// log this...
				exp.printStackTrace();
			}
		}

		protected StringBuffer makeJSONResponseEnvelope(int nResponse, StringBuffer strbufRequestData,
				StringBuffer strbufResponseData) {
			StringBuffer strbufJSON;
			String strStatusMsg;
			String strData = "";
			Map<String, Object> mapInputData;
			String strKey;

			strbufJSON = new StringBuffer();
			strbufJSON.append("{");
			strbufJSON.append("\"responseTo\":\"" + _clientRequest.getAction() + "\",");
			if (_clientRequest.getSessionID() != null)
				strbufJSON.append("\"sessionID\":\"" + _clientRequest.getSessionID() + "\",");

			strbufJSON.append("\"StatusID\":\"" + nResponse + "\",");
			strStatusMsg = (String) ResponseCodes.getMessage(Integer.toString(nResponse));
			strbufJSON.append("\"StatusMsg\":\"" + strStatusMsg + "\",");

			if (strbufRequestData != null)
				strbufJSON.append("\"requestData\":{" + strbufRequestData + "},");

			if (strbufResponseData != null) {
				if (strbufResponseData.charAt(0) == '[')
					strbufJSON.append("\"responseData\":" + strbufResponseData); // if
																					// it
																					// is
																					// a
																					// list,
																					// no
																					// curley
				else
					strbufJSON.append("\"responseData\":{" + strbufResponseData + "}");
			}
			if (strbufJSON.charAt(strbufJSON.length() - 1) == ',')
				strbufJSON.deleteCharAt(strbufJSON.length() - 1);

			strbufJSON.append("}");
			return strbufJSON;
		}

		protected StringBuffer serializeRequestDatatoJSON(ArrayList arrFieldstoKeep) throws Exception {

			return serializeMaptoJSON(_clientRequest.getData(), arrFieldstoKeep);
		}

		protected StringBuffer serializeResultSettoJSON(ResultSet resultSet, ArrayList arrColstoKeep, int nMaxSize)
				throws Exception {

			StringBuffer strbufJSON;
			int nCount;
			boolean bKeepColumn;

			strbufJSON = new StringBuffer();
			ResultSetMetaData rsmd = resultSet.getMetaData();
			strbufJSON.append("[ ");
			nCount = 0;

			while (resultSet.next() && ((nCount < nMaxSize) || (nMaxSize == 0))) {
				int nColumns = rsmd.getColumnCount();
				strbufJSON.append("{");
				nCount++;
				for (int nIndex = 1; nIndex < nColumns + 1; nIndex++) {
					String strColumnName = rsmd.getColumnName(nIndex);
					bKeepColumn = false;
					if (arrColstoKeep == null)
						bKeepColumn = true;
					else if (arrColstoKeep.contains(strColumnName))
						bKeepColumn = true;

					if (bKeepColumn) {
						strbufJSON.append("\"" + strColumnName + "\": ");

						if (rsmd.getColumnType(nIndex) == java.sql.Types.BIGINT)
							strbufJSON.append("\"" + resultSet.getInt(nIndex) + "\"");
						else if (rsmd.getColumnType(nIndex) == java.sql.Types.BOOLEAN)
							strbufJSON.append("\"" + resultSet.getBoolean(nIndex) + "\"");
						else if (rsmd.getColumnType(nIndex) == java.sql.Types.BLOB)
							strbufJSON.append("\"" + resultSet.getBlob(nIndex) + "\"");
						else if (rsmd.getColumnType(nIndex) == java.sql.Types.DOUBLE)
							strbufJSON.append("\"" + resultSet.getDouble(nIndex) + "\"");
						else if (rsmd.getColumnType(nIndex) == java.sql.Types.FLOAT)
							strbufJSON.append("\"" + resultSet.getFloat(nIndex) + "\"");
						else if (rsmd.getColumnType(nIndex) == java.sql.Types.INTEGER)
							strbufJSON.append("\"" + resultSet.getInt(nIndex) + "\"");
						else if (rsmd.getColumnType(nIndex) == java.sql.Types.NVARCHAR)
							strbufJSON.append("\"" + resultSet.getNString(nIndex) + "\"");
						else if (rsmd.getColumnType(nIndex) == java.sql.Types.VARCHAR)
							strbufJSON.append("\"" + resultSet.getString(nIndex) + "\"");
						else if (rsmd.getColumnType(nIndex) == java.sql.Types.TINYINT)
							strbufJSON.append("\"" + resultSet.getInt(nIndex) + "\"");
						else if (rsmd.getColumnType(nIndex) == java.sql.Types.SMALLINT)
							strbufJSON.append("\"" + resultSet.getInt(nIndex) + "\"");
						else if (rsmd.getColumnType(nIndex) == java.sql.Types.DATE)
							strbufJSON.append("\"" + resultSet.getDate(nIndex) + "\"");
						else if (rsmd.getColumnType(nIndex) == java.sql.Types.TIMESTAMP)
							strbufJSON.append("\"" + resultSet.getTimestamp(nIndex) + "\"");
						else
							strbufJSON.append("\"" + resultSet.getObject(nIndex) + "\"");

						strbufJSON.append(",");
					}
				}
				if (strbufJSON.charAt(strbufJSON.length() - 1) == ',')
					strbufJSON.setLength(strbufJSON.length() - 1);
				strbufJSON.append("},");
			}

			if (strbufJSON.charAt(strbufJSON.length() - 1) == ',')
				strbufJSON.setLength(strbufJSON.length() - 1);

			strbufJSON.append("]");

			return strbufJSON;
		}

		protected StringBuffer serializeMaptoJSON(Map<String, Object> map, ArrayList arrFieldstoKeep) {

			StringBuffer strbufData;

			strbufData = new StringBuffer();
			if (arrFieldstoKeep == null) {
				for (Map.Entry<String, Object> entry : map.entrySet())
					strbufData.append("\"" + entry.getKey() + "\":\"" + entry.getValue().toString() + "\",");
			} else {
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					if (arrFieldstoKeep.contains(entry.getKey()))
						strbufData.append("\"" + entry.getKey() + "\":\"" + entry.getValue().toString() + "\",");
				}
			}

			if (strbufData.charAt(strbufData.length() - 1) == ',')
				strbufData.setLength(strbufData.length() - 1);

			return strbufData;
		}

		public abstract StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception;
	}

	class SendMessageCmd extends Command implements Runnable {

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

	class FetchMessagesCmd extends Command implements Runnable {

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

			strbufResult =

					makeJSONResponseEnvelope(200, null, strBuf);
			// sqlProc.close();

			return strbufResult;
		}
	}

	class RetrieveUnclaimedMessagesCmd extends Command implements Runnable {

		public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {

			StringBuffer strbufResult;
			CallableStatement sqlProc;
			String strID;
			strID = (String) mapUserData.get("adminID");

			if (strID == null || strID.trim().length() == 0)
				return null;

			MongoCollection<Document> messages = mongoDB.getCollection("messages");

			System.out.println("before");

			FindIterable<Document> result = messages.find(new Document("receiverID", null));

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

	protected void dispatchRequest(ClientHandle clientHandle, ClientRequest clientRequest) throws Exception {

		System.out.println("hello world");

		Command cmd;
		String strAction;
		strAction = clientRequest.getAction();

		Class<?> innerClass = (Class<?>) _htblCommands.get(strAction);
		Class<?> enclosingClass = Class.forName("Dispatcher");
		Object enclosingInstance = enclosingClass.newInstance();
		Constructor<?> ctor = innerClass.getDeclaredConstructor(enclosingClass);
		cmd = (Command) ctor.newInstance(enclosingInstance);
		cmd.init(_hikariDataSource, clientHandle, clientRequest);

		_threadPoolCmds.execute((Runnable) cmd);

		System.out.println("enter thread");
	}

	protected void loadHikari(String strAddress, int nPort, String strDBName, String strUserName, String strPassword) {

		_hikariDataSource = new HikariDataSource();
		_hikariDataSource.setJdbcUrl("jdbc:postgresql://" + strAddress + ":" + nPort + "/" + strDBName);
		_hikariDataSource.setUsername(strUserName);
		_hikariDataSource.setPassword(strPassword);
	}

	protected void loadCommands() throws Exception {
		_htblCommands = new Hashtable();
		Properties prop = new Properties();
		InputStream in = getClass().getResourceAsStream("config/commands.properties");
		prop.load(in);
		in.close();
		Enumeration enumKeys = prop.propertyNames();
		String strActionName, strClassName;

		while (enumKeys.hasMoreElements()) {
			strActionName = (String) enumKeys.nextElement();
			strClassName = (String) prop.get(strActionName);
			Class<?> innerClass = Class.forName("Dispatcher$" + strClassName);
			_htblCommands.put(strActionName, innerClass);
		}
	}

	public static MongoDatabase connectMongo() {
		MongoDatabase db = null;
		try {
			// To connect to mongodb server
			MongoClient mongoClient = new MongoClient("localhost", 27017);
			// Now connect to your databases
			db = mongoClient.getDatabase("walmart");
			System.out.println("Connect to mongo database successfully");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return db;
	}

	protected void loadThreadPool() {
		_threadPoolCmds = Executors.newFixedThreadPool(20);
	}

	public void init() throws Exception {
		loadHikari("localhost", 5432, "postgres", "postgres", "boyka_88");
		loadThreadPool();
		loadCommands();
	}
}