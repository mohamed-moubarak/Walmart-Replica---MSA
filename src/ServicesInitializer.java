
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

import io.netty.channel.socket.SocketChannel;

import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsHandler;

import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpObjectAggregator;

import io.netty.handler.ssl.SslContext;

public class ServicesInitializer extends ChannelInitializer<SocketChannel> {

    protected final SslContext      _sslCtx;
    
    protected final Controller    _controller;

    public ServicesInitializer(SslContext sslCtx , Controller controller) {
        _sslCtx       =   sslCtx;
        _controller   =   controller;
    }

    @Override
    public void initChannel(SocketChannel socChannel) {
    
        CorsConfig corsConfig = CorsConfig.withAnyOrigin().build();
     
        ChannelPipeline pipeLine = socChannel.pipeline( );
        if(_sslCtx != null) {
            pipeLine.addLast( _sslCtx.newHandler( socChannel.alloc( ) ) );
        }
        
		pipeLine.addLast(new HttpRequestDecoder( ) );
        
		// Uncomment the following line if you don't want to handle HttpChunks.
        pipeLine.addLast(new HttpObjectAggregator(1048576));
        
		pipeLine.addLast(new HttpResponseEncoder());
        
		// Remove the following line if you don't want automatic content compression.
        //p.addLast(new HttpContentCompressor());
        pipeLine.addLast("1", new HttpStaticFileServerHandler( ) ); 
        pipeLine.addLast(new CorsHandler(corsConfig));
        pipeLine.addLast("2", new ServicesHandler( _controller ) );


    }
}