package com.itanelse.mobileguard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import com.itanelse.mobileguard.service.LostFindService;
import com.itanelse.mobileguard.unittest.EncryptUtils;
import com.itanelse.mobileguard.utils.MyConstants;
import com.itanelse.mobileguard.utils.SPTools;

/**
 * 开机的广播接收者
 * 
 * @author 毓添
 * 
 */
public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// 保存的sim卡信息
		// 手机启动完成，检测SIM卡是否变化
		// 取出原来保存的sim卡信息
		System.out.println("开机完成了.......");
		String oldsim = SPTools.getString(context, MyConstants.SIM, "");
		// 取出现在sim卡信息
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String simSerialNumber = tm.getSimSerialNumber();
		// 进行对比,如果发生变化,那么就发出报警短信,加1只为了模拟sim卡不一样,因为再模拟器中是一样的
		if (!oldsim.equals(simSerialNumber)) {
			// 说明不一样
			SmsManager sms = SmsManager.getDefault();
			// 安全号码肯定是有的,因为只有绑定安全号码才能进行防盗界面
			String encrytsafenumber = SPTools.getString(context,
					MyConstants.SAFENUMBER, "");
			// 对安全号码进行解密
			String safenumber = EncryptUtils.decryption(MyConstants.SEED,
					encrytsafenumber);
			sms.sendTextMessage(safenumber, "",
					"wo shi xiao tou,zhe shi wo xin hao", null, null);
		}

		// 开机启动防盗保护
		if (SPTools.getBoolean(context, MyConstants.LOSTFIND, false)) {
			// 返回true,说明已经设置了防盗保护,那么直接启动防盗保护
			Intent lostFindService = new Intent(context, LostFindService.class);
			context.startService(lostFindService);
		}
	}
}
