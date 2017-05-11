package com.xl.projectno.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Request.UrlRewriteListener;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.xl.projectno.data.bean.ModuleDataItemBean;
import com.xl.projectno.data.bean.PageDataBean;
import com.xl.projectno.data.protocol.ProtocolManager;
import com.xl.projectno.data.protocol.RequestBean;
import com.xl.projectno.data.protocol.ResponseHeaderBean;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.BaseDataModel;
import com.xl.projectno.model.FormImage;
import com.xl.projectno.model.UserBean;
import com.xl.projectno.utils.LogUtil;
import com.xl.projectno.utils.MD5;
import com.xl.projectno.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * 数据层具体加载类
 * <p>
 * CreateRequest（传进url、INetworkResponseParser）,DataRequest(传进url、INetworkResponseParser、Listener、ErrorListener)、
 * JsonRequest(调用INetworkResponseParser的parseNetworkResponse和getPostParams，返回的参数被Listener监听到)
 */
public class DataLoader {

    /**
     * RequestQueue for dispatching ImageRequests onto.
     */
    private final RequestQueue mRequestQueue;
    private final IDataCache<ModuleDataItemBean> mDataCache;
    private IDataParser<Map<String, BaseBean>> mDataParser;

    /**
     * <br>
     * 类描述: <br>
     * 功能详细描述:
     *
     * @author huanglun
     * @date [2013-11-7]
     */
    public class ItemDataContainer {
        public final ILoadDataListener mListener;

        public ItemDataContainer(ILoadDataListener listener) {
            mListener = listener;
        }
    }

    /**
     * HashMap of Cache keys -> BatchedImageRequest used to track in-flight
     * requests so that we can coalesce multiple requests to the same URL into a
     * single network request.
     */
    private final HashMap<String, BatchedItemDataRequest> mInFlightRequests = new HashMap<String, BatchedItemDataRequest>();

    private final Set<BatchedItemDataRequest> mWaitingRequests = new HashSet<BatchedItemDataRequest>();
    private final LinkedList<BatchedItemDataRequest> mCurrentRequests = new LinkedList<BatchedItemDataRequest>();
    //可替换的IP
    private final String[] OPTIONAL_IPS = new String[]{"http://69.28.57.171:8888", "http://69.28.57.172:8888",
            "http://69.28.57.173:8888", "http://69.28.57.174:8888",};
    private static String sCurrentIP;

    public DataLoader(Context appContext, RequestQueue queue) {
        mRequestQueue = queue;
        mDataCache = new DataCache(new DataFileCache());
        mDataParser = new DataParser();
    }

