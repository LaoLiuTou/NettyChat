package com.nettychat.service.chatuser;
import java.util.List;
import java.util.Map;
import com.nettychat.model.chatuser.Chatuser;
public interface IChatuserService {
/**
 * 通过id选取
 * @return
 */
public Chatuser selectchatuserById(String id);
/**
 * 通过查询参数获取信息
 * @return
 */ 
public List<Chatuser> selectchatuserByParam(Map paramMap); 
	/**
	* 通过查询参数获取总条数
	 * @return
	 */ 
	public int selectCountchatuserByParam(Map paramMap); 
/**
 * 更新 
 * @return 
 */ 
public  Object updatechatuser(Chatuser chatuser);
/**
 * 添加 
 * @return
 */ 
public  Object addchatuser(Chatuser chatuser);
/**
 * 删除 
 * @return 
 */ 
public  Object deletechatuser(String id);

}

