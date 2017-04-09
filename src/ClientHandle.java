
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.InetSocketAddress; 

import io.netty.handler.codec.http.HttpRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.Channel;

public class ClientHandle {
    
    protected	HttpRequest 			_httpRequest;
	protected	ChannelHandlerContext 	_ctx;
	protected   ServicesHandler       _serviceHandler;
	
    public ClientHandle( 	ChannelHandlerContext 	ctx,
							HttpRequest 			httpRequest,
							ServicesHandler 		serviceHandler ){
		_ctx			  =	ctx;
        _httpRequest      =   httpRequest;
        _serviceHandler   =   serviceHandler;
    }
	
	public  ChannelHandlerContext getContext( ){
		return _ctx;
	}
       
    public  HttpRequest getRequest( ){
        return _httpRequest;
    }
    
    public ServicesHandler    getServiceHandler( ){
        return _serviceHandler;
    }
    
    public void passResponsetoClient( StringBuffer strbufResponse ) {
       
        _serviceHandler.setResponse( strbufResponse  );
        synchronized( _serviceHandler ){
            _serviceHandler.notify( );
        }
     }    
    
    public void terminateClientRequest(  ){
        
        passResponsetoClient( null );
    }
	
	public String	getClientIP( ){
        String  strIPAddress;
        
		InetSocketAddress socketAddress = (InetSocketAddress) _ctx.channel( ).remoteAddress( );
		InetAddress inetaddress 		= socketAddress.getAddress( );
		strIPAddress 					= inetaddress.getHostAddress( ); 
		return strIPAddress;
	}
    
}