package com.whumap.zhengwenutils;

import java.io.IOException;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class GetDetailArticle {
	
	public static String getNewsDetails(String url,String news_title,String news_date){
		Document document = null;
		String data="<center><h2>"+news_title+"</h2></center>";
		try {
			document = Jsoup.connect(url).timeout(9000).get();
			Elements author = document.getElementsByClass("news_attrib");
			System.out.println(author.toString());
			if(author != null) {
				data = data + "<p align='center'>" + getAuthor(author.toString()) + "</p>";  
				data = data+"<p align='right' style='margin-right:10px'>"+news_date+"</p>";
				data = data+"<hr size='1' />";
			}
			Elements element = document.getElementsByClass("news_content");
			if(element!=null){
				data=data+element.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	private static String getAuthor(String str) {
		String name="";
		char[] ch = str.toCharArray();
		int position = str.indexOf("作");
		for(int i=position;i<ch.length;i++) {
			if(ch[i] != '&') {
				name += ch[i];
			} else {
				break;
			}
		}
		return name;
	}
}
