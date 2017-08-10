package com.nettychat.chat.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nettychat.chat.server.MyTask;
import com.nettychat.model.chatfriend.Chatfriend;
import com.nettychat.model.chatuser.Chatuser;
import com.nettychat.service.chatfriend.IChatfriendService;
import com.nettychat.service.chatuser.IChatuserService;
import com.nettychat.utils.RedisUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author lt
 * @version 1.0 
 */
public abstract class CustomHeartbeatHandler extends SimpleChannelInboundHandler<String> {
    public static final String PING_MSG = "1";
    public static final String PONG_MSG = "2";
    public static final String CUSTOM_MSG = "3";
    protected String name;
    private int heartbeatCount = 0;

    public CustomHeartbeatHandler(String name) {
        this.name = name;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, String string) throws Exception {
    	//System.out.println(context.name()+"|"+string);
    	if (string.equals(PING_MSG)) {
            sendPongMsg(context);
        } else if (string.equals(PONG_MSG)){
            System.out.println(name + " get pong msg from " + context.channel().remoteAddress());
        }
        else {
            handleData(context, string);
            //System.out.println(context.name()+"|");
        }
    }

    protected void sendPingMsg(ChannelHandlerContext context) {
        /*ByteBuf buf = context.alloc().buffer(5);
        buf.writeInt(5);
        buf.writeByte(PING_MSG);
        buf.retain();
        context.writeAndFlush(buf);*/
        context.writeAndFlush(PING_MSG);
        heartbeatCount++;
        System.out.println(name + " sent ping msg to " + context.channel().remoteAddress() + ", count: " + heartbeatCount);
    }

    private void sendPongMsg(ChannelHandlerContext context) {
        /*ByteBuf buf = context.alloc().buffer(5);
        buf.writeInt(5);
        buf.writeByte(PONG_MSG);
        context.channel().writeAndFlush(buf);*/
        context.channel().writeAndFlush(PONG_MSG);
        heartbeatCount++;
        System.out.println(name + " sent pong msg to " + context.channel().remoteAddress() + ", count: " + heartbeatCount);
    }

   //protected abstract void handleData(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf);
    protected abstract void handleData(ChannelHandlerContext channelHandlerContext, String content);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // IdleStateHandler 所产生的 IdleStateEvent 的处理逻辑.
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                    handleReaderIdle(ctx);
                    break;
                case WRITER_IDLE:
                    handleWriterIdle(ctx);
                    break;
                case ALL_IDLE:
                    handleAllIdle(ctx);
                    break;
                default:
                    break;
            }
        }
    }
 
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	
        //System.err.println("---" + ctx.channel().remoteAddress() + " is active---");
     
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        
    	ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IChatuserService iChatuserService = (IChatuserService)context.getBean("iChatuserService");
		IChatfriendService iChatfriendService = (IChatfriendService)context.getBean("iChatfriendService");
        String userid=NettyChannelMap.getkey(ctx);
        //System.err.println(userid+"---" + ctx.channel().remoteAddress() + " is inactive---");
        for (Map.Entry entry:NettyChannelMap.map.entrySet()){
            if (entry.getValue()==ctx){
            	//database
                
                Chatuser chatuser= new Chatuser(); 
                chatuser.setUserid(entry.getKey().toString());
                chatuser.setIsonline("1");
                chatuser.setDetail("");
                //chatuser.setFlag("0");
                iChatuserService.updatechatuser(chatuser);
                
                Chatfriend chatfriend = new Chatfriend();
                chatfriend.setFriendid(entry.getKey().toString());
                chatfriend.setIsonline("1");
                iChatfriendService.updatechatfriend(chatfriend);
            }
            /*else{//通知其他用户自己下线
            	Chatuser chatuser= new Chatuser(); 
                chatuser=iChatuserService.selectchatuserById(userid);
                if(chatuser!=null){
                	ChannelHandlerContext temp = (ChannelHandlerContext) entry.getValue();
                	Map<String,String> paramMap=new HashMap<String,String>();
            		paramMap.put("T", "5");
            		paramMap.put("FI", userid);
            		paramMap.put("FN", chatuser.getUsername());
            		ObjectMapper mapper = new ObjectMapper();
        			String json = "";
        			json = mapper.writeValueAsString(paramMap);
                    String content = json;
                    ByteBuf buf = temp.alloc().buffer(5 + content.getBytes().length);
                    buf.writeInt(5 + content.getBytes().length);
                    buf.writeByte(CustomHeartbeatHandler.CUSTOM_MSG);
                    buf.writeBytes(content.getBytes());
                    temp.writeAndFlush(buf);
                    temp.writeAndFlush(content);
                	 
                }
            	
            }*/
        }
        //移除
        NettyChannelMap.remove(ctx);
        if(MyTask.redisFlag){
        	//
        	RedisUtil.delOject(userid);
        }
        
    }

    protected void handleReaderIdle(ChannelHandlerContext ctx) {
        System.err.println("---READER_IDLE---");
    }

    protected void handleWriterIdle(ChannelHandlerContext ctx) {
        System.err.println("---WRITER_IDLE---");
    }

    protected void handleAllIdle(ChannelHandlerContext ctx) {
        System.err.println("---ALL_IDLE---");
    }
}