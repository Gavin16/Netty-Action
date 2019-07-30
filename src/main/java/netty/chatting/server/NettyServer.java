package netty.chatting.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import netty.chatting.encoder.PacketDecoder;
import netty.chatting.encoder.PacketEncoder;
import netty.chatting.server.handlers.*;
import netty.chatting.spliter.Spliter;

import java.net.InetSocketAddress;

public class NettyServer {

    private int port;

    public NettyServer(int port){
        this.port = port;
    }


    private void start() throws Exception{
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nsc) throws Exception {
                         nsc.pipeline().addLast(new ServerIdleStateHandler())
                                        .addLast(new Spliter())
                                        .addLast(new PacketDecoder())
                                        .addLast(new LoginRequestHandler())
                                        .addLast(new AuthHandler())
                                        .addLast(new HeartBeatRequestHandler())
                                        .addLast(new MessageRequestHandler())
                                        .addLast(new PacketEncoder());
                    }
                });

        // 增加对端口绑定的监听,打印绑定的结果
        serverBootstrap.bind().addListener(future -> {
            if(future.isSuccess()){
                System.out.println("启动成功,绑定端口号:[" + port + "]");
            }else{
                System.out.println("启动端口号:[" + port + "] 失败");
            }
        });
    }


    public static void main(String[]args) throws Exception {
        int port = 8001;

        NettyServer nettyServer = new NettyServer(port);
        nettyServer.start();
    }
}
