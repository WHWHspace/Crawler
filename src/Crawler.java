import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;


public class Crawler {

	//��ȡ��ҳԴ��
	public String getHtmlSourceCode(String url){
		String data = "";
		int status = 0;
		HttpClient client = new HttpClient();
		GetMethod get = new GetMethod(url);         //get����               
		try {
			
			status = client.executeMethod(get);			
			data = get.getResponseBodyAsString();
			
		} catch (HttpException e) {
			e.printStackTrace();
			System.out.println("http error! error code:" + status);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IO error!");
		}
		
		return data;
	}
}
