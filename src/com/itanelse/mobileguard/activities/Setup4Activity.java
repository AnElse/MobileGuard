package com.itanelse.mobileguard.activities;

import com.itanelse.mobileguard.R;

import android.app.Activity;
import android.os.Bundle;
/**
 * 第四个设置向导界面
 * @author 毓添
 *
 */
public class Setup4Activity extends BaseSetupActivity {

	@Override
	public void initView() {
		setContentView(R.layout.activity_setup4);
	}
	
	@Override
	public void nextActivity() {
		startActivity(LostFindActivity.class);
	}

	@Override
	public void prevActivity() {
		startActivity(Setup3Activity.class);
	}
	
}
