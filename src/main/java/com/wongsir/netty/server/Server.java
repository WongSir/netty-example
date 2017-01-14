package com.wongsir.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/** 
* @Description: Netty中，通讯的双方建立连接后，会把数据按照ByteBuf的方式进行传输，
* 例如http协议中，就是通过HttpRequestDecoder对ByteBuf数据流进行处理，转换成http的对象。
* @author hjd
* @date 2017年1月13日 下午5:13:41 
*  
*/
public class Server {
	private int port;
	public Server(int port){
		this.port = port;
	}
	public void run() throws Exception{
		//EventLoopGroup是用来处理IO操作的多线程事件循环器
		//bossGroup 用来接收进来的连接
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		//workerGroup用来处理已经被接收的连接
		EventLoopGroup workerGroup =  new NioEventLoopGroup();
		try{
			//启动NIO服务的辅助启动类
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup,workerGroup)
				//配置Channel
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG,1024)
				.childOption(ChannelOption.SO_KEEPALIVE,true)
				.childHandler(new ChannelInitializer<SocketChannel>(){
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						//注册Handler
						ch.pipeline().addLast(new ServerHandler());
					}
				});
			//绑定端口，开始接收进来的连接
			ChannelFuture f = b.bind(port).sync();
			 System.out.println("服务器开启："+port);
			//等待服务器 socket关闭
			f.channel().closeFuture().sync();
		}finally{
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception{
		new Server(9999).run();
	}
}
