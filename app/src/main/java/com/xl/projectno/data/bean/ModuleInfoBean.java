package com.xl.projectno.data.bean;

import android.text.TextUtils;


import com.xl.projectno.volley.IDataBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * <br>类描述:模块信息单元
 * <br>功能详细描述:
 */
public class ModuleInfoBean implements IDataBean {

	/**
	 * 模块id
	 */
	private int mModuleId;
	
	/**
	 * 模块名称
	 */
	private String mModuleName;
	
	/**
	 * 该模块的图标
	 */
	private String mIcon;
	
	/**
	 * 该模块的banner
	 */
	private String mBanner;
	
	public int getModuleId() {
		return mModuleId;
	}

	public String getModuleName() {
		return mModuleName;
	}

	public void setModuleName(String moduleName) {
		this.mModuleName = moduleName;
	}

	public String getIcon() {
		return mIcon;
	}

	public void setIcon(String icon) {
		this.mIcon = icon;
	}

	public String getBanner() {
		return mBanner;
	}

	public void setBanner(String banner) {
		this.mBanner = banner;
	}

	@Override
	public void parseJSON(String json) {
		if (!TextUtils.isEmpty(json)) {
			try {
				JSONObject jsonObject = new JSONObject(json);
				mModuleId = jsonObject.optInt("moduleId");
				mModuleName = jsonObject.optString("moduleName");
				mIcon = jsonObject.optString("icon");
				mBanner = jsonObject.optString("banner");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public JSONObject toJSON() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("moduleId", mModuleId);
			jsonObject.put("moduleName", mModuleName);
			jsonObject.put("icon", mIcon);
			jsonObject.put("banner", mBanner);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

}
