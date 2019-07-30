package netty.timeDemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Package: multiThread.nioTest.netty
 * @Description: TODO
 * @author: Minsky
 * @date: 2019/7/11 20:05
 * @version: v1.0
 */
@ChannelHandler.Sharable  // @ChannelHandler.Sharable 注解将当前handler 定义为单例模式
public class TimeServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;

//
//        byte[] bytes = new byte[buf.readableBytes()];
        String res = buf.toString(CharsetUtil.UTF_8);
        System.out.println("received context is : " + res);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String currnetTime = "QUERY TIME".equalsIgnoreCase(res) ? sdf.format(new Date()) : "BAD ORDER";

        buf.clear().writeBytes(currnetTime.getBytes());

        Channel channel = ctx.channel();
        // 获取channel 中的 key-value
        AttributeKey<String> key = AttributeKey.valueOf("bootStrapType");
        Attribute<String> attr = channel.attr(key);
        String str = attr.get();
        System.out.println(str);


        channel.write(buf);
//        ctx.write(buf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();
        String hello = "来自服务端的测试消息";
        buffer.writeBytes(hello.getBytes());

        ctx.writeAndFlush(buffer);
    }
}
