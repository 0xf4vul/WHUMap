package com.whumap.circlebutton;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;


import java.util.ArrayList;
import java.util.List;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;

public class CircleButtonAnimation {
	
	//设置半径
	public final int radius;
	//定义位置
	public static byte RIGHTBOTTOM = 1, CENTERBOTTOM = 2, LEFTBOTTOM = 3,
			LEFTCENTER = 4, LEFTTOP = 5, CENTERTOP = 6, RIGHTTOP = 7,
			RIGHTCENTER = 8;
	
	private int position; //位置坐标
	private ViewGroup circleButton; //定义父控件
	private final int childButtonCounts ; //定义子按钮个数
	private double layoutAngle = 180.0 ; //定义按钮排列最大角度
	private byte xOrientation = 1, yOrientation = 1; //定义方向，向上和向下
	private boolean isOpen = false;//记录打开还是关上的状态
	private List<ViewPropertyAnimator> viewAnimators = new ArrayList<ViewPropertyAnimator>();
	
	/**
	 * 
	 * @param circleButton 
	 * 
	 * @param position
	 * 
	 * @param radius
	 */
	public CircleButtonAnimation(ViewGroup circleButton, int position, int radius) {
		
		this.radius = radius;
		this.circleButton = circleButton;
		this.position = position;
		this.childButtonCounts = circleButton.getChildCount();
		
		for(int i=0; i<childButtonCounts ; i++) {
			View childAt = circleButton.getChildAt(i);
			ViewPropertyAnimator vpAnimator = animate(childAt);
			viewAnimators.add(vpAnimator);
			
		}
		
		if (position == RIGHTBOTTOM) { // 右下角
			layoutAngle = 90;
			xOrientation = -1;
			yOrientation = -1;
		} else if (position == CENTERBOTTOM) {// 中下
			layoutAngle = 180;
			xOrientation = -1;
			yOrientation = -1;
		} else if (position == LEFTBOTTOM) { // 左下角
			layoutAngle = 90;
			xOrientation = 1;
			yOrientation = -1;
		} else if (position == LEFTCENTER) { // 左中
			layoutAngle = 180;
			xOrientation = 1;
			yOrientation = -1;
		} else if (position == LEFTTOP) { // 左上角
			layoutAngle = 90;
			xOrientation = 1;
			yOrientation = 1;
		} else if (position == CENTERTOP) { // 中上
			layoutAngle = 180;
			xOrientation = -1;
			yOrientation = 1;
		} else if (position == RIGHTTOP) { // 右上角
			layoutAngle = 90;
			xOrientation = -1;
			yOrientation = 1;
		} else if (position == RIGHTCENTER) { // 右中
			layoutAngle = 180;
			xOrientation = -1;
			yOrientation = -1;
		}
	}

	private class AnimListener implements AnimatorListener {

		private View target;
		public AnimListener(View _target) {
			target = _target;
		}

		@Override
		public void onAnimationStart(Animator animation) {

		}

		@Override
		public void onAnimationEnd(Animator animation) {
			if (!isOpen) {
				target.setVisibility(View.INVISIBLE);
			}
		}

		@Override
		public void onAnimationCancel(Animator animation) {
		}

		@Override
		public void onAnimationRepeat(Animator animation) {
		}
	}
	
	/**
	 * 弹出几个按钮数
	 * @param durationMillis
	 */
	public void startAnimationsIn(int durationMillis) {
		isOpen = true;
		for (int i = 0; i < circleButton.getChildCount(); i++) {
			final LinearLayout inoutimagebutton = (LinearLayout) circleButton
					.getChildAt(i);

			double offangle = layoutAngle/ (childButtonCounts- 1);

			final double deltaY, deltaX;
			if (position == LEFTCENTER || position == RIGHTCENTER) {
				deltaX = Math.sin(offangle * i * Math.PI / 180) * radius;
				deltaY = Math.cos(offangle * i * Math.PI / 180) * radius;
			} else {
				deltaY = Math.sin(offangle * i * Math.PI / 180) * radius;
				deltaX = Math.cos(offangle * i * Math.PI / 180) * radius;
			}

			ViewPropertyAnimator viewPropertyAnimator = viewAnimators.get(i);
			viewPropertyAnimator.setListener(null);

			inoutimagebutton.setVisibility(View.VISIBLE);
			viewPropertyAnimator.x(
					(float) (inoutimagebutton.getLeft() + xOrientation * deltaX)).y(
					(float) (inoutimagebutton.getTop() + yOrientation * deltaY));

		}
	}
	
	/**
	 * 收回按钮数
	 * @param durationMillis
	 */
	public void startAnimationsOut(int durationMillis) {
		isOpen = false;
		for (int i = 0; i < circleButton.getChildCount(); i++) {
			final LinearLayout inoutimagebutton = (LinearLayout) circleButton
					.getChildAt(i);
			ViewPropertyAnimator viewPropertyAnimator = viewAnimators.get(i);
			viewPropertyAnimator.setListener(null);
			viewPropertyAnimator.x((float) inoutimagebutton.getLeft()).y(
					(float) inoutimagebutton.getTop());
			viewPropertyAnimator
					.setListener(new AnimListener(inoutimagebutton));

		}

	}
	
	
	/**
	 * 自转动画 
	 * @param fromDegrees
	 *           从多少度开始转 
	 * @param toDegrees
	 *           转到多少度 
	 * @param durationMillis
	 *           时间 
	 */
	public static Animation getRotateAnimation(float fromDegrees,
			float toDegrees, int durationMillis) {
		RotateAnimation rotate = new RotateAnimation(fromDegrees, toDegrees,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate.setDuration(durationMillis);
		rotate.setFillAfter(true);
		return rotate;
	}
}
