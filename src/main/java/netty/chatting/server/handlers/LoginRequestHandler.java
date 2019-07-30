package netty.chatting.server.handlers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.chatting.packets.LoginRequestPacket;
import netty.chatting.packets.LoginResponsePacket;
import netty.chatting.status.Session;
import netty.chatting.utils.LoginUtil;
import netty.chatting.utils.SessionUtil;

import java.util.Date;
import java.util.UUID;

public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket packet) throws Exception {
        LoginResponsePacket response = login(packet, ctx.channel());
        ctx.channel().writeAndFlush(response);
    }

    /**
     * 登录， 并返回响应信息
     * @param packet
     * @param channel
     * @return
     */
    private LoginResponsePacket login(LoginRequestPacket packet, Channel channel){
        System.out.println("收到客户端登录请求...");
        LoginResponsePacket response = new LoginResponsePacket();
        if(valid(packet)){
            String userId = reandomUserId();
            response.setUserId(userId);
            response.setSuccess(true);
            response.setVersion(packet.getVersion());
            response.setUserName(packet.getUserName());
            // 创建登录session 并将session保存到 Channel 中
            SessionUtil.bindSession(new Session(userId,packet.getUserName()),channel);
            System.out.println("客户端[" + userId + "] 登录成功！");
        }else{
            response.setReason("账号密码校验失败");
            response.setSuccess(false);
            System.out.println(new Date() + ": 登录失败!");
        }
        return response;
    }


    private boolean valid(LoginRequestPacket packet){
        return true;
    }

    private String reandomUserId() {
        return UUID.randomUUID().toString().split("-")[0];
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端[" + SessionUtil.getSession(ctx.channel()).getUserId() + "] 的连接失效了");
        SessionUtil.unbindSession(ctx.channel());
        LoginUtil.markAsLogout(ctx.channel());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("执行出现异常:[" + cause + "]");
        super.exceptionCaught(ctx, cause);
    }
}
