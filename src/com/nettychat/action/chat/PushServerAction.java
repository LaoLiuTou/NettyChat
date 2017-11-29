package com.nettychat.action.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;

import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nettychat.chat.client.SocketClient;
import com.nettychat.chat.common.NettyChannelMap;
import com.nettychat.chat.server.MyTask;
import com.nettychat.chat.server.Server;
import com.nettychat.chat.server.SocketServerInitializer;
import com.nettychat.chat.server.WebsocketServerInitializer;
import com.nettychat.model.chatfriend.Chatfriend;
import com.nettychat.model.chatmessage.Chatmessage;
import com.nettychat.model.chatuser.Chatuser;
import com.nettychat.service.chatfriend.IChatfriendService;
import com.nettychat.service.chatmessage.IChatmessageService;
import com.nettychat.service.chatuser.IChatuserService;
import com.nettychat.utils.ApnsTools;
import com.nettychat.utils.Base64;
import com.nettychat.utils.RedisUtil;
import com.opensymphony.xwork2.Action;

public class PushServerAction implements Action {

	private String userid;
	private String friendid;//拆分
	private String content;
	private String delay;
	private String username;
	private String type;
	private String flag;
	private String headimage;
	private String pushFlag;
	//
	private String key;
	private String operate;
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getFriendid() {
		return friendid;
	}

