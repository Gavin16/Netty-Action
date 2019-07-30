package netty.chatting.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.chatting.packets.LoginRequestPacket;
import netty.chatting.packets.LoginResponsePacket;
import netty.chatting.packets.MessageRequestPacket;
import netty.chatting.packets.MessageResponsePacket;
import netty.chatting.protocol.Packet;
import netty.chatting.protocol.PacketCoder;
import netty.chatting.utils.LoginUtil;

/**
 * 单独一个 ServerHandler 对请求进行处理, 会让代码逻辑显得很臃肿(表现为很多if else 语句)
 * 改进后, 采用不同的handler 对不同的请求packet 做处理，替代方案为：
 * 分别使用 LogingRequestHandler 和 MessageRequestHandler 处理请求
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        Packet decode = PacketCoder.decode(buf);

        LoginResponsePacket response = new LoginResponsePacket();
        if(decode instanceof LoginRequestPacket){
            LoginRequestPacket request = (LoginRequestPacket) decode;
            System.out.println("收到客户端登录请求...");

            response = checkLoginStatus(request,ctx.channel());
            ctx.writeAndFlush(PacketCoder.encode(response));
            return ;
        }if(decode instanceof MessageRequestPacket){
            // 检查登录状态,如果已掉线则提示重新登录
            boolean logined = LoginUtil.isLogined(ctx.channel());
            ByteBuf toWrite;
            if(logined){
                MessageRequestPacket request = (MessageRequestPacket) decode;
                System.out.println("收到客户端发送消息：["+request.getMsg() + "]");
                toWrite = PacketCoder.encode(messageToResponse(request.getMsg()));
            }else{
                toWrite = loginAgain();
            }
            ctx.writeAndFlush(toWrite);
            return ;
        }else{
            response.setMsg("不支持的操作类型");
        }
        ctx.writeAndFlush(PacketCoder.encode(response));
    }

    private ByteBuf loginAgain() {
        MessageResponsePacket responsePacket = new MessageResponsePacket();
        responsePacket.setMsg("登录失效,请重新登录");
        return PacketCoder.encode(responsePacket);
    }

    private MessageResponsePacket messageToResponse(String msg) {
        MessageResponsePacket responsePacket = new MessageResponsePacket();
        responsePacket.setMsg(msg);
        return responsePacket;
    }


    private LoginResponsePacket checkLoginStatus(LoginRequestPacket request, Channel channel) {
        LoginResponsePacket response = new LoginResponsePacket();

        if(request.getUserName().equalsIgnoreCase("zhsan") &&
                request.getPassword().equalsIgnoreCase("password")){
            response.setSuccess(true);
            // 判断登录成功，保存登录状态
            LoginUtil.markAsLogin(channel);
            System.out.println("登录成功!");
        }else{
            response.setMsg("用户名或密码错误！");
        }
        return response;
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
