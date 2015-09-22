package com.itanelse.mobileguard.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsMessage;

public class LostFindService extends Service {
	private SmsReceiver receiver;// 短信广播接收者

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private class SmsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 实现短信的拦截功能
			// System.out.println("短信到来了...");
			Object[] objs = (Object[]) intent.getExtras().get("pdus");// pdu
																		// 短信二进制数据报文
			for (Object obj : objs) {
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
				String sender = smsMessage.getOriginatingAddress();
				String mesbody = smsMessage.getMessageBody();
				// System.out.println("sender:" + sender);// 获取到短信的发送者
				// System.out.println("body:" + body);// 获取到短信的内
				if ("#*gps*#".equals(mesbody)) {
					// 获取当前的手机所在的位置,耗时的定位,把定位的功能放到服务中执行,在服务中进行耗时操作,并不是说服务中
					// 可以进行耗时操作,服务还是运行在主线程中的,所以在进行耗时操作时,还是写在子线程中
					Intent locationservice = new Intent(context,
							LocationService.class);
					startService(locationservice);// 启动定位的服务
					abortBroadcast();// 接收到后停止广播,因为很耗电
				}
			}
		}
	}

	/**
	 * 创建服务
	 */
	@Override
	public void onCreate() {
		// System.out.println("服务创建了");
		receiver = new SmsReceiver();// 买一个收音机
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");// 调频道
		filter.setPriority(Integer.MAX_VALUE);// 级别一样，清单文件，谁先注册谁先执行，如果级别一样，代码比清单要高
		// 注册短信监听
		this.registerReceiver(receiver, filter);
		super.onCreate();
	}

	/**
	 * 服务销毁
	 */
	@Override
	public void onDestroy() {
		this.unregisterReceiver(receiver);// 记得取消注册,当服务销毁时.否则会发生leak
		super.onDestroy();
	}

}
