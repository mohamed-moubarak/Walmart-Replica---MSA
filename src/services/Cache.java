package services;

import java.util.*;

public class Cache{

    protected static final Hashtable _htblCache = new Hashtable( );

    public static void init( ) throws Exception {
    }

    public static void loadFromDatabase( ) throws Exception{
        _htblCache.clear( );
        String  strKey,
                strValue;
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
