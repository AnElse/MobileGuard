package com.itanelse.mobileguard.activities;

import android.app.Activity;
import android.os.Bundle;

import com.itanelse.mobileguard.R;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initview();//初始化界面的组件
	}
	
	/**
	 * //初始化界面的组件
	 */
	private void initview() {
		setContentView(R.layout.activity_main);
	}
}
