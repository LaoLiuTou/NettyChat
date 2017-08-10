import java.text.SimpleDateFormat;
import java.util.Date;


public class LT {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//String str1="/opt/apache-tomcat-8.0.38/webapps/NettyChat/WEB-INF/classes/iosKeys/jida_tuisong_key.p12";
		//String str2="/opt/apache-tomcat-8.0.38/webapps/NettyChat/WEB-INF/classes/ioskeys/jida_tuisong_key.p12";
		String str1="/opt/apache-tomcat-8.0.38/webapps/NettyChat/WEB-INF/classes/iosKeys";
		String str2="/opt/apache-tomcat-8.0.38/webapps/NettyChat/WEB-INF/classes/ioskeys";
	
		if(str1.equals(str2)){
			
			System.out.println("YES");
		}
		else{
			System.out.println("NO");
		}
		 
		SimpleDateFormat dfs = new SimpleDateFormat("HH:mm:ss.SSS");  
		String bgdate = dfs.format(new Date());  
		System.out.println("start time:" + bgdate);  
	}

}
