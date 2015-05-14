package tool.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class EntityDisambiguationService implements Serializable{
	private static String weibos;
	private static List <String> entitys;
	private static String[] items;
	private static String[] itemLinks;
	
	public static String[] getItems() {
		return items;
	}

	public static void setItems(String[] items) {
		EntityDisambiguationService.items = items;
	}

	public static String[] getItemLinks() {
		return itemLinks;
	}

	public static void setItemLinks(String[] itemLinks) {
		EntityDisambiguationService.itemLinks = itemLinks;
	}

	public  static String getWeibos() {
		return weibos;
	}

	public static void setWeibos(String weibo) {
		weibos = weibo;
	}

	public  static List<String> getEntitys() {
		return entitys;
	}

	public static void setEntitys(List<String> entity) {
		entitys = entity;
	}

	public EntityDisambiguationService(){
		
	}
	
	public static String disambiguate(){
		String result = null;
		
		
		Socket s = new Socket();
		try {
			
			String SerIP =  "192.168.191.1";
			
			Log.i("XX","connect...  " + SerIP);
			
			//使用Socket连接服务器
			s.connect(new InetSocketAddress(SerIP, 10000));
			
			Log.i("XX","connected");
			BufferedReader is = new BufferedReader(new InputStreamReader(s.getInputStream()));
			BufferedWriter os = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			
			String weibo=weibos;
			String word=entitys.get(0);
            
			//将信息写入输入流
			os.write(weibo);
			os.write("\n");
			os.write(word);
			os.write("\n");
			
			os.flush();
			Log.i("XX","send");
			result = is.readLine();
			Log.i("XX",result);
			  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static String[] formDisambiguatedResult(String result){
		
		String[] results = result.split(";");
		items = new String[results.length];
		itemLinks = new String[results.length];
		int i = 0;
		 for(String fragment:results){
			 String[] fragments = fragment.split(",");
			 items[i] = fragments[0];
			 itemLinks[i] = fragments[1];
			 i++;
		 }
		return items;
	}
}
