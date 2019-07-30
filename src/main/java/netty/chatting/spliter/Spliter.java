package netty.chatting.spliter;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import netty.chatting.protocol.PacketCoder;

/**
 * 自定义 基于长度域的包拆分器， 在包拆分器中判断接收数据的当前协议是否是自定义协议, 若不是则关闭当前连接
 */
public class Spliter extends LengthFieldBasedFrameDecoder {

    private static final int MAX_FRAMELENGTH = Integer.MAX_VALUE;

    /** 长度域相对偏移 和 长度域长度 由协议定义可知 */
    private static final int LENGTH_FIELD_OFFSET = 7 ;

    private static final int LENGTH_FIELD_LENGTH = 4;

    public Spliter() {
        super(MAX_FRAMELENGTH, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        // 这里为什么不能用 in.readInt() 来读取数据？？？
        if(in.getInt(in.readerIndex()) != PacketCoder.MAGIC_NUMBER){
            ctx.channel().close();
            return null;
        }

        return super.decode(ctx,in);
    }
}
