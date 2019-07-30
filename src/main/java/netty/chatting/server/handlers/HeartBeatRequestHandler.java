package netty.chatting.server.handlers;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.chatting.packets.HeartBeatRequestPacket;
import netty.chatting.packets.HeartBeatResponsePacket;

@ChannelHandler.Sharable // @ChannelHandler.Sharable 注解将当前handler 定义为单例模式,对于无状态的Handler 可以采用该注解
public class HeartBeatRequestHandler extends SimpleChannelInboundHandler<HeartBeatRequestPacket> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatRequestPacket packet) throws Exception {
        System.out.println("接收到来自[" + packet.getHost() +"] 的心跳信息");
        // 回复客户端心跳请求, 相当于向客户端也发送的了心跳信息
        ctx.channel().writeAndFlush(new HeartBeatResponsePacket("收到心跳信息"));
    }

}
