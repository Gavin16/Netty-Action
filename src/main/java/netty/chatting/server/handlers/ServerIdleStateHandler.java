package netty.chatting.server.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 如果超过指定时间还未收到客户端发来的心跳信息或者数据,
 *  则认为客户端处于假死状态，服务端关闭假死的连接
 */
public class ServerIdleStateHandler extends IdleStateHandler {

    private static final int MAX_IDLE_TIME = 30;

    public ServerIdleStateHandler(){
        super(MAX_IDLE_TIME,0,0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        System.out.println("超过" + MAX_IDLE_TIME + "秒未收到客户端消息");
        ctx.channel().close();
    }
}
