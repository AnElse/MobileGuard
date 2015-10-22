package com.itanelse.mobileguard.unittest;

import java.util.List;

import com.itanelse.mobileguard.dao.BlackDao;
import com.itanelse.mobileguard.domain.BlackBean;
import com.itanelse.mobileguard.domain.BlackTable;

import android.test.AndroidTestCase;

public class TestBlackDB extends AndroidTestCase {
	public void testAddBlackNumber() {
		BlackDao dao = new BlackDao(getContext());
		for (int i = 0; i < 20; i++) {
			dao.add("1234567" + i, BlackTable.SMS);
		}
	}

	public void testDelete() {
		BlackDao dao = new BlackDao(getContext());
		dao.delete("12345670");
	}

	public void testUpdate() {
		BlackDao dao = new BlackDao(getContext());
		dao.update("12345670", BlackTable.ALL);
	}

	public void testFindAllBlackDatas() {
		BlackDao dao = new BlackDao(getContext());
		// 获取所有黑名单数据
		List<BlackBean> datas = dao.getAllDatas();
		System.out.println(datas);
	}
}
