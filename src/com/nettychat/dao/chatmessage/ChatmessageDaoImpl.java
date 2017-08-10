package com.nettychat.dao.chatmessage;
import java.util.List;
import java.util.Map;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import com.nettychat.model.chatmessage.Chatmessage;
public class ChatmessageDaoImpl extends SqlMapClientDaoSupport implements IChatmessageDao {

	/**
 * 通过id选取
 * @return
 */
	public Chatmessage selectchatmessageById(String id){
		return (Chatmessage) getSqlMapClientTemplate().queryForObject("selectchatmessageById",id);
	}

	/**
 * 通过查询参数获取信息
 * @return
 */ 
	@SuppressWarnings("unchecked")
	public List<Chatmessage> selectchatmessageByParam(Map paramMap){ 
		return  getSqlMapClientTemplate().queryForList("selectchatmessageByParam",paramMap);
	}

	/**
	* 通过查询参数获取总条数
	 * @return
	 */ 
	@SuppressWarnings("unchecked")
	public int selectCountchatmessageByParam(Map paramMap){ 
		return   Integer.parseInt(getSqlMapClientTemplate().queryForObject("selectCountchatmessageByParam",paramMap).toString()); 
	}

	/**
 * 更新 
 * @return 
 */ 
	public  Object updatechatmessage(Chatmessage chatmessage){
		return  getSqlMapClientTemplate().update("updatechatmessage",chatmessage); 
	}

	/**
 * 添加 
 * @return
 */ 
	public  Object addchatmessage(Chatmessage chatmessage){
		return  getSqlMapClientTemplate().insert("addchatmessage",chatmessage);
	}

	/**
 * 删除 
 * @return 
 */ 
	public  Object deletechatmessage(String id){
		return  getSqlMapClientTemplate().delete("deletechatmessage",id);
	}

}

