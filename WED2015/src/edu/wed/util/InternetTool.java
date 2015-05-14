package edu.wed.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class InternetTool {

	public InternetTool() {
		// TODO Auto-generated constructor stub
	}
	
	public static boolean isPolysemy(String pageHtml){
		return pageHtml.indexOf("多义词")>0 && pageHtml.indexOf("个义项")>0;
	}
	
	public static String getHtmlText(String path)
	{
		String htmltext="";
		try 
		{
			URL  url = new URL(path);
			URLConnection conn= url.openConnection();
			conn.setDoOutput(true);
			
			BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf8"));
	
			String inputLine;
			
			while ((inputLine = br.readLine()) != null) {
				htmltext =htmltext+inputLine+"\n";
			}
		}catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return htmltext;
	}
	
	public static String getBaikeExplanation(String a)
	{
		String explanation=null;
		if(a.indexOf("<img class=\"pchar\" style=\"margin-bottom:-")>0)
		{
			explanation=a.substring(a.indexOf("\">")+2,a.indexOf("</a>")); 
			explanation=explanation.substring(0,explanation.indexOf("<img"))
					  +explanation.substring(explanation.lastIndexOf("\" />")+4);
		 }
		 else
			 explanation=a.substring(a.indexOf("\">")+2,a.indexOf("</a>")); 
		 
		String[] tmp_strs=explanation.split("：");
		 		
		return explanation.replaceAll("：", ":");
					
		
	}

	//获取每个义项的title
	public static String getBaikeTitle(String baikehtml)
		{
			if(baikehtml.indexOf("X-UA-Compatible")>0)
			{
				return "NIL";		
			}
			else
			{
				int beginIndex2=baikehtml.indexOf("<title>");
				int endIndex2=baikehtml.indexOf("_百度百科</title>");	
				
				if(baikehtml.indexOf("imgzd")>0)
					System.out.println("百度拦截");
				
				String title=baikehtml.substring(beginIndex2+7,endIndex2);		
				
				
				return title;
			}
		}

	//获取义项的标签集合
	public static String getBaikeLabs(String item_html)
	{
	
		if(item_html.indexOf("词条标签：")>0)
		{
			
			Document doc=Jsoup.parse(item_html,"UTF-8");
			
			List<String> lab_list=new ArrayList<String>();
			Elements div1=doc.select(".open-tag");
			for(Element e:div1)
			{
				lab_list.add(e.text());
			}
			
			Elements div2=doc.select(".taglist");
			for(Element e:div2)
			{
				lab_list.add(e.text());
			}
		
			String labs=lab_list.toString();
			return lab_list.size()>0?labs.substring(1,labs.length()-1):"NIL";
			
		}
		else{
			return "NIL";
		}
		
	
	}

	public static String getBaikeDescription(String first_page_html) 
	{
		Document doc=Jsoup.parse(first_page_html,"UTF-8");
		Elements metas=doc.head().select("meta");
		
		String description=null;
		for (Element meta : metas) 
		{  
	         String content = meta.attr("content");  
	           
	          if ("description".equalsIgnoreCase(meta.attr("name"))) 
	                description=content;   
	    }  
	  
	    return description;
			
	}
	
	//获取多个一项列表url
	public static String getMultiMeaningListURL(String first_page_html)
	{
		int start=first_page_html.indexOf("href=\"/view/");
		int end=first_page_html.indexOf("个义项");
			
		String row_html=first_page_html.substring(start,end);
			
		Matcher matcher =Pattern.compile("/view/(.*)=1",Pattern.CASE_INSENSITIVE).matcher(row_html);
			
		List<String> all_link=new ArrayList<String>();
		while (matcher.find()) 
		{
			String link=matcher.group();
			all_link.add(link);
		}
			
		return "http://baike.baidu.com"+all_link.get(0);
			
	}

	//获取用解释作为百度搜索词发挥的搜索结果数
	public static int getItemQuery(String explanation) {
			
		explanation=explanation.replaceAll(" ", "");
		String query_url="http://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&tn=baidu&wd="+explanation;
		String query_html=getHtmlText(query_url);
			
		Document doc=Jsoup.parse(query_html,"UTF-8");
			
		Elements div=doc.select(".nums");
			
		String nums_str=div.text();
			
		//=========临时输出==========
		System.out.println(explanation+" "+nums_str);
		String num=nums_str.substring(nums_str.indexOf("约")+1,nums_str.indexOf("个"));
			
		return Integer.parseInt(num.replaceAll(",",""));
	}
	
}
