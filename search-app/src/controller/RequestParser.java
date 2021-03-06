package controller;

import java.util.HashMap;
import java.util.Map;

import org.boon.json.*;

import io.netty.handler.codec.http.multipart.MixedAttribute;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;

public class RequestParser implements Runnable {

    protected   ParseListener       _parseListener;
    protected   ClientHandle		_clientHandle;

    public RequestParser( ParseListener parseListener , ClientHandle clientHandle ){
        _parseListener  =	parseListener;
        _clientHandle	=	clientHandle;
    }

    public void run( ){
        try{
        _parseListener.parsingFinished(_clientHandle, _clientHandle.getRequest());
        }
        catch( Exception exp ){
            _parseListener.parsingFailed( _clientHandle, "Exception while parsing JSON object " + exp.toString( ) );
        }
    }

}
