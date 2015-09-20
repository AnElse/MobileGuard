package com.itanelse.mobileguard.activities;

import com.itanelse.mobileguard.R;

import android.app.Activity;
import android.os.Bundle;
/**
 * 第一个设置向导界面
 * @author 毓添
 *
 */
public class Setup1Activity extends BaseSetupActivity {
	
	/**
	 * 子类覆盖该方法实现界面的展示
	 */
	@Override
	public void initView() {
		setContentView(R.layout.activity_setup1);
	}
	
	@Override
	public void nextActivity() {
		// TODO Auto-generated method stub
		startActivity(Setup2Activity.class);
	}

	@Override
	public void prevActivity() {
		// TODO Auto-generated method stub
		
	}

}
