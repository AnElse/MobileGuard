package com.itanelse.mobileguard.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.itanelse.mobileguard.domain.ContactInfo;

/**
 * 获取手机联系人的信息的工具类(如果不做成工具类,那么另外建一个engine包(业务包),来写读取联系人的业务逻辑:readContactsEngine)
 * 
 * @author 毓添
 * @return List<ContactInfo> 返回一个封装了联系人信息的集合.
 */
public class ContactInfoUtils {
	/**
	 * 获取系统里面的全部的联系人信息
	 * 
	 * @param context
	 *            上下文
	 * @return
	 */
	public static List<ContactInfo> getAllContactInfos(Context context) {
		// 1. 查询raw_contacts表, 把每个联系人的contact_id.
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Cursor cursor = resolver.query(uri, new String[] { "contact_id" },
				null, null, null);
		// 创建一个集合,存放所有的联系人信息
		List<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
		while (cursor.moveToNext()) {
			String id = cursor.getString(0);
			System.out.println("联系人id:" + id);
			if (id != null) {
				ContactInfo contactInfo = new ContactInfo();
				// 2. 根据contact_id 查询data表,把联系人的数据取出来.
				Uri dataUri = Uri.parse("content://com.android.contacts/data");
				Cursor dataCursor = resolver.query(dataUri, new String[] {
						"data1", "mimetype" }, "raw_contact_id=?",
						new String[] { id }, null);
				while (dataCursor.moveToNext()) {
					String data1 = dataCursor.getString(0);
					String mimetype = dataCursor.getString(1);
					if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
						contactInfo.setPhone(data1);
					} else if ("vnd.android.cursor.item/email_v2"
							.equals(mimetype)) {
						contactInfo.setEmail(data1);
					} else if ("vnd.android.cursor.item/name".equals(mimetype)) {
						contactInfo.setName(data1);
					} else if ("vnd.android.cursor.item/im".equals(mimetype)) {
						contactInfo.setQq(data1);
					}
				}
				contactInfos.add(contactInfo);
				dataCursor.close();
			}
		}
		cursor.close();
		return contactInfos;
	}
}
