package com.xl.projectno.data.protocol;

import android.text.TextUtils;
import android.util.Log;


import com.xl.projectno.model.UserBean;
import com.xl.projectno.volley.IDataBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * <br>类描述:服务器响应的数据头信息
 * <br>功能详细描述:
 * 
 * @author  zhangjinfa
 * @date  [2014年9月1日]
 */
public class ResponseHeaderBean implements IDataBean {

	/**
	 * 服务器处理结果 
	 * status=1:处理成功；
	 * status=0:请求参数错误；
	 * status=-1:服务器处理出错；
	 * status=-2:业务处理异常；
	 * 仅当status=1时客户端才解析数据。
	 */
	private boolean mStatus;
	private String mData;
	private String mMessage;
	private int retCode;

	public boolean getStatus() {
		return mStatus;
	}
	public String getData(){return mData;}
	public String getMessage(){return mMessage;}
	public int getRetCode(){return retCode;}
	
	public void parseJSON(String json) {
		if (!TextUtils.isEmpty(json)) {
			try {
				JSONObject jsonObject = new JSONObject(json);
				mStatus = jsonObject.optBoolean("status");
				mData = jsonObject.optString("data");
				mMessage=jsonObject.optString("message");
				retCode=jsonObject.optInt("code");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public String getUrl(){
		try {
			JSONObject jsonObject = new JSONObject(mData);
			return jsonObject.optString("url");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public UserBean getUser(){
		UserBean user=null;
		try {
			JSONObject jsonObject = new JSONObject(mData);
			user = new UserBean();
			user.name = jsonObject.optString("username");
			user.password = jsonObject.optString("password");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("status", mStatus);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	@Override
	public String toString() {
		return "ResponseHeaderBean{" +
				"mStatus=" + mStatus +
				", mData='" + mData + '\'' +
				", mMessage='" + mMessage + '\'' +
				", retCode=" + retCode +
				'}';
	}
}
