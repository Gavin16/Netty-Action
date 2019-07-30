package netty.chatting.packets;

import lombok.Data;
import netty.chatting.protocol.Command;
import netty.chatting.protocol.Packet;

@Data
public class HeartBeatRequestPacket extends Packet {
    private String host;

    public HeartBeatRequestPacket(String host) {
        this.host = host;
    }

    @Override
    public byte getCommand() {
        return Command.HEARTBEAT_REQUEST;
    }
}
