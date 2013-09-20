package com.whumap.circlebutton;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class CircleButton extends RelativeLayout {

	public final int BASIC_CHILD_BUTTON_ID = 1000;
	public static byte RIGHTBOTTOM = 1, CENTERBOTTOM = 2, LEFTBOTTOM = 3,
		LEFTCENTER = 4, LEFTTOP = 5, CENTERTOP = 6, RIGHTTOP = 7,
		RIGHTCENTER = 8;
	private boolean hasInit = false ; //判断初始化
	private boolean areButtonClicked = false ; //判断是否按下按钮
	private Context myContext ;
	private ImageView mainImage; //主按钮中间的图片
	private RelativeLayout mainButton ; //主按钮
	private CircleButtonAnimation cbAnimation; //按钮动画
	private LinearLayout[] childButton ;//子按钮
	private int dureTime = 300;//动画时间
	
	
	public CircleButton(Context context) {
		super(context);
		myContext = context;
	}

	public CircleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		myContext = context;
	}
	
	public CircleButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		myContext = context;
	}

	/**
	 * 初始化一个弹出菜单的按钮
	 * @param imgResId 用来加载需要弹出按钮子按钮的id，需要在资源文件中定义
	 * 
	 * @param showhideButtonId 加载弹出按钮外围的图片id
	 * 
	 * @param crossId 加载弹出按钮的内曾图片id
	 * 
	 * @param positionId 定义按钮需要摆放的位置
	 * 
	 * @param radius 定义弹出子按钮的方向
	 * 
	 * @param durationMillis 定义动画时间
	 */
	public void init(int[] imgResId, int showhideButtonId, int crossId,
			byte positionId, int radius, final int durationMillis) {
		//动画时间
		dureTime = durationMillis;
		
		int align1 = 12, align2 = 14;
		if (positionId == RIGHTBOTTOM) { // 右下角
			align1 = ALIGN_PARENT_RIGHT;
			align2 = ALIGN_PARENT_BOTTOM;
		} else if (positionId == CENTERBOTTOM) {// 中下
			align1 = CENTER_HORIZONTAL;
			align2 = ALIGN_PARENT_BOTTOM;
		} else if (positionId == LEFTBOTTOM) { // 左下角
			align1 = ALIGN_PARENT_LEFT;
			align2 = ALIGN_PARENT_BOTTOM;
		} else if (positionId == LEFTCENTER) { // 左中
			align1 = ALIGN_PARENT_LEFT;
			align2 = CENTER_VERTICAL;
		} else if (positionId == LEFTTOP) { // 左上角
			align1 = ALIGN_PARENT_LEFT;
			align2 = ALIGN_PARENT_TOP;
		} else if (positionId == CENTERTOP) { // 中上
			align1 = CENTER_HORIZONTAL;
			align2 = ALIGN_PARENT_TOP;
		} else if (positionId == RIGHTTOP) { // 右上角
			align1 = ALIGN_PARENT_RIGHT;
			align2 = ALIGN_PARENT_TOP;
		} else if (positionId == RIGHTCENTER) { // 右中
			align1 = ALIGN_PARENT_RIGHT;
			align2 = CENTER_VERTICAL;
		}
		
		
		RelativeLayout.LayoutParams rlp = (LayoutParams)this.getLayoutParams();
		Bitmap circleBehind = BitmapFactory.decodeResource(myContext.getResources(),imgResId[0]	);
		
		if(positionId == CENTERBOTTOM || positionId == CENTERTOP) {
			if(rlp.width != -1 && rlp.width != -2
					&& rlp.width < radius + circleBehind.getWidth() + radius * 0.1) {
				rlp.width = (int) ((radius*1.1 + circleBehind.getHeight()) * 2 );
			}
		} else {
			if(rlp.width != -1
					&& rlp.width != -2
					&& rlp.width < radius +circleBehind.getWidth() + radius
							* 0.1) {
				rlp.width = (int) (radius * 1.1 + circleBehind.getWidth());
			}
		}
		if(positionId == LEFTCENTER || positionId == RIGHTBOTTOM ) {
			if (rlp.height != -1
					&& rlp.height != -2
					&& rlp.height < (radius + circleBehind.getHeight() + radius * 0.1) * 2) {
				rlp.width = (int) ((radius * 1.1 + circleBehind.getHeight()) * 2);
			}
		} else {
			if (rlp.height != -1
					&& rlp.height != -2
					&& rlp.height < radius + circleBehind.getHeight() + radius
							* 0.1) {
				rlp.height = (int) (radius * 1.1 + circleBehind.getHeight());
			}
		}
		this.setLayoutParams(rlp);
		
		//按钮布局
		RelativeLayout behindLayout = new RelativeLayout(myContext); //所有子按钮所在的区域
		mainButton = new RelativeLayout(myContext);
		childButton = new LinearLayout[imgResId.length];
		
		//添加子按钮
		for(int i=0; i<imgResId.length ; i++) {
			ImageView img = new ImageView(myContext); //子按钮图片
			img.setImageResource(imgResId[i]);
			
			LinearLayout.LayoutParams llps = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			img.setLayoutParams(llps);
			childButton[i] = new LinearLayout(myContext);
			childButton[i].setId(BASIC_CHILD_BUTTON_ID + i);
			childButton[i].addView(img);
			
			RelativeLayout.LayoutParams rlps = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			rlps.alignWithParent = true;
			rlps.addRule(align1, RelativeLayout.TRUE);
			rlps.addRule(align2, RelativeLayout.TRUE);
			childButton[i].setLayoutParams(rlps);
			childButton[i].setVisibility(View.INVISIBLE);// 此处不能为GONE
			behindLayout.addView(childButton[i]);
			
		}
		RelativeLayout.LayoutParams rlps1 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		rlps1.alignWithParent = true;
		rlps1.addRule(align1, RelativeLayout.TRUE);
		rlps1.addRule(align2, RelativeLayout.TRUE);
		behindLayout.setLayoutParams(rlps1);

		RelativeLayout.LayoutParams buttonlps = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		buttonlps.alignWithParent = true;
		buttonlps.addRule(align1, RelativeLayout.TRUE);
		buttonlps.addRule(align2, RelativeLayout.TRUE);
		mainButton.setLayoutParams(buttonlps);
		mainButton.setBackgroundResource(showhideButtonId);
		mainImage = new ImageView(myContext);
		mainImage.setImageResource(crossId);
		RelativeLayout.LayoutParams crosslps = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		crosslps.alignWithParent = true;
		crosslps.addRule(CENTER_IN_PARENT, RelativeLayout.TRUE);
		mainImage.setLayoutParams(crosslps);
		mainButton.addView(mainImage);
		cbAnimation = new CircleButtonAnimation(behindLayout, positionId, radius);
		mainButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (areButtonClicked) {
					cbAnimation.startAnimationsOut(dureTime);
					mainImage.startAnimation(cbAnimation.getRotateAnimation(-270,
							0, dureTime));
				} else {
					cbAnimation.startAnimationsIn(dureTime);
					mainImage.startAnimation(cbAnimation.getRotateAnimation(0,
							-270, dureTime));
				}
				areButtonClicked = !areButtonClicked;
			}
		});
		mainImage.startAnimation(cbAnimation.getRotateAnimation(0, 360, 200));
		this.addView(behindLayout);
		this.addView(mainButton);
		hasInit = true;
		
	}


	/**
	 * 收起菜单按钮
	 */
	public void collapse() {
		cbAnimation.startAnimationsOut(dureTime);
		mainImage.startAnimation(cbAnimation.getRotateAnimation(-270, 0, dureTime));
		areButtonClicked = false;
	}

	/**
	 * 判断主按钮是处于打开或者关闭状态
	 * @return
	 */
	public boolean getButtonState() {
		return areButtonClicked;
	}
	
	
	/**
	 * 打开菜单按钮
	 */
	public void expand() {
		cbAnimation.startAnimationsIn(dureTime);
		mainImage.startAnimation(cbAnimation.getRotateAnimation(0, -270, dureTime));
		areButtonClicked = true;
	}
	
	
	/**
	 * 是否初始化
	 */
	
	public boolean isInit() {
		return hasInit;
	}
	
	/**
	 * 是否打开按钮
	 */
	
	@Override
	public boolean isShown() {
		// TODO Auto-generated method stub
		return super.isShown();
	}
	/**
	 * 为子按钮的点击添加事件监听
	 * @param l
	 */
	public void setChildOnClickListener(final OnClickListener l) {

		if (childButton != null) {
			for (int i = 0; i < childButton.length; i++) {
				if (childButton[i] != null)
					childButton[i].setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(final View view) {
							collapse();
							l.onClick(view);
						}

					});
			}
		}
	}
}
