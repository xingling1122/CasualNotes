package com.xl.projectno.utils;


import java.io.Serializable;
import java.security.MessageDigest;

/**
 * 
 * <br>类描述:使用MD5对字符串处理
 * <br>功能详细描述:
 *
 */
public class MD5 implements Serializable {

	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = 1L;

	public final static String MD5generator(String s) {
		return MD5.to32BitString(s, true, "");
	}

	private static String to32BitString(String plainText, boolean is32, String charset) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (StringUtils.hasText(charset))
				md.update(plainText.getBytes(charset));
			else {
				md.update(plainText.getBytes());
			}
			byte[] b = md.digest();
			String buf = ByteUtils.toHexAscii(b, "");
			if (is32) {
				return buf;
			} else {
				return buf.substring(8, 24);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
		}

		return "";
	}
}
