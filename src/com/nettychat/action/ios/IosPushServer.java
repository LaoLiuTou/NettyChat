package com.nettychat.action.ios;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nettychat.chat.server.Server;


public class IosPushServer {
	private static Log log = LogFactory.getLog(IosPushServer.class.getName());
	
	 /************************************************
	 测试推送服务器地址：gateway.sandbox.push.apple.com /2195 
	 产品推送服务器地址：gateway.push.apple.com / 2195 

	需要javaPNS_2.2.jar包

	 ***************************************************/
	/**
     * apple的推送方法

     * @param tokens   iphone手机获取的token

     * @param path 这里是一个.p12格式的文件路径，需要去apple官网申请一个 

     * @param password  p12的密码 此处注意导出的证书密码不能为空因为空密码会报错

     * @param message 推送消息的内容

     * @param count 应用图标上小红圈上的数值

     * @param sendCount 单发还是群发  true：单发 false：群发

     */

	public void sendpush(List<String> tokens,String path, String password, String message,Integer count,boolean sendCount) {

	try {
		//message是一个json的字符串{“aps”:{“alert”:”iphone推送测试”}}

			PushNotificationPayload payLoad =  PushNotificationPayload.fromJSON(message);
			
			payLoad.addAlert("iphone推送测试"); // 消息内容
			
			payLoad.addBadge(count); // iphone应用图标上小红圈上的数值
			
			payLoad.addSound("default"); // 铃音 默认
			
			

			PushNotificationManager pushManager = new PushNotificationManager();
			
			//true：表示的是产品发布推送服务 false：表示的是产品测试推送服务
			
			pushManager.initializeConnection(new AppleNotificationServerBasicImpl(path, password, false));
			
			List<PushedNotification> notifications = new ArrayList<PushedNotification>(); 
			
			// 发送push消息
			
			if (sendCount) {			
			Device device = new BasicDevice();
			
			device.setToken(tokens.get(0));
			
			PushedNotification notification = pushManager.sendNotification(device, payLoad, true);
			
			notifications.add(notification);
			
			} else {			
			List<Device> device = new ArrayList<Device>();
			
			for (String token : tokens) {
			
			device.add(new BasicDevice(token));
			
			}
			
			notifications = pushManager.sendNotifications(payLoad, device);
			
			}
			
			List<PushedNotification> failedNotifications = PushedNotification.findFailedNotifications(notifications);
			
			List<PushedNotification> successfulNotifications = PushedNotification.findSuccessfulNotifications(notifications);
			
			int failed = failedNotifications.size();
			
			int successful = successfulNotifications.size();
			
			 
			
			if (successful > 0 && failed == 0) {
				System.out.print("推送成功"+failedNotifications.toString());
			
			} else if (successful == 0 && failed > 0) {
			System.out.print("推送失败"+failedNotifications.toString());
			
			} else if (successful == 0 && failed == 0) {
				System.out.print("推送失败"+failedNotifications.toString());
			System.out.println("No notifications could be sent, probably because of a critical error");
			
			} else {
				System.out.print("推送失败"+failedNotifications.toString());
			
			
			}
	
	// pushManager.stopConnection();

	} catch (Exception e) {
	
	e.printStackTrace();
	
	}

}
	
	/**
	 * TODO
	 * @param args
	 */
	public static void main(String[] args) {
		IosPushServer send=new IosPushServer();
		List<String> tokens=new ArrayList<String>();
		tokens.add("1ede66991c0f4a4ea5a093e49053c0cefbd2aae169f4c9b4a4cd97856195f8b0");
		String base = Server.class.getResource("/")
				.getPath();
		String path=base+"iosKeys/jida_tuisong_key.p12";
		path=path.replaceAll("%20", " ");
		String password="jiubaisoft123";
		String message="{'aps':{'alert':'iphone推送测试'}}";
		Integer count=1;
		boolean sendCount=false;
		send.sendpush(tokens, path, password, message, count, sendCount);
		
	}
	
	

}
