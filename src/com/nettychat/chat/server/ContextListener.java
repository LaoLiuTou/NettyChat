package com.nettychat.chat.server;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.nettychat.utils.TimerManager;
 
 

public class ContextListener implements ServletContextListener {

	 Timer timer = new Timer() ; 
	 

    public void contextInitialized(ServletContextEvent event) { 
        timer = new java.util.Timer(true);  

   	
        
        //event.getServletContext().log("定时器已启动"); 
        //System.out.println("定时器已启动");
        timer.schedule(new MyTask(event.getServletContext()), 10000);  
        
         //每天转存数据
        new TimerManager(); 

    } 
    public void contextDestroyed(ServletContextEvent event) { 
        timer.cancel(); 
        event.getServletContext().log("定时器以销毁"); 
    } 
}
