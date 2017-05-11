package com.xl.projectno.volley;

import java.io.Serializable;

public class BaseInfoBean implements Serializable {
	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 当前BaseInfoBean在ListDataBean哪个位置
	 */
	private int mPosition;
 	
	public void setPosition(int position) {
		mPosition = position;
	}
	
	public int getPosition() {
		return mPosition;
	}
}
