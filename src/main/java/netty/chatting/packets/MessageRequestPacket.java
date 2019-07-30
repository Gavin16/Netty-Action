package netty.chatting.packets;

import lombok.Data;
import netty.chatting.protocol.Command;
import netty.chatting.protocol.Packet;

@Data
public class MessageRequestPacket extends Packet {

    private String toUserId;

    private String msg;

    @Override
    public byte getCommand() {
        return Command.MESSAGE_REQUEST;
    }
}
