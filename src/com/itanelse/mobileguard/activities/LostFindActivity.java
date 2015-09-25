package com.itanelse.mobileguard.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itanelse.mobileguard.R;
import com.itanelse.mobileguard.utils.EncryptUtils;
import com.itanelse.mobileguard.utils.MyConstants;
import com.itanelse.mobileguard.utils.SPTools;

public class LostFindActivity extends Activity {

	private TextView tv__safenumber;
	AlertDialog dialog;// 修改防盗功能名称的对话框
	private LinearLayout ll_lostfind_menu;// 自定义菜单
	private boolean flag = false;// 是否显示自定义菜单
	private Button rename;// 自定义菜单中重新设置名字按钮
	View contentView;// 弹出窗体的view
	PopupWindow pw;// 弹出窗体
	private ScaleAnimation sa;//弹出窗体显示的动画
	private RelativeLayout rl_root;//作为弹出传窗体显示的根布局

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 如果是第一次访问该界面,要先进入设置向导界面
		if (SPTools.getBoolean(getApplicationContext(), MyConstants.ISSET,
				false)) {
			// 返回true,说明已经进入过设置向导界面,那么直接显示防盗界面
			initView();// 初始化手机防盗界面
			// initData();// 初始化数据
			initPopupView();// 初始化修改名的界面
			initPopupWindow();
		} else {
			// 返回false,说明是首次进入防盗界面,那么先进入设置向导界面
			Intent intent = new Intent(LostFindActivity.this,
					Setup1Activity.class);
			startActivity(intent);
			finish();
		}
	}

	/**
	 * 初始化弹出窗体
	 */
	private void initPopupWindow() {

		// 第一个参数contentView,弹出窗体显示的内容
		// width,-2表示wrapcontent
		pw = new PopupWindow(contentView, -2, -2);
		pw.setFocusable(true);// 获取焦点
		sa = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0f);
		sa.setDuration(1000);
	}

	private void initPopupView() {
		contentView = View.inflate(getApplicationContext(),
				R.layout.menu_modify_name, null);

		// 处理界面和事件
		final EditText et_name = (EditText) contentView
				.findViewById(R.id.et_menu_name);
		Button bt_modify = (Button) contentView
				.findViewById(R.id.bt_lostfind_munu_modify);
		Button bt_cancel = (Button) contentView
				.findViewById(R.id.bt_lostfind_menu_cancel);

		// 处理按钮事件
		bt_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pw.dismiss();
			}
		});
		bt_modify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取修改的名字
				String name = et_name.getText().toString().trim();
				if (TextUtils.isEmpty(name)) {
					Toast.makeText(getApplicationContext(), "名字不能为空", 1).show();
					return;
				}

				// 保存新名字到sp中
				SPTools.putString(getApplicationContext(), MyConstants.NAME,
						name);
				pw.dismiss();
				Toast.makeText(getApplicationContext(), "名字修改成功", 1).show();
			}
		});
	}

	/**
	 * 点击重新进入设置向导界面
	 * 
	 * @param view
	 */
	public void entersetup1(View view) {
		Intent intent = new Intent(this, Setup1Activity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 初始化界面组件
	 */
	private void initView() {
		setContentView(R.layout.activity_lostfind);
		rl_root = (RelativeLayout) findViewById(R.id.rl_root);
	}

	/**
	 * 初始化数据:安全号码
	 */
	private void initData() {
		String safenumber = SPTools.getString(getApplicationContext(),
				MyConstants.SAFENUMBER, "");

		tv__safenumber.setText("安全号码:"
				+ EncryptUtils.decryption(MyConstants.SEED, safenumber));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 用自定义的菜单来显示,所以注释掉这个,编写自己的自定义菜单
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * 检查自己的手机按钮
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// 如果点击的是菜单按钮
		// if (keyCode == KeyEvent.KEYCODE_MENU) {
		// LogUtils.v("LostFindActivity", "menu被点击了");
		// ll_lostfind_menu = (LinearLayout)
		// findViewById(R.id.ll_lostfind_menu);
		// rename = (Button) findViewById(R.id.bt_lostfind_rename);
		// if (flag) {
		// ll_lostfind_menu.setVisibility(View.GONE);
		// } else {
		// ll_lostfind_menu.setVisibility(View.VISIBLE);
		// }
		// flag = !flag;
		// }
		
		//使用弹出窗体来作为菜单
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (pw.isShowing() && pw != null) {
				pw.dismiss();
			}else{
				//注意:弹出窗体设置动画需要背景资源才可以显示
				pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				contentView.startAnimation(sa);
				// 设置弹出窗体显示的位置
				int height = getWindowManager().getDefaultDisplay().getHeight();
				int width = getWindowManager().getDefaultDisplay().getWidth();
				/**
				 * 父组件
				 * 对齐方式
				 * x坐标
				 * Y坐标
				 */
				pw.showAtLocation(rl_root, Gravity.LEFT|Gravity.BOTTOM, height/4, width/4);
			}
		}
		
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 当菜单条目被选中时
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_xiugaimingcheng:
			// Toast.makeText(getApplicationContext(), "修改防盗功能名称", 1).show();
			showDialogChangeName();
			break;
		case R.id.memu_me:
			Toast.makeText(getApplicationContext(), "AnElse", 1).show();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 修改防盗功能的名称
	 */
	private void showDialogChangeName() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = View.inflate(getApplicationContext(),
				R.layout.menu_changename_lostfind, null);
		final EditText et_name = (EditText) view
				.findViewById(R.id.et_menu_name);
		Button changename = (Button) view.findViewById(R.id.bt_menu_change);
		Button cancel = (Button) view.findViewById(R.id.bt_menu_cancel);
		builder.setView(view);
		dialog = builder.create();
		dialog.show();
		changename.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(et_name.getText().toString().trim())) {
					// 保存修改的名称
					SPTools.putString(getApplicationContext(),
							MyConstants.NAME, et_name.getText().toString()
									.trim());
					dialog.dismiss();
				} else {
					Toast.makeText(getApplicationContext(), "名称不能为空", 0).show();
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
}
