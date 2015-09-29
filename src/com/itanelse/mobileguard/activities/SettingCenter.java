package com.itanelse.mobileguard.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.itanelse.mobileguard.R;
import com.itanelse.mobileguard.utils.MyConstants;
import com.itanelse.mobileguard.utils.SPTools;
import com.itanelse.mobileguard.view.SettingCenterView;

public class SettingCenter extends Activity {
	private SettingCenterView scv_autoupdate;// 设置中心的item.

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initEvent();
		initData();
	}

	/**
	 * 初始化数据状态
	 */
	private void initData() {
		// 初始化自动更新复选框的初始值
		scv_autoupdate.setCheck(SPTools.getBoolean(getApplicationContext(),
			MyConstants.AUTOUPDATE, false));
	}

	/**
	 * 初始化组件的事件
	 */
	private void initEvent() {
		scv_autoupdate.setItemClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 原来的复选框状态的功能不变
				scv_autoupdate.setCheck(!scv_autoupdate.isChecked());
				// 添加新的功能
				// 如果复选框选中,自动更新开启
				// 记录复选框的状态
				SPTools.putBoolean(getApplicationContext(),
						MyConstants.AUTOUPDATE, scv_autoupdate.isChecked());
			}
		});
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		setContentView(R.layout.activiy_settingcenter);
		// 获得自动更新的自定义view
		scv_autoupdate = (SettingCenterView) findViewById(R.id.scv_settingcenter_autoupdate);
	}
}
