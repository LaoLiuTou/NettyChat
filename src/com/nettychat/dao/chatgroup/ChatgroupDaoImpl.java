package com.nettychat.dao.chatgroup;
import java.util.List;
import java.util.Map;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import com.nettychat.model.chatgroup.Chatgroup;
public class ChatgroupDaoImpl extends SqlMapClientDaoSupport implements IChatgroupDao {

	/**
 * 通过id选取
 * @return
 */
	public Chatgroup selectchatgroupById(String id){
		return (Chatgroup) getSqlMapClientTemplate().queryForObject("selectchatgroupById",id);
	}

	/**
 * 通过查询参数获取信息
 * @return
 */ 
	@SuppressWarnings("unchecked")
	public List<Chatgroup> selectchatgroupByParam(Map paramMap){ 
		return  getSqlMapClientTemplate().queryForList("selectchatgroupByParam",paramMap);
	}

	/**
	* 通过查询参数获取总条数
	 * @return
	 */ 
	@SuppressWarnings("unchecked")
	public int selectCountchatgroupByParam(Map paramMap){ 
		return   Integer.parseInt(getSqlMapClientTemplate().queryForObject("selectCountchatgroupByParam",paramMap).toString()); 
	}

	/**
 * 更新 
 * @return 
 */ 
	public  Object updatechatgroup(Chatgroup chatgroup){
		return  getSqlMapClientTemplate().update("updatechatgroup",chatgroup); 
	}

	/**
 * 添加 
 * @return
 */ 
	public  Object addchatgroup(Chatgroup chatgroup){
		return  getSqlMapClientTemplate().insert("addchatgroup",chatgroup);
	}

	/**
 * 删除 
 * @return 
 */ 
	public  Object deletechatgroup(String id){
		return  getSqlMapClientTemplate().delete("deletechatgroup",id);
	}

}

