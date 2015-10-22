package com.itanelse.mobileguard.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.itanelse.mobileguard.dao.BlackDao;
import com.itanelse.mobileguard.domain.BlackTable;

/**
 * @author Administrator 监听电话和短信
 */
public class TelSmsBlackService extends Service {

	private SmsReceiver receiver;
	private BlackDao dao;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @author Administrator 短信监听广播
	 */
	private class SmsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Object[] datas = (Object[]) intent.getExtras().get("pdus");
			for (Object sms : datas) {
				// 获取短信的数据
				SmsMessage sm = SmsMessage.createFromPdu((byte[]) sms);

				// 取短信的发件人号码
				String address = sm.getOriginatingAddress();

				// 判断是否存在黑名单中
				int mode = dao.getMode(address);

				// 短信拦截
				if ((mode & BlackTable.SMS) != 0) {
					// 具有短信拦截
					// if (mode == 短信 || mode == 全部)
					// 拦截此短信
					abortBroadcast();// 终止广播传递
				}
			}
		}

	}

	@Override
	public void onCreate() {

		// 初始化黑名单的业务类
		dao = new BlackDao(getApplicationContext());

		// 注册短信监听
		// 注册短信的广播
		receiver = new SmsReceiver();
		// 短信广播意图
		IntentFilter filter = new IntentFilter(
				"android.provider.Telephony.SMS_RECEIVED");
		// 设置拦截模式为最高
		filter.setPriority(Integer.MAX_VALUE);
		// 注册短信广播
		registerReceiver(receiver, filter);

		// 注册电话监听
		super.onCreate();

		// 注册电话监听
		// 通过电话管理器类TelephonyManager
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		PhoneStateListener listener = new PhoneStateListener() {

			/**
			 * 该方法用来监听电话的状态
			 */
			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				// TODO Auto-generated method stub
				// state 电话的状态 incomingNumber 打进来的号码
				switch (state) {
				case TelephonyManager.CALL_STATE_IDLE:// 挂断的状态，空闲的状态
					System.out.println("CALL_STATE_IDLE");
					break;
				case TelephonyManager.CALL_STATE_RINGING:// 响铃状态
					System.out.println("CALL_STATE_RINGING");
					// 判断是否是黑名单号码
					// 获取电话的模式
					int mode = dao.getMode(incomingNumber);

					// 如果是拦截号码
					if ((mode & BlackTable.TEL) != 0) {
						// 电话拦截
						System.out.println("挂断电话");
						endCall();
					}
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK:// 通话的状态
					System.out.println("CALL_STATE_OFFHOOK");
					break;

				default:
					break;
				}
				super.onCallStateChanged(state, incomingNumber);
			}

		};

		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	/**
	 * 通过反射和aidl挂断电话
	 */
	protected void endCall() {
		// tm.endCall(); 1.5版本后，把该方法阉割掉了
		// 想用该功能，实现方法
		// ServiceManager.getService();
		// 反射调用
		// 1.class
		try {
			Class clazz = Class.forName("android.os.ServiceManager");
			// 2. method
			Method method = clazz.getDeclaredMethod("getService", String.class);

			// 3.obj 不需要 静态方法
			// 4. 调用
			IBinder binder = (IBinder) method.invoke(null,
					Context.TELEPHONY_SERVICE);

			// 5.aidl
			ITelephony iTelephony = ITelephony.Stub.asInterface(binder);
			iTelephony.endCall();// 挂断电话
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {

		// 取消电话监听

		// 取消短信监听
		unregisterReceiver(receiver);
		super.onDestroy();
	}
}