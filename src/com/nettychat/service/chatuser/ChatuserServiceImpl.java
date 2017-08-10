package com.nettychat.service.chatuser;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.nettychat.dao.chatuser.IChatuserDao;
import com.nettychat.model.chatuser.Chatuser;
public class ChatuserServiceImpl  implements IChatuserService {

	@Autowired
	private IChatuserDao iChatuserDao;
	/**
 * 通过id选取
 * @return
 */
 @Transactional
	public Chatuser selectchatuserById(String id){
		return iChatuserDao.selectchatuserById(id);
	}

	/**
 * 通过查询参数获取信息
 * @return
 */ 
 @Transactional
	public List<Chatuser> selectchatuserByParam(Map paramMap){ 
		return iChatuserDao.selectchatuserByParam(paramMap);
	}

	/**
	* 通过查询参数获取总条数
	 * @return
	 */ 
 @Transactional
	public int selectCountchatuserByParam(Map paramMap){ 
		return iChatuserDao.selectCountchatuserByParam(paramMap);
	}

	/**
 * 更新 
 * @return 
 */ 
 @Transactional
	public  Object updatechatuser(Chatuser chatuser){
		return iChatuserDao.updatechatuser(chatuser);
	}

	/**
 * 添加 
 * @return
 */ 
 @Transactional
	public  Object addchatuser(Chatuser chatuser){
		return iChatuserDao.addchatuser(chatuser);
	}

	/**
 * 删除 
 * @return 
 */ 
 @Transactional
	public  Object deletechatuser(String id){
		return iChatuserDao.deletechatuser(id);
	}

}

