package com.xl.projectno.utils;


/**
 * 
 * <br>类描述:字节处理
 * <br>功能详细描述:MD5算法使用
 *
 */
public class ByteUtils {

	public static String toHexAscii(byte[] bs, String c) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bs.length; i++) {
			sb.append(toHexAscii(bs[i]));
			if (i < bs.length - 1)
				sb.append(c);
		}
		return sb.toString();
	}

	public static String toHexAscii(byte b) {
		String s = Integer.toHexString(b);
		return StringUtils.paddingToFixedString(s, '0', 2, true);
	}

	public static void clearBytes(byte[] data) {
		if(data == null) {
			return;
		}
		int len = data.length;
		for(int i = 0; i < len; i++) {
			data[i] = 0;
		}
	}

}
