package commands;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

public class AddUserSimpleCmd extends Command implements Runnable {

	public StringBuffer execute(Connection connection, Map<String, Object> mapUserData) throws Exception {

		System.out.println("I AM HERE");

		StringBuffer strbufResult;
		CallableStatement sqlProc;
		String strEmail, strPassword, strFirstName, strLastName;
		strEmail = (String) mapUserData.get("email");
		strPassword = (String) mapUserData.get("password");
		strFirstName = (String) mapUserData.get("firstname");
		strLastName = (String) mapUserData.get("lastname");

		if (strEmail == null || strEmail.trim().length() == 0 || strPassword == null || strPassword.trim().length() == 0
				|| strFirstName == null || strFirstName.trim().length() == 0 || strLastName == null
				|| strLastName.trim().length() == 0)
			return null;

		if (!EmailVerifier.verify(strEmail))
			return null;

		sqlProc = connection.prepareCall("{?=call addUserSimple(?, ?, ?, ?)}");

		sqlProc.registerOutParameter(1, Types.INTEGER);
		sqlProc.setString(2, strEmail);
		sqlProc.setString(3, strPassword);
		sqlProc.setString(4, strFirstName);
		sqlProc.setString(5, strLastName);

		sqlProc.execute();
		strbufResult =

				makeJSONResponseEnvelope(sqlProc.getInt(1), null, null);
		sqlProc.close();

		return strbufResult;
	}
}
