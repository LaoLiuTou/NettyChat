package com.nettychat.webService.groupuser;
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
import com.nettychat.service.groupuser.IGroupuserService;
import com.nettychat.model.groupuser.Groupuser;
public class GroupuserWSImpl implements IGroupuserWS {
 	/**
 	 * 根据ID查询
 	 * @return
 	 */
	public String getgroupuserById(String id) {
		Logger log = Logger.getLogger("NettyChatLogger");
		StringBuffer msg = new StringBuffer("{\"state\":");
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IGroupuserService iGroupuserService = (IGroupuserService)context.getBean("iGroupuserService");
		Groupuser groupuser=new Groupuser();
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			groupuser=iGroupuserService.selectgroupuserById(id);
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
			msg.append(JSONObject.fromObject(groupuser, jsonConfig));
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
	public String deletegroupuserById(String id) {
		Logger log = Logger.getLogger("NettyChatLogger");
		StringBuffer msg = new StringBuffer("{\"state\":");
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IGroupuserService iGroupuserService = (IGroupuserService)context.getBean("iGroupuserService");
		try {
			iGroupuserService.deletegroupuser(id);
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
	public String getgroupuserByParam(String id,String groupid,String userid,int page,int size){
		Logger log = Logger.getLogger("NettyChatLogger");
		StringBuffer msg = new StringBuffer("{\"state\":");
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IGroupuserService iGroupuserService = (IGroupuserService)context.getBean("iGroupuserService");
		List<Groupuser> list;
		try {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			Map paramMap = new HashMap();
			paramMap.put("id", id);
			paramMap.put("groupid", groupid);
			paramMap.put("userid", userid);
			paramMap.put("fromPage",(page-1)*size);
			paramMap.put("toPage",page*size);
			list=iGroupuserService.selectgroupuserByParam(paramMap);
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
	public String addgroupuser(String groupid,String userid){
		Logger log = Logger.getLogger("NettyChatLogger");
		StringBuffer msg = new StringBuffer("{\"state\":");
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IGroupuserService iGroupuserService = (IGroupuserService)context.getBean("iGroupuserService");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Groupuser groupuser=new Groupuser();
		groupuser.setGroupid(groupid);
		groupuser.setUserid(userid);
		try {
			int result = Integer.parseInt(iGroupuserService.addgroupuser(groupuser).toString());
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
	public String updategroupuser(String id,String groupid,String userid){
		Logger log = Logger.getLogger("NettyChatLogger");
		StringBuffer msg = new StringBuffer("{\"state\":");
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IGroupuserService iGroupuserService = (IGroupuserService)context.getBean("iGroupuserService");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Groupuser groupuser=new Groupuser();
		if (id != null && !id.equals(""))
			groupuser.setId(Long.parseLong(id));
		groupuser.setGroupid(groupid);
		groupuser.setUserid(userid);
		try {
			iGroupuserService.updategroupuser(groupuser);
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

