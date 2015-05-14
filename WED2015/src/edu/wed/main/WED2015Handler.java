package edu.wed.main;

import java.util.List;

import edu.wed.model.BaikeItem;

public class WED2015Handler {

	public WED2015Handler() {
		// TODO Auto-generated constructor stub
	}
	
	public static List<BaikeItem> handlerDisam(String word, String weibo){	
		List<BaikeItem> baikeitemList=DisamController.DisamToEntity(word,weibo);
		return baikeitemList;
	}

}
