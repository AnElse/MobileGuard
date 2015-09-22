package com.itanelse.mobileguard.service;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;

import com.itanelse.mobileguard.unittest.EncryptUtils;
import com.itanelse.mobileguard.utils.MyConstants;
import com.itanelse.mobileguard.utils.SPTools;

public class LocationService extends Service {
	LocationManager lm;// 位置服务
	LocationListener listener;// 位置变化的监听回调

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 服务被创建时调用的方法
	 */
	@Override
	public void onCreate() {
		final StringBuffer sb_location = new StringBuffer();
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		listener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			/**
			 * 该方法表示位置发生变化
			 */
			@Override
			public void onLocationChanged(Location location) {
				// 获取位置变化的结果
				float accuracy = location.getAccuracy();// 精确度,以米为单位
				double altitude = location.getAltitude();// 获取海拔高度
				double longitude = location.getLongitude();// 获取经度
				double latitude = location.getLatitude();// 获取纬度
				float speed = location.getSpeed();//获取速度
				sb_location.append("accuracy"+accuracy);
				sb_location.append("altitude"+altitude);
				sb_location.append("latitude"+latitude);
				sb_location.append("longitude"+longitude);
				sb_location.append("speed"+speed);

				// 发送该位置的信息到安全号码
				String encrytSafenumber = SPTools.getString(
						getApplicationContext(), MyConstants.SAFENUMBER, "");
				// 对密文进行解密
				String safenumber = EncryptUtils.decryption(MyConstants.SEED,
						encrytSafenumber);
				SmsManager sms = SmsManager.getDefault();
				sms.sendTextMessage(safenumber, "", sb_location + "", null,
						null);
				// 关闭gps
				stopSelf();// 关闭自己,会调用onDestory()方法
			}
		};

		// 获取所有的提供的定位方式
		List<String> allProviders = lm.getAllProviders();
		for (String string : allProviders) {
			System.out.println(string + "定位方式");
		}

		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);// 产生费用
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 动态获取手机的最佳定位方式
		String bestProvider = lm.getBestProvider(criteria, true);
		// 注册监听回调
		/**
		 * provider 定位的方式 "gps:卫星" "基站定位:3g 4g" "wifi:通过绑定ip" minTime
		 * 定位的时间差 10分钟 (0,0) minDistance 定位的距离差 10m
		 * (0,0)两个都写0表示自动自能智能检测到位置的变化 listener 定位的监听回调
		 */
		lm.requestLocationUpdates("gps", 0, 0, listener);

		super.onCreate();
	}

	/**
	 * 服务被启动时调用的方法,如果我们希望服务一被启动就去执行某个动作,那么可以将逻辑写在该方法中
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 当服务销毁时,可以在onDestroy()中回收那些不再使用的资源
	 */
	@Override
	public void onDestroy() {
		// 取消定位的监听
		lm.removeUpdates(listener);
		lm = null;
		super.onDestroy();
	}

}
