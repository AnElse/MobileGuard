package com.itanelse.mobileguard.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import com.itanelse.mobileguard.R;

/**
 * 设置向导界面的基类,封装了共同的功能(上一页,下一页的跳转),并且抽象了具体功能的调用(用Class type参数,
 * 让步同页面传进各自要跳转的页面传进来)
 * @author 毓添
 *
 */
public abstract class BaseSetupActivity extends Activity {
	
	private GestureDetector gd; //手势识别器
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();//初始化组件
		initGesture();//初始化手势识别器,完成界面的滑动
		initData();//初始化数据
		initEvent();//初始化事件
	}
	
	/**
	 * 初始化事件
	 */
	public void initEvent() {
		
	}

	/**
	 * 初始化数据
	 */
	public void initData() {
		
	}

	/**
	 * 要想手势识别器生效，绑定onTouch事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gd.onTouchEvent(event);// 绑定onTouch事件
		return super.onTouchEvent(event);
	}
	
	/**
	 * 初始化手势识别器,完成界面的滑动
	 */
	private void initGesture() {
		// 初始化手势识别器,要想手势识别器生效，绑定onTouch事件
		gd = new GestureDetector(new OnGestureListener() {
			
			/**
			 * 覆盖此方法完成手势识别器的切换效果, e1,按下的点,e2,松开屏幕的点
			 * velocityX x轴方向的速度
			 * velocityY y轴方向的速度
			 */
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				if (velocityX > 100) {//x轴方向的速度是否满足横向滑动的条件 pix/s,速度大于200像素每秒就可以完成滑动
					float dx = e2.getX() - e1.getX();
					if (Math.abs(dx) < 100) {//如果间距不符合直接无效
						return true;//return true 直接把该结果吸收,不往上传
					}
					if (dx < 0) {//说明从右往左滑动,不是组件的事件调用,所以不用view的子类作为参数,直接null即可
						next(null);
					}
					if (dx > 0) {
						prev(null);//说明从左往右滑
					}
				}
				return true;
			}
			
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
					float distanceY) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onDown(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}

	//设置为抽象的初始化界面组件,强制子类去实现该方法来初始化组件
	public abstract void initView();
	
	/**
	 * 进入到下一个页面的事件处理
	 */
	public void next(View view){
		//在里面完成界面的跳转以及跳转时的动画
		nextActivity();
		 nextAnimation();
	}
	
	/**
	 * 进入下一个页面的动画
	 */
	private void nextAnimation() {
		//两个参数.enterAnim表示进来动画,exitAnim表示出去动画
		overridePendingTransition(R.anim.next_in, R.anim.next_out);
	}
	
	/**
	 * 子类调用该类来完成界面的跳转
	 * @param type
	 */
	public void startActivity(Class type){
		Intent intent = new Intent(this,type);
		startActivity(intent);
		finish();
	}

	public abstract void nextActivity();
	public abstract void prevActivity();

	/**
	 * 进入到上一个页面的时间处理
	 */
	public void prev(View view){
		//完成页面跳转和动画
		prevActivity();
		prevAnimation();
	} 

	private void prevAnimation() {
		overridePendingTransition(R.anim.prev_in, R.anim.prev_out);
	}
}
