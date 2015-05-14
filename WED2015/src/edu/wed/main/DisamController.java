package edu.wed.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.wed.util.CommonUtil;
import edu.wed.util.NLPIRWrapper;
import edu.wed.util.StringTool;
import edu.wed.model.BaikeItem;
import edu.wed.util.InternetTool;

public class DisamController {

	public DisamController() {
		// TODO Auto-generated constructor stub
	}
	
	public static List<BaikeItem> DisamToEntity(String word, String weibo){
		List<BaikeItem> match_baikeitem_list = new ArrayList<BaikeItem>();
		
		//all_search_link
		List<String> all_search_link = proProcess(word);
		if(all_search_link.size()==0)  //如果百度百科完全搜不到，则返回原词
		{
			System.out.println("百度百科尚未收录此词语");
			return match_baikeitem_list;
		}
		
		//all_search_link => first_link
		String first_link=all_search_link.get(0); //获取搜索后第一个链接
		String first_page_html=InternetTool.getHtmlText(first_link);		
		if(InternetTool.isPolysemy(first_page_html)){
			match_baikeitem_list = disamProcess(first_link, first_page_html, weibo);
		}else{
			match_baikeitem_list = defaultProcess(first_link, first_page_html, weibo);
		}
		
		return match_baikeitem_list;
	}
	
	
	
	private static List<String> proProcess(String word){
		List<String> all_search_link = new ArrayList<String>();
		
		String url="http://baike.baidu.com/search?word="+word+"&pn=0&rn=10&enc=utf8";
		String search_html = InternetTool.getHtmlText(url);  //获取搜索页面html
		if(search_html.indexOf("imgzd")>0){
			System.out.println("百度拦截");
		}
		Matcher matcher = Pattern.compile("http://baike.baidu.com(.*)htm",Pattern.CASE_INSENSITIVE).matcher(search_html);
		
		while (matcher.find()) 
		{
			   String link = matcher.group();
			   all_search_link.add(link);
		}
		
		return all_search_link;
	}
	
	private static List<BaikeItem> defaultProcess(String link, String page_html, String weibo){
		System.out.println("##Into defaultProcess##");
		
		List<BaikeItem> match_baikeitem_list=new ArrayList<BaikeItem>();
		String item_url=link;
		  
		String title=InternetTool.getBaikeTitle(page_html).trim();  //获取义项页面的titile
		String description=InternetTool.getBaikeDescription(page_html);
		String labs=InternetTool.getBaikeLabs(page_html);

		BaikeItem baike_item=new BaikeItem();
		baike_item.setUrl(item_url);
		baike_item.setTitle(title);
		baike_item.setDescription(description);

		List<String> weibo_list=StringTool.getNouns(NLPIRWrapper.getNLPParseResult(weibo));
		List<String> labs_list=Arrays.asList(labs.split(","));
		double jd1=CommonUtil.getJaccardDistance(labs_list, weibo_list);
		
		List<String> description_nous=StringTool.getNouns(NLPIRWrapper.getNLPParseResult(description));
		double jd2=CommonUtil.getJaccardDistance(description_nous, weibo_list);
		
		double aa=0.3;
		double bb=0.7; 
		double jd=aa*jd1+bb*jd2;
		
		System.out.println(jd);
		if(jd>=0.01){
			match_baikeitem_list.add(baike_item);
		}
		return match_baikeitem_list;
		
	}
	
	private static List<BaikeItem> disamProcess(String link, String page_html, String weibo){
		
		System.out.println("##Into disamProcess##");
		
		List<BaikeItem> match_baikeitem_list=new ArrayList<BaikeItem>();
		
		//获取list页面各个义项的url
		String multi_meaning_list_url=InternetTool.getMultiMeaningListURL(page_html);  //获取多义表页面URL
		//==============临时输出===============
		System.out.println(multi_meaning_list_url);
		
		//获取list页面html
		String multi_meaning_list_html=InternetTool.getHtmlText(multi_meaning_list_url);
		
		//取得各个义项title、description、url、query

		String preprocess=InternetTool.getBaikeTitle(multi_meaning_list_html);
		
		Matcher matcher1 =Pattern.compile("/subview/(.*?)htm(.*?)"+preprocess+"(.*?)</a>",Pattern.CASE_INSENSITIVE).matcher(multi_meaning_list_html);
		
		while (matcher1.find()) 
		{
			  String a=matcher1.group();
			  System.out.println(a);
			  
			  if(a.length()<=150)
			  {
				  String explanation=InternetTool.getBaikeExplanation(a);
				
				  String item_url="http://baike.baidu.com"+a.substring(0,a.indexOf(".htm")+4);
				  String item_html=InternetTool.getHtmlText(item_url);
				  String title=InternetTool.getBaikeTitle(item_html);  //获取义项页面的titile
				 
				  String description=InternetTool.getBaikeDescription(item_html);
				  String labs=InternetTool.getBaikeLabs(item_html);
				  int query=InternetTool.getItemQuery(explanation);
				 
				  BaikeItem baike_item=new BaikeItem(preprocess,explanation,title,
						  item_url,description,labs,query);
				 	  
				  match_baikeitem_list.add(baike_item);
			  }
		}
		
		int size=match_baikeitem_list.size();
		
		//归一化查询次数
		int[] query_arrs=new int[size];
		for(int i=0;i<size;i++)
			query_arrs[i]=match_baikeitem_list.get(i).getQuery();
		double[] normalization=CommonUtil.getNormalization(query_arrs);
		
		//归一化微博名词组与词条标签、描述名词组的JD相似度 
		List<String> weibo_list=StringTool.getNouns(NLPIRWrapper.getNLPParseResult(weibo));
		double[] jds=new  double[size];
		for(int i=0;i<size;i++)
		{
			BaikeItem bi=match_baikeitem_list.get(i);
			
			String labs=bi.getLabs();
			if("NIL".equals(labs))
			{
				List<String> description_nous=StringTool.getNouns(NLPIRWrapper.getNLPParseResult(bi.getDescription()));
				jds[i]=CommonUtil.getJaccardDistance(description_nous, weibo_list);
			}
			else
			{
				
				List<String> labs_list=Arrays.asList(labs.split(","));
				double jd1=CommonUtil.getJaccardDistance(labs_list, weibo_list);
				
				List<String> description_nous=StringTool.getNouns(NLPIRWrapper.getNLPParseResult(bi.getDescription()));
				double jd2=CommonUtil.getJaccardDistance(description_nous, weibo_list);
				
				double aa=0.3;
				double bb=0.7; 
				jds[i]=aa*jd1+bb*jd2;
				
			}

		}
		
		
		double a=0.3;
		double b=0.7;
		for(int i=0;i<size;i++)
		{
			BaikeItem bi=match_baikeitem_list.get(i);
			System.out.println(normalization[i]+"  "+jds[i]);
			double match=a*normalization[i]+b*jds[i];
			System.out.println(match);
			bi.setMatch(match);
		}
		
		Collections.sort(match_baikeitem_list, new Comparator<BaikeItem>() {
			@Override
			public int compare(BaikeItem bi1, BaikeItem bi2) {
				// TODO Auto-generated method stub
				return bi2.getMatch()-bi1.getMatch()>0.0?1:-1;
			}
		});
		
		return match_baikeitem_list;
	}

   
	public static void main(String[] args){
		List<BaikeItem> match_baikeitem_list = DisamToEntity("猛龙", "#NBA酷响。公牛小腿伤在间的对决。");
		
		for(BaikeItem bi:match_baikeitem_list)
		{						
			System.out.println(bi.getTitle()+","+bi.getUrl()+";");
		}
	}
	
}