	public void setFriendid(String friendid) {
		this.friendid = friendid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getDelay() {
		return delay;
	}

	public void setDelay(String delay) {
		this.delay = delay;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getHeadimage() {
		return headimage;
	}

	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}

	@Autowired
	private IChatmessageService iChatmessageService;
	@Autowired
	private IChatfriendService iChatfriendService;
	@Autowired
	private IChatuserService iChatuserService;
	
	//Timer timer = new Timer() ; 
	HttpServletResponse response = ServletActionContext.getResponse(); 
	Logger logger = Logger.getLogger("NettyChatLogger");
	
	private Timer timer;
    private static Map<String,Timer> timerMap = new HashMap<String, Timer>();
	public String execute() throws Exception {
		
		flag= "0";
		
		pushFlag="7";
		//System.out.println(userid+"&"+friendid+"&"+content+"&"+delay+"&"
		//+username+"&"+type+"&"+flag+"&"+headimage+"&"+key+"&"+operate);
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=UTF-8");
		StringBuffer msg = new StringBuffer("{\"state\":");
		String message="";
		try {
			//System.out.println(timerMap.size());
			if(content!=null&&content.length()>0){
				//消息id
				if(key!=null&&key.length()>0){
					if(operate!=null&&operate.equals("0")){
						message="新建消息队列。";
						//////新建定时任务
						timer = new Timer(true); 
						timerMap.put(key, timer);
						if(delay!=null&&!delay.equals("")&&Long.parseLong(delay)>0){
							timer.schedule(new SendTask(key), Long.parseLong(delay));  
						}
						else{
							timer.schedule(new SendTask(key), 1000);  
						} 
					}
					else if(operate!=null&&operate.equals("1")){
						Timer tempTimer=timerMap.get(key);
						if(tempTimer!=null){
							tempTimer.cancel();
							timerMap.remove(key);
						}
						message="修改消息队列。";
						//////新建定时任务
						timer = new Timer(true); 
						timerMap.put(key, timer);
						if(delay!=null&&!delay.equals("")&&Long.parseLong(delay)>0){
							timer.schedule(new SendTask(key), Long.parseLong(delay));  
						}
						else{
							timer.schedule(new SendTask(key), 1000);  
						}
						System.out.println("修改消息：KEY:"+key);
					}
					else if(operate!=null&&operate.equals("2")){
						Timer tempTimer=timerMap.get(key);
						if(tempTimer!=null){
							tempTimer.cancel();
							timerMap.remove(key);
						}
						message="取消消息队列。";
						System.out.println("取消消息：KEY:"+key);
					}
					else{
						message="未操作消息队列。";
					}
					
					
					msg.append("\"success\",\"msg\":");
				}
				else{
					msg.append("\"failure\",\"msg\":");
					message="消息ID为空！";
					System.out.println("消息ID为空！");
				}
				
			}
			else{
				
				msg.append("\"failure\",\"msg\":");
				message="消息内容为空！";
				System.out.println("消息内容为空！");
			}
		       
			
			
			
			 
			
		} catch (Exception e) {
			msg.append("\"failure\",\"msg\":");
			message="操作失败！";
			timerMap.remove(key);
			// TODO: handle exception
			e.printStackTrace();
		} 
		msg.append("\""+message+"\"");
		msg.append("}");
		logger.info("收到推送消息，KEY:"+key+"发送者："+userid+";接收者："+friendid+";"+"延迟发送时间："+delay+";操作类型："+operate);
		response.getWriter().write(msg.toString()); 
		return null;
	}
	class SendTask extends TimerTask {  
		  
		private String taskKey;
	    public SendTask(String key) {
			// TODO Auto-generated constructor stub
	    	this.taskKey=key;
		}
		@Override  
	    public void run() {  
	        try {
	        	 System.out.println("taskKey:"+taskKey);
	        	//保存用户信息
	        	Chatuser chatuser= new Chatuser();
				chatuser.setUserid(userid);
				chatuser.setUsername(username);
				chatuser.setUserimage(headimage);
				chatuser.setAddtime(new Date());
				chatuser.setIsonline("0");
				 
				Map  param = new HashMap ();
				param.put("fromPage",0);
				param.put("toPage",100); 
				param.put("userid", userid);
				List<Chatuser> tempList = iChatuserService.selectchatuserByParam(param);
				
				if(tempList.size()==0){
					iChatuserService.addchatuser(chatuser);
				}
				else{
					iChatuserService.updatechatuser(chatuser);
				}
				
				Properties props = new Properties(); 
		        props.load(RedisUtil.class.getClassLoader().getResourceAsStream("socket/socket.properties"));
		        String  nettype=props.getProperty("nettype").trim();
		          
	        	String[] friendids = friendid.split("\\|",-1);
	        	
				for(String friend:friendids){
					if(friend.length()>0){
						ChannelHandlerContext channelHandlerContext=NettyChannelMap.map.get(friend);
						
						Map<String, String> paramMap = new HashMap<String, String>();
						paramMap.put("T", pushFlag);
						paramMap.put("UI", userid);
						paramMap.put("UN", username);
						paramMap.put("FI", friend); 
						//paramMap.put("CT", Base64.getBase64(content));
						paramMap.put("CT", content);
						paramMap.put("TP", type);
						paramMap.put("PT", flag);
						paramMap.put("HI", headimage);
						
						ObjectMapper mapper = new ObjectMapper();
						String json = "";
						json = mapper.writeValueAsString(paramMap);
						
						if(channelHandlerContext!=null){
							
							System.out.println("on_line");
							
							String content = json;
							
							
				          if(nettype.equals("socket")){
				        	   channelHandlerContext.writeAndFlush(content);
				          }
				          else if(nettype.equals("websocket")){
				               channelHandlerContext.writeAndFlush(new TextWebSocketFrame(content));
				          }
							
							//保存在线消息
							saveOnlineMessage(paramMap);
							
							 
						}
						else{
							
							
							String address =null;
							if(MyTask.redisFlag){
								address=RedisUtil.getObject(friend);
							}
							//redis 中是否有记录
							if(address== null){
							
								System.out.println("off_line");
								
								chatuser = iChatuserService.selectchatuserById(friend);
								if(chatuser!=null &&chatuser.getFlag()!=null&&chatuser.getFlag().equals("1")){//ios
									//ios push 
									String tokenStr=chatuser.getDetail();
									tokenStr=tokenStr.replace("<", "").replace(">", "");
									List<String> tokens=new ArrayList<String>();
									tokens.add(tokenStr);
									
									String message="{'aps':{'alert':'你有一条新消息！'}}";
									Integer count=1;
									boolean sendCount=true;
									try {
										//message是一个json的字符串{“aps”:{“alert”:”iphone推送测试”}}
										PushNotificationPayload payLoad =  PushNotificationPayload.fromJSON(message);
										payLoad.addAlert("你有一条新消息！"); // 消息内容
										payLoad.addBadge(count); // iphone应用图标上小红圈上的数值
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
										List<PushedNotification> failedNotifications = PushedNotification.findFailedNotifications(notifications);
										List<PushedNotification> successfulNotifications = PushedNotification.findSuccessfulNotifications(notifications);
										int failed = failedNotifications.size();
										int successful = successfulNotifications.size();
										if (successful > 0 && failed == 0) {
											System.out.print("推送成功"+failedNotifications.toString());
											//saveOnlineMessage(paramMap);
											saveOfflineMessage(paramMap);
										
										} else if (successful == 0 && failed > 0) {
											//保存离线消息
											saveOfflineMessage(paramMap);
											System.out.print("推送失败"+failedNotifications.toString());
										
										} else if (successful == 0 && failed == 0) {
											//保存离线消息
											saveOfflineMessage(paramMap);
											System.out.print("推送失败"+failedNotifications.toString());
										System.out.println("No notifications could be sent, probably because of a critical error");
										
										} else {
											//保存离线消息
											saveOfflineMessage(paramMap);
											System.out.print("推送失败"+failedNotifications.toString());
										
										}
									}catch (Exception e) {
										e.printStackTrace();
									}
								}
								else{//安卓
									//保存离线消息
									saveOfflineMessage(paramMap);
									
								}
							
							}
							else{
								//转发
								SocketClient.transmitMessage(address, json);
								//保存在线消息
								//saveOnlineMessage(paramMap);
								
							}
							
						}
						
					}
					
					
				}
				
				timerMap.remove(taskKey);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	  
	    }  
	  
	}  
    /**
     * 保存在线消息
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void saveOnlineMessage(Map<String, String> map){
    	try {
    		
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
			chatmessage.setFlag(pushFlag);//是否推送 1-yes 0-no
			iChatmessageService.addchatmessage(chatmessage);
			
			//接收消息
			chatmessage.setUserid(map.get("FI"));
			chatmessage.setFriendid(map.get("UI"));
			chatmessage.setChattype("1");//发送0--接收1
			iChatmessageService.addchatmessage(chatmessage);
			
			/*Map  param = new HashMap ();
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
				
				
			}*/
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
			chatmessage.setFlag(pushFlag);//是否推送 1-yes 0-no
			iChatmessageService.addchatmessage(chatmessage);
			
			//对方接收的消息
			chatmessage.setUserid(map.get("FI"));
			chatmessage.setFriendid(map.get("UI"));
			chatmessage.setChattype("1");//发送0--接收1
			chatmessage.setReadstatus("1");//是否已读 0-yes 1-no
			chatmessage.setSendstatus("1");//是否发送成功 0-yes 1-no
			iChatmessageService.addchatmessage(chatmessage);
			
			
	  
			/*Map  param = new HashMap ();
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
    			
    		}*/
    	} catch (Exception e) {
    		// TODO: handle exception
    		e.printStackTrace();
    	}
    	
    }
}
