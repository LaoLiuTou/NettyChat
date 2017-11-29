package com.nettychat.utils;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

public class EncryptFilter extends StrutsPrepareAndExecuteFilter {  
	 public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)  
			 throws IOException, ServletException {  
		 
		 
		 
		 	HttpServletRequest request = (HttpServletRequest) req; 
			HttpServletResponse  response = (HttpServletResponse) res; 
			 
				  
		  
		    response.setCharacterEncoding("UTF-8"); 
			response.setContentType("text/html;charset=UTF-8"); 
		    //CORS跨域
		    response.setHeader("Access-Control-Allow-Origin", "*");  
		    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");  
		    response.setHeader("Access-Control-Max-Age", "3600");  
		    //response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		    response.setHeader("Access-Control-Allow-Headers","x-requested-with,token,timesamp,sign");
		    //response.setHeader("Access-Control-Allow-Headers","x-requested-with,content-type，requesttype");
		    //chain.doFilter(req, res);  
		    //super.doFilter(req, res, chain);// 调用父类的方法  
		   
		    chain.doFilter(request, response);
			 
			
	 }  
  }  