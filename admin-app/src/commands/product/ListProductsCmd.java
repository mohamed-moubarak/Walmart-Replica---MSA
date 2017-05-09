package commands.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import org.json.*;

import commands.Command;

class ListProductsCmd extends Command implements Runnable {

    public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

        StringBuffer      strbufResult;
        PreparedStatement preparedStatement;
        ResultSet         resultSet;

        preparedStatement = connection.prepareStatement("select * from getProducts()");

        resultSet = preparedStatement.executeQuery();
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int numColumns = rsmd.getColumnCount();
        String[] columnNames = new String[numColumns];
        for (int i = 0; i < columnNames.length; i++) {
            columnNames[i] = rsmd.getColumnLabel(i + 1);
        }

        JSONArray result = new JSONArray();
        while (resultSet.next()) {
            Map<String, Object> row = new HashMap<String, Object>();
            for (int i = 0; i < columnNames.length; i++) {
                String columnName = columnNames[i];
                row.put(columnName, resultSet.getObject(columnName));
            }
            result.put(row);
        }

        strbufResult = makeJSONResponseEnvelope( 200, null, new StringBuffer("\"products\": "+ result) );

        preparedStatement.close( );

        return strbufResult;
    }
}
