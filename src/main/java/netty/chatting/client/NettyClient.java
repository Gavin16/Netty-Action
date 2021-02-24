package netty.chatting.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import netty.chatting.client.handlers.ClientIdleStateHandler;
import netty.chatting.client.handlers.HeartBeatResponseHandler;
import netty.chatting.client.handlers.LoginResponseHandler;
import netty.chatting.client.handlers.MessageResponseHandler;
import netty.chatting.encoder.PacketDecoder;
import netty.chatting.encoder.PacketEncoder;
import netty.chatting.packets.LoginRequestPacket;
import netty.chatting.packets.MessageRequestPacket;
import netty.chatting.protocol.PacketCoder;
import netty.chatting.spliter.Spliter;
import netty.chatting.utils.LoginUtil;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class NettyClient {

    private String host;

    private int port;

    public NettyClient(String host, int port){
        this.host = host;
        this.port = port;
    }


    public void start() throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host,port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ClientIdleStateHandler())
                                                    .addLast(new Spliter())
                                                    .addLast(new PacketDecoder())
                                                    .addLast(new LoginResponseHandler())
                                                    .addLast(new HeartBeatResponseHandler())
                                                    .addLast(new MessageResponseHandler())
                                                    .addLast(new PacketEncoder());
                        }
                    });

            ChannelFuture future = bootstrap.connect().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    System.out.println("连接成功！！");
                    Channel channel = channelFuture.channel();
                    startConsoleThread(channel);
                }

            }).sync();

            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    // 连接建立好之后,判断若登录成功则开启新的线程来接受控制台消息输入
    private void startConsoleThread(Channel channel) {
        new Thread(()->{
            while(!Thread.currentThread().isInterrupted()){
                Scanner sc = new Scanner(System.in);
                if(!LoginUtil.isLogined(channel)){
                    System.out.println("输入用户名和密码登录：");
                    String userName = sc.nextLine();
                    String psw = sc.nextLine();

                    LoginRequestPacket packet = new LoginRequestPacket();
                    packet.setUserName(userName);
                    packet.setPassword(psw);

                    channel.writeAndFlush(packet);
                    waitUtilLogin();
                }else{
                    String line = sc.nextLine();
                    // 发送消息中截取 接收用户ID 和 消息内容
                    if(StringUtils.isEmpty(line)) continue;
                    String[] idAndMsg = line.split(",");

                    MessageRequestPacket requestPacket = new MessageRequestPacket();
                    requestPacket.setToUserId(idAndMsg[0]);
                    if(idAndMsg.length > 1){
                        requestPacket.setMsg(idAndMsg[1]);
                    }
                    channel.writeAndFlush(PacketCoder.encode(requestPacket));
                }
            }
        }).start();
    }

    private void waitUtilLogin() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[]args) throws Exception {
        String host = "127.0.0.1";
        int port = 8001;

        NettyClient nettyClient = new NettyClient(host, port);
        nettyClient.start();
    }
}
