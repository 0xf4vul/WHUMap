package com.whumap.zhengwenutils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class SetNews {
	
	private final static String XIAOQINGZHWNGWEN = "【校庆征文】";
	private final static String LUOJIAFU = "珞珈赋";
	private final static String URL = "http://news.whu.edu.cn/012/";
	private final static String IMAGEABS = "http://news.whu.edu.cn";

	
	/**
	 * 通过url来加载选择的文章的具体内容，
	 * 因为官网图片的格式为相对路径，当读取时是无法加载具体图片的，
	 * 所以需要自己将图片资源的src改为绝对路径
	 * @param url
	 * @param news_title
	 * @param news_date
	 * @return
	 */
	public static String getNewsDetails(String url,String news_title,String news_date){
		Document document = null;
		String data="<center><h2>"+news_title+"</h2></center>";
		int articleType = 0;
		if(news_title.contains(LUOJIAFU)) {
			articleType = 2;
		} else {
			articleType = 6;
		}
		
		try {
			document = Jsoup.connect(url).timeout(5000).get();
			Elements author = document.getElementsByClass("news_attrib");
			if(author != null) {
				data += "<p align='center'>" + getAuthor(author.toString()) + "</p>";  
			}
			data += "<p align='right' style='margin-right:10px'>"+news_date+"</p>";
			data += "<hr size='1' />";
			/*
			Elements element = document.getElementsByClass("news_content");
			int elementSize = element.size();
			if(element!=null){
				data += element.toString();
				}
				*/
			//将校庆征文的文章最后几行去掉
			Element element = document.getElementsByClass("news_content").first();
			setCorrectSrc(element);
			if(element != null) {
				int elementSize = element.select("p").size();
				for(int i=0;i<elementSize - articleType;i++) {
					data += element.child(i).toString();
				}
			}
				
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	
	/**
	 * 读取指定页的符合条件的文章
	 * @param page_load
	 * @return
	 */
	public static List<Article> getNewsList(int page_load){
		List<Article> list = new ArrayList<Article>();
		Document document = null;
		try {
			document = Jsoup.connect(getUrl(page_load)).timeout(4000).get();
			Element element = document.getElementsByClass("list").first();
			Element element2 = element.child(0);
			for(Element element3 : element2.children()){
				if(element3.select("a").size()>0){
					String title = element3.select("a").first().toString();
					if(title.contains(XIAOQINGZHWNGWEN)){
						
						Article article=new Article();
						article.setTitle(getTitle(element3.select("a").first().text()));
//						news.setTitle(element3.select("a").first().text());
						article.setDate(element3.getElementsByClass("infodate").first().text());
						article.setUrl(element3.select("a").attr( "abs:href"));
						article.setLast_date(new Date().toLocaleString());
						list.add(article);
					} else if(title.contains(LUOJIAFU)) { 
						
						Article article=new Article();
						article.setTitle(element3.select("a").first().text());
						article.setDate(element3.getElementsByClass("infodate").first().text());
						article.setUrl(element3.select("a").attr( "abs:href"));
						article.setLast_date(new Date().toLocaleString());
						list.add(article);
					} else {
						
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 将网页中加载到图片的地方，全部修改为其绝对路径
	 * @param element
	 */
	private static void setCorrectSrc(Element element) {
		int elementSize = element.select("p").size();
		for(int i=0; i<elementSize; i++) {
			Element childElement = element.child(i);
			if(childElement.select("p").select("img").hasAttr("src")) {
				String src = childElement.select("p").select("img").attr("src");
				String absHref = IMAGEABS + src;
				Log.i("src", absHref);
				childElement.select("p").select("img").attr("src",absHref);
			}
		}
	}
	
	/**
	 * 因为第一页与其他也url不同，所以需要判断
	 * @param page_load
	 * @return
	 */
	private static String getUrl(int page_load){
		String url = URL;
		if(page_load == 1) {
			url = url + "index.html";
		} else {
			url = url + "index_" + page_load + ".html";
		}
		return url;
	}
	
	/**
	 * 把[珞珈征文]的前缀去掉
	 * @param title
	 * @return
	 */
	private static String getTitle(String title){
		return title.replaceFirst(XIAOQINGZHWNGWEN, "");
	}
	
	
	/**
	 * 获得作者名
	 * @param str
	 * @return
	 */
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
