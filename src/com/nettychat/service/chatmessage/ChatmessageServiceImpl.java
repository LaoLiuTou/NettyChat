package com.nettychat.service.chatmessage;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.nettychat.dao.chatmessage.IChatmessageDao;
import com.nettychat.model.chatmessage.Chatmessage;
public class ChatmessageServiceImpl  implements IChatmessageService {

	@Autowired
	private IChatmessageDao iChatmessageDao;
	/**
 * 通过id选取
 * @return
 */
 @Transactional
	public Chatmessage selectchatmessageById(String id){
		return iChatmessageDao.selectchatmessageById(id);
	}

	/**
 * 通过查询参数获取信息
 * @return
 */ 
 @Transactional
	public List<Chatmessage> selectchatmessageByParam(Map paramMap){ 
		return iChatmessageDao.selectchatmessageByParam(paramMap);
	}

	/**
	* 通过查询参数获取总条数
	 * @return
	 */ 
 @Transactional
	public int selectCountchatmessageByParam(Map paramMap){ 
		return iChatmessageDao.selectCountchatmessageByParam(paramMap);
	}

	/**
 * 更新 
 * @return 
 */ 
 @Transactional
	public  Object updatechatmessage(Chatmessage chatmessage){
		return iChatmessageDao.updatechatmessage(chatmessage);
	}

	/**
 * 添加 
 * @return
 */ 
 @Transactional
	public  Object addchatmessage(Chatmessage chatmessage){
		return iChatmessageDao.addchatmessage(chatmessage);
	}

	/**
 * 删除 
 * @return 
 */ 
 @Transactional
	public  Object deletechatmessage(String id){
		return iChatmessageDao.deletechatmessage(id);
	}

}

