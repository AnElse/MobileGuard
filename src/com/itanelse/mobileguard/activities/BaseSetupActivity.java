package com.itanelse.mobileguard.activities;

import com.itanelse.mobileguard.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 设置向导界面的基类,封装了共同的功能(上一页,下一页的跳转),并且抽象了具体功能的调用(用Class type参数,
 * 让步同页面传进各自要跳转的页面传进来)
 * @author 毓添
 *
 */
public abstract class BaseSetupActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
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
		prevActivity();
		prevAnimation();
	}

	private void prevAnimation() {
		overridePendingTransition(R.anim.prev_in, R.anim.prev_out);
	}
}
