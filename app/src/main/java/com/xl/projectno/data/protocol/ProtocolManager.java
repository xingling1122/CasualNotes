package com.xl.projectno.data.protocol;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.ParseError;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.base.MposApp;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.BaseDataModel;
import com.xl.projectno.model.UserBean;
import com.xl.projectno.utils.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <br>
 * 类描述:管理协议的相关数据 <br>
 * 功能详细描述:
 *
 * @author zhangjinfa
 * @date [2014年9月1日]
 */
public class ProtocolManager {
    private static final String TAG = "ProtocolManager";


    public static final String LABEL_DATA_ITEMS = "items"; // Item标签

    public static final String LABEL_DATA_FOLDERS = "folders"; // 文件夹标签

    public static final String LABEL_DATA_INVENTORYS = "inventory"; // 清单标签



    /**
     * 获取ResultHeaderBean的KEY
     */
    public static final String RESULT = "result";

    /**
     * 获取Types数据的KEY
     */
    public static final String DATAS = "datas";

    // 下面的都是才你喜歡或者配套主題
    public static final String OUTCONTENT = "data";
    public static final String CONTENTS = "contents";

    /**
     * 通信key 必须，由接口提供方提供
     */


    public static final String KEY = "gokeyboard_market_plugin";

    /**
     * 私钥 必须，String sign = md5(私钥+data数据+私钥);私钥由接口提供方提供
     */
    public static final String PRIVATE_KEY = "gokeyboard_market_plugin_sign";


