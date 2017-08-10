package com.nettychat.model.frienddetails;

import java.util.Date;

public class Frienddetails {

	/**  */
	private  Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	/** 用户id */
	private  String userid;
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	/** 聊天人id */
	private  String friendid;
	public String getFriendid() {
		return friendid;
	}
	public void setFriendid(String friendid) {
		this.friendid = friendid;
	}
	/** 是否是群聊 0-yes 1--no */
	private  String isgroup;
	public String getIsgroup() {
		return isgroup;
	}
	public void setIsgroup(String isgroup) {
		this.isgroup = isgroup;
	}
	/** 是否在线 0-yes 1- no */
	private  String isonline;
	public String getIsonline() {
		return isonline;
	}
	public void setIsonline(String isonline) {
		this.isonline = isonline;
	}
	
	private String friendname;
	private String firendheader;
	private String lmessage;
	private String lchattype;
	private String lcontenttype;
	private String unread;
	private Date ltime;
	public String getFriendname() {
		return friendname;
	}
	public void setFriendname(String friendname) {
		this.friendname = friendname;
	}
	public String getFirendheader() {
		return firendheader;
	}
	public void setFirendheader(String firendheader) {
		this.firendheader = firendheader;
	}
	public String getLmessage() {
		return lmessage;
	}
	public void setLmessage(String lmessage) {
		this.lmessage = lmessage;
	}
	public String getLchattype() {
		return lchattype;
	}
	public void setLchattype(String lchattype) {
		this.lchattype = lchattype;
	}
	public String getLcontenttype() {
		return lcontenttype;
	}
	public void setLcontenttype(String lcontenttype) {
		this.lcontenttype = lcontenttype;
	}
	public Date getLtime() {
		return ltime;
	}
	public void setLtime(Date ltime) {
		this.ltime = ltime;
	}
	public String getUnread() {
		return unread;
	}
	public void setUnread(String unread) {
		this.unread = unread;
	}
 
}
