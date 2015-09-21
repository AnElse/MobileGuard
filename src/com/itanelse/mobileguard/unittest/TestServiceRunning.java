package com.itanelse.mobileguard.unittest;

import com.itanelse.mobileguard.utils.ServiceRunningUtils;

import android.test.AndroidTestCase;

public class TestServiceRunning extends AndroidTestCase {
	public void isRunningService(){
		ServiceRunningUtils.ServiceIsRunning(getContext(), "com.itanelse.mobileguard.service.LostFindService");
	}
}
