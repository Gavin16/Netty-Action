package netty.chatting.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import netty.chatting.packets.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义的 编码解码工具类, 由于代码重用较多, 改为 继承 MessageToByteEncoder 和 ByteToMessageDecoder 后:
 * 实现方法调整如下：
 *
 */
public class PacketCoder{

    public static final int MAGIC_NUMBER = 0x12345678;
    /** 服务端解码序列化方式列表 */
    private static final Map<Byte,Serializer> SERIALIZER_MAP;
    /** 服务端能支持的packet 类型列表, 一个指令对应一个包裹类型 */
    private static final Map<Byte,Class<? extends Packet>> PACKETTYPE_MAP;

    static {
        SERIALIZER_MAP = new HashMap<>();
        JSONSerializer jsonSerializer = new JSONSerializer();
        SERIALIZER_MAP.put(Serializer.DEFAULT.getSerializerAlgorithm(),jsonSerializer);

        PACKETTYPE_MAP = new HashMap<>();
        PACKETTYPE_MAP.put(Command.LOGIN_REQUEST, LoginRequestPacket.class);
        PACKETTYPE_MAP.put(Command.LOGIN_RESPONSE, LoginResponsePacket.class);
        PACKETTYPE_MAP.put(Command.MESSAGE_REQUEST, MessageRequestPacket.class);
        PACKETTYPE_MAP.put(Command.MESSAGE_RESPONSE, MessageResponsePacket.class);
        PACKETTYPE_MAP.put(Command.HEARTBEAT_REQUEST, HeartBeatRequestPacket.class);
        PACKETTYPE_MAP.put(Command.HEARTBEAT_RESPONSE, HeartBeatResponsePacket.class);
    }

    /**
     * @Version 1.0
     * @param packet
     * @return
     */
    public static ByteBuf encode(Packet packet){
        byte[] bytes = Serializer.DEFAULT.serialize(packet);
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        buffer.writeInt(MAGIC_NUMBER);
        buffer.writeByte(packet.getVersion());
        buffer.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        buffer.writeByte(packet.getCommand());
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
        return buffer;
    }

    /**
     * 若编码器继承 MessageToByteEncoder 编码器  则可以在覆写的encode方法中调用该方法
     * @Version 2.0
     * @param buf
     * @param packet
     */
    public static void encode(ByteBuf buf, Packet packet){
        byte[] bytes = Serializer.DEFAULT.serialize(packet);
        buf.writeInt(MAGIC_NUMBER);
        buf.writeByte(packet.getVersion());
        buf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        buf.writeByte(packet.getCommand());
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }


    /**
     * @Version 1.0
     * @param buffer
     * @return
     */
    public static Packet decode(ByteBuf buffer){
        // 跳过协议标识 和 版本号
        buffer.skipBytes(4);
        buffer.skipBytes(1);

        // 序列化算法
        byte serializerAlgorithm = buffer.readByte();
        // 连接指令
        final byte command = buffer.readByte();
        // 发送实际数据长度
        int dataLen = buffer.readInt();
        // 实际数据读如 byte数组
        byte[] bytes = new byte[dataLen];
        buffer.readBytes(bytes);

        // 获取序列化算法 和指令
        Serializer serializer = getSerializer(serializerAlgorithm);
        Class<? extends Packet> clazz = getPacketType(command);

        if(null != serializer && null != clazz){
            Packet deserialize = serializer.deserialize(clazz, bytes);
            return deserialize;
        }
        return null;
    }


    private static Class<? extends Packet> getPacketType(byte command) {
        return PACKETTYPE_MAP.get(command);
    }

    private static Serializer getSerializer(byte serializerAlgorithm) {
        return SERIALIZER_MAP.get(serializerAlgorithm);
    }
}
