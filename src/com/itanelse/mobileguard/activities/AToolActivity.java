package com.itanelse.mobileguard.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.itanelse.mobileguard.R;

public class AToolActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		setContentView(R.layout.activity_atool);
	}

	/*
	 * 号码归属地查询
	 * @param v
	 */
	public void phoneQuery(View v) {
		Intent query = new Intent(this, PhoneLocationActivity.class);
		startActivity(query);// 启动手机归属地查询界面
	}
}
