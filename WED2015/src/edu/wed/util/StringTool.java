package edu.wed.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringTool 
{
	public static String deleteBlank(String text)
	{
		String result="";
		result=text.replaceAll(" ", "");
		return result;
	}
	public static boolean absolutelyChinese(char c)
	{
		if((c>=19968&&c<=40869))
			return true;
		else
			return false;	
	}
	
	public static boolean isChinese(char c)
	{
		if((c>=19968&&c<=40869)&&c!='·'&&c!='/')
			return true;
		else
			return false;
	}
	
	
	public static boolean isChinese(String str)
	{
		for(int i=0;i<str.length();i++)
			if(!isChinese(str.charAt(i)))
				return false;
		return true;
	}
	
	
	public static boolean isPureLetter(String str)
	{
		
		for(int i=0;i<str.length();i++)
			if(!Character.isLowerCase(str.charAt(i))&&!Character.isUpperCase(str.charAt(i))) 
				return false;
	
		return true;
	}
	

	public static boolean isDigit(char c)
	{
		if(c>='0'&&c<='9')
			return true;
		else
			return false;	
		
	}
	
	public static String getChinese(String str)
	{
		String chinese="";
		String temp="";
		
		for(int i=0;i<str.length();++i)
		{
			if(isChinese(str.charAt(i)))
			{
				temp+=str.charAt(i);
				if(i==str.length()-1)
					chinese=chinese+temp;
			}
			else
			{	
				if(!temp.equals(""))
					chinese=chinese+temp+",";
				temp="";	
			}
		}		
		
//		System.out.println(chinese);
		if(chinese.equals(""))
			return chinese;	
		else if(!isChinese(chinese.charAt(chinese.length()-1)))
			return chinese.substring(0,chinese.length()-1);
		else
			return chinese;	
	}

	public static String getChinese2(String str)
	{
		String chinese="";
		String temp="";
		for(int i=0;i<str.length();++i)
		{
			if(isChinese(str.charAt(i)))
			{
				temp+=str.charAt(i);
				if(i==str.length()-1)
					chinese=chinese+temp;
			}
			else
			{	
				if(!temp.equals(""))
					chinese=chinese+temp;
				temp="";	
			}
		}		
		
//		System.out.println(chinese);
		if(chinese.equals(""))
			return chinese;	
		else if(!isChinese(chinese.charAt(chinese.length()-1)))
			return chinese.substring(0,chinese.length()-1);
		else
			return chinese;	
	}

	public static String removeSymbol(String str)
	{
		String result="";
		for(int i=0;i<str.length();i++)
		{
			if(isChinese(str.charAt(i))||isDigit(str.charAt(i))||str.charAt(i)=='·')
				result=result+str.charAt(i);
			else if(str.charAt(i)=='?')
				result=result+'·';
			else if(str.charAt(i)=='-')
				result=result+'·';
		
		}
		return result;
	}
	
	public static String createLabs(String[][] labs)
	{
		String result="";
		String tmp="";
		for(int i=0;i<labs.length;i++)
		{
			tmp=labs[i][0]+"("+labs[i][1]+")"+"/";
			result=result+tmp;
		}
		return result;
	}
	
	public static String removeSpareBlank(String str)
	{	
		String tmp_str=str.trim();
		
		tmp_str=tmp_str.replaceAll("\u3000", " ");
	    String test = tmp_str.replaceAll("\\s{1,}", " ");
	
		return test.trim();
	}
	
	public static List<String> getNouns(String str)
	{
	
		String preprocess_str=removeSpareBlank(str);
		
		List<String> nouns=new ArrayList<String>();

		//---临时输出----
		System.out.println("获取名词组:"+preprocess_str); 
		
		String[] tmp_strs=preprocess_str.split(" ");
	
		String tmp_symbol;
		String tmp_noun;
		
		for(int i=0;i<tmp_strs.length;i++)
		{
		
			
			tmp_symbol=tmp_strs[i].substring(tmp_strs[i].indexOf("/")+1);
			tmp_noun=tmp_strs[i].substring(0,tmp_strs[i].indexOf("/")).trim();
			
//			System.out.println(tmp_noun+" "+tmp_symbol);
		
			if((tmp_symbol.indexOf("n")>=0&&isChinese(tmp_noun)&&tmp_noun.length()>1)
			||(isPureLetter(tmp_noun))&&tmp_noun.length()>0)
//					&&tmp_symbol.indexOf("x")>0))
			{
				
				nouns.add(tmp_noun);
			}
		}

		return nouns;
	}

	
	
	public static String getSubString(String str,String begin,String end,int offset)
	{
		int beginIndex=str.indexOf(begin)+offset;
		int endIndex=str.indexOf(end);
		
		System.out.println(beginIndex+":"+endIndex);
		
		if(beginIndex>0&&endIndex>0)	
			return str.substring(beginIndex, endIndex);
		else
		{
			System.out.println("取子串出现异常"+str);
			throw new StringIndexOutOfBoundsException();
		}
	}
	
	public static String simplifyStr(String str)
	{
		String result="";
		boolean flag=true;
		for(int i=0;i<str.length();i++)
		{
			if(str.charAt(i)=='['||str.charAt(i)==']')
				continue;
			else if(str.charAt(i)=='{')
				 flag=false;
			else if(str.charAt(i)=='}')
				 flag=true;
			else if(flag&&str.charAt(i)!='}')
				result=result+str.charAt(i);		
			
		}
		
		return result;
	}
	
	public static ArrayList<String> StringToArrayList(String str,String reg)
	{
		ArrayList<String> t_list=new ArrayList<String>();
		String[] t_arrayStr=str.split(reg);
		
		for(int i=0;i<t_arrayStr.length;i++)
			t_list.add(t_arrayStr[i]);
		return t_list;
	}
	
	 public static String replaceBlank(String str) 
	 {
		  String dest = "";
		  if(str!=null)
		  {
		      Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		      Matcher m = p.matcher(str);
		      dest = m.replaceAll("");
		  }
		  return dest;
	 }

	// 把中文字符转换为带百分号的浏览器编码
	    // @param word 中文字符
	    // @param encoding 字符编码
	 public static String toBrowserCode(String word, String encoding)
	  {
		   String result;
	       if(!isChinese(word))
	    	   result=word;
	       else
	       {
		 
				try {
					byte[] textByte = word.getBytes(encoding);
					
					 StringBuilder strBuilder = new StringBuilder();
				       
				      for (int j = 0; j < textByte.length; j++)
				      {
				            // 转换为16进制字符
				            String hexStr = Integer.toHexString(textByte[j] & 0xff);
				            strBuilder.append("%" + hexStr.toUpperCase());
				       }
				       
				      result=strBuilder.toString();
				        
				} catch (UnsupportedEncodingException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
	       }
	      
	       System.out.println("转化的结果是"+result);
	       return result;
	    }
	 public static String decodeUnicode(String theString) {
		  char aChar;
		  int len = theString.length();
		  StringBuffer outBuffer = new StringBuffer(len);
		  
		  for (int x = 0; x < len;) {
		   aChar = theString.charAt(x++);
		   if (aChar == '\\') {
		    aChar = theString.charAt(x++);
		    if (aChar == 'u') {
		     int value = 0;
		     for (int i = 0; i < 4; i++) {
		      aChar = theString.charAt(x++);
		      switch (aChar) {
		      case '0':
		      case '1':
		      case '2':
		      case '3':
		      case '4':
		      case '5':
		      case '6':
		      case '7':
		      case '8':
		      case '9':
		       value = (value << 4) + aChar - '0';
		       break;
		      case 'a':
		      case 'b':
		      case 'c':
		      case 'd':
		      case 'e':
		      case 'f':
		       value = (value << 4) + 10 + aChar - 'a';
		       break;
		      case 'A':
		      case 'B':
		      case 'C':
		      case 'D':
		      case 'E':
		      case 'F':
		       value = (value << 4) + 10 + aChar - 'A';
		       break;
		      default:
		       throw new IllegalArgumentException(
		         "Malformed      encoding.");
		      }
		     }
		     outBuffer.append((char) value);
		    } else {
		     if (aChar == 't') {
		      aChar = '\t';
		     } else if (aChar == 'r') {
		      aChar = '\r';
		     } else if (aChar == 'n') {
		      aChar = '\n';
		     } else if (aChar == 'f') {
		      aChar = '\f';
		     }
		     outBuffer.append(aChar);
		    }
		   } else {
		    outBuffer.append(aChar);
		   }
		  }
		  
		  return outBuffer.toString();
	 }
	 
	 public static String toUnicode(String s)
	 {
		 String as[]=new String[s.length()];
		 String s1="";
		 
		 for(int i=0;i<s.length();i++)
		 {
			 as[i]=Integer.toHexString(s.charAt(i)&0xffff);
			 s1=s1+"\\u"+as[i];
		 }
		 return s1;
		 
	 }
	 

	 public static String getDate(String text)
	 {
		 
		   try {  
	           List<String> matches = null;  
	           Pattern p = Pattern.compile("(\\d{1,4}[-|\\/|年|\\.]\\d{1,2}[-|\\/|月|\\.]\\d{1,2}([日|号])?(\\s)*(\\d{1,2}([点|时])?((:)?\\d{1,2}(分)?((:)?\\d{1,2}(秒)?)?)?)?(\\s)*(PM|AM)?)", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE);  
	           Matcher matcher = p.matcher(text);  
	          
	           if (matcher.find() && matcher.groupCount() >= 1) 
	           {  
	               matches = new ArrayList<String>();  
	               for (int i = 1; i <= matcher.groupCount(); i++) 
	               {  
	                   String temp = matcher.group(i);  
	                   matches.add(temp);  
	               }  
	           } else 
	               matches = Collections.EMPTY_LIST;  
	           
	           	if (matches.size() > 0)  
	           	{
		        	   String date_str=((String)matches.get(0)).trim();
		        	   
		        	   //只保留年月日起时分秒的数字
		        	   List<String> digit_list = new ArrayList<String>();
		        	   for(String sss:date_str.replaceAll("[^0-9]", ",").split(","))
		        	       if (sss.length()>0)
		        	    	   digit_list.add(sss);
		        	   
		        	   if(digit_list.size()==3)
		        	   {
		        		   digit_list.add("00");
		        		   digit_list.add("00");
		        		   digit_list.add("00");
		        	   }
		        	   
		        	   if(digit_list.size()==4)
		        	   {
		        		   digit_list.add("00");
		        		   digit_list.add("00");
		        	   }
		        	   
		        	   if(digit_list.size()==5)
		        	   {
		        		   digit_list.add("00");  
		        	   }
		        	   
		        	   for(int i=1;i<=5;i++)
		        		   digit_list.set(i, digit_list.get(i).length()==1?"0"+digit_list.get(i):digit_list.get(i));
		        	 
		        	   System.out.println(digit_list);
		        	   
		        	   return digit_list.get(0)+"-"+digit_list.get(1)+"-"+digit_list.get(2)
		        			   +" "+digit_list.get(3)+":"+digit_list.get(4)+":"+digit_list.get(5);
	        	 }
	          
	       } catch (Exception e) {  
	           return null;  
	       }  
	         
	    return null;    
		 
	 }
	 
	 //For test
	public static void main(String[] args) {
		
//		System.out.println(isPureLetter(" Google"));
		String str_input="高晓松，男，1969年11月14日生于北京，祖籍浙江杭州。音乐人、词曲创作者、制作人、导演、主持人。1988年考入清华大学电子工程系，后退学进入北京电影学院导演系研究生预备班学习。早期事业以电视编剧、音乐创作及制作人为主。1994年出版《校园民谣》合辑，1996年推出个人作品集《青春无悔》。19...";
		String result=NLPIRWrapper.getNLPParseResult(str_input);
		System.out.println(result);
		
		System.out.println(getNouns(result));
	}
}
