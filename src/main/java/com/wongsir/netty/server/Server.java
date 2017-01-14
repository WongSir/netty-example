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
* @Description: Netty�У�ͨѶ��˫���������Ӻ󣬻�����ݰ���ByteBuf�ķ�ʽ���д��䣬
* ����httpЭ���У�����ͨ��HttpRequestDecoder��ByteBuf���������д���ת����http�Ķ���
* @author hjd
* @date 2017��1��13�� ����5:13:41 
*  
*/
public class Server {
	private int port;
	public Server(int port){
		this.port = port;
	}
	public void run() throws Exception{
		//EventLoopGroup����������IO�����Ķ��߳��¼�ѭ����
		//bossGroup �������ս���������
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		//workerGroup���������Ѿ������յ�����
		EventLoopGroup workerGroup =  new NioEventLoopGroup();
		try{
			//����NIO����ĸ���������
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup,workerGroup)
				//����Channel
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG,1024)
				.childOption(ChannelOption.SO_KEEPALIVE,true)
				.childHandler(new ChannelInitializer<SocketChannel>(){
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						//ע��Handler
						ch.pipeline().addLast(new ServerHandler());
					}
				});
			//�󶨶˿ڣ���ʼ���ս���������
			ChannelFuture f = b.bind(port).sync();
			 System.out.println("������������"+port);
			//�ȴ������� socket�ر�
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
