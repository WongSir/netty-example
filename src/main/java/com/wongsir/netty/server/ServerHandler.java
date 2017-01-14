package com.wongsir.netty.server;

import com.wongsir.netty.utils.Calculator;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/** 
* @Description: 服务端请求处理Handler
* @author hjd
* @date 2017年1月13日 下午6:08:45 
*  
*/
public class ServerHandler extends ChannelInboundHandlerAdapter{
	
	@Override
	public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
		System.out.println("ServerHandler.channelRead");
		ByteBuf in = (ByteBuf) msg;
		byte[] req = new byte[in.readableBytes()];
		//msg中存储的是ByteBuf类型的数据，把数据读到byte[]中
		in.readBytes(req);
		String body = new String(req,"utf-8");
		System.out.println("收到客户端消息：" + body);
		//释放资源，这行很关键
		in.release();
		
		//处理客户端的请求
		String calrResult = null;
		try {
			calrResult = Calculator.cal(body).toString();
		} catch (Exception e) {
			calrResult = "错误的表达式：" + e.getMessage();
		}
//		ctx.write(Unpooled.copiedBuffer(calrResult.getBytes()));
		//在当前场景下，发送的数据必须转换成ByteBuf数组
		ByteBuf encoded = ctx.alloc().buffer(4*calrResult.length());
		encoded.writeBytes(calrResult.getBytes());
		ctx.write(encoded);
		ctx.flush();
	}
	
	/**
	 * 异常处理
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws Exception{
		//当出现异常就关闭连接
		cause.printStackTrace();
		ctx.close();
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
		ctx.flush();
	}
}
