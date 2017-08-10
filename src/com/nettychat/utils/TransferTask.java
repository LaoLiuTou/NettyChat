package com.nettychat.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nettychat.model.chatmessage.Chatmessage;
import com.nettychat.model.messagebackup.Messagebackup;
import com.nettychat.service.chatmessage.IChatmessageService;
import com.nettychat.service.messagebackup.IMessagebackupService;

 

public class TransferTask  extends TimerTask { 
	
	
	private IChatmessageService iChatmessageService;
	private IMessagebackupService iMessagebackupService;
	Logger logger = Logger.getLogger("NettyChatLogger");
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void run() {  
        
    	ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
 		iChatmessageService = (IChatmessageService)context.getBean("iChatmessageService");
 		iMessagebackupService = (IMessagebackupService)context.getBean("iMessagebackupService");
 		
 		//转存
 		Map paramMap= new HashMap();
 		paramMap.put("readstatus", "0");
 		paramMap.put("fromPage", 0);
 		paramMap.put("toPage", 100000);
 		List<Chatmessage> tempList= iChatmessageService.selectchatmessageByParam(paramMap);
 		
 		if(tempList.size()>0){
 			logger.info("本次转存"+tempList.size()+"条数据。");
 			for(Chatmessage chatmessage:tempList){
 				Messagebackup messagebackup = new Messagebackup();
 				messagebackup.setAddtime(chatmessage.getAddtime());
 				messagebackup.setChattype(chatmessage.getChattype());
 				messagebackup.setContent(chatmessage.getContent());
 				messagebackup.setContenttype(chatmessage.getContenttype());
 				messagebackup.setFlag(chatmessage.getFlag());
 				messagebackup.setFriendid(chatmessage.getFriendid());
 				messagebackup.setIsgroup(chatmessage.getIsgroup());
 				messagebackup.setReadstatus(chatmessage.getReadstatus());
 				messagebackup.setSendstatus(chatmessage.getSendstatus());
 				messagebackup.setUserid(chatmessage.getUserid());
 				try {
					Long id=(Long) iMessagebackupService.addmessagebackup(messagebackup);
					if(id>0){//存储成功后删除
						iChatmessageService.deletechatmessage(chatmessage.getId()+"");
						
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.info("转存失败记录ID:"+chatmessage.getId());
					e.printStackTrace();
				}
 				
 			}
 			
 			
 		}
 		
 		
    }  
}
