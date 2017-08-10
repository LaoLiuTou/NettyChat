package com.nettychat.webService.chatgroup;
public interface IChatgroupWS{
 	/**
 	 * 根据ID查询
 	 * @return
 	 */
	public String getchatgroupById(String id);

		/**
		 * 根据ID删除
		 * 
		 * @return
		 */
	public String deletechatgroupById(String id);

		/**
		 * 根据查询条件查询
		 * @return
		 */
		@SuppressWarnings("unchecked")
	public String getchatgroupByParam(String id,String groupid,String groupname,String adduserid,String groupimage,String groupdetail,String addtimeFrom,String addtimeTo,String addtime,String flag,int page,int size);

		/**
		 * 添加
		 * @return
		 * @throws ParseException
		 */
	public String addchatgroup(String groupid,String groupname,String adduserid,String groupimage,String groupdetail,String addtime,String flag);
		/**
		 * 更新
		 * @return
		 */
	public String updatechatgroup(String id,String groupid,String groupname,String adduserid,String groupimage,String groupdetail,String addtime,String flag);
}

