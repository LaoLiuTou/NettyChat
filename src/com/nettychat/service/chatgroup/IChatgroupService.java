package com.nettychat.service.chatgroup;
import java.util.List;
import java.util.Map;
import com.nettychat.model.chatgroup.Chatgroup;
public interface IChatgroupService {
/**
 * 通过id选取
 * @return
 */
public Chatgroup selectchatgroupById(String id);
/**
 * 通过查询参数获取信息
 * @return
 */ 
public List<Chatgroup> selectchatgroupByParam(Map paramMap); 
	/**
	* 通过查询参数获取总条数
	 * @return
	 */ 
	public int selectCountchatgroupByParam(Map paramMap); 
/**
 * 更新 
 * @return 
 */ 
public  Object updatechatgroup(Chatgroup chatgroup);
/**
 * 添加 
 * @return
 */ 
public  Object addchatgroup(Chatgroup chatgroup);
/**
 * 删除 
 * @return 
 */ 
public  Object deletechatgroup(String id);

}

