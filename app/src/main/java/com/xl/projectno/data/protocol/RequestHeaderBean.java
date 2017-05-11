package com.xl.projectno.data.protocol;

import android.content.Context;

import org.json.JSONObject;

/**
 * 
 * <br>类描述:网络请求头信息
 * <br>功能详细描述:
 * 
 * @author  zhangjinfa
 * @date  [2014年9月1日]
 */
public class RequestHeaderBean {
	
	/**
	 * 协议版本号：
	 * 1->2   插件商店去掉了精品的tab，对协议号为1的用户不下发该tab
	 * 2->3   主题商店接入Facebook的广告，对协议号为3以下的用户不下发该广告
	 * 6 支持商店请求重定向
	 */
	private static final int P_VERSION = 6;
	
	/**
	 * <br>功能简述:请求的数据头信息
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param context
	 * @param entranceId
	 * @return
	 */
	public static JSONObject createHttpHeader(Context context, int entranceId) {
		JSONObject data = new JSONObject();
//		try {
//			data.put("pversion", P_VERSION);
//
//			String aid = AppUtils.getAndroidId(context);
//			if (TextUtils.isEmpty(aid)) {
//				aid = "999";
//			}
//			data.put("aid", aid);
//
//			String gadid = ToolUtil.getAdvertisingId(context);
//			if (gadid == null) {
//				gadid = "unknow";
//			}
//			data.put("gadid", gadid);
//			String goId = ToolUtil.getGoId(context);
//			if(GoPluginEnv.DEBUG){
//				Log.e("jiangpeihe","android Id = "+aid +" goid = "+goId);
//			}
//			data.put("imei", AppUtils.getVirtualIMEI(context));
//
//			if (TextUtils.isEmpty(goId)) {
//				goId = "999";
//			}
//			data.put("goid", goId);
//			data.put("cid", GoKeyboardEnv.REQUEST_DATA_USED_PRODUCT_ID);
//
//			int cVersion = ToolUtil.getVersionCode(context);
//			if (cVersion == 0) {
//				cVersion = 1;
//			}
//			data.put("cversion", cVersion);
//			data.put("cversionname", AppUtils.buildVersion(context));
//			data.put("channel", AppUtils.getUid(context));
//			String country =  AppUtils.getSimCountry(context);
//			if (TextUtils.isEmpty(country)) {
//				country = "us";
//			}
//			data.put("local", country.toUpperCase());
//
//			String langCode = ToolUtil.getLanguage();
//			if (TextUtils.isEmpty(langCode)) {
//				langCode = "en";
//			}
//			data.put("lang", langCode);
//			data.put("imsi", ToolUtil.getImsi());
//			data.put("dpi", AppUtils.getDisplay(context));
//			data.put("sdk", Build.VERSION.SDK_INT);
//			data.put("sys", Build.VERSION.RELEASE);
//			String model = Build.MODEL;
//			if (TextUtils.isEmpty(model)) {
//				model = "unknow";
//			}
//			data.put("model", model);
//			data.put("requesttime", UtilTool.getBeiJinTime(System.currentTimeMillis()));
//			data.put("entranceId", entranceId);
//			data.put("official", 0);
//			data.put("hasmarket", AppUtils.isAppExist(context, GoPluginEnv.Market.PACKAGE) ? 1 : 0);
//			data.put("net", AppUtils.buildNetworkState(context));
//			data.put("sbuy", 0);
//			data.put("purchased_user", BuyUserManager.getInstance().isBuyUser());
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		return data;
	}
	
	/**
	 * <br>功能简述:请求的数据头信息
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param context
	 * @param entranceId
	 * @return
	 */
	public static JSONObject createRedPointHttpHeader(Context context) {
		JSONObject data = new JSONObject();
//		try {
//			data.put("pversion", P_VERSION);
//
//			String aid = AppUtils.getAndroidId(context);
//			if (TextUtils.isEmpty(aid)) {
//				aid = "999";
//			}
//			data.put("aid", aid);
//
//			String gadid = ToolUtil.getAdvertisingId(context);
//			if (gadid == null) {
//				gadid = "unknow";
//			}
//			data.put("gadid", gadid);
//			String goId = ToolUtil.getGoId(context);
//			if(GoPluginEnv.DEBUG){
//				Log.e("jiangpeihe","android Id = "+aid +" goid = "+goId);
//			}
//			data.put("imei", AppUtils.getVirtualIMEI(context));
//
//			if (TextUtils.isEmpty(goId)) {
//				goId = "999";
//			}
//			data.put("goid", goId);
//			data.put("cid", GoKeyboardEnv.REQUEST_DATA_USED_PRODUCT_ID);
//
//			int cVersion = ToolUtil.getVersionCode(context);
//			if (cVersion == 0) {
//				cVersion = 1;
//			}
//			data.put("cversion", cVersion);
//			data.put("cversionname", AppUtils.buildVersion(context));
//			data.put("channel", AppUtils.getUid(context));
//			String country =  AppUtils.getSimCountry(context);
//			if (TextUtils.isEmpty(country)) {
//				country = "us";
//			}
//			data.put("local", country.toUpperCase());
//
//			String langCode = ToolUtil.getLanguage();
//			if (TextUtils.isEmpty(langCode)) {
//				langCode = "en";
//			}
//			data.put("lang", langCode);
//			data.put("imsi", ToolUtil.getImsi());
//			data.put("dpi", AppUtils.getDisplay(context));
//			data.put("sdk", Build.VERSION.SDK_INT);
//			data.put("sys", Build.VERSION.RELEASE);
//			String model = Build.MODEL;
//			if (TextUtils.isEmpty(model)) {
//				model = "unknow";
//			}
//			data.put("model", model);
//			data.put("requesttime", UtilTool.getBeiJinTime(System.currentTimeMillis()));
//			data.put("official", 0);
//			data.put("hasmarket", AppUtils.isAppExist(context, GoPluginEnv.Market.PACKAGE) ? 1 : 0);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		return data;
	}
	
}
