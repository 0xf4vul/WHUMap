package com.whumap.zhengwenutils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class GetNewsListService {
	private final static String KEYWORDS = "校庆征文";
	public static List<News> getNewsList(int page_load){
		List<News> list = new ArrayList<News>();
		Document document = null;
		try {
			document = Jsoup.connect(UrlUtil.getUrl(page_load)).timeout(4000).get();
			Element element=document.getElementsByClass("list").first();
			Element element2=element.child(0);
			for(Element element3:element2.children()){
				if(element3.select("a").size()>0){
					String title = element3.select("a").first().toString();
					if(title.contains(KEYWORDS)){
						
						News news=new News();
						news.setTitle(element3.select("a").first().text());
						news.setDate(element3.getElementsByClass("infodate").first().text());
						news.setUrl(element3.select("a").attr( "abs:href"));
						news.setLast_date(new Date().toLocaleString());
						list.add(news);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
}
