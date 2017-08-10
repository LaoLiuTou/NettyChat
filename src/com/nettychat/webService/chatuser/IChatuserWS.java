package com.nettychat.webService.chatuser;
public interface IChatuserWS{
 	/**
 	 * 根据ID查询
 	 * @return
 	 */
	public String getchatuserById(String id);

		/**
		 * 根据ID删除
		 * 
		 * @return
		 */
	public String deletechatuserById(String id);

		/**
		 * 根据查询条件查询
		 * @return
		 */
		@SuppressWarnings("unchecked")
	public String getchatuserByParam(String id,String userid,String username,String userimage,String detail,String addtimeFrom,String addtimeTo,String addtime,String isonline,String flag,int page,int size);

		/**
		 * 添加
		 * @return
		 * @throws ParseException
		 */
	public String addchatuser(String userid,String username,String userimage,String detail,String addtime,String isonline,String flag);
		/**
		 * 更新
		 * @return
		 */
	public String updatechatuser(String id,String userid,String username,String userimage,String detail,String addtime,String isonline,String flag);
}

