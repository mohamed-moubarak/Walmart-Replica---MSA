package commands.product;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

import commands.Command;

class UpdateStockCmd extends Command implements Runnable {

    public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

        StringBuffer       strbufResult;
        CallableStatement  sqlProc;
        String             strName,
                           strDescription;
        Double             dblPrice;
        Integer            intStock,
        					intID;

        intID    = (Integer)mapUserData.get( "PID" );
        intStock = (Integer)mapUserData.get( "stock" );

        if( intStock == null )
            return null;

        sqlProc = connection.prepareCall("{?=call updateStock(?,?)}");
        sqlProc.registerOutParameter(1, Types.INTEGER );
        sqlProc.setInt(2, intID );
        sqlProc.setInt(3, intStock );

        sqlProc.execute( );
        strbufResult = makeJSONResponseEnvelope( sqlProc.getInt( 1 ) , null, null );
        sqlProc.close( );

        return strbufResult;
    }
}
