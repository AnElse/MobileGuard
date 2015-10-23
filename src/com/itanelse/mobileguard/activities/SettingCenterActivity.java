package com.itanelse.mobileguard.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.itanelse.mobileguard.R;
import com.itanelse.mobileguard.service.ComingPhoneService;
import com.itanelse.mobileguard.service.TelSmsBlackService;
import com.itanelse.mobileguard.utils.MyConstants;
import com.itanelse.mobileguard.utils.SPTools;
import com.itanelse.mobileguard.utils.ServiceRunningUtils;
import com.itanelse.mobileguard.view.SettingCenterView;

public class SettingCenterActivity extends Activity {
	private SettingCenterView scv_autoupdate;// 设置中心的item.
	private SettingCenterView sciv_blackservice;
	private SettingCenterView sciv_comingphonelocation;

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
		// 判断黑名单服务，来设置复选框的初始值
		sciv_blackservice.setCheck(ServiceRunningUtils.isServiceRunning(
				getApplicationContext(),
				"com.itanelse.mobileguard.service.TelSmsBlackService"));
	}

	/**
	 * 初始化组件的事件
	 */
	private void initEvent() {
		// 来电归属地启动或者关闭
		sciv_comingphonelocation.setItemClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 判断黑名单拦截服务是否运行
				if (ServiceRunningUtils.isServiceRunning(
						getApplicationContext(),
						"com.itanelse.mobileguard.service.ComingPhoneService")) {
					// 服务在运行,关闭服务
					Intent comingPhoneLocationService = new Intent(
							SettingCenterActivity.this,
							ComingPhoneService.class);
					stopService(comingPhoneLocationService);
					// 设置复选框的状态
					sciv_comingphonelocation.setCheck(false);
				} else {
					// 服务停止,打开服务
					Intent comingPhoneLocationService = new Intent(
							SettingCenterActivity.this,
							ComingPhoneService.class);
					startService(comingPhoneLocationService);
					// 设置复选框的状态
					sciv_comingphonelocation.setCheck(true);
				}
			}
		});

		// 黑名单服务启动或关闭
		sciv_blackservice.setItemClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 判断黑名单拦截服务是否运行
				if (ServiceRunningUtils.isServiceRunning(
						getApplicationContext(),
						"com.itanelse.mobileguard.service.TelSmsBlackService")) {
					// 服务在运行,关闭服务
					Intent blackService = new Intent(
							SettingCenterActivity.this,
							TelSmsBlackService.class);
					stopService(blackService);
					// 设置复选框的状态
					sciv_blackservice.setCheck(false);
				} else {
					// 服务停止,打开服务
					Intent blackService = new Intent(
							SettingCenterActivity.this,
							TelSmsBlackService.class);
					startService(blackService);
					// 设置复选框的状态
					sciv_blackservice.setCheck(true);
				}
			}
		});

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
		// 来电归属地的自定义view
		sciv_comingphonelocation = (SettingCenterView) findViewById(R.id.sciv_setting_center_comingphonelocation);
		// 获得自动更新的自定义view
		scv_autoupdate = (SettingCenterView) findViewById(R.id.scv_settingcenter_autoupdate);
		// 获取黑名单拦截的自定义view
		sciv_blackservice = (SettingCenterView) findViewById(R.id.sciv_setting_center_blackservice);
	}
}
