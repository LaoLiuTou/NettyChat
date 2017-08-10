package com.nettychat.service.groupuser;
import java.util.List;
import java.util.Map;
import com.nettychat.model.groupuser.Groupuser;
public interface IGroupuserService {
/**
 * 通过id选取
 * @return
 */
public Groupuser selectgroupuserById(String id);
/**
 * 通过查询参数获取信息
 * @return
 */ 
public List<Groupuser> selectgroupuserByParam(Map paramMap); 
	/**
	* 通过查询参数获取总条数
	 * @return
	 */ 
	public int selectCountgroupuserByParam(Map paramMap); 
/**
 * 更新 
 * @return 
 */ 
public  Object updategroupuser(Groupuser groupuser);
/**
 * 添加 
 * @return
 */ 
public  Object addgroupuser(Groupuser groupuser);
/**
 * 删除 
 * @return 
 */ 
public  Object deletegroupuser(String id);

}

