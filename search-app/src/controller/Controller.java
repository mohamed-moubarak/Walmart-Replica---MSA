package controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.boon.json.*;
import io.netty.channel.ChannelHandlerContext;

import commands.Dispatcher;
import services.Cache;

public class Controller implements ParseListener {

    protected   Dispatcher        _dispatcher;
    protected   ExecutorService   _threadPoolParsers;

    public Controller( ){
    }

    public void init( ) throws Exception{
        _dispatcher = new Dispatcher( );
        _dispatcher.init( );
        _threadPoolParsers = Executors.newFixedThreadPool( 10 );
    }

    public void execRequest( ClientHandle clientHandle ){
        _threadPoolParsers.execute( new RequestParser( this, clientHandle ) );
    }

    public synchronized void parsingFinished(   ClientHandle 	clientHandle,
            ClientRequest   clientRequest ){
        try{
            String strAction;
            strAction = clientRequest.getAction( );
<<<<<<< HEAD:src/controller/Controller.java
            if( strAction.equalsIgnoreCase( "attemptLogin" ) ||
                    strAction.equalsIgnoreCase( "addUserSimple" ) ||
                    strAction.equalsIgnoreCase( "listProducts" ) ||
                    strAction.equalsIgnoreCase( "addProduct" ) ||
                    strAction.equalsIgnoreCase( "updateStock" ) ||
                    strAction.equalsIgnoreCase( "decreaseStock" ) ||
                    strAction.equalsIgnoreCase( "editInfo" ) ){
=======
            if( strAction.equalsIgnoreCase( "searchProducts" ) ) {
>>>>>>> search-app:search-app/src/controller/Controller.java
                _dispatcher.dispatchRequest( clientHandle , clientRequest  );
            }
            else{
                String strSessionID;
                strSessionID = clientRequest.getSessionID( );
                if( strSessionID == null ||
                        strSessionID.length( ) == 0 ||
                        !Cache.sessionExists( strSessionID ) ){
                    clientHandle.terminateClientRequest( );

                        }
                else{
                    _dispatcher.dispatchRequest( clientHandle , clientRequest );
                }
            }
        }
        catch( Exception exp ){
            clientHandle.terminateClientRequest( );
            System.err.println( exp.toString( ) );
        }
    }

    public synchronized void parsingFailed( ClientHandle clientHandle, String strError ){
        clientHandle.terminateClientRequest( );
        System.err.println( "An error in parsing " + strError );
    }
}
