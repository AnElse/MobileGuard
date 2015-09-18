package com.itanelse.mobileguard.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 使用MD5把防盗界面的密码进行加密
 * 
 * @author 毓添
 * @return String 返回加密后的十六进制密文
 */
public class Md5Utils {
	public static String md5(String str) {
		StringBuilder mess = new StringBuilder();
		try {
			// 获取MD5加密器对象
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = str.getBytes();
			byte[] digest = md.digest(bytes);

			for (byte b : digest) {
				// 把每个字节转成16进制数
				int d = b & 0xff;// 0x000000ff
				String hexString = Integer.toHexString(d);
				if (hexString.length() == 1) {// 字节的高4位为0
					hexString = "0" + hexString;
				}
				mess.append(hexString);// 把每个字节对应的2位十六进制数当成字符串拼接一起

			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return mess + "";
	}
}
