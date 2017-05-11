package com.xl.projectno.volley;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 类描述: 服务器响应的数据头信息的封装
 */
public class ResultHeaderBean implements IDataBean {

	/**
	 * 服务器处理结果 
	 * status=1:处理成功；status=0:请求参数错误；
	 * status=-1:服务器处理出错；status=-2:业务处理异常；
	 * 仅当status=1时客户端才解析数据。
	 */
	private int mStatus;

	public int getStatus() {
		return mStatus;
	}
	
	public void parseJSON(String json) {
		if (!TextUtils.isEmpty(json)) {
			try {
				JSONObject jsonObject = new JSONObject(json);
				mStatus = jsonObject.optInt("status", 0);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("mStatus", mStatus);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
}
