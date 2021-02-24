package netty.demo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class NettyServer {

    private int port;

    public NettyServer(int port){
        this.port = port;
    }


    private void start() throws Exception{
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        serverBootstrap.group(bossGroup,workGroup)
                        .channel(NioServerSocketChannel.class)
                        .localAddress(new InetSocketAddress(port))
                        .option(ChannelOption.SO_BACKLOG, 1024)
                        .childOption(ChannelOption.SO_KEEPALIVE,true)
                        .childOption(ChannelOption.TCP_NODELAY,true)
                        .childHandler(new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel ch) throws Exception {
                                // 设置入站和出站事件
                                ch.pipeline().addLast();
                            }
                        });

        serverBootstrap.bind(port).addListener(future -> {
            System.out.println("服务端-->");
            if(future.isSuccess()){
                System.out.println("启动成功,绑定端口号:[" + port + "]");
            }else{
                System.out.println("启动端口号:[" + port + "] 失败");
            }
        });

    }


    public static void main(String[] args) throws Exception{
        int port = 9096;
        NettyServer server = new NettyServer(port);

        server.start();
    }
}
