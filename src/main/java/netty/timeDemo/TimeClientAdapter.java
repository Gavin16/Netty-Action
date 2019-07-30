package netty.timeDemo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Package: multiThread.nioTest.netty
 * @Description: client 改为继承 ChannelInboundHandlerAdapter类
 * @author: Minsky
 * @date: 2019/7/11 20:24
 * @version: v1.0
 */
public class TimeClientAdapter extends ChannelInboundHandlerAdapter{

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer(1024);
        // 测试 ByteBuf API
//        System.out.println(buffer.maxWritableBytes());
//        System.out.println(buffer.isWritable());
//        System.out.println(buffer.writableBytes());

        
        String order = "QUERY TIME";
        buffer.writeBytes(order.getBytes());
        ctx.writeAndFlush(buffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];

        buf.readBytes(bytes);
        System.out.println("服务端响应,当前时间为: " + new String(bytes,"UTF-8"));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
