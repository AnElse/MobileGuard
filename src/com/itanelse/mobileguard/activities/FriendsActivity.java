package com.itanelse.mobileguard.activities;

import java.util.List;

import com.itanelse.mobileguard.domain.ContactInfo;
import com.itanelse.mobileguard.engine.ReadContantsEngine;

/**
 * 当页面中只有listView一个组件时,可以继承ListActivity 它里面封装了一个组件组成的listView 显示所有好友信息的界面
 * 
 * @author 毓添
 * 
 */
public class FriendsActivity extends BaseFriendsCallSmsActivity {

	/*
	 * 提取数据的方法,需要覆盖此方法完成数据的显示
	 * com.itheima62.mobileguard.activities.BaseFriendsCallSmsActivity#getDatas
	 */
	@Override
	public List<ContactInfo> getDatas() {
		// TODO Auto-generated method stub
		return ReadContantsEngine.readContants(getApplicationContext());
	}

}
