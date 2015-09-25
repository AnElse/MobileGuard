package com.itanelse.mobileguard.utils;

import android.util.Log;

/**
 * 
 * 自定义的日志工具,只需要修改LEVEL的值即可以自由控制日志的打印.
 * 比如让LEVEL = VERBOSE(1)就可以打印所有的日志,让LEVEL = WARN(4)
 * 就可以打印警告以上的级别的日志,让LEVEL = NOTHING就可以把所有的
 * 日志屏蔽掉.
 * 开发中,把LEVEL改为VERBOSE,项目上线改为NOTHING即可.
 * @author 
 */

public class LogUtils {

	public static final int VERBOSE = 1;
	public static final int DEBUG = 2;
	public static final int INFO = 3;
	public static final int WARN = 4;
	public static final int ERROR = 5;
	public static final int NOTHING = 6;
	public static final int LEVEL = VERBOSE;

	public static void v(String tag, String msg) {
		if (LEVEL <= VERBOSE) {
			Log.v(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (LEVEL <= DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (LEVEL <= INFO) {
			Log.i(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (LEVEL <= WARN) {
			Log.w(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (LEVEL <= ERROR) {
			Log.e(tag, msg);
		}
	}

}
