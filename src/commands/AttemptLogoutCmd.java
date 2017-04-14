package commands;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

public class AttemptLogoutCmd extends Command implements Runnable {

	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {

		CallableStatement sqlProc;
		StringBuffer strbufResult = null, strbufResponseJSON;
		String strID;
		int nSQLResult;

		String strSessionID = ((String) mapUserData.get("sessionID"));

		if (strSessionID == null || strSessionID.trim().length() == 0)
			return null;

		sqlProc = connection.prepareCall("{?=call logout(?)}");
		sqlProc.registerOutParameter(1, Types.INTEGER);
		sqlProc.setString(2, strSessionID);
		sqlProc.execute();
		nSQLResult = sqlProc.getInt(1);
		strbufResult =

				makeJSONResponseEnvelope(sqlProc.getInt(1), null, null);
		sqlProc.close();

		return strbufResult;
	}
}
