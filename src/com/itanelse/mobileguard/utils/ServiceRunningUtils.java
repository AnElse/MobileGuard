package com.itanelse.mobileguard.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * 判断服务是否开启的工具类
 * 
 * @author 毓添
 * 
 */
public class ServiceRunningUtils {
	/**
	 * @param context
	 * @param serviceName
	 *            service完整的名字 包名+类名
	 * @return 该servcie是否在运行
	 */
	public static Boolean ServiceIsRunning(Context context, String serviceName) {
		boolean isRunning = false;
		// 判断运行中的服务状态，ActivityManager
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 获取android手机中运行的所有服务
		List<RunningServiceInfo> runningServices = am.getRunningServices(50);
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			if (runningServiceInfo.service.getClassName().equals(serviceName)) {
				// 名字一样，该服务在运行中
				isRunning = true;
				// System.out.println("返回true");
				// System.out.println(serviceName);
				break;// 已经找到 退出循环
			}
		}
		return isRunning;
	}
}
