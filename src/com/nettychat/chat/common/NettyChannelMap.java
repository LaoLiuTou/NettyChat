package com.nettychat.chat.common;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * @author lt
 * @version 1.0 
 */
public class NettyChannelMap {
    public static Map<String,ChannelHandlerContext> map=new ConcurrentHashMap<String, ChannelHandlerContext>();
    public static void add(String clientId,ChannelHandlerContext channelHandlerContext){
        map.put(clientId,channelHandlerContext);
    }
    public static ChannelHandlerContext get(String clientId){
       return map.get(clientId);
    }
    public static String  getkey(ChannelHandlerContext channelHandlerContext){
    	String key="";
    	for (Map.Entry entry:map.entrySet()){
            if (entry.getValue()==channelHandlerContext){
            	key=entry.getKey().toString();
            }
        }
    	 return key;
     }
    public static void remove(ChannelHandlerContext channelHandlerContext){
        for (Map.Entry entry:map.entrySet()){
            if (entry.getValue()==channelHandlerContext){
                map.remove(entry.getKey());
            }
        }
    }

}
