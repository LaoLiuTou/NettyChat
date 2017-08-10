package com.nettychat.webService.groupuser;
public interface IGroupuserWS{
 	/**
 	 * 根据ID查询
 	 * @return
 	 */
	public String getgroupuserById(String id);

		/**
		 * 根据ID删除
		 * 
		 * @return
		 */
	public String deletegroupuserById(String id);

		/**
		 * 根据查询条件查询
		 * @return
		 */
		@SuppressWarnings("unchecked")
	public String getgroupuserByParam(String id,String groupid,String userid,int page,int size);

		/**
		 * 添加
		 * @return
		 * @throws ParseException
		 */
	public String addgroupuser(String groupid,String userid);
		/**
		 * 更新
		 * @return
		 */
	public String updategroupuser(String id,String groupid,String userid);
}

