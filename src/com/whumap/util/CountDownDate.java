package com.whumap.util;

import java.util.Calendar;

public class CountDownDate {

	/** 指定倒计时的结束时间，年，月，日*/
	private int year ; 
	private int month ; 
	private int day;
	/** 当前系统时间,年，月，日 */
	private int currentYear;
	private int currentMonth;
	private int currentDay;

	/**
	 * 在构造函数中初始化倒计时时间
	 * @param year
	 * @param month  系统默认月份从0开始
	 * @param day
	 */
	public CountDownDate(int year , int month , int day) {
		
		this.year = year; 
		this.month = month -1 ;
		this.day = day;
		
	}

	/**
	 * 初始化当前时间
	 */
	private void initCurrentDate() {
		
		Calendar c = Calendar.getInstance();
		currentYear = c.get(Calendar.YEAR);
		currentMonth = c.get(Calendar.MONTH);
		currentDay = c.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 计算当前时间与倒计时时间的天数
	 * @return
	 */
	public int countDownDate() {
		
		initCurrentDate();
		int days = 0;
		int daysRemainOfMonth = daysOfMonth(currentMonth) - currentDay;
		if(currentYear == year) {
			if(month == currentMonth ) {
				days = day - currentDay;
			} else if(month - currentMonth == 1) {
				
				days = daysRemainOfMonth + day - 1;
			} else {
				days = daysRemainOfMonth + day + daysBetweenMonths(year, currentMonth, month);
			}
		} else {
			
		}
		return days;
	}
	/**
	 * 判断是否为闰年
	 * @return
	 */
	private boolean isBissextile(int year) {
		
		return (year%4 == 0 && year%400 == 0 && year%100 != 0) ;
		
	}
	
	/**
	 * 计算某年两个月之间的时间,月份之间至少需要间隔一个月
	 * @param year
	 * @param firstMonth
	 * @param secondMonth
	 * @return
	 */
	private int daysBetweenMonths(int year , int firstMonth , int secondMonth) {
		int days = 0;
			firstMonth++;
			secondMonth--;
		while(firstMonth <= secondMonth) {
			days += daysOfMonth(firstMonth);
			firstMonth++;
		}	
		return days;
	}
	
	/**
	 * 获取指定月的天数
	 * @param month
	 * @return
	 */
	private int daysOfMonth(int month){
		
		int daysOfMonth = 0;
			switch(month) {
			case 0:
			case 2:
			case 4:
			case 6:
			case 7:
			case 9:
			case 11:
				daysOfMonth += 31;
				break;
			case 1:
				if(isBissextile(year)) {
					daysOfMonth += 29;
				} else {
					daysOfMonth += 28;
				}
				break;
			case 3:
			case 5:
			case 8:
			case 10:
				daysOfMonth += 30;
				break;
			}
		return daysOfMonth;
	}
}
