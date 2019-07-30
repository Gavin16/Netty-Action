package netty.chatting.packets;

import lombok.Data;
import netty.chatting.protocol.Command;
import netty.chatting.protocol.Packet;

@Data
public class HeartBeatResponsePacket extends Packet {

    private String msg;

    public HeartBeatResponsePacket(String msg) {
        this.msg = msg;
    }

    @Override
    public byte getCommand() {
        return Command.HEARTBEAT_RESPONSE;
    }
}
