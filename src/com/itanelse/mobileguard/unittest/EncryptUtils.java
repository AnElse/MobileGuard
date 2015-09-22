package com.itanelse.mobileguard.unittest;

/**
 * 加密解密算法:利用的是一个数按位与或自己两次,得到的还是自己
 * @author 毓添
 */
public class EncryptUtils {
	/**
	 * 加密算法
	 * @param seed 加密的种子
 	 * @param str 加密的字符串
 	 * @return String 加密后的密文
	 */
	public static String encrption(int seed, String str){
		byte[] bytes = str.getBytes();
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] ^= seed;//bytes[i] = (byte)(bytes[i] ^ seed);
		}
		return new String(bytes);
	}
	
	/**
	 * 解密算法
	 * @param seed 解密的种子(与加密的种子一样)
	 * @param str 要进行解密的字符串
	 * @return 解密后的数字本身
	 */
	public static String  decryption(int seed, String str){
		byte[] bytes = str.getBytes();
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] ^= seed;//bytes[i] = (byte)(bytes[i] ^ seed);
		}
		return new String(bytes);
	}
}
