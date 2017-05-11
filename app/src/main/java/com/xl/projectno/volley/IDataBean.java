package com.xl.projectno.volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * <br>类描述: 协议模型的统一行为接口
 * <br>功能详细描述:
 */
public interface IDataBean {

	public void parseJSON(String json) throws JSONException;
	
	public JSONObject toJSON() throws JSONException;
}
