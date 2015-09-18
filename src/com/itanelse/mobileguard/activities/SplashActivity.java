package com.itanelse.mobileguard.activities;

import java.io.BufferedReader;
import java.io.File;
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
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itanelse.mobileguard.R;
import com.itanelse.mobileguard.domain.VersionBean;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class SplashActivity extends Activity {

	private static final int LOAD_MAIN = 1;// 进入主界面
	private static final int SHOWUPDATADIALOG = 2;// 显示是否进行更新的对话框
	protected static final int ERROR = 3;//
	private RelativeLayout rl_splash_root;// splash界面根布局
	private TextView tv_splash_versionName;// 当前的版本名
	private int versionCode;// 当前的版本号
	private String versionName;// 当前的版本名
	private VersionBean versionbean;// 版本信息封装的bean类
	private long startTime;// 开始访问网络的时间
	private long endTime;// 访问网络结束的时间
	private ProgressBar pb_download;

	private Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 处理消息
			switch (msg.what) {
			case LOAD_MAIN:// 加载主界面
				loadMain();
				break;
			case ERROR:// 有异常
				switch (msg.arg1) {
				case 404: // 资源找不到
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
				showUpdateDailog();//alt+left:返回该方法的调用者
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
		// 禁用点击返回取消键,不过不提倡
		// builder.setCancelable(false);
		// 定义一个取消事件(处理用户点击返回键来取消对话框)
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// 点击取消更新,进入主界面
				loadMain();
			}
		}).setTitle("提醒")
				.setMessage("是否要更新新版本,新版本有如下特征:" + versionbean.getDesc())
				.setPositiveButton("更新", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 进行下载更新,使用xutils
						downloadNewApk();
					}
				}).setNegativeButton("取消更新", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 取消更新,直接进入主界面
						loadMain();
					}
				});
		builder.show();// 显示对话框
	}

	/**
	 * 下载apk,使用xutils
	 */
	protected void downloadNewApk() {
		HttpUtils utils = new HttpUtils();
		// url:下载的apk路径
		// target:本地的路径
		// callback:回调,返回下载的文件
		// 下载之前先把就版本卸载
		File file = new File("mnt/sdcard/MobileGuard.apk");
		file.delete();
		utils.download(versionbean.getUrl(), "mnt/sdcard/MobileGuard.apk",
				new RequestCallBack<File>() {

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						pb_download.setVisibility(View.VISIBLE);// 设置进度条的显示
						pb_download.setMax((int) total);// 设置进度条的最大值
						pb_download.setProgress((int) current);// 设置当前进度
						super.onLoading(total, current, isUploading);
					}

					@Override
					public void onSuccess(ResponseInfo<File> arg0) {
						// 下载成功,安装apk
						// 在主线程中执行的,以及封装好了,所以不用handler
						Toast.makeText(getApplicationContext(), "下载成功", 0)
								.show();
						installApk();
						// 不管下载是否成功,都要隐藏进度条
						pb_download.setVisibility(View.GONE);
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// 下载失败
						System.out.println("下载失败" + arg0);
						Toast.makeText(getApplicationContext(), "下载失败" + arg0,
								0).show();
						// 不管下载成功还是失败,都要隐藏进度条
						pb_download.setVisibility(View.GONE);
						loadMain();
					}
				});
	}

	/**
	 * 安装apk
	 */
	protected void installApk() {
		/*
		 * <activity android:name=".PackageInstallerActivity"
		 * android:configChanges="orientation|keyboardHidden"
		 * android:theme="@style/TallTitleBarTheme"> <intent-filter> <action
		 * android:name="android.intent.action.VIEW" /> <category
		 * android:name="android.intent.category.DEFAULT" /> <data
		 * android:scheme="content" /> <data android:scheme="file" /> <data
		 * android:mimeType="application/vnd.android.package-archive" />
		 */
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		Uri data = Uri.fromFile(new File("mnt/sdcard/MobileGuard.apk"));
		String type = "application/vnd.android.package-archive";
		intent.setDataAndType(data, type);
		// startActivity(intent);
		// 用startActivityForResult启动更新界面的APK,并复写onActivityResult方法,防止用户下载完成，取消更新
		startActivityForResult(intent, 0);
		tv_splash_versionName.setText(versionName);// 下载完后设置版本名
	}

	// 再覆盖
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// 如果用户取消更新apk,那么直接进入主界面
		loadMain();
		super.onActivityResult(requestCode, resultCode, data);
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
			int errorCode = -1; // 表示正常,没有错误
			HttpURLConnection conn;
			BufferedReader bufr;

			public void run() {
				// 开始访问网络的时间
				startTime = System.currentTimeMillis();
				try {
					String path = "http://192.168.1.101:8080/MobileSafeGuard.json";
					URL url = new URL(path);
					conn = (HttpURLConnection) url.openConnection();
					// 设置连接的超时时间和读取数据的超时时间
					conn.setReadTimeout(5000);
					conn.setConnectTimeout(5000);
					// 设置请求方式
					conn.setRequestMethod("GET");
					// 获得返回码
					int code = conn.getResponseCode();// 500,404,200
					if (code == 200) {
						// 请求成功,获得服务器返回的字节流数据
						InputStream is = conn.getInputStream();
						bufr = new BufferedReader(new InputStreamReader(is));
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
		String newVersionName = jobject.getString("versionName");// 得到新版本的名字
		// 将获取到的参数添加到VersionBean中
		versionbean.setVersion(version);
		versionbean.setUrl(url);
		versionbean.setDesc(desc);
		versionbean.setVersionName(newVersionName);
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

			// 设置textView
			// tv_splash_versionName.setText(versionName);
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
		tv_splash_versionName = (TextView) findViewById(R.id.tv_splash_versionname);
		pb_download = (ProgressBar) findViewById(R.id.pb_splash_download_progress);
	}
}
