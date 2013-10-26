package com.whumap.zhengwenutils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper{

	private static final String DATABASE_NAME="whuzhengwen.database";
	private static final int DATEBASE_VERSION=1;
	private static final String CREATE_TABLE_NEWSLIST = "CREATE TABLE IF NOT EXISTS newslist" +
			" (id INTEGER PRIMARY KEY AUTOINCREMENT,type varchar,title varchar,details varchar" +
			",date varchar,url varchar,last_date varchar)";
	
	public DataBaseHelper(Context context)	{
		super(context,DATABASE_NAME,null,DATEBASE_VERSION);
		
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_NEWSLIST);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	public boolean isExist(Article article){
		boolean isexist=true;
		SQLiteDatabase sq = this.getReadableDatabase();
		Cursor cursor = sq.query("newslist", new String[] { "title",
				"details", "url"}, "url=?",
				new String[] { article.getUrl()}, null, null, null);
		if(cursor.getCount()==0){
			isexist=false;
		}
		sq.close();
		return isexist;
	}
	
	public void clearNewsListByType(String type){
		SQLiteDatabase sqLiteDatabase = getWritableDatabase();
		if (sqLiteDatabase != null)  {
			sqLiteDatabase.delete("newslist", "type=?", new String[] {type});
		} 
		sqLiteDatabase.close();
	}
	
	public List<Article> getNewsListByType(String type){
		List<Article> list = new ArrayList<Article>();
		SQLiteDatabase sqLiteDatabase = getReadableDatabase();
		Cursor cursor = sqLiteDatabase.query("newslist", new String[] { "title",
				"details", "url", "date" ,"last_date"}, "type=?",
				new String[] { type }, null, null, null);
		while (cursor.moveToNext()) {
			Article article = new Article();
			article.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			article.setDetails(cursor.getString(cursor.getColumnIndex("details")));
			article.setUrl(cursor.getString(cursor.getColumnIndex("url")));
			article.setDate(cursor.getString(cursor.getColumnIndex("date")));
			article.setLast_date(cursor.getString(cursor.getColumnIndex("last_date")));
			list.add(article);
		}
		sqLiteDatabase.close();
		return list;
	}

	public void addNewsList(List<Article> list,String type){
		for(Article article:list){
			addNews(article,type);
		}
	}

	@SuppressWarnings("deprecation")
	public void addNews(Article article,String type){
		ContentValues value = new ContentValues();
		if (!isExist(article)) {
			value.put("type", type);
			value.put("title", article.getTitle());
			value.put("details", article.getDetails());
			value.put("url", article.getUrl());
			value.put("date", article.getDate());
			value.put("last_date", new Date().toLocaleString());
			SQLiteDatabase sqLiteDatabase=getWritableDatabase();
			if(sqLiteDatabase!=null){
				sqLiteDatabase.insert("newslist", null, value);
				sqLiteDatabase.close();
			}
		} else {
			Log.i("data", "已存在");
		}
	}
}
