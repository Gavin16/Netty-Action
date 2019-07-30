package netty.chatting.client.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import multiThread.nioTest.netty.chatting.packets.HeartBeatRequestPacket;
import multiThread.nioTest.netty.chatting.packets.LoginResponsePacket;
import multiThread.nioTest.netty.chatting.utils.LoginUtil;

import java.util.concurrent.TimeUnit;

public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    private static final int HEART_BEAT_INTERVAL = 10;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 连接上服务端之后, 每隔10秒向服务端发送一次心跳检测
        sendHeartBeatMessage(ctx);
        super.channelActive(ctx);
    }

    /**
     * 定时向服务端发 HeartBeatRequestPacket 心跳检测
     *
     * 这里 定时任务为什么需要递归调用自己？？？
     * @param ctx
     */
    private void sendHeartBeatMessage(ChannelHandlerContext ctx) {
        ctx.executor().schedule(()->{
            if(ctx.channel().isActive()){
                ctx.channel().writeAndFlush(new HeartBeatRequestPacket("localhost"));
                sendHeartBeatMessage(ctx);
            }

        }, HEART_BEAT_INTERVAL, TimeUnit.SECONDS);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket packet) throws Exception {
        System.out.print("服务端响应：");
        if(packet.isSuccess()){
            System.out.println("登录成功,当前用户ID为：[" + packet.getUserId() + "]");
            LoginUtil.markAsLogin(ctx.channel());
        }else{
            System.out.println("登录失败！");
        }

    }
}