    private Request<?> createRequest(int method, String url, final int retryCount, final String cacheKey,
                                     DataRequest.INetworkResponseParser networkResponseParser) {
        final Request<?> newRequest = new DataRequest(method, url, retryCount, networkResponseParser,
                new Response.Listener<Object>() {
                    @Override
                    public void onResponse(Object response) {
                        onGetItemDataSuccess(cacheKey, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onGetItemDataError(cacheKey, error);
            }
        });
        newRequest.setShouldCache(false);
        return newRequest;
    }

    public void uploadSaleFlow(String billTime, JSONArray object, ILoadDataListener listener) {
        final String cacheKey = "uploadsaleflow" + billTime;
        // 无缓存
        ItemDataContainer container = new ItemDataContainer(listener);
        //这里的监听什么时候被回调

        BatchedItemDataRequest request = mInFlightRequests.get(cacheKey);
        if (request != null) {
            request.addContainer(container);
            return;
        }

        String url = UrlCreateFactory.getRequestUrl(UrlCreateFactory.ShopType.API_DATA_UPLOAD);
        final Request<?> newRequest = createRequest(Request.Method.POST, url, 2, cacheKey,
                new UploadFlowNetworkResponseParser(object, "0"));

//		newRequest.setUrlRewriteListener(mUrlRewriteListener);
        // tag
        newRequest.setTag(cacheKey);
        // 放入请求队列
        addDownQueue(new BatchedItemDataRequest(newRequest, container));
    }

    public void query(String tokenId, ILoadDataListener listener) {
        final String cacheKey = "alldata";
        // 无缓存
        ItemDataContainer container = new ItemDataContainer(listener);
        //弄懂container什么时候被调用

        //请求队列在这里没有多大作用
        BatchedItemDataRequest request = mInFlightRequests.get(cacheKey);
        if (request != null) {
            request.addContainer(container);
            return;
        }

        String url = UrlCreateFactory.getRequestUrl(UrlCreateFactory.ShopType.API_DATA_GET);
        final Request<?> newRequest = createRequest(Request.Method.POST, url, 2, cacheKey,
                new BaseDataNetworkResponseParser(tokenId, 0));

//		newRequest.setUrlRewriteListener(mUrlRewriteListener);
        // tag
        newRequest.setTag(cacheKey);
        // 放入请求队列
        addDownQueue(new BatchedItemDataRequest(newRequest, container));
    }

    private class UploadFlowNetworkResponseParser
            implements
            DataRequest.INetworkResponseParser {
        private JSONArray mJson;
        private String mTokenId;

        UploadFlowNetworkResponseParser(JSONArray json, String tokenId) {
            mJson = json;
            mTokenId = tokenId;
        }

        @Override
        public Response parseNetworkResponse(NetworkResponse response) {
            byte[] resultJson = response.data;
            boolean ret = false;
            try {
                ret = mDataParser.parseUploadFlowJSON(resultJson, false);
            } catch (ParseError e) {
//				e.printStackTrace();
            }
            return Response.success(ret, HttpHeaderParser.parseCacheHeaders(response));
        }

        @Override
        public Map<String, String> getPostParams() {
            Map<String, String> formdata = new HashMap<String, String>();
            formdata.put("bills", mJson.toString());
            LogUtil.d("huanglun", formdata.toString());
            return formdata;
        }

    }


    private class BaseDataNetworkResponseParser
            implements
            DataRequest.INetworkResponseParser<BaseDataModel> {
        private String mTokenId;
        private String updateTime;

        BaseDataNetworkResponseParser(String token, int entranceId) {
            mTokenId = token;
        }

        @Override
        public Response<BaseDataModel> parseNetworkResponse(NetworkResponse response) {
            byte[] resultJson = response.data;
            BaseDataModel model = null;
            try {
                model = mDataParser.parseDataJSON(resultJson, false);
            } catch (ParseError e) {
//				e.printStackTrace();
            }
            return Response.success(model, HttpHeaderParser.parseCacheHeaders(response));
        }

        @Override
        public Map<String, String> getPostParams() {
            Map<String, String> formdata = new HashMap<String, String>();
            formdata.put("api_token", mTokenId);
            formdata.put("updateTime", "0");
            return formdata;
        }
    }

    public void register(String usr,String psw,String createTime,ILoadDataListener<UserBean> listener){
        final String cacheKey = usr + psw + createTime;
        // 无缓存
        ItemDataContainer container = new ItemDataContainer(listener);

        BatchedItemDataRequest request = mInFlightRequests.get(cacheKey);
        if (request != null) {
            request.addContainer(container);
            return;
        }
        //得到登录的url
        String url = UrlCreateFactory.getRequestUrl(UrlCreateFactory.ShopType.API_REGISTER);
        Log.d("xingling",url);
        final Request<?> newRequest = createRequest(Request.Method.POST, url, 2, cacheKey,
                new RegisterNetworkResponseParser(usr, psw,createTime));

//		newRequest.setUrlRewriteListener(mUrlRewriteListener);
        // tag
        newRequest.setTag(cacheKey);
        // 放入请求队列
        addDownQueue(new BatchedItemDataRequest(newRequest, container));
    }

    private class RegisterNetworkResponseParser
            implements
            DataRequest.INetworkResponseParser<UserBean> {
        private String mUserId;
        private String mPsw;
        private String createTime;

        RegisterNetworkResponseParser(String userId, String psw,String createTime) {
            this.mUserId = userId;
            this.mPsw = psw;
            this.createTime = createTime;
        }

        @Override
        public Response<UserBean> parseNetworkResponse(NetworkResponse response) {
            byte[] resultJson = response.data;
            if (GoPluginEnv.DEBUG) {
                LogUtil.d(GoPluginEnv.TAG, " : onResponse success :" + (resultJson != null));
            }
            UserBean bean = null;
            try {
                Log.d("xingling",resultJson.toString());
                bean = mDataParser.parseRegisterJSON(resultJson, false);
            } catch (ParseError e) {
                e.printStackTrace();
            }
            return Response.success(bean, HttpHeaderParser.parseCacheHeaders(response));
        }

        @Override
        public Map<String, String> getPostParams() {
            Map<String, String> formdata = new HashMap<String, String>();
            formdata.put("name", mUserId);
            formdata.put("password", mPsw);
            formdata.put("createTime", createTime);
            return formdata;
        }
    }

    public void login(String usr, String psw,String loginTime, ILoadDataListener<UserBean> listener) {
        final String cacheKey = usr + psw + loginTime;
        // 无缓存
        ItemDataContainer container = new ItemDataContainer(listener);

        BatchedItemDataRequest request = mInFlightRequests.get(cacheKey);
        if (request != null) {
            request.addContainer(container);
            return;
        }
        //得到登录的url
        String url = UrlCreateFactory.getRequestUrl(UrlCreateFactory.ShopType.API_LOGIN);
        Log.d("xingling",url);
        final Request<?> newRequest = createRequest(Request.Method.POST, url, 2, cacheKey,
                new LoginNetworkResponseParser(usr, psw,loginTime));

//		newRequest.setUrlRewriteListener(mUrlRewriteListener);
        // tag
        newRequest.setTag(cacheKey);
        // 放入请求队列
        addDownQueue(new BatchedItemDataRequest(newRequest, container));
    }

    private class LoginNetworkResponseParser
            implements
            DataRequest.INetworkResponseParser<UserBean> {
        private String mUserId;
        private String mPsw;
        private String loginTime;

        LoginNetworkResponseParser(String userId, String psw , String loginTime) {
            mUserId = userId;
            mPsw = psw;
            this.loginTime = loginTime;
        }

        @Override
        public Response<UserBean> parseNetworkResponse(NetworkResponse response) {
            byte[] resultJson = response.data;
            if (GoPluginEnv.DEBUG) {
                LogUtil.d(GoPluginEnv.TAG, " : onResponse success :" + (resultJson != null));
            }
            UserBean bean = null;
            try {
                Log.d("xingling",resultJson.toString());
                bean = mDataParser.parseLoginJSON(resultJson, false);
                bean.name = mUserId;
                bean.password = mPsw;
                bean.loginTime = loginTime;
            } catch (ParseError e) {
                e.printStackTrace();
            }
            return Response.success(bean, HttpHeaderParser.parseCacheHeaders(response));
        }

        @Override
        public Map<String, String> getPostParams() {
            Map<String, String> formdata = new HashMap<String, String>();
            formdata.put("name", mUserId);
            formdata.put("password", mPsw);
            formdata.put("loginTime", loginTime);
            return formdata;
        }
    }


    public void logout(String usr, String psw,ILoadDataListener<ResponseHeaderBean> listener) {
        final String cacheKey = usr + psw;
        // 无缓存
        ItemDataContainer container = new ItemDataContainer(listener);

        BatchedItemDataRequest request = mInFlightRequests.get(cacheKey);
        if (request != null) {
            request.addContainer(container);
            return;
        }
        //得到登录的url
        String url = UrlCreateFactory.getRequestUrl(UrlCreateFactory.ShopType.API_LOGOUT);
        Log.d("xingling",url);
        final Request<?> newRequest = createRequest(Request.Method.POST, url, 2, cacheKey,
                new LogoutNetworkResponseParser(usr, psw));

//		newRequest.setUrlRewriteListener(mUrlRewriteListener);
        // tag
        newRequest.setTag(cacheKey);
        // 放入请求队列
        addDownQueue(new BatchedItemDataRequest(newRequest, container));
    }

    private class LogoutNetworkResponseParser
            implements
            DataRequest.INetworkResponseParser<ResponseHeaderBean> {
        private String mUserId;
        private String mPsw;

        LogoutNetworkResponseParser(String userId, String psw) {
            mUserId = userId;
            mPsw = psw;
        }

        @Override
        public Response<ResponseHeaderBean> parseNetworkResponse(NetworkResponse response) {
            byte[] resultJson = response.data;
            if (GoPluginEnv.DEBUG) {
                LogUtil.d(GoPluginEnv.TAG, " : onResponse success :" + (resultJson != null));
            }
            ResponseHeaderBean bean = null;
            try {
                Log.d("xingling",resultJson.toString());
                bean = mDataParser.parseLogoutJSON(resultJson, false);
            } catch (ParseError e) {
                e.printStackTrace();
            }
            return Response.success(bean, HttpHeaderParser.parseCacheHeaders(response));
        }

        @Override
        public Map<String, String> getPostParams() {
            Map<String, String> formdata = new HashMap<String, String>();
            formdata.put("name", mUserId);
            formdata.put("password", mPsw);
            return formdata;
        }
    }

    public void keepLive(String usr, String api_token,ILoadDataListener<ResponseHeaderBean> listener) {
        final String cacheKey = usr + api_token;
        // 无缓存
        ItemDataContainer container = new ItemDataContainer(listener);

        BatchedItemDataRequest request = mInFlightRequests.get(cacheKey);
        if (request != null) {
            request.addContainer(container);
            return;
        }
        //得到登录的url
        String url = UrlCreateFactory.getRequestUrl(UrlCreateFactory.ShopType.API_KEEPLIVE);
        Log.d("xingling",url);
        final Request<?> newRequest = createRequest(Request.Method.POST, url, 2, cacheKey,
                new KeepLiveNetworkResponseParser(usr, api_token));

//		newRequest.setUrlRewriteListener(mUrlRewriteListener);
        // tag
        newRequest.setTag(cacheKey);
        // 放入请求队列
        addDownQueue(new BatchedItemDataRequest(newRequest, container));
    }

    private class KeepLiveNetworkResponseParser
            implements
            DataRequest.INetworkResponseParser<ResponseHeaderBean> {
        private String mUserId;
        private String api_token;

        KeepLiveNetworkResponseParser(String userId, String api_token) {
            mUserId = userId;
            this.api_token = api_token;
        }

        @Override
        public Response<ResponseHeaderBean> parseNetworkResponse(NetworkResponse response) {
            byte[] resultJson = response.data;
            if (GoPluginEnv.DEBUG) {
                LogUtil.d(GoPluginEnv.TAG, " : onResponse success :" + (resultJson != null));
            }
            ResponseHeaderBean bean = null;
            try {
                Log.d("xingling",resultJson.toString());
                bean = mDataParser.parseKeepLiveJSON(resultJson, false);
            } catch (ParseError e) {
                e.printStackTrace();
            }
            return Response.success(bean, HttpHeaderParser.parseCacheHeaders(response));
        }

        @Override
        public Map<String, String> getPostParams() {
            Map<String, String> formdata = new HashMap<String, String>();
            formdata.put("name", mUserId);
            formdata.put("api_token", api_token);
            return formdata;
        }
    }

    public void changeinfo(String usr, String api_token,String nickname,String phone,String imail,ILoadDataListener<ResponseHeaderBean> listener) {
        final String cacheKey = usr + api_token+nickname;
        // 无缓存
        ItemDataContainer container = new ItemDataContainer(listener);

        BatchedItemDataRequest request = mInFlightRequests.get(cacheKey);
        if (request != null) {
            request.addContainer(container);
            return;
        }
        //得到登录的url
        String url = UrlCreateFactory.getRequestUrl(UrlCreateFactory.ShopType.API_CHANGEINFO);
        Log.d("xingling",url);
        final Request<?> newRequest = createRequest(Request.Method.POST, url, 2, cacheKey,
                new ChangeInfoNetworkResponseParser(usr, api_token,nickname,phone,imail));

//		newRequest.setUrlRewriteListener(mUrlRewriteListener);
        // tag
        newRequest.setTag(cacheKey);
        // 放入请求队列
        addDownQueue(new BatchedItemDataRequest(newRequest, container));
    }

    private class ChangeInfoNetworkResponseParser
            implements
            DataRequest.INetworkResponseParser<ResponseHeaderBean> {
        private String mUserId;
        private String api_token;
        private String nickname;
        private String phone;
        private String imail;

        ChangeInfoNetworkResponseParser(String userId, String api_token,String nickname,String phone,String imail) {
            mUserId = userId;
            this.api_token = api_token;
            this.nickname = nickname;
            this.phone = phone;
            this.imail = imail;
        }

        @Override
        public Response<ResponseHeaderBean> parseNetworkResponse(NetworkResponse response) {
            byte[] resultJson = response.data;
            if (GoPluginEnv.DEBUG) {
                LogUtil.d(GoPluginEnv.TAG, " : onResponse success :" + (resultJson != null));
            }
            ResponseHeaderBean bean = null;
            try {
                Log.d("xingling",resultJson.toString());
                bean = mDataParser.parseChangeInfoJSON(resultJson, false);
            } catch (ParseError e) {
                e.printStackTrace();
            }
            return Response.success(bean, HttpHeaderParser.parseCacheHeaders(response));
        }

        @Override
        public Map<String, String> getPostParams() {
            Map<String, String> formdata = new HashMap<String, String>();
            formdata.put("name", mUserId);
            formdata.put("api_token", api_token);
            formdata.put("nickname",nickname);
            formdata.put("phone",phone);
            formdata.put("imail",imail);
            return formdata;
        }
    }

    public void alterps(String usr, String oldpassword,String newpassword,ILoadDataListener<ResponseHeaderBean> listener) {
        final String cacheKey = usr + oldpassword+newpassword;
        // 无缓存
        ItemDataContainer container = new ItemDataContainer(listener);

        BatchedItemDataRequest request = mInFlightRequests.get(cacheKey);
        if (request != null) {
            request.addContainer(container);
            return;
        }
        //得到登录的url
        String url = UrlCreateFactory.getRequestUrl(UrlCreateFactory.ShopType.API_ALTERPS);
        Log.d("xingling",url);
        final Request<?> newRequest = createRequest(Request.Method.POST, url, 2, cacheKey,
                new AlterPsNetworkResponseParser(usr, oldpassword,newpassword));

//		newRequest.setUrlRewriteListener(mUrlRewriteListener);
        // tag
        newRequest.setTag(cacheKey);
        // 放入请求队列
        addDownQueue(new BatchedItemDataRequest(newRequest, container));
    }

    private class AlterPsNetworkResponseParser
            implements
            DataRequest.INetworkResponseParser<ResponseHeaderBean> {
        private String mUserId;
        private String oldpassword;
        private String newpassword;

        AlterPsNetworkResponseParser(String userId, String oldpassword,String newpassword) {
            mUserId = userId;
            this.oldpassword = oldpassword;
            this.newpassword = newpassword;
        }

        @Override
        public Response<ResponseHeaderBean> parseNetworkResponse(NetworkResponse response) {
            byte[] resultJson = response.data;
            if (GoPluginEnv.DEBUG) {
                LogUtil.d(GoPluginEnv.TAG, " : onResponse success :" + (resultJson != null));
            }
            ResponseHeaderBean bean = null;
            try {
                Log.d("xingling",resultJson.toString());
                bean = mDataParser.parseAlterPsJSON(resultJson, false);
            } catch (ParseError e) {
                e.printStackTrace();
            }
            return Response.success(bean, HttpHeaderParser.parseCacheHeaders(response));
        }

        @Override
        public Map<String, String> getPostParams() {
            Map<String, String> formdata = new HashMap<String, String>();
            formdata.put("name", mUserId);
            formdata.put("oldpassword", oldpassword);
            formdata.put("newpassword",newpassword);
            return formdata;
        }
    }

    public void resetps(String imail, String phone,String checkcode,ILoadDataListener<ResponseHeaderBean> listener) {
        final String cacheKey = imail + phone+checkcode;
        // 无缓存
        ItemDataContainer container = new ItemDataContainer(listener);

        BatchedItemDataRequest request = mInFlightRequests.get(cacheKey);
        if (request != null) {
            request.addContainer(container);
            return;
        }
        //得到登录的url
        String url = UrlCreateFactory.getRequestUrl(UrlCreateFactory.ShopType.API_RESETPS);
        Log.d("xingling",url);
        final Request<?> newRequest = createRequest(Request.Method.POST, url, 2, cacheKey,
                new ResetPsNetworkResponseParser(imail, phone,checkcode));

//		newRequest.setUrlRewriteListener(mUrlRewriteListener);
        // tag
        newRequest.setTag(cacheKey);
        // 放入请求队列
        addDownQueue(new BatchedItemDataRequest(newRequest, container));
    }

    private class ResetPsNetworkResponseParser
            implements
            DataRequest.INetworkResponseParser<ResponseHeaderBean> {
        private String imail;
        private String phone;
        private String checkcode;

        ResetPsNetworkResponseParser(String imail, String phone,String checkcode) {
            this.imail = imail;
            this.phone = phone;
            this.checkcode = checkcode;
        }

        @Override
        public Response<ResponseHeaderBean> parseNetworkResponse(NetworkResponse response) {
            byte[] resultJson = response.data;
            if (GoPluginEnv.DEBUG) {
                LogUtil.d(GoPluginEnv.TAG, " : onResponse success :" + (resultJson != null));
            }
            ResponseHeaderBean bean = null;
            try {
                Log.d("xingling",resultJson.toString());
                bean = mDataParser.parseResetPsJSON(resultJson, false);
            } catch (ParseError e) {
                e.printStackTrace();
            }
            return Response.success(bean, HttpHeaderParser.parseCacheHeaders(response));
        }

        @Override
        public Map<String, String> getPostParams() {
            Map<String, String> formdata = new HashMap<String, String>();
            formdata.put("imail", imail);
            formdata.put("phone", phone);
            formdata.put("checkcode",checkcode);
            return formdata;
        }
    }

    public void generatecode(String imail, String phone,ILoadDataListener<ResponseHeaderBean> listener) {
        final String cacheKey = imail + phone;
        // 无缓存
        ItemDataContainer container = new ItemDataContainer(listener);

        BatchedItemDataRequest request = mInFlightRequests.get(cacheKey);
        if (request != null) {
            request.addContainer(container);
            return;
        }
        //得到登录的url
        String url = UrlCreateFactory.getRequestUrl(UrlCreateFactory.ShopType.API_GENERATECODE);
        Log.d("xingling",url);
        final Request<?> newRequest = createRequest(Request.Method.POST, url, 2, cacheKey,
                new GenerateCodeNetworkResponseParser(imail, phone));

//		newRequest.setUrlRewriteListener(mUrlRewriteListener);
        // tag
        newRequest.setTag(cacheKey);
        // 放入请求队列
        addDownQueue(new BatchedItemDataRequest(newRequest, container));
    }

    private class GenerateCodeNetworkResponseParser
            implements
            DataRequest.INetworkResponseParser<ResponseHeaderBean> {
        private String imail;
        private String phone;

        GenerateCodeNetworkResponseParser(String imail, String phone) {
            this.imail = imail;
            this.phone = phone;
        }

        @Override
        public Response<ResponseHeaderBean> parseNetworkResponse(NetworkResponse response) {
            byte[] resultJson = response.data;
            if (GoPluginEnv.DEBUG) {
                LogUtil.d(GoPluginEnv.TAG, " : onResponse success :" + (resultJson != null));
            }
            ResponseHeaderBean bean = null;
            try {
                Log.d("xingling",resultJson.toString());
                bean = mDataParser.parseGenerateCodeJSON(resultJson, false);
            } catch (ParseError e) {
                e.printStackTrace();
            }
            return Response.success(bean, HttpHeaderParser.parseCacheHeaders(response));
        }

        @Override
        public Map<String, String> getPostParams() {
            Map<String, String> formdata = new HashMap<String, String>();
            formdata.put("imail", imail);
            formdata.put("phone", phone);
            return formdata;
        }
    }

    public void checkVersion(String productID, String versionCode, ILoadDataListener<ResponseHeaderBean> listener) {
        final String cacheKey = productID + versionCode;
        // 无缓存
        ItemDataContainer container = new ItemDataContainer(listener);

        BatchedItemDataRequest request = mInFlightRequests.get(cacheKey);
        if (request != null) {
            request.addContainer(container);
            return;
        }
        //得到登录的url
        String url = "http://112.74.34.58:8888/api/version/check";
        final Request<?> newRequest = createRequest(Request.Method.POST, url, 2, cacheKey,
                new CheckVersionResponseParser(productID, versionCode));

//		newRequest.setUrlRewriteListener(mUrlRewriteListener);
        // tag
        newRequest.setTag(cacheKey);
        // 放入请求队列
        addDownQueue(new BatchedItemDataRequest(newRequest, container));
    }

    /**
     * 检查更新版本返回数据解析
     */
    private class CheckVersionResponseParser
            implements
            DataRequest.INetworkResponseParser<ResponseHeaderBean> {
        private String productID;
        private String versionCode;

        CheckVersionResponseParser(String productID, String versionCode) {
            this.productID = productID;
            this.versionCode = versionCode;
        }

        @Override
        public Response<ResponseHeaderBean> parseNetworkResponse(NetworkResponse response) {
            byte[] resultJson = response.data;
            if (GoPluginEnv.DEBUG) {
                LogUtil.d(GoPluginEnv.TAG, " : onResponse success :" + (resultJson != null));
            }
            ResponseHeaderBean bean = null;
            try {
                bean = mDataParser.parseCheckVersionJSON(resultJson, false);
            } catch (ParseError e) {
                e.printStackTrace();
            }
            return Response.success(bean, HttpHeaderParser.parseCacheHeaders(response));
        }

        @Override
        public Map<String, String> getPostParams() {
            Map<String, String> formdata = new HashMap<String, String>();
            formdata.put("productID", productID);
            formdata.put("versionCode", versionCode);
            return formdata;
        }
    }

    public void pay(String code, String total_fee, String body, String auth_code, String tenantId, ILoadDataListener<ResponseHeaderBean> listener) {
        final String cacheKey = code + total_fee + body + auth_code + tenantId;
        // 无缓存
        ItemDataContainer container = new ItemDataContainer(listener);

        BatchedItemDataRequest request = mInFlightRequests.get(cacheKey);
        if (request != null) {
            request.addContainer(container);
            return;
        }
        //得到登录的url
        String url = UrlCreateFactory.getRequestUrl(UrlCreateFactory.ShopType.API_DATA_PAY);
        final Request<?> newRequest = createRequest(Request.Method.POST, url, 2, cacheKey,
                new PaymentResponseParser(code, total_fee, body, auth_code, tenantId));

//		newRequest.setUrlRewriteListener(mUrlRewriteListener);
        // tag
        newRequest.setTag(cacheKey);
        // 放入请求队列
        addDownQueue(new BatchedItemDataRequest(newRequest, container));
    }

    /**
     * 支付返回数据解析
     */
    private class PaymentResponseParser
            implements
            DataRequest.INetworkResponseParser<ResponseHeaderBean> {
        private String code;
        private String total_fee;
        private String body;
        private String auth_code;
        private String tenantId;

        PaymentResponseParser(String code, String total_fee, String body, String auth_code, String tenantId) {
            this.code = code;
            this.total_fee = total_fee;
            this.body = body;
            this.auth_code = auth_code;
            this.tenantId = tenantId;
        }

        @Override
        public Response<ResponseHeaderBean> parseNetworkResponse(NetworkResponse response) {
            byte[] resultJson = response.data;
            if (GoPluginEnv.DEBUG) {
                LogUtil.d(GoPluginEnv.TAG, " : onResponse success :" + (resultJson != null));
            }
            ResponseHeaderBean bean = null;
            try {
                bean = mDataParser.parsePayJSON(resultJson, false);
            } catch (ParseError e) {
                e.printStackTrace();
            }
            return Response.success(bean, HttpHeaderParser.parseCacheHeaders(response));
        }

        @Override
        public Map<String, String> getPostParams() {
            Map<String, String> formdata = new HashMap<String, String>();
            formdata.put("code", code);
            formdata.put("total_fee", total_fee);
            formdata.put("body", body);
            formdata.put("auth_code", auth_code);
            formdata.put("tenantId", tenantId);
            return formdata;
        }
    }

    /**
     * 请求成功返回
     *
     * @param cacheKey
     * @param resultJson
     */
    private void onGetItemDataSuccess(String cacheKey, Object result) {
        BatchedItemDataRequest batchRequest = mInFlightRequests.remove(cacheKey);
        if (GoPluginEnv.DEBUG) {
            LogUtil.d(GoPluginEnv.TAG, "mInFlightRequests.remove=" + batchRequest + ",cacheKey="
                    + cacheKey);
        }
        if (batchRequest == null) {
            return;
        }
        //保存有效的已经替换的IP
        saveCurrentIP(batchRequest.getRequest());
        mCurrentRequests.remove(batchRequest);
        for (ItemDataContainer container : batchRequest.mContainers) {
            if (GoPluginEnv.DEBUG) {
                LogUtil.d(GoPluginEnv.TAG, "onDataListner" + container);
            }
            if (container.mListener != null) {
                container.mListener.onDataListner(result);
            }
        }
    }

    //保存有效的已经替换的IP
    private void saveCurrentIP(Request<?> request) {
        if (!TextUtils.isEmpty(sCurrentIP)) {
            return;
        }
        if (request == null || request.getUrlRewriteListener() == null) {
            return;
        }
        String url = request.getUrl();
        if (TextUtils.isEmpty(url) || url.contains(GoPluginEnv.Server.HOST)) {
            return;
        }
        int lastIndex = url.indexOf(GoPluginEnv.Server.GET_STORE_MODULE_DATA_URL);
        if (lastIndex < 0) {
            return;
        }
        sCurrentIP = url.substring(0, lastIndex);
        if (GoPluginEnv.DEBUG) {
            Log.e("jiangpeihe", "mCurrentIP = " + sCurrentIP);
        }
    }

    /**
     * 请求失败返回
     *
     * @param cacheKey
     * @param error    错误类型
     */
    private void onGetItemDataError(String cacheKey, VolleyError error) {
        mDataCache.clearCache(cacheKey, false); // 所有异常都需要清一次缓存

        if (GoPluginEnv.DEBUG) {
            LogUtil.e(GoPluginEnv.TAG,
                    "cacheKey=" + cacheKey + " : onErrorResponse " + error.getMessage(), error);
        }
        BatchedItemDataRequest batchRequest = mInFlightRequests.remove(cacheKey);
        if (batchRequest == null) {
            return;
        }
        mCurrentRequests.remove(batchRequest);
        for (ItemDataContainer container : batchRequest.mContainers) {
            if (container.mListener != null) {
                container.mListener.onErrorResponse(error);
            }
        }
    }

    public void addDownQueue(BatchedItemDataRequest batRequest) {
        if (GoPluginEnv.DEBUG) {
            LogUtil.d(GoPluginEnv.TAG, "addDownQueue:" + batRequest);
        }
        mRequestQueue.add(batRequest.mRequest);
        mInFlightRequests.put(batRequest.getCacheKey(), batRequest);
        mCurrentRequests.add(batRequest);
    }

    public void cancelLoader(Object tag) {
        if (GoPluginEnv.DEBUG) {
            LogUtil.d(GoPluginEnv.TAG, "tag=" + tag);
        }
        // 取消等待队列中的任务
        Iterator<BatchedItemDataRequest> iterator = mWaitingRequests.iterator();
        while (iterator.hasNext()) {
            BatchedItemDataRequest request = iterator.next();
            if (request.cancelRequest(tag)) {
                if (GoPluginEnv.DEBUG) {
                    LogUtil.i(GoPluginEnv.TAG, "取消数据加载任务：" + request.getCacheKey());
                }
                iterator.remove();
            }
        }
        // 取消正在处理的队列中的任务
        Iterator<BatchedItemDataRequest> curIterator = mCurrentRequests.iterator();
        while (iterator.hasNext()) {
            BatchedItemDataRequest request = curIterator.next();
            if (request.cancelRequest(tag)) {
                if (GoPluginEnv.DEBUG) {
                    LogUtil.i(GoPluginEnv.TAG, "取消数据加载任务：" + request.getCacheKey());
                }
                iterator.remove();
                mInFlightRequests.remove(request.getCacheKey());
            }
        }

        Iterator<BatchedItemDataRequest> inFlightIterator = mInFlightRequests.values().iterator();
        while (inFlightIterator.hasNext()) {
            BatchedItemDataRequest request = inFlightIterator.next();
            if (request.cancelRequest(tag)) {
                if (GoPluginEnv.DEBUG) {
                    LogUtil.i(GoPluginEnv.TAG, "取消数据加载任务：" + request.getCacheKey());
                }
                inFlightIterator.remove();
            }
        }
    }

    /**
     * Wrapper class used to map a Request to the set of active ImageContainer
     * objects that are interested in its results.
     */
    private class BatchedItemDataRequest {
        /**
         * The request being tracked
         */
        private final Request<?> mRequest;
        /**
         * List of all of the active ImageContainers that are interested in the
         * request
         */
        private final LinkedList<ItemDataContainer> mContainers = new LinkedList<ItemDataContainer>();

        public BatchedItemDataRequest(Request<?> request, ItemDataContainer container) {
            mRequest = request;
            mContainers.add(container);
        }

        public String getCacheKey() {
            return mRequest.getTag().toString();
        }

        public boolean pauseRequest(Object tag) {
            if (tag.equals(mRequest.getTag())) {
                return true;
            }
            return false;
        }

        public boolean resumeRequest(Object tag) {
            if (tag.equals(mRequest.getTag())) {
                return true;
            }
            return false;
        }

        public boolean cancelRequest(Object tag) {
            if (tag.equals(mRequest.getTag())) {
                mRequest.cancel();
                //取消请求后移除回调
                mContainers.removeLast();
                return true;
            }
            return false;
        }

        /**
         * Adds another ImageContainer to the list of those interested in the
         * results of the request.
         */
        public void addContainer(ItemDataContainer container) {
            if (!isHasSameRequest(container)) {
                mContainers.add(container);
            }
        }

        private boolean isHasSameRequest(ItemDataContainer newContainer) {
            for (ItemDataContainer container : mContainers) {
                if (container.mListener == newContainer.mListener) {
                    return true;
                }
            }
            return false;
        }

        public Request<?> getRequest() {
            return mRequest;
        }
    }

    public boolean isCached(String cacheKey) {
        return mDataCache.isCached(cacheKey);
    }

    public ModuleDataItemBean getCache(String cacheKey) {
        return mDataCache.getCache(cacheKey);
    }

    public PageDataBean getPageCache(String cacheKey) {
        return mDataCache.getPageCache(cacheKey);
    }

    public void clearCache(String cacheKey, boolean deleteLocal) {
        mDataCache.clearCache(cacheKey, deleteLocal);
    }

    /**
     * 清除所有缓存
     */
    public void clearAll() {
        mDataCache.clearAll();
    }

    public boolean isLocalCached(String cacheKey) {
        return mDataCache.isLocalCached(cacheKey);
    }
}
