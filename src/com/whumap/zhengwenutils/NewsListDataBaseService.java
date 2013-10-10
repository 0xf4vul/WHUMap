package com.whumap.zhengwenutils;

import java.util.List;


import android.content.Context;

public class NewsListDataBaseService {

	public static void addNewsList(List<News> list, Context context, String type) {
		
		new DataBaseHelper(context).clearNewsListByType(type);
		new DataBaseHelper(context).addNewsList(list, type);
	}
	
	public static List<News> getNewsList(Context context,String type){
		List<News> list = new DataBaseHelper(context).getNewsListByType(type);
		return list;
	}
}
