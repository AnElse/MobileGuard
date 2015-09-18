package com.itanelse.mobileguard.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itanelse.mobileguard.R;
import com.itanelse.mobileguard.utils.Md5Utils;
import com.itanelse.mobileguard.utils.MyConstants;
import com.itanelse.mobileguard.utils.SPTools;

public class MainActivity extends Activity {

	private GridView gv_menu;// 主界面菜单
	private int[] icons = { R.drawable.safe, R.drawable.callmsgsafe,
			R.drawable.gv_list_selector_app, R.drawable.taskmanager,
			R.drawable.netmanager, R.drawable.trojan, R.drawable.sysoptimize,
			R.drawable.atools, R.drawable.settings };// 获取到菜单的图片资源
	private String[] names = { "手机防盗", "通讯卫士", "软件管家", "进程管理", "流量统计", "病毒查杀",
			"缓存清理", "高级工具", "设置中心" };// 获取到菜单的名称
	private AlertDialog dialog;// 自定义的进入防盗中心的对话框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initview();// 初始化界面的组件
		initData();// 初始化界面数据
		initEvent();// 初始menu的点击事件
	}

	/**
	 * 初始menu的点击事件
	 */
	private void initEvent() {
		gv_menu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 判断点击位置
				switch (position) {
				case 0:// 手机防盗
						// 先进行判断是否已经设置过密码,如果已经设置过密码,那么进入自定义登陆对话框
					if (!TextUtils.isEmpty(SPTools.getString(
							getApplicationContext(), MyConstants.PASSWORD, ""))) {
						// 进入自定义登陆对话框
							showEnterPasswordDialog();

					} else {
						// 否则,进入自定义设置密码对话框
						showSettingPasswordDialog();
					}

					break;

				default:
					break;
				}
			}
		});
	}

	/**
	 * 自定义登陆对话框
	 */
	protected void showEnterPasswordDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = View.inflate(getApplicationContext(), R.layout.dialog_enter_password, null);
		// 获取到输出的密码的edittext组件
		final EditText et_password = (EditText) view
				.findViewById(R.id.et_dialog_enter_password);
		// 获取确认输入和取消输入的按钮
		final Button bt_enter = (Button) view
				.findViewById(R.id.bt_dialog_enter_sure);
		final Button bt_cancel = (Button) view
				.findViewById(R.id.bt_dialog_enter_cancel);
		// 把自定义view设置到对话框
		builder.setView(view);
		// 设置点击确认输入按钮
		bt_enter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String password = et_password.getText().toString().trim();
System.out.println(password);
				//把获取到的密码进行两次MD5加密
				String mdpassword = Md5Utils.md5(Md5Utils.md5(password));
System.out.println(mdpassword);				
				// 进行判断
				if (TextUtils.isEmpty(password)) {
					Toast.makeText(getApplicationContext(), "密码不能为空", 0).show();
					return;
				} else {
					//有密文,进行对比,如果相同,那么进入防盗界面
					if (mdpassword.equals(SPTools.getString(getApplicationContext(), MyConstants.PASSWORD, ""))) {
						Intent intent = new Intent(MainActivity.this,LostFindActivity.class);
						startActivity(intent);
						finish();
					}else{
						Toast.makeText(getApplicationContext(), "密码不正确",  0).show();
					}
					dialog.dismiss();
				}
			}
		});
		// 设置点击取消输入按钮
		bt_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 关闭对话框
				dialog.dismiss();
			}
		});
		dialog = builder.create();
		dialog.show();
	}

	/**
	 * 显示自定义的保存密码对话框
	 */
	protected void showSettingPasswordDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = View.inflate(getApplicationContext(),
				R.layout.dialog_setting_password, null);
		// 获取到输出的密码
		final EditText et_passone = (EditText) view
				.findViewById(R.id.et_dialog_passwordone);
		final EditText et_passtwo = (EditText) view
				.findViewById(R.id.et_dialog_passwordtwo);
		// 获取确认输入和取消输入的按钮
		final Button bt_setpassword = (Button) view
				.findViewById(R.id.bt_dialog_setpassword);
		final Button bt_cancelset = (Button) view
				.findViewById(R.id.bt_dialog_cancelsetpassword);
		// 把自定义view设置到对话框
		builder.setView(view);
		// 设置点击确认输入按钮
		bt_setpassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String passone = et_passone.getText().toString().trim();
				String passtwo = et_passtwo.getText().toString().trim();
				// 进行判断
				if (TextUtils.isEmpty(passone) || TextUtils.isEmpty(passtwo)) {
					Toast.makeText(getApplicationContext(), "密码不能为空", 0).show();
					return;
				} else if (!passone.equals(passtwo)) {
					Toast.makeText(getApplicationContext(), "密码不一致", 0).show();
					return;
				} else {
					// 密码一致,对密码进行MD5加密,加密两次(银行加密10次以上)
					String mdpassword = Md5Utils.md5(Md5Utils.md5(passtwo));
					// 保存密码并关闭对话框
					SPTools.putString(getApplicationContext(),
							MyConstants.PASSWORD, mdpassword);
					Toast.makeText(getApplicationContext(), "保存密码成功", 0).show();
					dialog.dismiss();
				}
			}
		});
		// 设置点击取消输入按钮
		bt_cancelset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 关闭对话框
				dialog.dismiss();
			}
		});
		dialog = builder.create();
		dialog.show();
	}

	/**
	 * 初始化界面数据的方法
	 */
	private void initData() {
		// 设置Gridview适配器数据
		gv_menu.setAdapter(new MyAdapter());
	}

	/**
	 * 设置适配器
	 * 
	 * @author 毓添
	 * 
	 */
	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return icons.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(getApplicationContext(),
					R.layout.item_main_gv_menu, null);
			// 获取组件
			ImageView gv_icon = (ImageView) view
					.findViewById(R.id.iv_item_main_gv_icon);
			TextView gv_name = (TextView) view
					.findViewById(R.id.tv_item_main_gv_name);
			// 设置图片和名称
			gv_icon.setImageResource(icons[position]);
			gv_name.setText(names[position]);
			return view;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	/**
	 * //初始化界面的组件
	 */
	private void initview() {
		setContentView(R.layout.activity_main);
		gv_menu = (GridView) findViewById(R.id.gv_main_menu);
	}
}
