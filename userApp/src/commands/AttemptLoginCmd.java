package commands;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cache.Cache;

public class AttemptLoginCmd extends Command implements Runnable {

	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {

		CallableStatement sqlProc;
		StringBuffer strbufResult = null, strbufResponseJSON;
		String strSessionID, strEmail, strPassword, strFirstName, strClientIP;
		int nSQLResult;

		strEmail = ((String) mapUserData.get("email"));
		strPassword = ((String) mapUserData.get("password"));

		if (strEmail == null || strEmail.trim().length() == 0 || strPassword == null
				|| strPassword.trim().length() == 0)
			return null;

		if (!EmailVerifier.verify(strEmail))
			return null;

		strClientIP = _clientHandle.getClientIP();
		strSessionID = UUID.randomUUID().toString();

		sqlProc = connection.prepareCall("{?=call attemptLogin(?,?,?,?)}");
		sqlProc.registerOutParameter(1, Types.INTEGER);
		sqlProc.setString(2, strEmail);
		sqlProc.setString(3, strPassword);
		sqlProc.setString(4, strSessionID);
		sqlProc.setString(5, strClientIP);
		sqlProc.execute();
		nSQLResult = sqlProc.getInt(1);
		sqlProc.close();
		if (nSQLResult >= 0) {
			Cache.addSession(strSessionID, strEmail);
			System.err.println(" adding following session to Cache " + strSessionID);
			Map<String, Object> mapResult = new HashMap<String, Object>();
			mapResult.put("userID", Integer.toString(nSQLResult));
			mapResult.put("sessionID", strSessionID);
			sqlProc = connection.prepareCall("{?=call getUserFirstName(?)}");
			sqlProc.registerOutParameter(1, Types.VARCHAR);
			sqlProc.setInt(2, nSQLResult);
			sqlProc.execute();
			strFirstName = sqlProc.getString(1);
			sqlProc.close();
			mapResult.put("firstName", strFirstName);
			strbufResponseJSON = serializeMaptoJSON(mapResult, null);
			strbufResult = makeJSONResponseEnvelope(0, null, strbufResponseJSON);
		} else
			strbufResult = makeJSONResponseEnvelope(nSQLResult, null, null);

		return strbufResult;
	}
}
