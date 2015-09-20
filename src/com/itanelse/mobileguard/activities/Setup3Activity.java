package com.itanelse.mobileguard.activities;

import com.itanelse.mobileguard.R;

import android.app.Activity;
import android.os.Bundle;
/**
 * 第三个设置向导界面
 * @author 毓添
 *
 */
public class Setup3Activity extends BaseSetupActivity {

	@Override
	public void initView() {
		setContentView(R.layout.activity_setup3);
	}
	
	@Override
	public void nextActivity() {
		startActivity(Setup4Activity.class);
	}

	@Override
	public void prevActivity() {
		startActivity(Setup2Activity.class);
	}

}
