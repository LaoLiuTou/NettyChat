package com.nettychat.service.messagebackup;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.nettychat.dao.messagebackup.IMessagebackupDao;
import com.nettychat.model.messagebackup.Messagebackup;
public class MessagebackupServiceImpl  implements IMessagebackupService {

	@Autowired
	private IMessagebackupDao iMessagebackupDao;
	/**
 * 通过id选取
 * @return
 */
 @Transactional
	public Messagebackup selectmessagebackupById(String id){
		return iMessagebackupDao.selectmessagebackupById(id);
	}

	/**
 * 通过查询参数获取信息
 * @return
 */ 
 @Transactional
	public List<Messagebackup> selectmessagebackupByParam(Map paramMap){ 
		return iMessagebackupDao.selectmessagebackupByParam(paramMap);
	}

	/**
	* 通过查询参数获取总条数
	 * @return
	 */ 
 @Transactional
	public int selectCountmessagebackupByParam(Map paramMap){ 
		return iMessagebackupDao.selectCountmessagebackupByParam(paramMap);
	}

	/**
 * 更新 
 * @return 
 */ 
 @Transactional
	public  Object updatemessagebackup(Messagebackup messagebackup){
		return iMessagebackupDao.updatemessagebackup(messagebackup);
	}

	/**
 * 添加 
 * @return
 */ 
 @Transactional
	public  Object addmessagebackup(Messagebackup messagebackup){
		return iMessagebackupDao.addmessagebackup(messagebackup);
	}

	/**
 * 删除 
 * @return 
 */ 
 @Transactional
	public  Object deletemessagebackup(String id){
		return iMessagebackupDao.deletemessagebackup(id);
	}

}

