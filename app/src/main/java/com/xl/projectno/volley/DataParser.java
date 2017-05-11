package com.xl.projectno.volley;

import android.text.TextUtils;

import com.android.volley.ParseError;
import com.xl.projectno.data.protocol.ProtocolManager;
import com.xl.projectno.data.protocol.ResponseHeaderBean;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.BaseDataModel;
import com.xl.projectno.model.UserBean;
import com.xl.projectno.utils.FileUtils;
import com.xl.projectno.utils.LogUtil;
import com.xl.projectno.utils.StringUtil;

import java.util.Map;

/**
 * 数据层解析实现类,具体实现由协议层提供
 */
public class DataParser implements IDataParser<Map<String, BaseBean>> {

	@Override
	public ResponseHeaderBean parseGenerateCodeJSON(byte[] json, boolean isZipData) throws ParseError {
		String jsonStr = unzipData(json, isZipData);

		if (GoPluginEnv.DEBUG) {
			LogUtil.d(GoPluginEnv.TAG, "response: " + jsonStr);
		}

		return ProtocolManager.parseGenerateCodeJSON(jsonStr);
	}

	@Override
	public ResponseHeaderBean parseResetPsJSON(byte[] json, boolean isZipData) throws ParseError {
		String jsonStr = unzipData(json, isZipData);

		if (GoPluginEnv.DEBUG) {
			LogUtil.d(GoPluginEnv.TAG, "response: " + jsonStr);
		}

		return ProtocolManager.parseResetPsJSON(jsonStr);
	}

	@Override
	public ResponseHeaderBean parseAlterPsJSON(byte[] json, boolean isZipData) throws ParseError {
		String jsonStr = unzipData(json, isZipData);

		if (GoPluginEnv.DEBUG) {
			LogUtil.d(GoPluginEnv.TAG, "response: " + jsonStr);
		}

		return ProtocolManager.parseAlterPsJSON(jsonStr);
	}

	@Override
	public ResponseHeaderBean parseChangeInfoJSON(byte[] json, boolean isZipData) throws ParseError {
		String jsonStr = unzipData(json, isZipData);

		if (GoPluginEnv.DEBUG) {
			LogUtil.d(GoPluginEnv.TAG, "response: " + jsonStr);
		}

		return ProtocolManager.parseChangeInfoJSON(jsonStr);
	}

	@Override
	public ResponseHeaderBean parseUploadImageJSON(byte[] json, boolean isZipData) throws ParseError {
		String jsonStr = unzipData(json, isZipData);

		if (GoPluginEnv.DEBUG) {
			LogUtil.d(GoPluginEnv.TAG, "response: " + jsonStr);
		}

		return ProtocolManager.parseUploadImageJSON(jsonStr);
	}

	@Override
	public ResponseHeaderBean parseKeepLiveJSON(byte[] json, boolean isZipData) throws ParseError {
		String jsonStr = unzipData(json, isZipData);

		if (GoPluginEnv.DEBUG) {
			LogUtil.d(GoPluginEnv.TAG, "response: " + jsonStr);
		}

		return ProtocolManager.parseKeepLiveJSON(jsonStr);
	}

	@Override
	public ResponseHeaderBean parseLogoutJSON(byte[] json, boolean isZipData) throws ParseError {
		String jsonStr = unzipData(json, isZipData);

		if (GoPluginEnv.DEBUG) {
			LogUtil.d(GoPluginEnv.TAG, "response: " + jsonStr);
		}

		return ProtocolManager.parseLogoutJSON(jsonStr);
	}

	@Override
	public UserBean parseLoginJSON(byte[] json, boolean isZipData) throws ParseError {
		String jsonStr = unzipData(json, isZipData);

		if (GoPluginEnv.DEBUG) {
			LogUtil.d(GoPluginEnv.TAG, "response: " + jsonStr);
		}

		return ProtocolManager.parseLoginJSON(jsonStr);
	}

