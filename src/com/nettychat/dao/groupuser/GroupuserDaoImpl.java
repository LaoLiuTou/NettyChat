package com.nettychat.dao.groupuser;
import java.util.List;
import java.util.Map;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import com.nettychat.model.groupuser.Groupuser;
public class GroupuserDaoImpl extends SqlMapClientDaoSupport implements IGroupuserDao {

	/**
 * 通过id选取
 * @return
 */
	public Groupuser selectgroupuserById(String id){
		return (Groupuser) getSqlMapClientTemplate().queryForObject("selectgroupuserById",id);
	}

	/**
 * 通过查询参数获取信息
 * @return
 */ 
	@SuppressWarnings("unchecked")
	public List<Groupuser> selectgroupuserByParam(Map paramMap){ 
		return  getSqlMapClientTemplate().queryForList("selectgroupuserByParam",paramMap);
	}

	/**
	* 通过查询参数获取总条数
	 * @return
	 */ 
	@SuppressWarnings("unchecked")
	public int selectCountgroupuserByParam(Map paramMap){ 
		return   Integer.parseInt(getSqlMapClientTemplate().queryForObject("selectCountgroupuserByParam",paramMap).toString()); 
	}

	/**
 * 更新 
 * @return 
 */ 
	public  Object updategroupuser(Groupuser groupuser){
		return  getSqlMapClientTemplate().update("updategroupuser",groupuser); 
	}

	/**
 * 添加 
 * @return
 */ 
	public  Object addgroupuser(Groupuser groupuser){
		return  getSqlMapClientTemplate().insert("addgroupuser",groupuser);
	}

	/**
 * 删除 
 * @return 
 */ 
	public  Object deletegroupuser(String id){
		return  getSqlMapClientTemplate().delete("deletegroupuser",id);
	}

}

