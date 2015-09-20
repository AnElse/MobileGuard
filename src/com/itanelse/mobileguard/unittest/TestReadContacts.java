package com.itanelse.mobileguard.unittest;

import android.test.AndroidTestCase;

import com.itanelse.mobileguard.utils.ContactInfoUtils;

public class TestReadContacts extends AndroidTestCase {
	public void testReadContacts(){
		ContactInfoUtils.getAllContactInfos(getContext());
	}
}
