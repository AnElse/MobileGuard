package com.itanelse.mobileguard.activities;

import com.itanelse.mobileguard.R;

import android.app.Activity;
import android.os.Bundle;
/**
 * 第二个设置向导界面
 * @author 毓添
 *
 */
public class Setup2Activity extends BaseSetupActivity {

	@Override
	public void initView() {
		setContentView(R.layout.activity_setup2);
	}
	
	@Override
	public void nextActivity() {
		startActivity(Setup3Activity.class);
	}

	@Override
	public void prevActivity() {
		startActivity(Setup1Activity.class);
	}

}
