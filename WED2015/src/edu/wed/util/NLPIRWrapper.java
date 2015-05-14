package edu.wed.util;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.sun.jna.Library;
import com.sun.jna.Native;

import edu.wed.util.NLPIRWrapper.CLibrary;

public class NLPIRWrapper {

	// 定义接口CLibrary，继承自com.sun.jna.Library
		public interface CLibrary extends Library {
			// 定义并初始化接口的静态变量
			CLibrary Instance = (CLibrary) Native.loadLibrary(
					"NLPIR", CLibrary.class);
			
			public int NLPIR_Init(String sDataPath, int encoding,
					String sLicenceCode);
					
			public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);

			public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
					boolean bWeightOut);
			
			public String NLPIR_GetFileKeyWords(String sLine, int nMaxKeyLimit,
					boolean bWeightOut);
			
			public int NLPIR_AddUserWord(String sWord);//add by qp 2008.11.10
			public int NLPIR_DelUsrWord(String sWord);//add by qp 2008.11.10
			public String NLPIR_GetLastErrorMsg();
			public void NLPIR_Exit();
		}

		public static String transString(String aidString, String ori_encoding,
				String new_encoding) {
			try {
				return new String(aidString.getBytes(ori_encoding), new_encoding);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return null;
		}

		public static String  getNLPParseResult(String str_input)
		{
			String argu = "";
			// String system_charset = "GBK";//GBK----0
			String system_charset = "UTF-8";
			int charset_type = 1;
			
			int init_flag = CLibrary.Instance.NLPIR_Init(argu, charset_type, "0");
			String nativeBytes = null;

			if (0 == init_flag) 
			{
				nativeBytes = CLibrary.Instance.NLPIR_GetLastErrorMsg();
				System.err.println("初始化失败！fail reason is "+nativeBytes);
				return "NIL";
			}

			
			//String nativeBytes = null;
			try {
				nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(str_input, 1);

				System.out.println("分词结果为： " + nativeBytes);
				
				CLibrary.Instance.NLPIR_Exit();
				 
				return nativeBytes;
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			
			return "NIL";
		}
		
		public static String getNLPParseResult(String str_input,List<String> user_dict)
		{
			String argu = "";
			// String system_charset = "GBK";//GBK----0
			String system_charset = "UTF-8";
			int charset_type = 1;
			
			int init_flag = CLibrary.Instance.NLPIR_Init(argu, charset_type, "0");
			String nativeBytes = null;

			if (0 == init_flag) 
			{
				nativeBytes = CLibrary.Instance.NLPIR_GetLastErrorMsg();
				System.err.println("初始化失败！fail reason is "+nativeBytes);
				return "NIL";
			}

			
			try {

				String nativeByte = CLibrary.Instance.NLPIR_GetKeyWords(str_input, 10,false);

				System.out.print("关键词提取结果是：" + nativeByte);
				
				CLibrary.Instance.NLPIR_Exit();
				 
				return nativeBytes;
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			
			return "NIL";
			
		}

		public static String getKeyWords(String str_input)
		{
			String argu = "";
			// String system_charset = "GBK";//GBK----0
			String system_charset = "UTF-8";
			int charset_type = 1;
			
			int init_flag = CLibrary.Instance.NLPIR_Init(argu, charset_type, "0");
			String nativeBytes = null;

			if (0 == init_flag) 
			{
				nativeBytes = CLibrary.Instance.NLPIR_GetLastErrorMsg();
				System.err.println("初始化失败！fail reason is "+nativeBytes);
				return "NIL";
			}

			
			//String nativeBytes = null;
			try {

				nativeBytes = CLibrary.Instance.NLPIR_GetKeyWords(str_input, 10,false);

				System.out.println("关键词提取结果是：" + nativeBytes);

				CLibrary.Instance.NLPIR_Exit();
				 
				return nativeBytes;
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			
			return "NIL";
		}
		
		public static void main(String[] args) throws Exception 
		{
			String sInput = "　　埃里克-斯波尔斯特拉 　　不管他手下是勒布朗-詹姆斯还是伯纳德-詹姆斯，至少，27连胜是无可争辩的事实。就凭这个，斯帅也应该获得外界更多的尊重了。教练的天赋，本来就不像球员那样有机会表露。更不用说，在斯帅的调教下，热火拿到史上第二长连胜了。";
			sInput=StringTool.removeSpareBlank(sInput);
			System.out.println(sInput); 
			String result=getNLPParseResult(sInput);
			System.out.println(StringTool.getNouns(result));
		}

}
