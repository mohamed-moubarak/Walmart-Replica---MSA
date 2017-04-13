package commands.product;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

import commands.Command;

class AddProductCmd extends Command implements Runnable {

    public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

        StringBuffer       strbufResult;
        CallableStatement  sqlProc;
        String             strName,
                           strDescription;
        Double             dblPrice;
        Integer            intStock;

        strName        = (String)mapUserData.get( "name" );
        strDescription = (String)mapUserData.get( "description" );
        dblPrice       = (Double)mapUserData.get( "price" );
        intStock       = (Integer)mapUserData.get( "stock" );

        if( strName == null || strName.trim( ).length( ) == 0 ||
                intStock == null )
            return null;

        sqlProc = connection.prepareCall("{?=call addProduct(?,?,?,?)}");
        sqlProc.registerOutParameter(1, Types.INTEGER );
        sqlProc.setString(2, strName );
        sqlProc.setString(3, strDescription );
        sqlProc.setDouble(4, dblPrice );
        sqlProc.setInt(5, intStock );

        sqlProc.execute( );
        strbufResult = makeJSONResponseEnvelope( sqlProc.getInt( 1 ) , null, null );
        sqlProc.close( );

        return strbufResult;
    }
}
