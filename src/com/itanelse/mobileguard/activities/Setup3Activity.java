package com.itanelse.mobileguard.activities;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itanelse.mobileguard.R;
import com.itanelse.mobileguard.utils.EncryptUtils;
import com.itanelse.mobileguard.utils.MyConstants;
import com.itanelse.mobileguard.utils.SPTools;

/**
 * 第三个设置向导界面
 * 
 * @author 毓添
 * 
 */
public class Setup3Activity extends BaseSetupActivity {

	private EditText et_safenumber;// 安全号码
	private Button chooseSafenumber;// 选择安全号码按钮
	private String safenumber;// 得到输入或选择安全号码后的号码

	@Override
	public void initView() {
		setContentView(R.layout.activity_setup3);
		et_safenumber = (EditText) findViewById(R.id.et_setup3_safenumber);
		chooseSafenumber = (Button) findViewById(R.id.bt_setup3_chooseSafenumber);
	}

	/**
	 * 初始化数据
	 */
	@Override
	public void initData() {
		//保持安全号码在文本输入框
		//对密文进行解密后再取出保存在界面的安全号码输入框
		String decryptSafenumber = EncryptUtils.decryption(MyConstants.SEED, SPTools.getString(getApplicationContext(),
				MyConstants.SAFENUMBER, ""));
		et_safenumber.setText(decryptSafenumber);
		super.initData();
	}

	@Override
	public void next(View view) {
		safenumber = et_safenumber.getText().toString().trim();
		if (TextUtils.isEmpty(safenumber)) {
			// 如果安全号码为空,那么就不能进行页面跳转
			Toast.makeText(getApplicationContext(), "安全号码不能为空", 0).show();
			return;
		} else {
			// 进来,说明有安全号码,那么对安全号码进行加密,进行保存,不能使用MD5加密,因为是不可逆的.所以,这里我们
			//自己设计一种加密算法
			// String mdSafenumber = Md5Utils.md5(Md5Utils.md5(safenumber));//不可逆的
			String encrptSafenumber = EncryptUtils.encrption(MyConstants.SEED, safenumber);
			SPTools.putString(getApplicationContext(), MyConstants.SAFENUMBER,
					encrptSafenumber);
		}
		super.next(view);// 调用父类功能完成界面的切换.
	}

	/**
	 * 初始化事件
	 */
	@Override
	public void initEvent() {
		super.initEvent();
		chooseSafenumber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 点击选择安全号码按钮,获取手机联系人中的号码
				Intent intent = new Intent(Setup3Activity.this,
						FriendsActivity.class);
				// 第二个参数为请求码,只要是一个唯一值即可,一般 写1即可
				startActivityForResult(intent, 1);// 启动显示好友界面
			}
		});
	}

	/**
	 * 上面的startActivityForResult(friends,1)从friends界面中获取
	 * 结果,必须通过onActivityResult来获取结果
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {// 用户选择数据来关闭联系人界面,而不是直接点击返回按钮
			String safenumber = data.getStringExtra(MyConstants.SAFENUMBER);
			// 显示安全号码
			et_safenumber.setText(safenumber);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void nextActivity() {
		startActivity(Setup4Activity.class);
	}

	@Override
	public void prevActivity() {
		startActivity(Setup2Activity.class);
	}

}
