package com.nettychat.webService.messagebackup;
public interface IMessagebackupWS{
 	/**
 	 * 根据ID查询
 	 * @return
 	 */
	public String getmessagebackupById(String id);

		/**
		 * 根据ID删除
		 * 
		 * @return
		 */
	public String deletemessagebackupById(String id);

		/**
		 * 根据查询条件查询
		 * @return
		 */
		@SuppressWarnings("unchecked")
	public String getmessagebackupByParam(String id,String userid,String friendid,String addtimeFrom,String addtimeTo,String addtime,String chattype,String contenttype,String content,String readstatus,String sendstatus,String isgroup,String flag,int page,int size);

		/**
		 * 添加
		 * @return
		 * @throws ParseException
		 */
	public String addmessagebackup(String userid,String friendid,String addtime,String chattype,String contenttype,String content,String readstatus,String sendstatus,String isgroup,String flag);
		/**
		 * 更新
		 * @return
		 */
	public String updatemessagebackup(String id,String userid,String friendid,String addtime,String chattype,String contenttype,String content,String readstatus,String sendstatus,String isgroup,String flag);
}

