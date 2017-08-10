package com.nettychat.chat.server;

import java.io.FileInputStream;
import java.util.Properties;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import javax.servlet.http.HttpServlet;
 
import com.sun.org.apache.commons.digester.rss.Channel;


/**
 * @author lt
 * @version 1.0 
 */
public class Server extends HttpServlet{
	
	 
	
	 public static void main(String[] args) {
        /*NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workGroup = new NioEventLoopGroup(4);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            boots                    trap
                    .group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            p.addLast(new IdleStateHandler(10, 0, 0));
                            p.addLast(new LengthFieldBasedFrameDecoder(1024000, 0, 4, -4, 0));
                            p.addLast(new ServerHandler());
                        }
                    });

            Channel ch = bootstrap.bind(8888).sync().channel();
            ch.closeFuture().sync();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }*/
	
         
    	try {
			new Server().run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    } 
    
    
    
public void run() throws Exception {
        
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class) // (3)
             .childHandler(new SocketServerInitializer())  //(4)
             .option(ChannelOption.SO_BACKLOG, 128)          // (5)
             .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)
            
    		//System.out.println("SimpleChatServer 启动了");
    		
    		
    		
    		//参数
       	 	Properties properties = new Properties();
    		String base = Server.class.getResource("/")
    				.getPath();
    		try {
    			properties.load(new FileInputStream(base
    					+ "socket/socket.properties"));
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}   
    		
    		int port=Integer.parseInt(properties.getProperty("port").trim());
    		
    		
    		
            // 绑定端口，开始接收进来的连接
            ChannelFuture f = b.bind(port).sync(); // (7)

            // 等待服务器  socket 关闭 。
            // 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
            f.channel().closeFuture().sync();
            
             

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            
    		System.out.println("ChatServer 关闭了");
        }
    }
}