package com.itanelse.mobileguard.activities;

import com.itanelse.mobileguard.R;

import android.app.Activity;
import android.os.Bundle;

public class LostFindActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();//初始化界面组件
	}
	
	/**
	 * 初始化界面组件
	 */
	private void initView() {
		setContentView(R.layout.activity_lostfind);
	}
}
