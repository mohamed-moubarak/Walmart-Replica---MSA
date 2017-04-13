import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public final class Services {

	public static final boolean SSL = System.getProperty("ssl") != null;
	public static final int PORT = Integer.parseInt(SSL ? "88" : "8080");

	protected static Controller _controller;

	public static void main(String[] args) throws Exception {

		// Configure SSL.
		final SslContext sslCtx;
		if (SSL) {
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
		} else {
			sslCtx = null;
		}

		// Configure the server.
		EventLoopGroup bossGroup = new NioEventLoopGroup(5);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			_controller = new Controller();
			_controller.init();

			Cache.init();
			Cache.loadFromDatabase();
			
			ServerBootstrap serverBoot = new ServerBootstrap();
			serverBoot.group(bossGroup, workerGroup);
			serverBoot.channel(NioServerSocketChannel.class);
			serverBoot.handler(new LoggingHandler(LogLevel.TRACE));
			serverBoot.childHandler(new ServicesInitializer(sslCtx, _controller));
			Channel channel = serverBoot.bind(PORT).sync().channel();
			System.err.println("Services running on  " + (SSL ? "https" : "http") + "://127.0.0.1:" + PORT + '/');
			channel.closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}