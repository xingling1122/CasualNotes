package com.xl.projectno.model;

import com.xl.projectno.volley.IJsonObject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/3/18.
 */
public class BaseBean implements IJsonObject {
    public int id;
    public String createTime;
    public BaseBean(String createTime){
        this.createTime=createTime;
    }
    public BaseBean(){}

    @Override
    public void parseJson(JSONObject jsonObj) throws JSONException {

    }
}
