package commands.user;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Map;

import commands.Command;

class EditInfoCmd extends Command implements Runnable {

    public StringBuffer execute(Connection connection,  Map<String, Object> mapUserData ) throws Exception {

        StringBuffer        strbufResult;
        CallableStatement   sqlProc;
        String              strEmail,
                            strPassword,
                            strFirstName,
                            strLastName,
                            strnewPassword,
                            strPicturePath,
                            strGender;
        strEmail    =   (String)mapUserData.get( "email" );
        strPassword =   (String)mapUserData.get( "password" );
        strnewPassword =   (String)mapUserData.get( "newpassword" );
        strFirstName=   (String)mapUserData.get( "firstName" );
        strLastName =   (String)mapUserData.get( "lastName" );
        strPicturePath =   (String)mapUserData.get( "picturepath" );
        strGender =   (String)mapUserData.get( "gender" );

        if( strEmail == null || strEmail.trim( ).length( ) == 0 ||
                strPassword == null || strPassword.trim( ).length( ) == 0  ||
                strFirstName == null || strFirstName.trim( ).length( ) == 0  ||
                strLastName == null || strLastName.trim( ).length( ) == 0 )
            return null;

        if( !EmailVerifier.verify( strEmail ) )
            return null;

        sqlProc = connection.prepareCall("{?=call editinfo(?,?,?,?,?,?,?)}");
        sqlProc.registerOutParameter(1, Types.INTEGER );
        sqlProc.setString(2, strEmail );
        sqlProc.setString(3, strPassword );
        sqlProc.setString(4, strnewPassword );
        sqlProc.setString(5, strFirstName );
        sqlProc.setString(6, strLastName );
        sqlProc.setString(7, strPicturePath );
        sqlProc.setString(8, strGender );

        sqlProc.execute( );
        strbufResult = makeJSONResponseEnvelope( sqlProc.getInt( 1 ) , null, null );
        sqlProc.close( );

        return strbufResult;
    }
}
