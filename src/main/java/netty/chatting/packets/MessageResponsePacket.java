package netty.chatting.packets;

import lombok.Data;
import netty.chatting.protocol.Command;
import netty.chatting.protocol.Packet;

@Data
public class MessageResponsePacket extends Packet {

    private String msg;

    private String fromUserId;

    private String fromUserName;

    @Override
    public byte getCommand() {
        return Command.MESSAGE_RESPONSE;
    }
}
