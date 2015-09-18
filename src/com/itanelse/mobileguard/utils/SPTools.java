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
		sp.edit().putString(key, value).commit();// 保存数据
	}

	public static String getString(Context context, String key, String defValue) {
		SharedPreferences sp = context.getSharedPreferences(MyConstants.SPFILE,
				Context.MODE_PRIVATE);
		return sp.getString(key, defValue);// 获取数据
	}
}
