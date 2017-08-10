package com.nettychat.dao.messagebackup;
import java.util.List;
import java.util.Map;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import com.nettychat.model.messagebackup.Messagebackup;
public class MessagebackupDaoImpl extends SqlMapClientDaoSupport implements IMessagebackupDao {

	/**
 * 通过id选取
 * @return
 */
	public Messagebackup selectmessagebackupById(String id){
		return (Messagebackup) getSqlMapClientTemplate().queryForObject("selectmessagebackupById",id);
	}

	/**
 * 通过查询参数获取信息
 * @return
 */ 
	@SuppressWarnings("unchecked")
	public List<Messagebackup> selectmessagebackupByParam(Map paramMap){ 
		return  getSqlMapClientTemplate().queryForList("selectmessagebackupByParam",paramMap);
	}

	/**
	* 通过查询参数获取总条数
	 * @return
	 */ 
	@SuppressWarnings("unchecked")
	public int selectCountmessagebackupByParam(Map paramMap){ 
		return   Integer.parseInt(getSqlMapClientTemplate().queryForObject("selectCountmessagebackupByParam",paramMap).toString()); 
	}

	/**
 * 更新 
 * @return 
 */ 
	public  Object updatemessagebackup(Messagebackup messagebackup){
		return  getSqlMapClientTemplate().update("updatemessagebackup",messagebackup); 
	}

	/**
 * 添加 
 * @return
 */ 
	public  Object addmessagebackup(Messagebackup messagebackup){
		return  getSqlMapClientTemplate().insert("addmessagebackup",messagebackup);
	}

	/**
 * 删除 
 * @return 
 */ 
	public  Object deletemessagebackup(String id){
		return  getSqlMapClientTemplate().delete("deletemessagebackup",id);
	}

}

