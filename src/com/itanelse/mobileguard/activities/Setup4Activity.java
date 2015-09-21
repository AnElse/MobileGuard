package com.itanelse.mobileguard.activities;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.itanelse.mobileguard.R;
import com.itanelse.mobileguard.service.LostFindService;
import com.itanelse.mobileguard.utils.MyConstants;
import com.itanelse.mobileguard.utils.SPTools;
import com.itanelse.mobileguard.utils.ServiceRunningUtils;

/**
 * 第四个设置向导界面
 * 
 * @author 毓添
 * 
 */
public class Setup4Activity extends BaseSetupActivity {

	private CheckBox cb_isprotected;// 防盗保护是否勾选
	private TextView tv_protectIsOpen;// 防盗保护勾选后的提示

	@Override
	public void initView() {
		setContentView(R.layout.activity_setup4);
		cb_isprotected = (CheckBox) findViewById(R.id.cb_setup4_isprotected);
		tv_protectIsOpen = (TextView) findViewById(R.id.tv_setup4_protectIsOpen);
	}

	/**
	 * 初始化单选框的勾选事件
	 */
	@Override
	public void initEvent() {
		cb_isprotected
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							// 勾选以后,防盗保护开启,防盗保护是一个服务
							Intent lostFindService = new Intent(
									Setup4Activity.this, LostFindService.class);
							startService(lostFindService);
							System.out.println("勾选了,开启服务");
							tv_protectIsOpen.setVisibility(View.VISIBLE);// 显示防盗保护勾选后的提示
						} else {
							// 继续去掉勾选,那么停止服务
							Intent lostFindService = new Intent(
									Setup4Activity.this, LostFindService.class);
							stopService(lostFindService);
							System.out.println("停止勾选,停止服务");
						}
					}
				});
		super.initEvent();
	}

	/**
	 * 初始化复选框的数据状态:即保存服务有没有开启的状态
	 */
	@Override
	public void initData() {
		if (ServiceRunningUtils.ServiceIsRunning(getApplicationContext(),
				"com.itanelse.mobileguard.service.LostFindService")) {
			// 服务开启
			cb_isprotected.setChecked(true);//初始化复选框的状态
			System.out.println("保持勾选状态");
			
		} else {
			cb_isprotected.setChecked(false);//初始化复选框的状态
		}
		super.initData();
	}
	
	/**
	 * 复写父类进行下一页跳转的功能,这里改为setup4的设置完成
	 * 不点击打开勾选防盗保护,不让点击设置完成
	 */
	@Override
	public void next(View view) {
		if (!cb_isprotected.isChecked()) {
			Toast.makeText(getApplicationContext(), "还没有开启防盗保护", 0).show();
			return;
		}else{
			//勾选了,那么保存好防盗界面设置完成的状态
			SPTools.putBoolean(getApplicationContext(), MyConstants.ISSET, 
					true);
		}
		super.next(view);
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
