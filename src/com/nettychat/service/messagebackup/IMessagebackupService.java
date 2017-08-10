package com.nettychat.service.messagebackup;
import java.util.List;
import java.util.Map;
import com.nettychat.model.messagebackup.Messagebackup;
public interface IMessagebackupService {
/**
 * 通过id选取
 * @return
 */
public Messagebackup selectmessagebackupById(String id);
/**
 * 通过查询参数获取信息
 * @return
 */ 
public List<Messagebackup> selectmessagebackupByParam(Map paramMap); 
	/**
	* 通过查询参数获取总条数
	 * @return
	 */ 
	public int selectCountmessagebackupByParam(Map paramMap); 
/**
 * 更新 
 * @return 
 */ 
public  Object updatemessagebackup(Messagebackup messagebackup);
/**
 * 添加 
 * @return
 */ 
public  Object addmessagebackup(Messagebackup messagebackup);
/**
 * 删除 
 * @return 
 */ 
public  Object deletemessagebackup(String id);

}

