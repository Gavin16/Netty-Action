package netty.chatting.server.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.chatting.utils.SessionUtil;

/**
 * 每次发送消息前, 判断用户是否登录,若未登录则关闭连接
 * 若判断登录，则移除本逻辑处理器(登录时只做一次校验,目的在于找出并关闭那些未登录的连接)
 */
public class AuthHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(SessionUtil.hasLogin(ctx.channel())){
            ctx.pipeline().remove(this);
            super.channelRead(ctx,msg);
        }else{
            ctx.channel().close();
        }

    }
}
