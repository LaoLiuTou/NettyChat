package com.nettychat.webService.chatmessage;
public interface IChatmessageWS{
 	/**
 	 * 根据ID查询
 	 * @return
 	 */
	public String getchatmessageById(String id);

		/**
		 * 根据ID删除
		 * 
		 * @return
		 */
	public String deletechatmessageById(String id);

		/**
		 * 根据查询条件查询
		 * @return
		 */
		@SuppressWarnings("unchecked")
	public String getchatmessageByParam(String id,String userid,String friendid,String addtimeFrom,String addtimeTo,String addtime,String chattype,String contenttype,String content,String readstatus,String sendstatus,String isgroup,String flag,int page,int size);

		/**
		 * 添加
		 * @return
		 * @throws ParseException
		 */
	public String addchatmessage(String userid,String friendid,String addtime,String chattype,String contenttype,String content,String readstatus,String sendstatus,String isgroup,String flag);
		/**
		 * 更新
		 * @return
		 */
	public String updatechatmessage(String id,String userid,String friendid,String addtime,String chattype,String contenttype,String content,String readstatus,String sendstatus,String isgroup,String flag);
}

