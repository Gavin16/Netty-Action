package netty.timeDemo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @Package: multiThread.nioTest.netty
 * @Description: TODO
 * @author: Minsky
 * @date: 2019/7/11 20:24
 * @version: v1.0
 */
public class TimeClient {

    private String host;

    private int port;

    public TimeClient(String host,int port){
        this.host = host ;
        this.port = port ;
    }

    public void start() throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(group)
                    .channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host,port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new TimeClientAdapter());
                        }
                    });

            ChannelFuture future = bootstrap.connect();

//            bootstrap.connect().addListener(new ChannelFutureListener() {
//                @Override
//                public void operationComplete(ChannelFuture channelFuture) throws Exception {
//                    System.out.println("连接成功！！");
//                }
//            });
//            bootstrap.bind(host,port);
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    System.out.println("连接成功！！");
                }
            }).sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[]args) throws Exception{
        String host = "127.0.0.1";
        int port = 9093;

        new TimeClient(host,port).start();
    }
}
