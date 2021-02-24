package netty.demo.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class NettyClient {

    private String address;

    private int port;

    public NettyClient(String address , int port){
        this.address = address;
        this.port = port;
    }


    private void start() throws Exception{
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group).channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(address,port))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast();
                    }
                });

        ChannelFuture future = bootstrap.connect().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("客户端-->" + "连接成功！");
            }
        }).sync();

        future.channel().closeFuture().sync();
    }




    public static void main(String[] args) throws Exception {
        String address = "127.0.0.1";
        int port = 9096;
        NettyClient client = new NettyClient(address, port);
        client.start();
    }
}
