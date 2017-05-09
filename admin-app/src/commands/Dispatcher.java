package commands;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.math.*;
import java.lang.reflect.*;
import java.util.concurrent.*;

import com.zaxxer.hikari.HikariDataSource;

import controller.ClientHandle;
import controller.ClientRequest;

public class Dispatcher{
    protected Hashtable         _htblCommands;
    protected ExecutorService   _threadPoolCmds;
    protected HikariDataSource  _hikariDataSource;

    public Dispatcher( ){
    }

    public void dispatchRequest( ClientHandle  clientHandle,
            ClientRequest clientRequest ) throws Exception{

        Command       cmd;
        String        strAction;

        strAction = clientRequest.getAction( );

        Class<?> klass      = (Class<?>) _htblCommands.get(strAction);
        Constructor<?> ctor = klass.getDeclaredConstructor();
        ctor.setAccessible(true);

        cmd = (Command) ctor.newInstance();
        cmd.init(_hikariDataSource, clientHandle, clientRequest);
        _threadPoolCmds.execute((Runnable) cmd);
    }

    protected void loadHikari( String strAddress, int nPort,
            String strDBName,
            String strUserName, String strPassword   ){

        _hikariDataSource 	= new HikariDataSource(  );
        _hikariDataSource.setJdbcUrl( "jdbc:postgresql://" + strAddress + ":" + nPort + "/" + strDBName  );
        _hikariDataSource.setUsername( strUserName );
        _hikariDataSource.setPassword( strPassword );
    }

    protected void loadCommands( ) throws Exception{
        _htblCommands   = new Hashtable( );
        Properties prop = new Properties();
        InputStream in  = getClass().getResourceAsStream("config/commands.properties");
        prop.load( in );
        in.close( );
        Enumeration enumKeys = prop.propertyNames( );
        String  strActionName,
                strClassName;

        while( enumKeys.hasMoreElements( ) ){
            strActionName = (String)enumKeys.nextElement(  );
            strClassName  = (String)prop.get( strActionName );
            Class<?> innerClass = Class.forName(strClassName);
            _htblCommands.put( strActionName, innerClass );
        }
    }

    protected void loadThreadPool( ){
        _threadPoolCmds = Executors.newFixedThreadPool( 20 );
    }

    public void init( ) throws Exception{
        Properties prop = new Properties();
        InputStream in  = getClass().getResourceAsStream("config/database.properties");
        prop.load( in );
        in.close( );

        String host = prop.getProperty("host");
        int port = Integer.parseInt(prop.getProperty("port"));
        String database = prop.getProperty("database");
        String user = prop.getProperty("user");
        String password = prop.getProperty("password");

        loadHikari( host, port, database, user, password );
        loadThreadPool( );
        loadCommands( );
    }
}
