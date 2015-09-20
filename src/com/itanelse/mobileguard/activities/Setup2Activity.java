package com.itanelse.mobileguard.activities;

import android.content.Intent;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.itanelse.mobileguard.R;
import com.itanelse.mobileguard.utils.MyConstants;
import com.itanelse.mobileguard.utils.SPTools;

/**
 * 第二个设置向导界面
 * 
 * @author 毓添
 * 
 */
public class Setup2Activity extends BaseSetupActivity {

	private Button bt_bindsim;// 获取绑定sim卡按钮
	private ImageView iv_lock_sim;// 是否绑定sim卡的锁图标
	private String simSerialNumber;// 获取绑定的sim卡号码

	@Override
	public void initView() {
		setContentView(R.layout.activity_setup2);
		// 获取绑定sim卡按钮
		bt_bindsim = (Button) findViewById(R.id.bt_setup2_bindsim);
		// 是否绑定sim卡的锁图标
		iv_lock_sim = (ImageView) findViewById(R.id.iv_setup2_lock);
	}

	/**
	 * 初始化数据
	 */
	@Override
	public void initData() {
		super.initData();
		if (TextUtils.isEmpty(SPTools.getString(getApplicationContext(),
				MyConstants.SIM, ""))) {
			{
				// 未绑定
				// 锁图标变为开锁
				iv_lock_sim.setImageResource(R.drawable.unlock);
			}
		} else {
			// 图标变为锁上
			iv_lock_sim.setImageResource(R.drawable.lock);
		}
	}

	/**
	 * 初始化
	 */
	@Override
	public void initEvent() {
		super.initEvent();
		bt_bindsim.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(SPTools.getString(
						getApplicationContext(), MyConstants.SIM, ""))) {
					// 如果没有绑定,点击绑定sim卡,锁图标变为锁上
					// 获取sim卡
					TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					simSerialNumber = tm.getSimSerialNumber();
					// 绑定sim卡
					SPTools.putString(getApplicationContext(), MyConstants.SIM,
							simSerialNumber);
					{
						// 图标变为锁上
						iv_lock_sim.setImageResource(R.drawable.lock);
					}
				} else {
					// 已经绑定,点击解绑,锁图标变为开锁
					SPTools.putString(getApplicationContext(), MyConstants.SIM,
							"");// 保存一个空值即为解绑
					{
						// 锁图标变为开锁
						iv_lock_sim.setImageResource(R.drawable.unlock);
					}
				}
			}
		});
	}
	
	/**
	 * 重写父类跳转方法,因为父类中的该方法有两个方法,界面跳转和动画,所以不可以直接在nextActivity跳转方法中判断是否绑定了sim
	 * 卡,不然不能跳转到下一个界面.这里重写了该方法,只有一个功能--跳转,所以可以在这里判断是否绑定了sim卡,不绑定不能跳转
	 */
	@Override
	public void next(View view) {
		if (TextUtils.isEmpty(SPTools.getString(getApplicationContext(),
				MyConstants.SIM, ""))) {
			Toast.makeText(getApplicationContext(), "sim卡不能为空", 0).show();
			return;
		}
		super.next(view);// 调用父类的功能
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
