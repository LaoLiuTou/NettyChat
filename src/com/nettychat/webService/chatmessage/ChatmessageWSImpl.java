package com.nettychat.webService.chatmessage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.nettychat.service.chatmessage.IChatmessageService;
import com.nettychat.model.chatmessage.Chatmessage;
public class ChatmessageWSImpl implements IChatmessageWS {
 	/**
 	 * 根据ID查询
 	 * @return
 	 */
	public String getchatmessageById(String id) {
		Logger log = Logger.getLogger("NettyChatLogger");
		StringBuffer msg = new StringBuffer("{\"state\":");
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IChatmessageService iChatmessageService = (IChatmessageService)context.getBean("iChatmessageService");
		Chatmessage chatmessage=new Chatmessage();
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			chatmessage=iChatmessageService.selectchatmessageById(id);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
				public Object processArrayValue(Object value, JsonConfig jsonConfig) {
					return value;  
				} 
			public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) { 
				if(value instanceof Date){ 
					return sdf.format((Date)value);
				}
				return value; 
			}
			});
			msg.append("\"success\",\"msg\":");
			msg.append(JSONObject.fromObject(chatmessage, jsonConfig));
		} catch (Exception e) {
			msg.append("\"failure\",\"msg\":");
			msg.append("\"查询失败.\"");
			log.info("查询失败.ID："+id+";E:"+e);
			e.printStackTrace();
		}
		msg.append("}");
		return msg.toString();
	}


		/**
		 * 根据ID删除
		 * 
		 * @return
		 */
	public String deletechatmessageById(String id) {
		Logger log = Logger.getLogger("NettyChatLogger");
		StringBuffer msg = new StringBuffer("{\"state\":");
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IChatmessageService iChatmessageService = (IChatmessageService)context.getBean("iChatmessageService");
		try {
			iChatmessageService.deletechatmessage(id);
			msg.append("\"success\",\"msg\":");
			msg.append("\"删除成功.ID："+id+"\"");
		} catch (Exception e) {
			msg.append("\"failure\",\"msg\":");
			msg.append("\"删除失败.\"");
			log.info("删除失败.ID："+id+";E:"+e);
			e.printStackTrace();
		}
		msg.append("}");
		return msg.toString();
	}


		/**
		 * 根据查询条件查询
		 * @return
		 */
		@SuppressWarnings("unchecked")
	public String getchatmessageByParam(String id,String userid,String friendid,String addtimeFrom,String addtimeTo,String addtime,String chattype,String contenttype,String content,String readstatus,String sendstatus,String isgroup,String flag,int page,int size){
		Logger log = Logger.getLogger("NettyChatLogger");
		StringBuffer msg = new StringBuffer("{\"state\":");
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IChatmessageService iChatmessageService = (IChatmessageService)context.getBean("iChatmessageService");
		List<Chatmessage> list;
		try {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			Map paramMap = new HashMap();
			paramMap.put("id", id);
			paramMap.put("userid", userid);
			paramMap.put("friendid", friendid);
			if(addtimeFrom!=null&&!addtimeFrom.equals(""))
			paramMap.put("addtimeFrom", sdf.parse(addtimeFrom));
			if(addtimeTo!=null&&!addtimeTo.equals(""))
			paramMap.put("addtimeTo", sdf.parse(addtimeTo));
			paramMap.put("addtime", addtime);
			paramMap.put("chattype", chattype);
			paramMap.put("contenttype", contenttype);
			paramMap.put("content", content);
			paramMap.put("readstatus", readstatus);
			paramMap.put("sendstatus", sendstatus);
			paramMap.put("isgroup", isgroup);
			paramMap.put("flag", flag);
			paramMap.put("fromPage",(page-1)*size);
			paramMap.put("toPage",page*size);
			list=iChatmessageService.selectchatmessageByParam(paramMap);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
				public Object processArrayValue(Object value, JsonConfig jsonConfig) {
					return value;  
				} 
			public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) { 
				if(value instanceof Date){ 
					return sdf.format((Date)value);
				}
				return value; 
			}
			});
			msg.append("\"success\",\"msg\":");
			msg.append(JSONArray.fromObject(list, jsonConfig));
		} catch (Exception e) {
			msg.append("\"failure\",\"msg\":");
			msg.append("\"查询失败.\"");
			log.info("查询失败."+e);
			e.printStackTrace();
		}
		msg.append("}");
		return msg.toString();
	}
		/**
		 * 添加
		 * @return
		 * @throws ParseException
		 */
	public String addchatmessage(String userid,String friendid,String addtime,String chattype,String contenttype,String content,String readstatus,String sendstatus,String isgroup,String flag){
		Logger log = Logger.getLogger("NettyChatLogger");
		StringBuffer msg = new StringBuffer("{\"state\":");
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IChatmessageService iChatmessageService = (IChatmessageService)context.getBean("iChatmessageService");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Chatmessage chatmessage=new Chatmessage();
		chatmessage.setUserid(userid);
		chatmessage.setFriendid(friendid);
		try {
		if (addtime != null && !addtime.equals(""))
			chatmessage.setAddtime(sdf.parse(addtime));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		chatmessage.setChattype(chattype);
		chatmessage.setContenttype(contenttype);
		chatmessage.setContent(content);
		chatmessage.setReadstatus(readstatus);
		chatmessage.setSendstatus(sendstatus);
		chatmessage.setIsgroup(isgroup);
		chatmessage.setFlag(flag);
		try {
			int result = Integer.parseInt(iChatmessageService.addchatmessage(chatmessage).toString());
			msg.append("\"success\",\"msg\":");
			msg.append("\""+result+"\"");
		} catch (Exception e) {
			msg.append("\"failure\",\"msg\":");
			msg.append("\"添加失败.\"");
			log.info("添加失败."+e);
			e.printStackTrace();
		}
		msg.append("}");
		return msg.toString();
	}
		/**
		 * 更新
		 * @return
		 */
	public String updatechatmessage(String id,String userid,String friendid,String addtime,String chattype,String contenttype,String content,String readstatus,String sendstatus,String isgroup,String flag){
		Logger log = Logger.getLogger("NettyChatLogger");
		StringBuffer msg = new StringBuffer("{\"state\":");
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IChatmessageService iChatmessageService = (IChatmessageService)context.getBean("iChatmessageService");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Chatmessage chatmessage=new Chatmessage();
		if (id != null && !id.equals(""))
			chatmessage.setId(Long.parseLong(id));
		chatmessage.setUserid(userid);
		chatmessage.setFriendid(friendid);
		try {
		if (addtime != null && !addtime.equals(""))
			chatmessage.setAddtime(sdf.parse(addtime));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		chatmessage.setChattype(chattype);
		chatmessage.setContenttype(contenttype);
		chatmessage.setContent(content);
		chatmessage.setReadstatus(readstatus);
		chatmessage.setSendstatus(sendstatus);
		chatmessage.setIsgroup(isgroup);
		chatmessage.setFlag(flag);
		try {
			iChatmessageService.updatechatmessage(chatmessage);
			msg.append("\"success\",\"msg\":");
			msg.append("\"更新成功.\"");
		} catch (Exception e) {
			msg.append("\"failure\",\"msg\":");
			msg.append("\"更新失败.\"");
			log.info("更新失败."+e);
			e.printStackTrace();
		}
		msg.append("}");
		return msg.toString();
	}

}

