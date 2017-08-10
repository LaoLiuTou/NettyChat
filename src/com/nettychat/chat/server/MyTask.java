package com.nettychat.chat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.nettychat.utils.RedisUtil;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
 



public class MyTask extends TimerTask { 
	 private static boolean isRunning = false;  
     private ServletContext context = null; 
     //是否启用redis负载
     public static boolean redisFlag; 
     //本服务socket地址
     public static String socketIp; 
     public static String socketPort; 
     public static String nettype; 
      
     //towired
 	 //private IKung_messageService iKung_messageService;
     
     public MyTask(ServletContext servletContext) { 
         this.context = servletContext; 
         try {
           Properties props = new Properties();  
      	
           props.load(RedisUtil.class.getClassLoader().getResourceAsStream("redis/redis.properties"));
           String flag=props.getProperty("redisFlag").trim();
           if(flag.equals("true")){
        	   redisFlag=true;
           }
           else{
        	   redisFlag=false;
           }
           props = new Properties(); 
           props.load(RedisUtil.class.getClassLoader().getResourceAsStream("socket/socket.properties"));
           
           socketIp=props.getProperty("ip").trim();
           socketPort=props.getProperty("port").trim();
           nettype=props.getProperty("nettype").trim();
           
           
          
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
     	 
     } 
     
     
     
       @Override 
     public void run() { 
       if(!isRunning) 
       { 
    	 Logger logger = Logger.getLogger("Logger");
         isRunning = true; 
        
         EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
         EventLoopGroup workerGroup = new NioEventLoopGroup();
         try {
             ServerBootstrap b = new ServerBootstrap(); // (2)
             if(nettype.equals("socket")){
            	 b.group(bossGroup, workerGroup)
                 .channel(NioServerSocketChannel.class) // (3)
                 .childHandler(new SocketServerInitializer())  //(4)
                 .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                 .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)
             }
             else if(nettype.equals("websocket")){
            	 
            	 b.group(bossGroup, workerGroup)
                 .channel(NioServerSocketChannel.class) // (3)
                 .childHandler(new WebsocketServerInitializer())  //(4)
                 .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                 .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)
             }
             logger.info("是否启用Redis:"+MyTask.redisFlag);
             logger.info("服务类型："+nettype);
             
     		System.out.println("ChatServer 启动了");
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

         }
         catch (Exception e) {
			// TODO: handle exception
        	 e.printStackTrace();
		 }
         finally {
             workerGroup.shutdownGracefully();
             bossGroup.shutdownGracefully();
             
     		System.out.println("ChatServer 关闭了");
         }
 		
         
         isRunning = false; 
       } 
       else 
       { 
           context.log("上次的任务还未执行完成"); 
       } 
     } 
      
}
