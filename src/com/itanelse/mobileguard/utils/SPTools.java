package com.itanelse.mobileguard.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 保存数据的工具类 SharedPreferences
 * 
 * @author 毓添
 * @return String 保存的密码
 */
public class SPTools {
	public static void putString(Context context, String key, String value) {
		SharedPreferences sp = context.getSharedPreferences(MyConstants.SPFILE,
				Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();// 保存数据(进入防盗界面的密码)
	}

	public static String getString(Context context, String key, String defValue) {
		SharedPreferences sp = context.getSharedPreferences(MyConstants.SPFILE,
				Context.MODE_PRIVATE);
		return sp.getString(key, defValue);// 获取数据(进入防盗界面的密码)
	}

	public static void putBoolean(Context context, String key, boolean value) {
		SharedPreferences sp = context.getSharedPreferences(MyConstants.SPFILE,
				Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();// 保存是否设置了过向导界面
	}

	/**
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * @return 是否设置过向导界面.
	 * 注意:在jdk1.5之后,基本数据类型和封装类之间可以互换使用.所以,写Boolean defValue也是可以的
	 */
	public static Boolean getBoolean(Context context, String key,
			Boolean defValue) {
		SharedPreferences sp = context.getSharedPreferences(MyConstants.SPFILE,
				Context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);// 保存是否设置了过向导界面
	}
}
