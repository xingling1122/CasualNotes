package com.xl.projectno.volley;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonRequest;
import com.xl.projectno.utils.LogUtil;

import org.json.JSONArray;

import java.util.Map;

/**
 * 请求类
 * 
 * @param <T>
 *            A request for retrieving a {@link JSONArray} response body at a
 *            given URL.
 */
public class DataRequest<T> extends JsonRequest<T> {
	private INetworkResponseParser<T> mNetworkResponseParser;

	/**
	 * 
	 * <br>
	 * 类描述: 网络数据返回解析接口 <br>
	 * 功能详细描述:
	 * 
	 * @author xiongsheng
	 * @date [2014年1月13日]
	 */
	public static interface INetworkResponseParser<T> {
		Response<T> parseNetworkResponse(NetworkResponse response);

		public Map<String, String> getPostParams();
	}

	/**
	 * Creates a new request.
	 *
	 * @param method
	 * 			  Method.POST/GET
	 *
	 * @param url
	 *            URL to fetch the JSON from
	 * @param listener
	 *            Listener to receive the JSON response
	 * @param errorListener
	 *            Error listener, or null to ignore errors.
	 */
	public DataRequest(int method,String url,int retryCount,
			INetworkResponseParser networkResponseParser, Listener<T> listener,
			ErrorListener errorListener) {
		super(method, url, null, listener, errorListener);
		mNetworkResponseParser = networkResponseParser;
		// 设置读取的超时时间为 30s
		int timeOut = 30 * 1000;
		setRetryPolicy(new DefaultRetryPolicy(timeOut, retryCount,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	}

	//请求完得到参数，回调传进来的接口
	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		return mNetworkResponseParser.parseNetworkResponse(response);
	}

	public Map<String, String> getPostParams() {
		Map<String, String> map = mNetworkResponseParser.getPostParams();
		if (GoPluginEnv.DEBUG) {
			LogUtil.d(GoPluginEnv.TAG, "key=" + getCacheKey() + ","
					+ mNetworkResponseParser + ",thread="
					+ Thread.currentThread().getName() + ",url="
					+ getCacheKey() + " getPostParams=" + map);
		}
		return map;
	}

}
