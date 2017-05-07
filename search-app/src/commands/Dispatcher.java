package commands;

import java.io.*;
import java.util.*;
import java.math.*;
import java.lang.reflect.*;
import java.util.concurrent.*;

import controller.ClientHandle;
import controller.ClientRequest;

public class Dispatcher{
    protected Hashtable         _htblCommands;
    protected ExecutorService   _threadPoolCmds;

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
        cmd.init(clientHandle, clientRequest);
        _threadPoolCmds.execute((Runnable) cmd);
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
        loadThreadPool( );
        loadCommands( );
    }
}
