package com.whumap.zhengwenutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ObjectListToHashMap {

	public static List<HashMap<String,Object>> newsListToHashMapList(List<Article> list){
		List<HashMap<String,Object>> news_list=new ArrayList<HashMap<String,Object>>();
		for(Article article:list){
			HashMap<String, Object> hashMap=new HashMap<String, Object>();
			hashMap.put("news_title",article.getTitle());
			hashMap.put("news_details", article.getDetails());
			hashMap.put("news_date", article.getDate());
			hashMap.put("news_url", article.getUrl());
			hashMap.put("last_date", article.getLast_date());
			news_list.add(hashMap);
		}
		return news_list;
	}
}