	@Override
	public UserBean parseRegisterJSON(byte[] json, boolean isZipData) throws ParseError {
		String jsonStr = unzipData(json, isZipData);

		if (GoPluginEnv.DEBUG) {
			LogUtil.d(GoPluginEnv.TAG, "response: " + jsonStr);
		}

		return ProtocolManager.parseRegisterJSON(jsonStr);
	}

	@Override
	public BaseDataModel parseDataJSON(byte[] json, boolean isZipData) throws ParseError {
		String jsonStr = unzipData(json, isZipData);

		if (GoPluginEnv.DEBUG) {
			LogUtil.d(GoPluginEnv.TAG, "response: " + jsonStr);
		}

		return ProtocolManager.parseDataJSON(jsonStr);
	}

	@Override
	public boolean parseUploadFlowJSON(byte[] json, boolean isZipData) throws ParseError {
		String jsonStr = unzipData(json, isZipData);

		if (GoPluginEnv.DEBUG) {
			LogUtil.d(GoPluginEnv.TAG, "response: " + jsonStr);
		}

		return ProtocolManager.parseUploadFlowJSON(jsonStr);
	}

	@Override
	public boolean parseUploadLogJSON(byte[] json, boolean isZipData) throws ParseError {
		String jsonStr = unzipData(json, isZipData);

		if (GoPluginEnv.DEBUG) {
			LogUtil.d(GoPluginEnv.TAG, "response: " + jsonStr);
		}

		return ProtocolManager.parseUploadLogFileJSON(jsonStr);
	}

	@Override
	public ResponseHeaderBean parseCheckVersionJSON(byte[] json, boolean isZipData) throws ParseError {
		String jsonStr = unzipData(json, isZipData);

		if (GoPluginEnv.DEBUG) {
			LogUtil.d(GoPluginEnv.TAG, "response: " + jsonStr);
		}

		return ProtocolManager.parseCheckVersionJSON(jsonStr);
	}

	@Override
	public ResponseHeaderBean parsePayJSON(byte[] json, boolean isZipData) throws ParseError {
		String jsonStr = unzipData(json, isZipData);

		if (GoPluginEnv.DEBUG) {
			LogUtil.d(GoPluginEnv.TAG, "response: " + jsonStr);
		}
		return ProtocolManager.parseCheckVersionJSON(jsonStr);
	}

	/**
	 * <br>功能简述:解压json数据
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param json
	 * @param isZipData
	 * @return
	 * @throws ParseError
	 */
	private String unzipData(byte[] json, boolean isZipData) throws ParseError {
		String jsonStr = null;
		if (GoPluginEnv.DEBUG) {
			LogUtil.d(GoPluginEnv.TAG, "unzipData bengin");
		}
		// 需要解压
		if (isZipData) {
			jsonStr = StringUtil.unzipData(json);
		} else {
			jsonStr = new String(json);
		}
		
		if (GoPluginEnv.DEBUG) {
			LogUtil.d(GoPluginEnv.TAG, "unzipData end ");
		}

		if (TextUtils.isEmpty(jsonStr)) {
			throw new ParseError("数据为空");
		}

		if (GoPluginEnv.DEBUG) {
			LogUtil.d(GoPluginEnv.TAG, String.format("parseResultJSON[isZipData=%b,json=%s]", isZipData, jsonStr));
			saveToFile(jsonStr);
		}
		return jsonStr;
	}
	
	/**
	 * <br>功能简述:保存json数据
	 * <br>功能详细描述:调试时使用
	 * <br>注意:
	 * @param jsonStr
	 */
	public void saveToFile(final String jsonStr) {
		// 异步存起来
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					FileUtils.saveByteToSDFile(jsonStr.getBytes(), GoPluginEnv.Path.DEBUG_QUEST_DATA_PATH);
					LogUtil.d(GoPluginEnv.TAG, "返回的json数据保存至文件:" + GoPluginEnv.Path.DEBUG_QUEST_DATA_PATH);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
