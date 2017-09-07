package com.nettychat.chat.server;
 
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nettychat.chat.client.SocketClient;
import com.nettychat.chat.common.CustomHeartbeatHandler;
import com.nettychat.chat.common.NettyChannelMap;
import com.nettychat.model.chatfriend.Chatfriend;
import com.nettychat.model.chatmessage.Chatmessage;
import com.nettychat.model.chatuser.Chatuser;
import com.nettychat.service.chatfriend.IChatfriendService;
import com.nettychat.service.chatmessage.IChatmessageService;
import com.nettychat.service.chatuser.IChatuserService;
import com.nettychat.utils.ApnsTools;
import com.nettychat.utils.RedisUtil;
/**
 * @author lt
 * @version 1.0 
 */
public class SocketServerHandler extends CustomHeartbeatHandler {
	
	private IChatuserService iChatuserService;
	private IChatmessageService iChatmessageService;
	private IChatfriendService iChatfriendService;
	 
	Logger logger = Logger.getLogger("NettyChatLogger");
    public SocketServerHandler() {
        super("server");
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		iChatuserService = (IChatuserService)context.getBean("iChatuserService");
		iChatmessageService = (IChatmessageService)context.getBean("iChatmessageService");
		iChatfriendService = (IChatfriendService)context.getBean("iChatfriendService");
		
		 
    }
   
    
    @Override
    protected void handleData(ChannelHandlerContext channelHandlerContext, String string) {
		try {
			/*byte[] data = new byte[buf.readableBytes() - 5];
			ByteBuf responseBuf = Unpooled.copiedBuffer(buf);
			buf.skipBytes(5);
			buf.readBytes(data);
			String content = new String(data);*/
			String content = string;
			//System.out.println(content);
			//
			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> map = new HashMap<String, String>(); 
			map = mapper.readValue(content, new TypeReference<Map<String, String>>(){});
			 
			if(map.get("T").equals("1")){//login
		        //登录
				String replayContent ="";
				if(NettyChannelMap.get(map.get("UI"))!=null){
					//被踢下线
					replayContent = "{\"T\":\"6\",\"R\":\"0\"}";//被踢下线
					ChannelHandlerContext tempCtx=NettyChannelMap.get(map.get("UI"));
	                tempCtx.writeAndFlush(replayContent);
	                tempCtx.close();
					NettyChannelMap.remove(NettyChannelMap.get(map.get("UI")));
					//
					if(MyTask.redisFlag){
						RedisUtil.delOject(map.get("UI"));
					}
					
				}
					
		        //database   run
		        try {
		        	NettyChannelMap.add(map.get("UI"),channelHandlerContext);
			        replayContent = "{\"T\":\"1\",\"R\":\"0\"}";//登录成功
					Chatuser chatuser= new Chatuser();
					chatuser.setUserid(map.get("UI"));
					chatuser.setUsername(map.get("UN"));
					chatuser.setUserimage(map.get("UH"));
					chatuser.setAddtime(new Date());
					chatuser.setIsonline("0");
					
					//是否是ios设备  存token
					if(map.containsKey("TK")){
						chatuser.setDetail(map.get("TK"));
						chatuser.setFlag("1");//0-安卓  1-ios
					}
					else{
						chatuser.setDetail("");
						chatuser.setFlag("0");//0-安卓  1-ios
					}
					
					
					if(iChatuserService.selectchatuserById(map.get("UI"))==null){
						iChatuserService.addchatuser(chatuser);
					}
					else{
						iChatuserService.updatechatuser(chatuser);
					}
					
					Chatfriend chatfriend = new Chatfriend();
					chatfriend.setFriendid(map.get("UI"));
					chatfriend.setIsonline("0");
					iChatfriendService.updatechatfriend(chatfriend);
					
					//发送离线消息
					sendOfflineMessage(channelHandlerContext,map.get("UI"));
					 
					//通知上线 
					/*String userid=map.get("UI");
					String username=map.get("UN");
					if(userid.length()>0){ 
					    sendExceptMessage(channelHandlerContext, "4",userid,username);
					}*/
					channelHandlerContext.writeAndFlush(replayContent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					replayContent = "{\"T\":\"1\",\"R\":\"1\"}";//登录失败
					 channelHandlerContext.writeAndFlush(replayContent);
					e.printStackTrace();
				}
		        if(MyTask.redisFlag){
		        	//
		        	addToRedis(map.get("UI")); 
		        }
				logger.info("用户登录，ID:"+map.get("UI")+"；用户名："+map.get("UN"));
		        //System.out.println("Login:ID-"+map.get("UI")+";Name-"+map.get("UN"));
			}
			else if(map.get("T").equals("2")){//logout
				
				String replayContent = "{\"T\":\"2\",\"R\":\"0\"}";//退出成功
				//database
				try {
					Chatuser chatuser= new Chatuser();
					chatuser.setUserid(map.get("UI"));
					chatuser.setIsonline("1");
					chatuser.setDetail("");
					chatuser.setFlag("0");
					iChatuserService.updatechatuser(chatuser);
					
					Chatfriend chatfriend = new Chatfriend();
					chatfriend.setFriendid(map.get("UI"));
					chatfriend.setIsonline("1");
					iChatfriendService.updatechatfriend(chatfriend);
					

					/*String userid=NettyChannelMap.getkey(channelHandlerContext);
					String username=map.get("UN");
					if(userid.length()>0){ 
					    //通知下线
					    sendExceptMessage(channelHandlerContext, "5",userid,username);
					}*/
					
					NettyChannelMap.remove(channelHandlerContext);
					channelHandlerContext.writeAndFlush(replayContent);
					channelHandlerContext.close();
					
					//
					RedisUtil.delOject(map.get("UI"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					replayContent = "{\"T\":\"2\",\"R\":\"1\"}";//退出失败
					channelHandlerContext.writeAndFlush(replayContent);
					channelHandlerContext.close();
					e.printStackTrace();
				}
				logger.info("用户退出，ID:"+map.get("UI")+"；用户名："+map.get("UN"));
			}
			else if(map.get("T").equals("3")||map.get("T").equals("7")||map.get("T").equals("8")
					||map.get("T").equals("9")||map.get("T").equals("10")){//text message
				
				//判断消息类型  查询group中的用户信息 循环发送消息
				
				SimpleDateFormat dfs = new SimpleDateFormat("HH:mm:ss.SSS");  
				ChannelHandlerContext chc=NettyChannelMap.get(map.get("FI"));
				
				if(chc!=null){
					//保存在线消息
					saveOnlineMessage(map);
					chc.writeAndFlush(content);
				}
				else{
					
					String address =null;
					if(MyTask.redisFlag){
						address=RedisUtil.getObject(map.get("FI"));
					}
					//redis 中是否有记录
					if(address== null){
						if(map.get("T").equals("10")){
							return;
						}
						
						Chatuser chatuser = iChatuserService.selectchatuserById(map.get("FI"));
						if(chatuser!=null&& chatuser.getFlag()!=null&&chatuser.getFlag().equals("1")){//ios
							//ios push 
							String tokenStr=chatuser.getDetail();
							tokenStr=tokenStr.replace("<", "").replace(">", "");
							List<String> tokens=new ArrayList<String>();
							tokens.add(tokenStr);
							
							Integer count=1;
							boolean sendCount=true;
							try {
								System.out.println("start push:"+dfs.format(new Date()));
								
								String message="{'aps':{'alert':'你有一条新消息！'}}";
								//message是一个json的字符串{“aps”:{“alert”:”iphone推送测试”}}
								PushNotificationPayload payLoad =  PushNotificationPayload.fromJSON(message);
								payLoad.addAlert("你有一条新消息！"); // 消息内容
								//payLoad.addBadge(count); // iphone应用图标上小红圈上的数值
								payLoad.addSound("default"); // 铃音 默认
								
								List<PushedNotification> notifications = new ArrayList<PushedNotification>();
								// 发送push消息
								if (sendCount) {			
									Device device = new BasicDevice();
									device.setToken(tokens.get(0));
									PushedNotification notification = new ApnsTools().pushManager.sendNotification(device, payLoad, true);
									notifications.add(notification);
								} else {			
									List<Device> device = new ArrayList<Device>();
									for (String token : tokens) {
										device.add(new BasicDevice(token));
									}
									notifications = new ApnsTools().pushManager.sendNotifications(payLoad, device);
								}
								System.out.println("end push:"+dfs.format(new Date()));
								List<PushedNotification> failedNotifications = PushedNotification.findFailedNotifications(notifications);
								List<PushedNotification> successfulNotifications = PushedNotification.findSuccessfulNotifications(notifications);
								int failed = failedNotifications.size();
								int successful = successfulNotifications.size();
								if (successful > 0 && failed == 0) {
									System.out.print("推送成功"+failedNotifications.toString());
									//saveOnlineMessage(map);
									//保存离线消息
									saveOfflineMessage(map);
								
								} else if (successful == 0 && failed > 0) {
									//保存离线消息
									saveOfflineMessage(map);
									System.out.print("推送失败"+failedNotifications.toString());
								
								} else if (successful == 0 && failed == 0) {
									//保存离线消息
									saveOfflineMessage(map);
									System.out.print("推送失败"+failedNotifications.toString());
								    System.out.println("No notifications could be sent, probably because of a critical error");
								
								} else {
									//保存离线消息
									saveOfflineMessage(map);
									System.out.print("推送失败"+failedNotifications.toString());
								
								}
							}catch (Exception e) {
								e.printStackTrace();
							}
						}
						else{//安卓
							//保存在线消息
							saveOfflineMessage(map);
							
						}
					}
					else{
						//转发
						SocketClient.transmitMessage(address, content);
						
					}
					
				}
				
				//logger.info("用户发送消息，ID:"+map.get("UI")+"；用户名："+map.get("UN")+"；接收ID:"+map.get("FI")); 
				
			}
	        
	       //System.out.println(name + " get content: " + content+map);
	        //channelHandlerContext.write(responseBuf);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("PUSH:"+e.toString());
			e.printStackTrace();
		} 

		
    }
    
    /**
 	 * 发送离线消息
 	 * @return
 	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void sendOfflineMessage(ChannelHandlerContext ctx,String userId){
    	try {
    		//database
			//ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
			//IChatmessageService iChatmessageService = (IChatmessageService)context.getBean("iChatmessageService");
			Thread.sleep(5000);
    		Map  param = new HashMap ();
			param.put("fromPage",0);
			param.put("toPage",100); 
			param.put("userid", userId);
			param.put("readstatus", "1");
			param.put("sendstatus", "1"); 
			param.put("chattype", "1"); 
			List<Chatmessage> list=iChatmessageService.selectchatmessageByParam(param); 
			//for (Chatmessage cm : list) {
			for (int index=list.size()-1;index>=0;index--) {
				Chatmessage cm=list.get(index);
			 
				Map<String, String> paramMap = new HashMap<String, String>();
				/*if(cm.getFlag().equals("0"))
					paramMap.put("T", "3");
				else{
					paramMap.put("T", "7");
					paramMap.put("PT", cm.getFlag());//推送or待办
				}*/
				paramMap.put("T", cm.getFlag());
				paramMap.put("FI", cm.getUserid());
				paramMap.put("UI", cm.getFriendid());
				paramMap.put("CT", cm.getContent());
				paramMap.put("TP", cm.getContenttype());
				paramMap.put("CF", cm.getFlag());
				
				
				Map  userparam = new HashMap ();
				userparam.put("fromPage",0);
				userparam.put("toPage",100); 
				userparam.put("userid", cm.getFriendid());
				
				List<Chatuser> chatuserTemp=iChatuserService.selectchatuserByParam(userparam);
				Chatuser chatuser= null;
				if(chatuserTemp.size()>0){
					chatuser=chatuserTemp.get(0);
				}
				//Chatuser chatuser=iChatuserService.selectchatuserById(cm.getFriendid());
				if(chatuser!=null){
					paramMap.put("UN", chatuser.getUsername());
					paramMap.put("HI", chatuser.getUserimage());
				}
				else{
					paramMap.put("UN", "");
					paramMap.put("HI", "");
				}
				
				ObjectMapper mapper = new ObjectMapper();
				String json = "";
				json = mapper.writeValueAsString(paramMap);
				String content = json;
				/*ByteBuf buf = ctx.channel().alloc()
						.buffer(5 + content.getBytes().length);
				buf.writeInt(5 + content.getBytes().length);
				buf.writeByte(CustomHeartbeatHandler.CUSTOM_MSG);
				buf.writeBytes(content.getBytes());
				ctx.channel().writeAndFlush(buf);*/
				ctx.channel().writeAndFlush(content);
				
				//更新消息状态
				cm.setReadstatus("0");
				cm.setSendstatus("0");
				iChatmessageService.updatechatmessage(cm);

				Thread.sleep(1000);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    	
    }
    /**
     * 保存在线消息
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void saveOnlineMessage(Map<String, String> map){
    	try {
    		//database
    		//ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    		//IChatuserService iChatuserService = (IChatuserService)context.getBean("iChatuserService");
			//IChatmessageService iChatmessageService = (IChatmessageService)context.getBean("iChatmessageService");
			//IChatfriendService iChatfriendService = (IChatfriendService)context.getBean("iChatfriendService");
			
			//发送消息
    		Chatmessage chatmessage= new Chatmessage();
			chatmessage.setUserid(map.get("UI"));
			chatmessage.setFriendid(map.get("FI"));
			chatmessage.setChattype("0");//发送0--接收1
			chatmessage.setAddtime(new Date());
			chatmessage.setIsgroup("0");//个人0--分组1
			chatmessage.setContenttype(map.get("TP"));//0-文本，1-图片，2-语音，3-视频，4-文件
			chatmessage.setContent(map.get("CT"));
			chatmessage.setReadstatus("0");//是否已读 0-yes 1-no
			chatmessage.setSendstatus("0");//是否发送成功 0-yes 1-no
			chatmessage.setFlag(map.get("T"));//是否推送 1-yes 0-no
			iChatmessageService.addchatmessage(chatmessage);
			//接收消息
			chatmessage.setUserid(map.get("FI"));
			chatmessage.setFriendid(map.get("UI"));
			chatmessage.setChattype("1");//发送0--接收1
			iChatmessageService.addchatmessage(chatmessage);
			
			Map  param = new HashMap ();
			param.put("fromPage",0);
			param.put("toPage",1); 
			param.put("friendid", map.get("FI")); 
			param.put("userid", map.get("UI"));  
			if(iChatfriendService.selectchatfriendByParam(param).size()>0){
				 
				
			}
			else{
				Chatfriend chatfriend = new Chatfriend();
				chatfriend.setUserid(map.get("UI"));
				chatfriend.setIsgroup("1");
		        chatfriend.setFriendid(map.get("FI"));
		        chatfriend.setIsonline("0");
		        iChatfriendService.addchatfriend(chatfriend);
		        chatfriend.setUserid(map.get("FI"));
		        chatfriend.setFriendid(map.get("UI"));
		        iChatfriendService.addchatfriend(chatfriend);
				
				
			}
    	} catch (Exception e) {
    		// TODO: handle exception
    		e.printStackTrace();
    	}
    	
    }
    /**
     * 保存离线消息
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void saveOfflineMessage(Map<String, String> map){
    	try {
    		//database
    		//ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    		//IChatuserService iChatuserService = (IChatuserService)context.getBean("iChatuserService");
    		//IChatmessageService iChatmessageService = (IChatmessageService)context.getBean("iChatmessageService");
    		//IChatfriendService iChatfriendService = (IChatfriendService)context.getBean("iChatfriendService");
    		

			//发送的消息
			Chatmessage chatmessage= new Chatmessage();
			chatmessage.setUserid(map.get("UI"));
			chatmessage.setFriendid(map.get("FI"));
			chatmessage.setChattype("0");//发送0--接收1
			chatmessage.setAddtime(new Date());
			chatmessage.setIsgroup("0");//个人0--分组1
			chatmessage.setContenttype(map.get("TP"));//0-文本，1-图片，2-语音，3-视频，4-文件
			chatmessage.setContent(map.get("CT"));
			chatmessage.setReadstatus("0");//是否已读 0-yes 1-no
			chatmessage.setSendstatus("0");//是否发送成功 0-yes 1-no
			chatmessage.setFlag(map.get("T"));//是否推送 1-yes 0-no
			iChatmessageService.addchatmessage(chatmessage);
			
			//对方接收的消息
			chatmessage.setUserid(map.get("FI"));
			chatmessage.setFriendid(map.get("UI"));
			chatmessage.setChattype("1");//发送0--接收1
			chatmessage.setReadstatus("1");//是否已读 0-yes 1-no
			chatmessage.setSendstatus("1");//是否发送成功 0-yes 1-no
			iChatmessageService.addchatmessage(chatmessage);
			
			
	  
    		Map  param = new HashMap ();
    		param.put("fromPage",0);
    		param.put("toPage",1); 
    		param.put("friendid", map.get("FI")); 
    		param.put("userid", map.get("UI"));  
    		if(iChatfriendService.selectchatfriendByParam(param).size()>0){
    			 
    			
    		}
    		else{
    			Chatfriend chatfriend = new Chatfriend();
    			chatfriend.setUserid(map.get("UI"));
    			chatfriend.setIsgroup("1");
    	        chatfriend.setFriendid(map.get("FI"));
    	        chatfriend.setIsonline("1");
    	        iChatfriendService.addchatfriend(chatfriend);
    	        
    	        chatfriend.setUserid(map.get("FI"));
		        chatfriend.setFriendid(map.get("UI"));
		        chatfriend.setIsonline("0");
		        iChatfriendService.addchatfriend(chatfriend);
    			
    		}
    	} catch (Exception e) {
    		// TODO: handle exception
    		e.printStackTrace();
    	}
    	
    }
    /**
     * 向除了自己其他所有人发送消息
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void sendExceptMessage(ChannelHandlerContext channelHandlerContext,String type,
    		String userid,String username){
    	//String userid=NettyChannelMap.getkey(channelHandlerContext);
		for (Map.Entry entry:NettyChannelMap.map.entrySet()){
            if (entry.getValue()==channelHandlerContext){
            	
            }
            else{//通知其他用户自己上线或下线
            	try {
					ChannelHandlerContext temp = (ChannelHandlerContext) entry
							.getValue();
					Map<String, String> paramMap = new HashMap<String, String>();
					paramMap.put("T", type);
					paramMap.put("FI", userid);
					paramMap.put("FN", username);
					ObjectMapper mapper = new ObjectMapper();
					String json = "";
					json = mapper.writeValueAsString(paramMap);
					String content = json;
					/*ByteBuf buf = temp.alloc().buffer(
							5 + content.getBytes().length);
					buf.writeInt(5 + content.getBytes().length);
					buf.writeByte(CustomHeartbeatHandler.CUSTOM_MSG);
					buf.writeBytes(content.getBytes());
					temp.writeAndFlush(buf);*/
					temp.writeAndFlush(content);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
            	
            }
        }
    	
    }
    @Override
    protected void handleReaderIdle(ChannelHandlerContext ctx) {
        super.handleReaderIdle(ctx);
       
        String userid=NettyChannelMap.getkey(ctx);
        if(userid.length()>0){
        	//database
            Chatuser chatuser= new Chatuser(); 
            chatuser.setUserid(userid);
            chatuser.setIsonline("1");
            chatuser.setDetail("");
            //chatuser.setFlag("0");
            iChatuserService.updatechatuser(chatuser);
            
            Chatfriend chatfriend = new Chatfriend();
            chatfriend.setFriendid(userid); 
            chatfriend.setIsonline("1");
            iChatfriendService.updatechatfriend(chatfriend); 
            //通知下线
            /*chatuser= new Chatuser(); 
            chatuser=iChatuserService.selectchatuserById(userid);
            if(chatuser!=null){
            	
            	sendExceptMessage(ctx, "5",userid,chatuser.getUsername());
            }*/
        }
        
        //移除
        NettyChannelMap.remove(ctx);
        	
        	
        //System.err.println("---client " + ctx.channel().remoteAddress().toString() + " reader timeout, close it---");
        ctx.close();
    }
    
    /**
     * 注册到redis
     * @return
     */
    private void addToRedis(String id){
    	//参数
   	 	/*Properties properties = new Properties();
		String base = getClass().getResource("/").getPath();
		try {
			properties.load(new FileInputStream(base
					+ "socket/socket.properties"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		String ip=properties.getProperty("ip").trim();
		int port=Integer.parseInt(properties.getProperty("port").trim());*/
		
		String socketIp= MyTask.socketIp;
		String socketPort= MyTask.socketPort;
		
		RedisUtil.setObject(id, socketIp+":"+socketPort);
		
    	
    }
    //{"T":"1","UI":"101","UN":"用户名","UH":""}
    //{"T":"3","CT":"dQ==\n","UI":"100","UN":"用户名999","FI":"101","TP":"0","HI":""}
}