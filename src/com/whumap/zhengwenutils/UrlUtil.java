package com.whumap.zhengwenutils;


public class UrlUtil {

	private final static String URL = "http://news.whu.edu.cn/012/";
	
	public static String getUrl(int page_load){
		String url = URL;
		if(page_load == 1) {
			url = url + "index.html";
		} else {
			url = url + "index_" + page_load + ".html";
		}
		return url;
	}
}
