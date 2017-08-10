package com.nettychat.action.chatmessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import com.opensymphony.xwork2.Action;
import com.nettychat.service.chatmessage.IChatmessageService;
import com.nettychat.model.chatmessage.Chatmessage;
public class ChatmessageAction implements Action {
	private int page;
	private int size;
	private int totalpage;
	private int totalnumber;
	private String message;
	@Autowired
	private IChatmessageService iChatmessageService;
	private List<Chatmessage> list;
	private Chatmessage chatmessage;
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getTotalpage() {
		return totalpage;
	}
	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}
	public int getTotalnumber() {
		return totalnumber;
	}
	public void setTotalnumber(int totalnumber) {
		this.totalnumber = totalnumber;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Chatmessage getChatmessage() {
		return chatmessage;
	}
	public void setChatmessage(Chatmessage chatmessage) {
		this.chatmessage = chatmessage;
	}
	public List<Chatmessage> getList() {
		return list;
	}
	public void setList(List<Chatmessage> list) {
		this.list = list;
	}
	HttpServletResponse response = ServletActionContext.getResponse(); 
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Logger logger = Logger.getLogger("NettyChatLogger");
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	private String userid;
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	private String friendid;
	public String getFriendid() {
		return friendid;
	}
	public void setFriendid(String friendid) {
		this.friendid = friendid;
	}
	private String addtimeFrom;
	public String getAddtimeFrom() {
		return addtimeFrom;
	}
	public void setAddtimeFrom(String addtimeFrom) {
		this.addtimeFrom = addtimeFrom;
	}
	private String addtimeTo;
	public String getAddtimeTo() {
		return addtimeTo;
	}
	public void setAddtimeTo(String addtimeTo) {
		this.addtimeTo = addtimeTo;
	}
	private String addtime;
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	private String chattype;
	public String getChattype() {
		return chattype;
	}
	public void setChattype(String chattype) {
		this.chattype = chattype;
	}
	private String contenttype;
	public String getContenttype() {
		return contenttype;
	}
	public void setContenttype(String contenttype) {
		this.contenttype = contenttype;
	}
	private String content;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	private String readstatus;
	public String getReadstatus() {
		return readstatus;
	}
	public void setReadstatus(String readstatus) {
		this.readstatus = readstatus;
	}
	private String sendstatus;
	public String getSendstatus() {
		return sendstatus;
	}
	public void setSendstatus(String sendstatus) {
		this.sendstatus = sendstatus;
	}
	private String isgroup;
	public String getIsgroup() {
		return isgroup;
	}
	public void setIsgroup(String isgroup) {
		this.isgroup = isgroup;
	}
	private String flag;
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String add() throws Exception {
		response.setCharacterEncoding("UTF-8"); 
		Chatmessage chatmessage =new Chatmessage(); 
		if(id!=null&&!id.equals(""))
		chatmessage.setId(Long.parseLong(id));
		chatmessage.setUserid(userid);
		chatmessage.setFriendid(friendid);
		if(addtime!=null&&!addtime.equals(""))
		chatmessage.setAddtime(sdf.parse(addtime));
		chatmessage.setChattype(chattype);
		chatmessage.setContenttype(contenttype);
		chatmessage.setContent(content);
		chatmessage.setReadstatus(readstatus);
		chatmessage.setSendstatus(sendstatus);
		chatmessage.setIsgroup(isgroup);
		chatmessage.setFlag(flag);
		try {
			int result = Integer.parseInt(iChatmessageService.addchatmessage(chatmessage).toString());
			response.getWriter().write("添加成功！"); 
			message="添加成功！";  
			logger.info(result+"添加成功！。");
		} catch (Exception e) {
			message="添加失败！";  
			logger.info("添加失败！。");
			e.printStackTrace();
		}
		return "add";
	}
	@SuppressWarnings("unchecked")
	public String list() throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		StringBuffer msg = new StringBuffer("{\"state\":");
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map  paramMap = new HashMap ();
		paramMap.put("fromPage",(page-1)*size);
		paramMap.put("toPage",page*size); 
			paramMap.put("id", id);
			paramMap.put("userid", userid);
			paramMap.put("friendid", friendid);
			if(addtimeFrom!=null&&!addtimeFrom.equals(""))
			paramMap.put("addtimeFrom", sdf.parse(addtimeFrom));
			if(addtimeTo!=null&&!addtimeTo.equals(""))
			paramMap.put("addtimeTo", sdf.parse(addtimeTo));
			paramMap.put("chattype", chattype);
			paramMap.put("contenttype", contenttype);
			paramMap.put("content", content);
			paramMap.put("readstatus", readstatus);
			paramMap.put("sendstatus", sendstatus);
			paramMap.put("isgroup", isgroup);
			paramMap.put("flag", flag);
		try {
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
			logger.info("查询失败."+e);
			e.printStackTrace();
		}
		msg.append("}");
		response.getWriter().write(msg.toString()); 
		return null;
	}
	@SuppressWarnings("unchecked")
	public String lista() throws Exception {
		
		size=15;
		Map  paramMap = new HashMap ();
		paramMap.put("fromPage",(page-1)*size);
		paramMap.put("toPage",page*size); 
		paramMap.put("id", id);
		paramMap.put("userid", userid);
		paramMap.put("friendid", friendid);
		if(addtimeFrom!=null&&!addtimeFrom.equals(""))
			paramMap.put("addtimeFrom", sdf.parse(addtimeFrom));
		if(addtimeTo!=null&&!addtimeTo.equals(""))
			paramMap.put("addtimeTo", sdf.parse(addtimeTo));
		paramMap.put("chattype", chattype);
		paramMap.put("contenttype", contenttype);
		paramMap.put("content", content);
		paramMap.put("readstatus", readstatus);
		paramMap.put("sendstatus", sendstatus);
		paramMap.put("isgroup", isgroup);
		paramMap.put("flag", flag);
		try {
			list=iChatmessageService.selectchatmessageByParam(paramMap); 
			totalnumber=iChatmessageService.selectCountchatmessageByParam(paramMap);
			if((totalnumber%size)==0){
				totalpage=(totalnumber/size);
			}
			else{
				totalpage=(totalnumber/size)+1;
			}	
			logger.info("获取列表成功！");
		} catch (Exception e) {
			logger.info("获取列表失败！"+e);
			e.printStackTrace();
		}
		return "list";
	}

	public String update() throws Exception {
		Chatmessage chatmessage =new Chatmessage(); 
		if(id!=null&&!id.equals(""))
		chatmessage.setId(Long.parseLong(id));
		chatmessage.setUserid(userid);
		chatmessage.setFriendid(friendid);
		if(addtime!=null&&!addtime.equals(""))
		chatmessage.setAddtime(sdf.parse(addtime));
		chatmessage.setChattype(chattype);
		chatmessage.setContenttype(contenttype);
		chatmessage.setContent(content);
		chatmessage.setReadstatus(readstatus);
		chatmessage.setSendstatus(sendstatus);
		chatmessage.setIsgroup(isgroup);
		chatmessage.setFlag(flag);
		try {
			iChatmessageService.updatechatmessage(chatmessage);
			response.getWriter().write("更新成功！"); 
			message="更新成功！";  
			logger.info(id+"更新成功！");
		} catch (Exception e) {
			logger.info(id+"更新失败！"+e);
			message="更新失败！"; 
			response.getWriter().write("更新失败！"); 
			e.printStackTrace();
		}
			return null;
	}
	public String delete() throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		StringBuffer msg = new StringBuffer("{\"state\":");
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			iChatmessageService.deletechatmessage(id);
			msg.append("\"success\",\"msg\":");
			msg.append("\"删除成功.\"");
		} catch (Exception e) {
			msg.append("\"failure\",\"msg\":");
			msg.append("\"删除失败.\"");
			logger.info("删除失败."+e);
			e.printStackTrace();
		}
		msg.append("}");
		response.getWriter().write(msg.toString()); 
		return null;
	}
	public String deletea() throws Exception {
		try {
			iChatmessageService.deletechatmessage(id);
			response.getWriter().write("删除成功！"); 
			logger.info(id+"删除成功！");
		} catch (Exception e) {
			logger.info(id+"删除失败！"+e);
			response.getWriter().write("删除失败！"); 
			e.printStackTrace();
		}
		return null;
	}
	public String select() throws Exception {
		try {
			chatmessage=iChatmessageService.selectchatmessageById(id);
			logger.info(id+"查询成功！");
		} catch (Exception e) {
			logger.info(id+"查询失败！"+e);
			e.printStackTrace();
		}
		return "select";
	}
    public String execute() throws Exception {
		return null;
	}
}
