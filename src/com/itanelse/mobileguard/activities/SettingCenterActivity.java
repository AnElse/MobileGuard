package com.itanelse.mobileguard.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
	private RelativeLayout rl_style_root;
	private ImageView iv_locationSytle_click;
	private TextView tv_locationStyle_content;
	private AlertDialog dialog;

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
		// 归属地根布局点击事件
		rl_style_root.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// 按下
					iv_locationSytle_click
							.setImageResource(R.drawable.jiantou1_pressed);
					break;
				case MotionEvent.ACTION_UP:// 松开
					iv_locationSytle_click
							.setImageResource(R.drawable.jiantou1_disable);
					// 显示选择归属地样式的对话框
					showStyleDialog();
					break;
				default:
					break;
				}
				return false;
			}
		});
		// 箭头的点击事件
		iv_locationSytle_click.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 显示选择归属地样式的对话框
				showStyleDialog();
			}
		});

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
	 * 显示归属地样式的对话框
	 */
	private String[] styleNames = new String[] { "卫士蓝", "金属灰", "苹果绿", "活力橙",
			"半透明" };

	private void showStyleDialog() {
		// 对话框让用户选择样式
		AlertDialog.Builder ab = new AlertDialog.Builder(
				SettingCenterActivity.this);
		ab.setTitle("选择归属地样式");
		ab.setSingleChoiceItems(styleNames, Integer.parseInt(SPTools.getString(
				getApplicationContext(), MyConstants.STYLEBGINDEX, "0")),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// which 点击的位置
						// 保存sp中 字符串的方式保存归属地样式
						SPTools.putString(getApplicationContext(),
								MyConstants.STYLEBGINDEX, which + "");
						tv_locationStyle_content.setText(styleNames[which]);
						dialog.dismiss();// 关闭对话框
					}
				});

		dialog = ab.create();
		dialog.show();
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
		// 归属地样式的根布局
		rl_style_root = (RelativeLayout) findViewById(R.id.rl_settingcenter_locationsytle_root);

		// 归属地样式的名字
		tv_locationStyle_content = (TextView) findViewById(R.id.tv_settingcenter_locationsytle_content);

		// 点击图片按钮(箭头)来显示样式选择对话框
		iv_locationSytle_click = (ImageView) findViewById(R.id.iv_settingcenter_locationsytle_select);
	}
}
