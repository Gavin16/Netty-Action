package netty.chatting.utils;

import io.netty.channel.Channel;
import netty.chatting.status.Attributes;
import netty.chatting.status.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  将session 保存在 channel 的 attributes 中
 *  然后将用户的userId 保存在session , userId 与 channel 的映射保存在 userIdChannelMap 中
 *
 *  由于每个用户都有自己的 channel, 因此各个session其实是保存在不同的channel的，仅需要 channel.attr(AttributeKey) 就能获取到自己独有的 session
 *  然后在关闭连接的时候 unbindSession
 *
 */
public class SessionUtil {

    private static final Map<String, Channel> userIdChannelMap =  new ConcurrentHashMap<>();

    /** 登录时将当前用户的session 保存到channel中 */
    public static void bindSession(Session session, Channel channel){
        userIdChannelMap.put(session.getUserId(),channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    public static void unbindSession(Channel channel){
        if(hasLogin(channel)){
            userIdChannelMap.remove(getSession(channel).getUserId());
            channel.attr(Attributes.SESSION).set(null);
        }
    }

    public static boolean hasLogin(Channel channel){
        return channel.hasAttr(Attributes.SESSION);
    }

    public static Session getSession(Channel channel){
        return channel.attr(Attributes.SESSION).get();
    }

    /** 获取接收当前消息的用户的channel  */
    public static Channel getChannel(String userId){
        return userIdChannelMap.get(userId);
    }

}
