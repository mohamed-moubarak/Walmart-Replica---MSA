
import java.sql.*;
import java.util.*;

public class Cache{
    
    protected static final Hashtable _htblCache = new Hashtable( );
    
    protected static Connection   _connection;
    
    public static void init( ) throws Exception {
      // connectToDB( "localhost", 5432, "thedb", "postgres", "thepassword" );
    }
	
    public static void loadFromDatabase( ) throws Exception{
        _htblCache.clear( );
        String  strKey, 
                strValue;
    
        /*_connection.setAutoCommit( false );
        CallableStatement     sqlProc;
        sqlProc = _connection.prepareCall("{?=call getAllSessions( )}");
        sqlProc.registerOutParameter( 1, Types.OTHER );
        sqlProc.execute( );
        ResultSet resultSet = (ResultSet) sqlProc.getObject( 1 );
        while( resultSet.next( ) ) {
            strValue=   Integer.toString( resultSet.getInt( 2 ) );
            strKey  =   resultSet.getString( 3 );
            System.out.println( strKey + " .... " + strValue );
            _htblCache.put( strKey , strValue );
        }  
        resultSet.close( );
        
		sqlProc.close( );
*/		
	}
        
    protected static void connectToDB(  String strAddress, int nPort, 
                                        String strDBName, 
                                        String strUserName, String strPassword ) throws Exception {
        Class.forName("org.postgresql.Driver");
        _connection = DriverManager.getConnection( 
        "jdbc:postgresql://"+ strAddress + ":" + nPort + "/" + strDBName,
        strUserName, strPassword );
    }
	
	public static void addSession( String strSessionID, String strEmail ){
        _htblCache.put( strSessionID , strEmail );
    }
    
    public static void removeSession( String strSessionID ){
        _htblCache.remove( strSessionID );
    }
    
    public static boolean sessionExists( String strSessionID ){
		if( strSessionID == null )
			return false;
        System.err.println(" you passed " + strSessionID + " to Cache.sessionExists " );
        System.err.println(" checking existance " + _htblCache.get( strSessionID  ) );
        if( _htblCache.get( strSessionID ) == null )
            return false;
       
        return true;
    }
	
	public static String getUserEmail( String strSessionID ){
		return (String)_htblCache.get( strSessionID );
	}
    

}