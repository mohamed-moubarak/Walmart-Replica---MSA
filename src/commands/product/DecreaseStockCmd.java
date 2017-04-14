package commands.product;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

import commands.Command;

class DecreaseStockCmd extends Command implements Runnable {

    public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

        StringBuffer       strbufResult;
        CallableStatement  sqlProc;
        String             strName,
                           strDescription;
        Double             dblPrice;
        Integer            intStock,
        					intID;

        strName        = (String)mapUserData.get( "name" );
        intID = 		(Integer)mapUserData.get( "PID" );
        intStock       = (Integer)mapUserData.get( "stock" );
        if( strName == null || strName.trim( ).length( ) == 0 ||
                intStock == null || intStock ==0)
            return null;

        sqlProc = connection.prepareCall("{?=call decreaseStock(?)}");
        sqlProc.registerOutParameter(1, Types.INTEGER );
        sqlProc.setInt(2, intStock );

        sqlProc.execute( );
        strbufResult = makeJSONResponseEnvelope( sqlProc.getInt( 1 ) , null, null );
        sqlProc.close( );

        return strbufResult;
    }
}
