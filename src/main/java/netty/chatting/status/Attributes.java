package netty.chatting.status;

import io.netty.util.AttributeKey;

public class Attributes {

   public static AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");

   public static AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
}
