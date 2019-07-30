package netty.timeDemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

/**
 * @Package: multiThread.nioTest.netty
 * @Description: TODO
 * @author: Minsky
 * @date: 2019/7/11 20:05
 * @version: v1.0
 */
public class TimeServer {

    private final int port;

    public TimeServer(int port){
        this.port = port;
    }

    public static void main(String[]args){
        new TimeServer(9093).start();
    }


    public void start(){
        TimeServerHandler timeServer = new TimeServerHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            ServerBootstrap boot = new ServerBootstrap();
            boot.group(group).channel(NioServerSocketChannel.class)
                            .localAddress(new InetSocketAddress(port))
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel socketChannel) throws Exception {
                                    socketChannel.pipeline().addLast(timeServer);
                                }
                            });

            boot.childAttr(AttributeKey.newInstance("bootStrapType"),"ServerBootStrap");
            ChannelFuture f = boot.bind().sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }
}
