package com.nettychat.webService.chatfriend;
public interface IChatfriendWS{
 	/**
 	 * 根据ID查询
 	 * @return
 	 */
	public String getchatfriendById(String id);

		/**
		 * 根据ID删除
		 * 
		 * @return
		 */
	public String deletechatfriendById(String id);

		/**
		 * 根据查询条件查询
		 * @return
		 */
		@SuppressWarnings("unchecked")
	public String getchatfriendByParam(String id,String userid,String friendid,String isgroup,String isonline,int page,int size);

		/**
		 * 添加
		 * @return
		 * @throws ParseException
		 */
	public String addchatfriend(String userid,String friendid,String isgroup,String isonline);
		/**
		 * 更新
		 * @return
		 */
	public String updatechatfriend(String id,String userid,String friendid,String isgroup,String isonline);
}

