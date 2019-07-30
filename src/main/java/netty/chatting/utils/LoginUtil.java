package netty.chatting.utils;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import netty.chatting.status.Attributes;

public class LoginUtil {

    public static void markAsLogin(Channel channel){
        channel.attr(Attributes.LOGIN).set(true);
    }

    public static  boolean isLogined(Channel channel){
        Attribute<Boolean> loginAttr = channel.attr(Attributes.LOGIN);
        return loginAttr.get() != null && loginAttr.get();
    }

    public static void markAsLogout(Channel channel){
        channel.attr(Attributes.LOGIN).set(false);
    }

}
