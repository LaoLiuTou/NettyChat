package com.nettychat.webService.chatgroup;
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
import com.nettychat.service.chatgroup.IChatgroupService;
import com.nettychat.model.chatgroup.Chatgroup;
public class ChatgroupWSImpl implements IChatgroupWS {
 	/**
 	 * 根据ID查询
 	 * @return
 	 */
	public String getchatgroupById(String id) {
		Logger log = Logger.getLogger("NettyChatLogger");
		StringBuffer msg = new StringBuffer("{\"state\":");
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IChatgroupService iChatgroupService = (IChatgroupService)context.getBean("iChatgroupService");
		Chatgroup chatgroup=new Chatgroup();
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			chatgroup=iChatgroupService.selectchatgroupById(id);
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
			msg.append(JSONObject.fromObject(chatgroup, jsonConfig));
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
	public String deletechatgroupById(String id) {
		Logger log = Logger.getLogger("NettyChatLogger");
		StringBuffer msg = new StringBuffer("{\"state\":");
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IChatgroupService iChatgroupService = (IChatgroupService)context.getBean("iChatgroupService");
		try {
			iChatgroupService.deletechatgroup(id);
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
	public String getchatgroupByParam(String id,String groupid,String groupname,String adduserid,String groupimage,String groupdetail,String addtimeFrom,String addtimeTo,String addtime,String flag,int page,int size){
		Logger log = Logger.getLogger("NettyChatLogger");
		StringBuffer msg = new StringBuffer("{\"state\":");
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IChatgroupService iChatgroupService = (IChatgroupService)context.getBean("iChatgroupService");
		List<Chatgroup> list;
		try {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			Map paramMap = new HashMap();
			paramMap.put("id", id);
			paramMap.put("groupid", groupid);
			paramMap.put("groupname", groupname);
			paramMap.put("adduserid", adduserid);
			paramMap.put("groupimage", groupimage);
			paramMap.put("groupdetail", groupdetail);
			if(addtimeFrom!=null&&!addtimeFrom.equals(""))
			paramMap.put("addtimeFrom", sdf.parse(addtimeFrom));
			if(addtimeTo!=null&&!addtimeTo.equals(""))
			paramMap.put("addtimeTo", sdf.parse(addtimeTo));
			paramMap.put("addtime", addtime);
			paramMap.put("flag", flag);
			paramMap.put("fromPage",(page-1)*size);
			paramMap.put("toPage",page*size);
			list=iChatgroupService.selectchatgroupByParam(paramMap);
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
	public String addchatgroup(String groupid,String groupname,String adduserid,String groupimage,String groupdetail,String addtime,String flag){
		Logger log = Logger.getLogger("NettyChatLogger");
		StringBuffer msg = new StringBuffer("{\"state\":");
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IChatgroupService iChatgroupService = (IChatgroupService)context.getBean("iChatgroupService");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Chatgroup chatgroup=new Chatgroup();
		chatgroup.setGroupid(groupid);
		chatgroup.setGroupname(groupname);
		chatgroup.setAdduserid(adduserid);
		chatgroup.setGroupimage(groupimage);
		chatgroup.setGroupdetail(groupdetail);
		try {
		if (addtime != null && !addtime.equals(""))
			chatgroup.setAddtime(sdf.parse(addtime));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		if (flag != null && !flag.equals(""))
			chatgroup.setFlag(Long.parseLong(flag));
		try {
			int result = Integer.parseInt(iChatgroupService.addchatgroup(chatgroup).toString());
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
	public String updatechatgroup(String id,String groupid,String groupname,String adduserid,String groupimage,String groupdetail,String addtime,String flag){
		Logger log = Logger.getLogger("NettyChatLogger");
		StringBuffer msg = new StringBuffer("{\"state\":");
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IChatgroupService iChatgroupService = (IChatgroupService)context.getBean("iChatgroupService");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Chatgroup chatgroup=new Chatgroup();
		if (id != null && !id.equals(""))
			chatgroup.setId(Long.parseLong(id));
		chatgroup.setGroupid(groupid);
		chatgroup.setGroupname(groupname);
		chatgroup.setAdduserid(adduserid);
		chatgroup.setGroupimage(groupimage);
		chatgroup.setGroupdetail(groupdetail);
		try {
		if (addtime != null && !addtime.equals(""))
			chatgroup.setAddtime(sdf.parse(addtime));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		if (flag != null && !flag.equals(""))
			chatgroup.setFlag(Long.parseLong(flag));
		try {
			iChatgroupService.updatechatgroup(chatgroup);
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

