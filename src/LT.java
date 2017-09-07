import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class LT {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	 
		
		/*String delay =null ;
		
		//System.out.println(Long.parseLong(delay));
		
		Timer timer = new Timer(true);  
		if(delay!=null&&!delay.equals("")&&Long.parseLong(delay)>0){
			timer.schedule(new SendTask(), Long.parseLong(delay));  
		}*/
		String url = "http://192.168.1.144/NettyChat/PushServer";
		
		
		try {
			 
			for(int i=0;i<1;i++){
				List<NameValuePair> params= new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("userid", "552"));    
				params.add(new BasicNameValuePair("friendid", "1|2|3|4|5|6|7|8|9|10"));    
				params.add(new BasicNameValuePair("delay", "0"));    
				params.add(new BasicNameValuePair("username", "username"));    
				params.add(new BasicNameValuePair("headimage", ""));    
				params.add(new BasicNameValuePair("type", "1"));   
				params.add(new BasicNameValuePair("operate", "0"));    
				params.add(new BasicNameValuePair("key", (1000+i)+"")); 
				params.add(new BasicNameValuePair("content", i+"content")); 
				System.out.println(doPost(params, url));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 
	}
	
	static class SendTask extends TimerTask {  
		  
		 
		@Override  
	    public void run() {  
	        System.out.println("ccc");
	  
	    }  
	  
	}  
	
	
	
	
	
	
	
	private static CloseableHttpClient httpclient;
    private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
	 /**
     * Post
     * 
     * @param params
     *            向服务器端传的参数
     * @param url
     * @return String 数据字符串
     * @throws Exception
     */
    public static String doPost(List<NameValuePair> params, String url) throws Exception {
        String result = null;
        httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
        //设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        CloseableHttpResponse httpResp = httpclient.execute(httpPost);
        try {
            int statusCode = httpResp.getStatusLine().getStatusCode();
            // 判断是够请求成功
            if (statusCode == HttpStatus.SC_OK) {
                //System.out.println("状态码:" + statusCode);
                //System.out.println("请求成功!");
                // 获取返回的数据
                result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
            } else {
                System.out.println("状态码:"
                        + httpResp.getStatusLine().getStatusCode());
                System.out.println("HttpPost方式请求失败!");
            }
        } finally {
            httpResp.close();
            httpclient.close();
        }
        return result;
    }

}


