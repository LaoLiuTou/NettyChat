package com.nettychat.service.chatfriend;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.nettychat.dao.chatfriend.IChatfriendDao;
import com.nettychat.model.chatfriend.Chatfriend;
public class ChatfriendServiceImpl  implements IChatfriendService {

	@Autowired
	private IChatfriendDao iChatfriendDao;
	/**
 * 通过id选取
 * @return
 */
 @Transactional
	public Chatfriend selectchatfriendById(String id){
		return iChatfriendDao.selectchatfriendById(id);
	}

	/**
 * 通过查询参数获取信息
 * @return
 */ 
 @Transactional
	public List<Chatfriend> selectchatfriendByParam(Map paramMap){ 
		return iChatfriendDao.selectchatfriendByParam(paramMap);
	}

	/**
	* 通过查询参数获取总条数
	 * @return
	 */ 
 @Transactional
	public int selectCountchatfriendByParam(Map paramMap){ 
		return iChatfriendDao.selectCountchatfriendByParam(paramMap);
	}

	/**
 * 更新 
 * @return 
 */ 
 @Transactional
	public  Object updatechatfriend(Chatfriend chatfriend){
		return iChatfriendDao.updatechatfriend(chatfriend);
	}

	/**
 * 添加 
 * @return
 */ 
 @Transactional
	public  Object addchatfriend(Chatfriend chatfriend){
		return iChatfriendDao.addchatfriend(chatfriend);
	}

	/**
 * 删除 
 * @return 
 */ 
 @Transactional
	public  Object deletechatfriend(String id){
		return iChatfriendDao.deletechatfriend(id);
	}

}

