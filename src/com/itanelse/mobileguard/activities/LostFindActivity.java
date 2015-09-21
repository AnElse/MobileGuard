package com.itanelse.mobileguard.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.itanelse.mobileguard.R;
import com.itanelse.mobileguard.utils.MyConstants;
import com.itanelse.mobileguard.utils.SPTools;

public class LostFindActivity extends Activity {
	
	private TextView tv__safenumber;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 如果是第一次访问该界面,要先进入设置向导界面
		if (SPTools.getBoolean(getApplicationContext(), MyConstants.ISSET,
				false)) {
			// 返回true,说明已经进入过设置向导界面,那么直接显示防盗界面
			initView();// 初始化手机防盗界面
		} else {
			//返回false,说明是首次进入防盗界面,那么先进入设置向导界面
			Intent intent = new Intent(LostFindActivity.this,Setup1Activity.class);
			startActivity(intent);
			finish();
		}
		
		initData();//初始化数据
	}
	
	/**
	 * 初始化数据:安全号码
	 */
	private void initData() {
		tv__safenumber.setText("安全号码:"+ SPTools.getString(getApplicationContext(), MyConstants.SAFENUMBER, ""));
	}

	/**
	 * 初始化界面组件
	 */
	private void initView() {
		setContentView(R.layout.activity_lostfind);
		tv__safenumber = (TextView) findViewById(R.id.tv_lostfindactivity_safenumber);
	}
}
