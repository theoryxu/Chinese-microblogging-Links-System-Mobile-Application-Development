package edu.wed.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class CommonUtil 
{
	//获取IDOLConfig.properties里的配置参数
		public static String getPropertyValue(String filePath,String propertyName)
		{
			Properties props=new Properties();
			FileInputStream fis;
			
			try {
				fis = new FileInputStream(filePath);
				props.load(fis);
				fis.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return "".equals(props.getProperty(propertyName))||props.getProperty(propertyName)==null
					?null:props.getProperty(propertyName);
			
		}
	
		public static double getJaccardDistance(List<String> source,List<String> target)
		{
			Set<String> set1=new HashSet<String>(source);
			Set<String> set2=new HashSet<String>(target);
		
			Set<String> intersection=new HashSet<String>();
			intersection.addAll(set1);
			intersection.retainAll(set2);
			
			Set<String> union=new HashSet<String>();
			union.addAll(set1);
			union.addAll(set2);
			
			return intersection.size()/(double)union.size();
		}

		public static double[] getNormalization(int[] a)
		{
			int max=a[0];
			int min=a[0];
			
			double[] normalization=new double[a.length];
			
			for(int i=0;i<a.length;i++)
			{
				max=max>a[i]?max:a[i];
				min=min<a[i]?min:a[i];
			}
			
			
			for(int i=0;i<a.length;i++)
			{
				normalization[i]=(a[i]-min)/(double)(max-min);
			}
			
			return normalization;
		}
		
		public static void main(String[] args) {
			
			int[] a=new int[]{1,3,5,7,9};
			double[] b=getNormalization(a);
			
			for(double i:b)
				System.out.print(i+" ");
		}
}
