package com.nettychat.webService.chatuser;
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
import com.nettychat.service.chatuser.IChatuserService;
import com.nettychat.model.chatuser.Chatuser;
public class ChatuserWSImpl implements IChatuserWS {
 	/**
 	 * 根据ID查询
 	 * @return
 	 */
	public String getchatuserById(String id) {
		Logger log = Logger.getLogger("NettyChatLogger");
		StringBuffer msg = new StringBuffer("{\"state\":");
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IChatuserService iChatuserService = (IChatuserService)context.getBean("iChatuserService");
		Chatuser chatuser=new Chatuser();
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			chatuser=iChatuserService.selectchatuserById(id);
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
			msg.append(JSONObject.fromObject(chatuser, jsonConfig));
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
	public String deletechatuserById(String id) {
		Logger log = Logger.getLogger("NettyChatLogger");
		StringBuffer msg = new StringBuffer("{\"state\":");
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IChatuserService iChatuserService = (IChatuserService)context.getBean("iChatuserService");
		try {
			iChatuserService.deletechatuser(id);
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
	public String getchatuserByParam(String id,String userid,String username,String userimage,String detail,String addtimeFrom,String addtimeTo,String addtime,String isonline,String flag,int page,int size){
		Logger log = Logger.getLogger("NettyChatLogger");
		StringBuffer msg = new StringBuffer("{\"state\":");
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IChatuserService iChatuserService = (IChatuserService)context.getBean("iChatuserService");
		List<Chatuser> list;
		try {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			Map paramMap = new HashMap();
			paramMap.put("id", id);
			paramMap.put("userid", userid);
			paramMap.put("username", username);
			paramMap.put("userimage", userimage);
			paramMap.put("detail", detail);
			if(addtimeFrom!=null&&!addtimeFrom.equals(""))
			paramMap.put("addtimeFrom", sdf.parse(addtimeFrom));
			if(addtimeTo!=null&&!addtimeTo.equals(""))
			paramMap.put("addtimeTo", sdf.parse(addtimeTo));
			paramMap.put("addtime", addtime);
			paramMap.put("isonline", isonline);
			paramMap.put("flag", flag);
			paramMap.put("fromPage",(page-1)*size);
			paramMap.put("toPage",page*size);
			list=iChatuserService.selectchatuserByParam(paramMap);
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
	public String addchatuser(String userid,String username,String userimage,String detail,String addtime,String isonline,String flag){
		Logger log = Logger.getLogger("NettyChatLogger");
		StringBuffer msg = new StringBuffer("{\"state\":");
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IChatuserService iChatuserService = (IChatuserService)context.getBean("iChatuserService");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Chatuser chatuser=new Chatuser();
		chatuser.setUserid(userid);
		chatuser.setUsername(username);
		chatuser.setUserimage(userimage);
		chatuser.setDetail(detail);
		try {
		if (addtime != null && !addtime.equals(""))
			chatuser.setAddtime(sdf.parse(addtime));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		chatuser.setIsonline(isonline);
		chatuser.setFlag(flag);
		try {
			int result = Integer.parseInt(iChatuserService.addchatuser(chatuser).toString());
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
	public String updatechatuser(String id,String userid,String username,String userimage,String detail,String addtime,String isonline,String flag){
		Logger log = Logger.getLogger("NettyChatLogger");
		StringBuffer msg = new StringBuffer("{\"state\":");
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IChatuserService iChatuserService = (IChatuserService)context.getBean("iChatuserService");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Chatuser chatuser=new Chatuser();
		if (id != null && !id.equals(""))
			chatuser.setId(Long.parseLong(id));
		chatuser.setUserid(userid);
		chatuser.setUsername(username);
		chatuser.setUserimage(userimage);
		chatuser.setDetail(detail);
		try {
		if (addtime != null && !addtime.equals(""))
			chatuser.setAddtime(sdf.parse(addtime));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		chatuser.setIsonline(isonline);
		chatuser.setFlag(flag);
		try {
			iChatuserService.updatechatuser(chatuser);
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

