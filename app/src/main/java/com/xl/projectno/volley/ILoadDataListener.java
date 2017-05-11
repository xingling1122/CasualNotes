package com.xl.projectno.volley;

import com.android.volley.Response.ErrorListener;

/**
 * 
 * <br>类描述:
 * <br>功能详细描述:
 */
public interface ILoadDataListener<T> extends ErrorListener {
	public void onDataListner(T result);
}
