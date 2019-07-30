package netty.chatting.protocol;

public interface Command {

    byte LOGIN_REQUEST = 1;
    byte LOGIN_RESPONSE = 2;
    byte MESSAGE_REQUEST = 3;
    byte MESSAGE_RESPONSE = 4;
    byte HEARTBEAT_REQUEST = 5;
    byte HEARTBEAT_RESPONSE = 6;
}
