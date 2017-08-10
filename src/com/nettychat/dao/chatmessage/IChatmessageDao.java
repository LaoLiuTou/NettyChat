package com.nettychat.dao.chatmessage;
import java.util.List;
import java.util.Map;
import com.nettychat.model.chatmessage.Chatmessage;
public interface IChatmessageDao {
/**
 * 通过id选取
 * @return
 */
public Chatmessage selectchatmessageById(String id);
/**
 * 通过查询参数获取信息
 * @return
 */ 
public List<Chatmessage> selectchatmessageByParam(Map paramMap); 
	/**
	* 通过查询参数获取总条数
	 * @return
	 */ 
	public int selectCountchatmessageByParam(Map paramMap); 
/**
 * 更新 
 * @return 
 */ 
public  Object updatechatmessage(Chatmessage chatmessage);
/**
 * 添加 
 * @return
 */ 
public  Object addchatmessage(Chatmessage chatmessage);
/**
 * 删除 
 * @return 
 */ 
public  Object deletechatmessage(String id);

}