    public static UserBean parseLoginJSON(String json) throws ParseError {
        UserBean bean = new UserBean();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            Log.d("xingling",jsonObject.toString());
            ResponseHeaderBean resultHeaderBean = new ResponseHeaderBean();
            resultHeaderBean.parseJSON(jsonObject.toString());
            if (resultHeaderBean.getStatus()) {
                JSONObject resultJSONObject = jsonObject
                        .optJSONObject("data");
                if(resultJSONObject != null) {
                    bean.token = resultJSONObject.optString("api_token");
                    bean.imail = resultJSONObject.optString("imail");
                    bean.imageUrl = resultJSONObject.optString("imageUrl");
                    bean.rank = resultJSONObject.optInt("rank");
                    bean.createTime = resultJSONObject.optString("createTime");
                    bean.nickname = resultJSONObject.optString("nickname");
                    bean.phone = resultJSONObject.optString("phone");
                }
            }
            bean.message = resultHeaderBean.getMessage();
        } catch (JSONException e) {
            throw new ParseError("ParseError,JSONException parseMatchedThems", e);
        }
        return bean;
    }

    public static UserBean parseRegisterJSON(String json) throws ParseError{
        JSONObject jsonObject;
        ResponseHeaderBean resultHeaderBean = null;
        UserBean bean = null;
        try {
            jsonObject = new JSONObject(json);
            Log.d("xingling",jsonObject.toString());
            resultHeaderBean = new ResponseHeaderBean();
            resultHeaderBean.parseJSON(jsonObject.toString());
            if (resultHeaderBean.getStatus()) {
                JSONObject resultJSONObject = jsonObject
                        .optJSONObject("data");
                if(resultJSONObject != null) {
                    bean = new UserBean();
                    bean.token = resultJSONObject.optString("api_token");
                    bean.imail = resultJSONObject.optString("imail");
                    bean.imageUrl = resultJSONObject.optString("imageUrl");
                    bean.rank = resultJSONObject.optInt("rank");
                    bean.createTime = resultJSONObject.optString("createTime");
                }
            }
            bean.message = resultHeaderBean.getMessage();
        } catch (JSONException e) {
            throw new ParseError("ParseError,JSONException parseMatchedThems", e);
        }
        return bean;
    }

    public static ResponseHeaderBean parseLogoutJSON(String json) throws ParseError{
        JSONObject jsonObject;
        ResponseHeaderBean resultHeaderBean = null;
        try {
            jsonObject = new JSONObject(json);
            Log.d("xingling",jsonObject.toString());
            resultHeaderBean = new ResponseHeaderBean();
            resultHeaderBean.parseJSON(jsonObject.toString());
        } catch (JSONException e) {
            throw new ParseError("ParseError,JSONException parseMatchedThems", e);
        }
        return resultHeaderBean;
    }

    public static ResponseHeaderBean parseKeepLiveJSON(String json) throws ParseError{
        JSONObject jsonObject;
        ResponseHeaderBean resultHeaderBean = null;
        try {
            jsonObject = new JSONObject(json);
            Log.d("xingling",jsonObject.toString());
            resultHeaderBean = new ResponseHeaderBean();
            resultHeaderBean.parseJSON(jsonObject.toString());
        } catch (JSONException e) {
            throw new ParseError("ParseError,JSONException parseMatchedThems", e);
        }
        return resultHeaderBean;
    }

    public static ResponseHeaderBean parseChangeInfoJSON(String json) throws ParseError{
        JSONObject jsonObject;
        ResponseHeaderBean resultHeaderBean = null;
        try {
            jsonObject = new JSONObject(json);
            Log.d("xingling",jsonObject.toString());
            resultHeaderBean = new ResponseHeaderBean();
            resultHeaderBean.parseJSON(jsonObject.toString());
        } catch (JSONException e) {
            throw new ParseError("ParseError,JSONException parseMatchedThems", e);
        }
        return resultHeaderBean;
    }

    public static ResponseHeaderBean parseAlterPsJSON(String json) throws ParseError{
        JSONObject jsonObject;
        ResponseHeaderBean resultHeaderBean = null;
        try {
            jsonObject = new JSONObject(json);
            Log.d("xingling",jsonObject.toString());
            resultHeaderBean = new ResponseHeaderBean();
            resultHeaderBean.parseJSON(jsonObject.toString());
        } catch (JSONException e) {
            throw new ParseError("ParseError,JSONException parseMatchedThems", e);
        }
        return resultHeaderBean;
    }

    public static ResponseHeaderBean parseResetPsJSON(String json) throws ParseError{
        JSONObject jsonObject;
        ResponseHeaderBean resultHeaderBean = null;
        try {
            jsonObject = new JSONObject(json);
            Log.d("xingling",jsonObject.toString());
            resultHeaderBean = new ResponseHeaderBean();
            resultHeaderBean.parseJSON(jsonObject.toString());
        } catch (JSONException e) {
            throw new ParseError("ParseError,JSONException parseMatchedThems", e);
        }
        return resultHeaderBean;
    }

    public static ResponseHeaderBean parseGenerateCodeJSON(String json) throws ParseError{
        JSONObject jsonObject;
        ResponseHeaderBean resultHeaderBean = null;
        try {
            jsonObject = new JSONObject(json);
            Log.d("xingling",jsonObject.toString());
            resultHeaderBean = new ResponseHeaderBean();
            resultHeaderBean.parseJSON(jsonObject.toString());
        } catch (JSONException e) {
            throw new ParseError("ParseError,JSONException parseMatchedThems", e);
        }
        return resultHeaderBean;
    }

    public static ResponseHeaderBean parseUploadImageJSON(String json) throws ParseError{
        JSONObject jsonObject;
        ResponseHeaderBean resultHeaderBean = null;
        try {
            jsonObject = new JSONObject(json);
            Log.d("xingling",jsonObject.toString());
            resultHeaderBean = new ResponseHeaderBean();
            resultHeaderBean.parseJSON(jsonObject.toString());
        } catch (JSONException e) {
            throw new ParseError("ParseError,JSONException parseMatchedThems", e);
        }
        return resultHeaderBean;
    }

    public static BaseDataModel parseDataJSON(String json) throws ParseError {
        JSONObject jsonObject;
        BaseDataModel model = new BaseDataModel();
        try {
            jsonObject = new JSONObject(json);
            ResponseHeaderBean resultHeaderBean = new ResponseHeaderBean();
            resultHeaderBean.parseJSON(jsonObject.toString());
            if (resultHeaderBean.getStatus()) {
                JSONObject typesJsonObject = jsonObject
                        .optJSONObject("data");
                if(typesJsonObject != null) {
                    model = new BaseDataModel();
                    model.parseJSON(typesJsonObject);
                }

            }
        } catch (JSONException e) {
            throw new ParseError("ParseError,JSONException parseMatchedThems", e);
        }
        return model;
    }

    public static boolean parseUploadFlowJSON(String json) throws ParseError {
        JSONObject jsonObject;
        boolean ret = false;
        try {
            jsonObject = new JSONObject(json);
            ResponseHeaderBean resultHeaderBean = new ResponseHeaderBean();
            resultHeaderBean.parseJSON(jsonObject.toString());
            if (resultHeaderBean.getStatus()) {
                ret = true;
            }
        } catch (JSONException e) {
            throw new ParseError("ParseError,JSONException parseMatchedThems", e);
        }
        return ret;
    }

    public static boolean parseUploadLogFileJSON(String json) throws ParseError {
        JSONObject jsonObject;
        boolean ret = false;
        try {
            jsonObject = new JSONObject(json);
            ResponseHeaderBean resultHeaderBean = new ResponseHeaderBean();
            resultHeaderBean.parseJSON(jsonObject.toString());
            if (resultHeaderBean.getStatus()) {
                ret = true;
            }
        } catch (JSONException e) {
            throw new ParseError("ParseError,JSONException parseMatchedThems", e);
        }
        return ret;
    }

    public static ResponseHeaderBean parseCheckVersionJSON(String json) throws ParseError {
        JSONObject jsonObject;
        ResponseHeaderBean bean = null;
        try {
            jsonObject = new JSONObject(json);
            ResponseHeaderBean resultHeaderBean = new ResponseHeaderBean();
            resultHeaderBean.parseJSON(jsonObject.toString());
            bean = resultHeaderBean;
        } catch (JSONException e) {
            throw new ParseError("ParseError,JSONException parseMatchedThems", e);
        }
        return bean;
    }

    /**
     * <br>
     * 功能简述:获取缓存的Key <br>
     * 功能详细描述: <br>
     * 注意:
     *
     * @param moduleId
     * @param pageId
     * @param entranceId
     * @return
     */
    public static String getCacheKey(int moduleId, int pageId, int entranceId) {
        StringBuilder sb = new StringBuilder();
        sb.append(moduleId).append("_").append(pageId).append("_")
                .append(entranceId);
        return sb.toString();
    }

    /**
     * <br>
     * 功能简述:缓存id的组装 <br>
     * 功能详细描述: <br>
     * 注意:
     *
     * @param cacheKey
     * @return
     */
    public static RequestBean parseCacheKey(String cacheKey) {
        if (TextUtils.isEmpty(cacheKey)) {
            return null;
        }
        String[] keys = cacheKey.split("_");
        if (keys == null || keys.length != 3) {
            return null;
        }

        return new RequestBean(Integer.parseInt(keys[0]),
                Integer.parseInt(keys[1]), Integer.parseInt(keys[2]));
    }

    /**
     * <br>
     * 功能简述:组装网络请求前数据 <br>
     * 功能详细描述: <br>
     * 注意:
     *
     * @param requestBeans
     * @return
     */
    private static JSONObject getPostJson(List<RequestBean> requestBeans) {
        JSONObject request = new JSONObject();
        try {
            int count = requestBeans.size();
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = null;
            for (int i = 0; i < count; i++) {
                jsonObject = new JSONObject();
                RequestBean requestBean = requestBeans.get(i);

                jsonObject.put("moduleId", requestBean.getModuleId());
                jsonObject.put("pageid", requestBean.getPageId());
                jsonArray.put(jsonObject);
            }

            request.put("phead", RequestHeaderBean.createHttpHeader(
                    MposApp.getApp(), requestBeans.get(0)
                            .getEntranceId()));
            request.put("reqs", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError error) {
            error.printStackTrace();
        }

        return request;
    }
}
