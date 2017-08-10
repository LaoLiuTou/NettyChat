package com.nettychat.service.groupuser;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.nettychat.dao.groupuser.IGroupuserDao;
import com.nettychat.model.groupuser.Groupuser;
public class GroupuserServiceImpl  implements IGroupuserService {

	@Autowired
	private IGroupuserDao iGroupuserDao;
	/**
 * 通过id选取
 * @return
 */
 @Transactional
	public Groupuser selectgroupuserById(String id){
		return iGroupuserDao.selectgroupuserById(id);
	}

	/**
 * 通过查询参数获取信息
 * @return
 */ 
 @Transactional
	public List<Groupuser> selectgroupuserByParam(Map paramMap){ 
		return iGroupuserDao.selectgroupuserByParam(paramMap);
	}

	/**
	* 通过查询参数获取总条数
	 * @return
	 */ 
 @Transactional
	public int selectCountgroupuserByParam(Map paramMap){ 
		return iGroupuserDao.selectCountgroupuserByParam(paramMap);
	}

	/**
 * 更新 
 * @return 
 */ 
 @Transactional
	public  Object updategroupuser(Groupuser groupuser){
		return iGroupuserDao.updategroupuser(groupuser);
	}

	/**
 * 添加 
 * @return
 */ 
 @Transactional
	public  Object addgroupuser(Groupuser groupuser){
		return iGroupuserDao.addgroupuser(groupuser);
	}

	/**
 * 删除 
 * @return 
 */ 
 @Transactional
	public  Object deletegroupuser(String id){
		return iGroupuserDao.deletegroupuser(id);
	}

}

