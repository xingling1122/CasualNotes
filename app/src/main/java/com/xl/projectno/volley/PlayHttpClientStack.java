package com.xl.projectno.volley;

import com.android.volley.Request;
import com.android.volley.toolbox.HttpClientStack;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 表单提交
 */
public class PlayHttpClientStack extends HttpClientStack {
	private final static String HEADER_CONTENT_TYPE = "Content-Type";
	public PlayHttpClientStack(HttpClient client) {
		super(client);
	}
	
	@Override
	protected void onPrepareRequest(HttpUriRequest uriRequest, Request<?> request) throws IOException {
		if (uriRequest instanceof HttpEntityEnclosingRequest) {
			// 表单提交
			if (request instanceof DataRequest) {
				HttpEntityEnclosingRequest httpRequest = (HttpEntityEnclosingRequest) uriRequest;
				httpRequest.removeHeaders(HEADER_CONTENT_TYPE); // 请求需要去掉该头部,否则发送的数据会有问题
				DataRequest request2 = (DataRequest) request;
				
				// 提交的表单数据
				Map<String, String> formdata = request2.getPostParams();
				List<NameValuePair> postParameterPairs = getPostParameterPairs(formdata);
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameterPairs, HTTP.UTF_8);
				formEntity.setChunked(false);
				httpRequest.setEntity(formEntity);
			}
		}
		super.onPrepareRequest(uriRequest, request);
	}

	private static List<NameValuePair> getPostParameterPairs(Map<String, String> postParams) {
		List<NameValuePair> result = new ArrayList<NameValuePair>(postParams.size());
		for (String key : postParams.keySet()) {
			result.add(new BasicNameValuePair(key, postParams.get(key)));
		}
		return result;
	}
}
