package netty.chatting.server.handlers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.chatting.packets.MessageRequestPacket;
import netty.chatting.packets.MessageResponsePacket;
import netty.chatting.status.Session;
import netty.chatting.utils.SessionUtil;

public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket packet) throws Exception {
        System.out.println("服务端接收到消息：[" + packet.getMsg() + "]");
        MessageResponsePacket responsePacket = new MessageResponsePacket();
        // 获取到当前用户的session
        responsePacket.setMsg(packet.getMsg());
        Session session = SessionUtil.getSession(ctx.channel());
        responsePacket.setFromUserId(session.getUserId());
        responsePacket.setFromUserName(session.getName());

        // 获取接收用户的 channel
        Channel recChannel = SessionUtil.getChannel(packet.getToUserId());
        // 向接收用户的channel 发送消息
        if(recChannel != null && SessionUtil.hasLogin(recChannel)){
            recChannel.writeAndFlush(responsePacket);
        }else{
            responsePacket.setMsg("接收用户(" + packet.getToUserId() + ")不在线，消息发送失败！");
            ctx.channel().writeAndFlush(responsePacket);
        }
    }
}
