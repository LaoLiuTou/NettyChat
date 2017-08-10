package com.nettychat.dao.chatuser;
import java.util.List;
import java.util.Map;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import com.nettychat.model.chatuser.Chatuser;
public class ChatuserDaoImpl extends SqlMapClientDaoSupport implements IChatuserDao {

	/**
 * 通过id选取
 * @return
 */
	public Chatuser selectchatuserById(String id){
		return (Chatuser) getSqlMapClientTemplate().queryForObject("selectchatuserById",id);
	}

	/**
 * 通过查询参数获取信息
 * @return
 */ 
	@SuppressWarnings("unchecked")
	public List<Chatuser> selectchatuserByParam(Map paramMap){ 
		return  getSqlMapClientTemplate().queryForList("selectchatuserByParam",paramMap);
	}

	/**
	* 通过查询参数获取总条数
	 * @return
	 */ 
	@SuppressWarnings("unchecked")
	public int selectCountchatuserByParam(Map paramMap){ 
		return   Integer.parseInt(getSqlMapClientTemplate().queryForObject("selectCountchatuserByParam",paramMap).toString()); 
	}

	/**
 * 更新 
 * @return 
 */ 
	public  Object updatechatuser(Chatuser chatuser){
		return  getSqlMapClientTemplate().update("updatechatuser",chatuser); 
	}

	/**
 * 添加 
 * @return
 */ 
	public  Object addchatuser(Chatuser chatuser){
		return  getSqlMapClientTemplate().insert("addchatuser",chatuser);
	}

	/**
 * 删除 
 * @return 
 */ 
	public  Object deletechatuser(String id){
		return  getSqlMapClientTemplate().delete("deletechatuser",id);
	}

}

