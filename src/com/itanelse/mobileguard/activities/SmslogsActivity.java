package com.itanelse.mobileguard.activities;

import java.util.List;

import com.itanelse.mobileguard.domain.ContactInfo;
import com.itanelse.mobileguard.engine.ReadContantsEngine;

public class SmslogsActivity extends BaseFriendsCallSmsActivity {

	@Override
	public List<ContactInfo> getDatas() {
		return ReadContantsEngine.readSmslog(getApplicationContext());
	}

}
