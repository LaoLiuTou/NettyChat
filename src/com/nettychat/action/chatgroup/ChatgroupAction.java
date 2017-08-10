package com.nettychat.action.chatgroup;
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
import com.nettychat.service.chatgroup.IChatgroupService;
import com.nettychat.model.chatgroup.Chatgroup;
public class ChatgroupAction implements Action {
	private int page;
	private int size;
	private int totalpage;
	private int totalnumber;
	private String message;
	@Autowired
	private IChatgroupService iChatgroupService;
	private List<Chatgroup> list;
	private Chatgroup chatgroup;
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
	public Chatgroup getChatgroup() {
		return chatgroup;
	}
	public void setChatgroup(Chatgroup chatgroup) {
		this.chatgroup = chatgroup;
	}
	public List<Chatgroup> getList() {
		return list;
	}
	public void setList(List<Chatgroup> list) {
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
	private String groupid;
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	private String groupname;
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	private String adduserid;
	public String getAdduserid() {
		return adduserid;
	}
	public void setAdduserid(String adduserid) {
		this.adduserid = adduserid;
	}
	private String groupimage;
	public String getGroupimage() {
		return groupimage;
	}
	public void setGroupimage(String groupimage) {
		this.groupimage = groupimage;
	}
	private String groupdetail;
	public String getGroupdetail() {
		return groupdetail;
	}
	public void setGroupdetail(String groupdetail) {
		this.groupdetail = groupdetail;
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
	private String flag;
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String add() throws Exception {
		response.setCharacterEncoding("UTF-8"); 
		Chatgroup chatgroup =new Chatgroup(); 
		if(id!=null&&!id.equals(""))
		chatgroup.setId(Long.parseLong(id));
		chatgroup.setGroupid(groupid);
		chatgroup.setGroupname(groupname);
		chatgroup.setAdduserid(adduserid);
		chatgroup.setGroupimage(groupimage);
		chatgroup.setGroupdetail(groupdetail);
		if(addtime!=null&&!addtime.equals(""))
		chatgroup.setAddtime(sdf.parse(addtime));
		if(flag!=null&&!flag.equals(""))
		chatgroup.setFlag(Long.parseLong(flag));
		try {
			int result = Integer.parseInt(iChatgroupService.addchatgroup(chatgroup).toString());
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
			paramMap.put("groupid", groupid);
			paramMap.put("groupname", groupname);
			paramMap.put("adduserid", adduserid);
			paramMap.put("groupimage", groupimage);
			paramMap.put("groupdetail", groupdetail);
			if(addtimeFrom!=null&&!addtimeFrom.equals(""))
			paramMap.put("addtimeFrom", sdf.parse(addtimeFrom));
			if(addtimeTo!=null&&!addtimeTo.equals(""))
			paramMap.put("addtimeTo", sdf.parse(addtimeTo));
			paramMap.put("flag", flag);
		try {
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
		paramMap.put("groupid", groupid);
		paramMap.put("groupname", groupname);
		paramMap.put("adduserid", adduserid);
		paramMap.put("groupimage", groupimage);
		paramMap.put("groupdetail", groupdetail);
		if(addtimeFrom!=null&&!addtimeFrom.equals(""))
			paramMap.put("addtimeFrom", sdf.parse(addtimeFrom));
		if(addtimeTo!=null&&!addtimeTo.equals(""))
			paramMap.put("addtimeTo", sdf.parse(addtimeTo));
		paramMap.put("flag", flag);
		try {
			list=iChatgroupService.selectchatgroupByParam(paramMap); 
			totalnumber=iChatgroupService.selectCountchatgroupByParam(paramMap);
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
		Chatgroup chatgroup =new Chatgroup(); 
		if(id!=null&&!id.equals(""))
		chatgroup.setId(Long.parseLong(id));
		chatgroup.setGroupid(groupid);
		chatgroup.setGroupname(groupname);
		chatgroup.setAdduserid(adduserid);
		chatgroup.setGroupimage(groupimage);
		chatgroup.setGroupdetail(groupdetail);
		if(addtime!=null&&!addtime.equals(""))
		chatgroup.setAddtime(sdf.parse(addtime));
		if(flag!=null&&!flag.equals(""))
		chatgroup.setFlag(Long.parseLong(flag));
		try {
			iChatgroupService.updatechatgroup(chatgroup);
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
			iChatgroupService.deletechatgroup(id);
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
			chatgroup=iChatgroupService.selectchatgroupById(id);
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
