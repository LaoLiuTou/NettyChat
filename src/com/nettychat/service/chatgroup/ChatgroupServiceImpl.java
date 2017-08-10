package com.nettychat.service.chatgroup;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.nettychat.dao.chatgroup.IChatgroupDao;
import com.nettychat.model.chatgroup.Chatgroup;
public class ChatgroupServiceImpl  implements IChatgroupService {

	@Autowired
	private IChatgroupDao iChatgroupDao;
	/**
 * 通过id选取
 * @return
 */
 @Transactional
	public Chatgroup selectchatgroupById(String id){
		return iChatgroupDao.selectchatgroupById(id);
	}

	/**
 * 通过查询参数获取信息
 * @return
 */ 
 @Transactional
	public List<Chatgroup> selectchatgroupByParam(Map paramMap){ 
		return iChatgroupDao.selectchatgroupByParam(paramMap);
	}

	/**
	* 通过查询参数获取总条数
	 * @return
	 */ 
 @Transactional
	public int selectCountchatgroupByParam(Map paramMap){ 
		return iChatgroupDao.selectCountchatgroupByParam(paramMap);
	}

	/**
 * 更新 
 * @return 
 */ 
 @Transactional
	public  Object updatechatgroup(Chatgroup chatgroup){
		return iChatgroupDao.updatechatgroup(chatgroup);
	}

	/**
 * 添加 
 * @return
 */ 
 @Transactional
	public  Object addchatgroup(Chatgroup chatgroup){
		return iChatgroupDao.addchatgroup(chatgroup);
	}

	/**
 * 删除 
 * @return 
 */ 
 @Transactional
	public  Object deletechatgroup(String id){
		return iChatgroupDao.deletechatgroup(id);
	}

}

