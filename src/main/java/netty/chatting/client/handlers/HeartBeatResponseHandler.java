package netty.chatting.client.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.chatting.packets.HeartBeatResponsePacket;

public class HeartBeatResponseHandler extends SimpleChannelInboundHandler<HeartBeatResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatResponsePacket packet) throws Exception {
        System.out.println("接收到服务端的心跳信息");
    }
}
