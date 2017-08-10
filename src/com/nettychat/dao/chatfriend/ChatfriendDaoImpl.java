package com.nettychat.dao.chatfriend;
import java.util.List;
import java.util.Map;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import com.nettychat.model.chatfriend.Chatfriend;
public class ChatfriendDaoImpl extends SqlMapClientDaoSupport implements IChatfriendDao {

	/**
 * 通过id选取
 * @return
 */
	public Chatfriend selectchatfriendById(String id){
		return (Chatfriend) getSqlMapClientTemplate().queryForObject("selectchatfriendById",id);
	}

	/**
 * 通过查询参数获取信息
 * @return
 */ 
	@SuppressWarnings("unchecked")
	public List<Chatfriend> selectchatfriendByParam(Map paramMap){ 
		return  getSqlMapClientTemplate().queryForList("selectchatfriendByParam",paramMap);
	}

	/**
	* 通过查询参数获取总条数
	 * @return
	 */ 
	@SuppressWarnings("unchecked")
	public int selectCountchatfriendByParam(Map paramMap){ 
		return   Integer.parseInt(getSqlMapClientTemplate().queryForObject("selectCountchatfriendByParam",paramMap).toString()); 
	}

	/**
 * 更新 
 * @return 
 */ 
	public  Object updatechatfriend(Chatfriend chatfriend){
		return  getSqlMapClientTemplate().update("updatechatfriend",chatfriend); 
	}

	/**
 * 添加 
 * @return
 */ 
	public  Object addchatfriend(Chatfriend chatfriend){
		return  getSqlMapClientTemplate().insert("addchatfriend",chatfriend);
	}

	/**
 * 删除 
 * @return 
 */ 
	public  Object deletechatfriend(String id){
		return  getSqlMapClientTemplate().delete("deletechatfriend",id);
	}

}

