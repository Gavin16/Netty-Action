package netty.chatting.packets;

import lombok.Data;
import netty.chatting.protocol.Command;
import netty.chatting.protocol.Packet;

@Data
public class LoginResponsePacket extends Packet {

    private boolean success;

    private String userId;

    private String msg;

    private String userName;

    private String reason;


    @Override
    public byte getCommand() {
        return Command.LOGIN_RESPONSE;
    }
}
