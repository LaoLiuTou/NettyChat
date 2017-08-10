package com.nettychat.dao.chatfriend;
import java.util.List;
import java.util.Map;
import com.nettychat.model.chatfriend.Chatfriend;
public interface IChatfriendDao {
/**
 * 通过id选取
 * @return
 */
public Chatfriend selectchatfriendById(String id);
/**
 * 通过查询参数获取信息
 * @return
 */ 
public List<Chatfriend> selectchatfriendByParam(Map paramMap); 
	/**
	* 通过查询参数获取总条数
	 * @return
	 */ 
	public int selectCountchatfriendByParam(Map paramMap); 
/**
 * 更新 
 * @return 
 */ 
public  Object updatechatfriend(Chatfriend chatfriend);
/**
 * 添加 
 * @return
 */ 
public  Object addchatfriend(Chatfriend chatfriend);
/**
 * 删除 
 * @return 
 */ 
public  Object deletechatfriend(String id);

}

