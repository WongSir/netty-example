package com.wongsir.netty.server;

import com.wongsir.netty.utils.Calculator;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/** 
* @Description: �����������Handler
* @author hjd
* @date 2017��1��13�� ����6:08:45 
*  
*/
public class ServerHandler extends ChannelInboundHandlerAdapter{
	
	@Override
	public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
		System.out.println("ServerHandler.channelRead");
		ByteBuf in = (ByteBuf) msg;
		byte[] req = new byte[in.readableBytes()];
		//msg�д洢����ByteBuf���͵����ݣ������ݶ���byte[]��
		in.readBytes(req);
		String body = new String(req,"utf-8");
		System.out.println("�յ��ͻ�����Ϣ��" + body);
		//�ͷ���Դ�����кܹؼ�
		in.release();
		
		//����ͻ��˵�����
		String calrResult = null;
		try {
			calrResult = Calculator.cal(body).toString();
		} catch (Exception e) {
			calrResult = "����ı��ʽ��" + e.getMessage();
		}
//		ctx.write(Unpooled.copiedBuffer(calrResult.getBytes()));
		//�ڵ�ǰ�����£����͵����ݱ���ת����ByteBuf����
		ByteBuf encoded = ctx.alloc().buffer(4*calrResult.length());
		encoded.writeBytes(calrResult.getBytes());
		ctx.write(encoded);
		ctx.flush();
	}
	
	/**
	 * �쳣����
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws Exception{
		//�������쳣�͹ر�����
		cause.printStackTrace();
		ctx.close();
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
		ctx.flush();
	}
}
