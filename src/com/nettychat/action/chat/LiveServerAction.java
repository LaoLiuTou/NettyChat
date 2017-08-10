package com.nettychat.action.chat;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

//直播下面用 不保存离线消息
public class LiveServerAction implements Action {

	private String userid;
	private String friendid;//拆分
	private String content;
	private String delay;
	private String username;
	private String type;
	private String flag;
	private String headimage;
	
	private String liveFlag;
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
		//固定值
		operate="0";
		delay="100";
		flag= "0";
		liveFlag="9";
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
						message="新建群发消息队列。";
						//////新建定时任务
						timer = new Timer(true); 
						timerMap.put(key, timer);
						if(delay!=null&&!delay.equals("")&&!delay.equals("0")){
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
						if(delay!=null&&!delay.equals("")){
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
					message="用户ID为空！";
					System.out.println("用户ID为空！");
				}
				
			}
			else{
				
				msg.append("\"failure\",\"msg\":");
				message="消息内容为空！";
				System.out.println("消息内容为空！");
			}
		       
			
			
			
			 
			
		} catch (Exception e) {
			msg.append("\"failure\",\"msg\":");
			// TODO: handle exception
			e.printStackTrace();
		} 
		msg.append("\""+message+"\"");
		msg.append("}");
		logger.info("收到推送消息，KEY:"+key+"发送者："+userid+";接收者："+friendid+"；内容："+content+"延迟发送时间："+delay+"操作类型："+operate);
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
	         
	        	String[] friendids = friendid.split("\\|",-1);
	        	
				for(String friend:friendids){
					if(friend.length()>0){
						ChannelHandlerContext channelHandlerContext=NettyChannelMap.map.get(friend);
						
						Map<String, String> paramMap = new HashMap<String, String>();
						paramMap.put("T", liveFlag);
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
							channelHandlerContext.writeAndFlush(content);
							//保存在线消息
							saveOnlineMessage(paramMap);
							
							 
						}
						else{
							
							
							String address =null;
							if(MyTask.redisFlag){
								address=RedisUtil.getObject(friend);
							}
							 if(address!=null){
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
			chatmessage.setIsgroup("1");//个人0--分组1
			chatmessage.setContenttype(map.get("TP"));//0-文本，1-图片，2-语音，3-视频，4-文件 
			chatmessage.setContent(map.get("CT"));
			chatmessage.setReadstatus("0");//是否已读 0-yes 1-no
			chatmessage.setSendstatus("0");//是否发送成功 0-yes 1-no 
			chatmessage.setFlag(liveFlag);//是否推送 1-yes 0-no
			iChatmessageService.addchatmessage(chatmessage);
			
			//接收消息
			chatmessage.setUserid(map.get("FI"));
			chatmessage.setFriendid(map.get("UI"));
			chatmessage.setChattype("1");//发送0--接收1
			iChatmessageService.addchatmessage(chatmessage);
			
    	} catch (Exception e) {
    		// TODO: handle exception
    		e.printStackTrace();
    	}
    	
    }
    
}

//http://192.168.1.144/NettyChat/LiveServer?userid=100&friendid=101|102|103&content=123456&username=李三&type=0&key=1000

