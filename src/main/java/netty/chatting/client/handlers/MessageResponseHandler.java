package netty.chatting.client.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.chatting.packets.MessageResponsePacket;

public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket packet) throws Exception {
        System.out.println("接收到来自：[" + packet.getFromUserName()+ "]--{"+ packet.getFromUserId()
                + "] 的消息 --> [" + packet.getMsg() + "]");
    }
}
