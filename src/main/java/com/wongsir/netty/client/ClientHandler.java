package com.wongsir.netty.client;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/** 
* @Description: 客户端请求处理Handler
* @author hjd
* @date 2017年1月13日 下午6:37:31 
*  
*/
public class ClientHandler extends ChannelInboundHandlerAdapter{
	ChannelHandlerContext ctx;
	
	/**
	 * tcp链路建立成功后调用
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception{
		this.ctx = ctx;
	}
	public boolean sendMsg(String msg){
		System.out.println("客户端发送消息：" + msg);
		byte[] req = msg.getBytes();
		ByteBuf m = Unpooled.buffer(req.length);
		m.writeBytes(req);
		ctx.writeAndFlush(m);
		return msg.equals("q")?false:true;
	}
	
	/**
	 * 收到服务器消息后调用
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx,Object msg) throws UnsupportedEncodingException{
		System.out.println("ClientHandler.channelRead");
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req,"utf-8");
		System.out.println("服务器消息：" + body);
	}
	
	/**
	 * 发生异常时调用
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
		cause.printStackTrace();
		ctx.close();
	}
}
