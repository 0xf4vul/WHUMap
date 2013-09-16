package com.whumap.fucbutton;

import java.util.ArrayList;
import java.util.List;

import com.nineoldandroids.view.ViewPropertyAnimator;

import android.view.ViewGroup;

public class FunctionButtonAnimation {
	
	//设置半径
	public final int radius;
	//定义位置
	public static byte RIGHTBOTTOM = 1, CENTERBOTTOM = 2, LEFTBOTTOM = 3,
			LEFTCENTER = 4, LEFTTOP = 5, CENTERTOP = 6, RIGHTTOP = 7,
			RIGHTCENTER = 8;
	
	private int position; //位置坐标
	private ViewGroup fucButton; //定义父控件
	private final int buttonCounts ; //定义子按钮个数
	private double layoutAangle = 180.0 ; //定义按钮排列最大角度
	private byte xOrientation = 1, yOrientation = 1; //定义方向，向上和向下
	private boolean isOpen = false;//记录打开还是关上的状态
	private List<ViewPropertyAnimator> viewAnimators = new ArrayList<ViewPropertyAnimator>();
	
	
	
	
	public FunctionButtonAnimation(ViewGroup fucButton, int position, int radius) {
		
		this.radius = radius;
		this.fucButton = fucButton;
		this.position = position;
		this.buttonCounts = fucButton.getChildCount();
	}

	
	}
