package com.nettychat.action.chatuser;
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
import com.nettychat.service.chatuser.IChatuserService;
import com.nettychat.model.chatuser.Chatuser;
public class ChatuserAction implements Action {
	private int page;
	private int size;
	private int totalpage;
	private int totalnumber;
	private String message;
	@Autowired
	private IChatuserService iChatuserService;
	private List<Chatuser> list;
	private Chatuser chatuser;
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
	public Chatuser getChatuser() {
		return chatuser;
	}
	public void setChatuser(Chatuser chatuser) {
		this.chatuser = chatuser;
	}
	public List<Chatuser> getList() {
		return list;
	}
	public void setList(List<Chatuser> list) {
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
	private String username;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	private String userimage;
	public String getUserimage() {
		return userimage;
	}
	public void setUserimage(String userimage) {
		this.userimage = userimage;
	}
	private String detail;
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
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
	private String isonline;
	public String getIsonline() {
		return isonline;
	}
	public void setIsonline(String isonline) {
		this.isonline = isonline;
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
		Chatuser chatuser =new Chatuser(); 
		if(id!=null&&!id.equals(""))
		chatuser.setId(Long.parseLong(id));
		chatuser.setUserid(userid);
		chatuser.setUsername(username);
		chatuser.setUserimage(userimage);
		chatuser.setDetail(detail);
		if(addtime!=null&&!addtime.equals(""))
		chatuser.setAddtime(sdf.parse(addtime));
		chatuser.setIsonline(isonline);
		chatuser.setFlag(flag);
		try {
			int result = Integer.parseInt(iChatuserService.addchatuser(chatuser).toString());
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
			paramMap.put("username", username);
			paramMap.put("userimage", userimage);
			paramMap.put("detail", detail);
			if(addtimeFrom!=null&&!addtimeFrom.equals(""))
			paramMap.put("addtimeFrom", sdf.parse(addtimeFrom));
			if(addtimeTo!=null&&!addtimeTo.equals(""))
			paramMap.put("addtimeTo", sdf.parse(addtimeTo));
			paramMap.put("isonline", isonline);
			paramMap.put("flag", flag);
		try {
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
		paramMap.put("username", username);
		paramMap.put("userimage", userimage);
		paramMap.put("detail", detail);
		if(addtimeFrom!=null&&!addtimeFrom.equals(""))
			paramMap.put("addtimeFrom", sdf.parse(addtimeFrom));
		if(addtimeTo!=null&&!addtimeTo.equals(""))
			paramMap.put("addtimeTo", sdf.parse(addtimeTo));
		paramMap.put("isonline", isonline);
		paramMap.put("flag", flag);
		try {
			list=iChatuserService.selectchatuserByParam(paramMap); 
			totalnumber=iChatuserService.selectCountchatuserByParam(paramMap);
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
		Chatuser chatuser =new Chatuser(); 
		if(id!=null&&!id.equals(""))
		chatuser.setId(Long.parseLong(id));
		chatuser.setUserid(userid);
		chatuser.setUsername(username);
		chatuser.setUserimage(userimage);
		chatuser.setDetail(detail);
		if(addtime!=null&&!addtime.equals(""))
		chatuser.setAddtime(sdf.parse(addtime));
		chatuser.setIsonline(isonline);
		chatuser.setFlag(flag);
		try {
			iChatuserService.updatechatuser(chatuser);
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
		try {
			iChatuserService.deletechatuser(id);
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
			chatuser=iChatuserService.selectchatuserById(id);
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
