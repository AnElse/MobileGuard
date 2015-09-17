package com.itanelse.mobileguard.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itanelse.mobileguard.R;
import com.itanelse.mobileguard.domain.VersionBean;

public class SplashActivity extends Activity {

	private static final int LOAD_MAIN = 1;// 进入主界面
	private static final int SHOWUPDATADIALOG = 2;// 显示是否进行更新的对话框
	protected static final int ERROR = 3;//
	private RelativeLayout rl_splash_root;// splash界面根布局
	private TextView tv_splash_versionname;// 当前的版本名
	private int versionCode;// 当前的版本号
	private String versionName;// 当前的版本名
	private VersionBean versionbean;// 版本信息封装的bean类
	private long startTime;//开始访问网络的时间
	private long endTime;//访问网络结束的时间

	private Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 处理消息
			switch (msg.what) {
			case LOAD_MAIN:// 加载主界面
				loadMain();
				break;
			case ERROR:// 有异常
				switch (msg.arg1) {
				case 404:	// 资源找不到
					Toast.makeText(getApplicationContext(), "404资源找不到", 0)
							.show();
					break;
				case 4001:// 找不到网络
					Toast.makeText(getApplicationContext(), "4001没有网络", 0)
							.show();
					break;
				case 4003:// json格式错误
					Toast.makeText(getApplicationContext(), "4003json格式错误", 0)
							.show();
					break;
				default:
					break;
				}
				loadMain();// 进入主界面
				break;
			case SHOWUPDATADIALOG:// 显示更新版本的对话框
				showUpdateDailog();
				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();// 初始化界面中的组件
		initAnimation();// 初始化动画
		initData();// 初始化数据
		checkVersion();// 检查版本
	}
	
	/**
	 * 是否下载新版本的更新下载对话框
	 */
	protected void showUpdateDailog() {
		AlertDialog.Builder builder = new Builder(SplashActivity.this);
		builder.setTitle("提醒")
		.setMessage("是否要更新新版本,新版本有如下特征:"+versionbean.getDesc())
		.setPositiveButton("是", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 进行下载更新,使用xutils
				downloadNewApk();
			}
		}).setNegativeButton("否", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 取消更新,直接进入主界面
				loadMain();
			}
		});
		builder.show();// 显示对话框
	}
	
	/**
	 * 下载apk
	 */
	protected void downloadNewApk() {
		
	}

	/**
	 * 进入主界面
	 */
	protected void loadMain() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(intent);
		finish();// 进入后关闭自己
	}

	/**
	 * 在子线程中完成访问服务器,获取新版本
	 */
	private void checkVersion() {
		new Thread() {
			int errorCode = -1;  // 表示正常,没有错误
			HttpURLConnection conn;
			BufferedReader bufr;
			public void run() {
				// 开始访问网络的时间
				startTime = System.currentTimeMillis();
				try {
					String path = "http://192.168.1.101:8080/MobileSafeGuard.json";
					URL url = new URL(path);
					conn = (HttpURLConnection) url
							.openConnection();
					// 设置连接的超时时间和读取数据的超时时间
					conn.setReadTimeout(5000);
					conn.setConnectTimeout(8000);
					// 设置请求方式
					conn.setRequestMethod("GET");
					// 获得返回码
					int code = conn.getResponseCode();// 500,404,200
					if (code == 200) {
						// 请求成功,获得服务器返回的字节流数据
						InputStream is = conn.getInputStream();
						bufr = new BufferedReader(
								new InputStreamReader(is));
						String line = null;
						StringBuilder json = new StringBuilder();
						while ((line = bufr.readLine()) != null) {
							json.append(line);
						}
						// 解析json数据
						parserJsonWithGson(json.toString());
						conn.disconnect();// 断开连接
						bufr.close();// 关闭流
					} else {
						errorCode = 404;// 资源找不到
					}

				} catch (IOException e) { // 连接网络异常 4001
					e.printStackTrace();
					errorCode = 4001;
				} catch (JSONException e) {// 读取json数据异常 4003
					e.printStackTrace();
					errorCode = 4003;
				} finally {
					Message msg = Message.obtain();
					if (errorCode == -1) {
						// 检测是否有新版本
						int version = isNewVersion(versionbean);
						msg.what = version;// LOAD_MAIN或者SHOWUPDATADIALOG
					} else {
						msg.what = ERROR;
						msg.arg1 = errorCode;
					}
					endTime = System.currentTimeMillis();
					if (endTime - startTime < 3000) {
						SystemClock.sleep(3000 - (endTime - startTime));// 时间不超过3秒，补足3秒
					}
					mhandler.sendMessage(msg);
					if (conn == null || bufr == null) {
						return;
					}
					try {
						conn.disconnect();// 断开连接
						bufr.close();// 关闭流
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	protected int isNewVersion(VersionBean versionbean) {
		if (versionbean.getVersion() == versionCode) {
			// 版本一致,直接进入主界面
			// 由于在子线程,所以不可以直接进入主界面,需要在主线程中完成进入新的界面,所以要用到Handler
			return LOAD_MAIN;
		} else {// 有新版本
				// 不一致,弹出新版本的描述信息,让用户点击是否下载新版本
			return SHOWUPDATADIALOG;
		}
	}

	/**
	 * 解析json数据
	 * 
	 * @param string
	 * @throws JSONException
	 */
	protected void parserJsonWithGson(String json) throws JSONException {
		versionbean = new VersionBean();
		JSONObject jobject = new JSONObject(json);
		int version = jobject.getInt("version");// 访问网络得到的版本号
		String url = jobject.getString("url");// 访问网络得到的路径
		String desc = jobject.getString("desc");// 访问网络得到的新版本
		// 将获取到的参数添加到VersionBean中
		versionbean.setVersion(version);
		versionbean.setUrl(url);
		versionbean.setDesc(desc);
	}

	/**
	 * 初始化数据,获取当前的版本号
	 */
	private void initData() {
		try {
			PackageManager packageManager = getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			versionCode = packageInfo.versionCode;// 当前的版本号
			versionName = packageInfo.versionName;// 当前的版本名
		} catch (NameNotFoundException e) {
			// can not reach 因为我们通过getPackageName()获取包名
		}
	}

	/**
	 * 初始化动画
	 */
	private void initAnimation() {
		/**
		 * 渐变动画
		 */
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		aa.setDuration(3000);
		aa.setFillAfter(true);

		/**
		 * 旋转动画
		 */
		RotateAnimation ra = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setDuration(3000);
		ra.setFillAfter(true);

		/**
		 * 比例动画
		 */
		ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		sa.setDuration(3000);
		sa.setFillAfter(true);

		/**
		 * 动画集
		 * 
		 * @shareInterpolator true
		 */
		AnimationSet as = new AnimationSet(true);
		as.addAnimation(aa);// 添加渐变动画
		as.addAnimation(ra);// 添加旋转动画
		as.addAnimation(sa);// 添加比例动画

		rl_splash_root.startAnimation(as);// 三种动画效果一起显示
	}

	/**
	 * 初始化界面(组件)
	 */
	private void initView() {
		setContentView(R.layout.activity_splash);
		rl_splash_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
		tv_splash_versionname = (TextView) findViewById(R.id.tv_splash_versionname);
	}
}
