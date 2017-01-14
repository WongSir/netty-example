package com.wongsir.netty.client;

import java.util.Scanner;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


/** 
* @Description: 客户端 
* @author hjd
* @date 2017年1月13日 下午6:31:59 
*  
*/
public class Client implements Runnable {
	static ClientHandler client = new ClientHandler();
	public static void main(String[] args) throws Exception{
		new Thread(new Client()).start();
		System.out.println("客户端启动并连接服务器");
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		while(client.sendMsg(scanner.nextLine()));
	}
	@Override
	public void run() {
		String host = "127.0.0.1";
		int port = 9999;
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(client);
				}
			});
			 // Start the client.  
			ChannelFuture f = b.connect(host,port).sync();
			// Wait until the connection is closed.  
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			workerGroup.shutdownGracefully();
		}
		
	}
}
