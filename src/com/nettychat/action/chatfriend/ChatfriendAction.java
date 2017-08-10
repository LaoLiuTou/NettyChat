package com.nettychat.action.chatfriend;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.nettychat.model.chatfriend.Chatfriend;
import com.nettychat.model.chatmessage.Chatmessage;
import com.nettychat.model.chatuser.Chatuser;
import com.nettychat.model.frienddetails.Frienddetails;
import com.nettychat.service.chatfriend.IChatfriendService;
import com.nettychat.service.chatmessage.IChatmessageService;
import com.nettychat.service.chatuser.IChatuserService;
import com.nettychat.utils.Base64;
import com.opensymphony.xwork2.Action;
public class ChatfriendAction implements Action {
	private int page;
	private int size;
	private int totalpage;
	private int totalnumber;
	private String message;
	@Autowired
	private IChatfriendService iChatfriendService;
	@Autowired
	private IChatuserService iChatuserService;
	@Autowired
	private IChatmessageService iChatmessageService;
	private List<Chatfriend> list;
	private Chatfriend chatfriend;
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
	public Chatfriend getChatfriend() {
		return chatfriend;
	}
	public void setChatfriend(Chatfriend chatfriend) {
		this.chatfriend = chatfriend;
	}
	public List<Chatfriend> getList() {
		return list;
	}
	public void setList(List<Chatfriend> list) {
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
	private String isgroup;
	public String getIsgroup() {
		return isgroup;
	}
	public void setIsgroup(String isgroup) {
		this.isgroup = isgroup;
	}
	private String isonline;
	public String getIsonline() {
		return isonline;
	}
	public void setIsonline(String isonline) {
		this.isonline = isonline;
	}
	public String add() throws Exception {
		response.setCharacterEncoding("UTF-8"); 
		Chatfriend chatfriend =new Chatfriend(); 
		if(id!=null&&!id.equals(""))
		chatfriend.setId(Long.parseLong(id));
		chatfriend.setUserid(userid);
		chatfriend.setFriendid(friendid);
		chatfriend.setIsgroup(isgroup);
		chatfriend.setIsonline(isonline);
		try {
			int result = Integer.parseInt(iChatfriendService.addchatfriend(chatfriend).toString());
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
			paramMap.put("isgroup", isgroup);
			paramMap.put("isonline", isonline);
		try {
			List<Frienddetails> friendList= new ArrayList<Frienddetails>();
			list=iChatfriendService.selectchatfriendByParam(paramMap); 
			for(Chatfriend cf:list){
				
				Frienddetails fd=new Frienddetails();
				
				Chatuser cu = iChatuserService.selectchatuserById(cf.getFriendid());
				Map  param = new HashMap ();
				param.put("fromPage",0);
				param.put("toPage",1); 
				param.put("flag",0); 
				param.put("userid", cf.getUserid());
				param.put("friendid", cf.getFriendid());
				List<Chatmessage> cmList =iChatmessageService.selectchatmessageByParam(param);
				fd.setId(cf.getId());
				fd.setFriendid(cf.getFriendid());
				fd.setIsgroup(cf.getIsgroup());
				
				fd.setUserid(cf.getUserid());
				if(cu!=null){
					fd.setFirendheader(cu.getUserimage());
					fd.setFriendname(cu.getUsername());
					fd.setIsonline(cu.getIsonline());
				}
				if(cmList.size()>0){
					fd.setLchattype(cmList.get(0).getChattype());
					fd.setLcontenttype(cmList.get(0).getContenttype());
					if(cmList.get(0).getContenttype().equals("0")){
						fd.setLmessage(cmList.get(0).getContent());
					}
					else if(cmList.get(0).getContenttype().equals("1")){
						fd.setLmessage(Base64.getBase64("[图片]"));
					}
					else if(cmList.get(0).getContenttype().equals("2")){
						fd.setLmessage(Base64.getBase64("[语音]"));
					}
					else if(cmList.get(0).getContenttype().equals("3")){
						fd.setLmessage(Base64.getBase64("[视频]"));
					}
					else if(cmList.get(0).getContenttype().equals("4")){
						fd.setLmessage(Base64.getBase64("[文件]"));
					}
					fd.setLtime(cmList.get(0).getAddtime());
					
				}
				
				param.put("chattype", "1");
				param.put("readstatus", "1");
				param.put("flag", "0");//消息/推送
				param.put("fromPage",0);
				param.put("toPage",100); 
				fd.setUnread(iChatmessageService.selectCountchatmessageByParam(param)+"");
				friendList.add(fd);
				
			}
		
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
			msg.append(JSONArray.fromObject(friendList, jsonConfig));
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
		paramMap.put("isgroup", isgroup);
		paramMap.put("isonline", isonline);
		try {
			list=iChatfriendService.selectchatfriendByParam(paramMap); 
			totalnumber=iChatfriendService.selectCountchatfriendByParam(paramMap);
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
		Chatfriend chatfriend =new Chatfriend(); 
		if(id!=null&&!id.equals(""))
		chatfriend.setId(Long.parseLong(id));
		chatfriend.setUserid(userid);
		chatfriend.setFriendid(friendid);
		chatfriend.setIsgroup(isgroup);
		chatfriend.setIsonline(isonline);
		try {
			iChatfriendService.updatechatfriend(chatfriend);
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
			iChatfriendService.deletechatfriend(id);
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
			iChatfriendService.deletechatfriend(id);
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
			chatfriend=iChatfriendService.selectchatfriendById(id);
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
