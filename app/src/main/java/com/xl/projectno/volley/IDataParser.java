package com.xl.projectno.volley;

import com.android.volley.ParseError;
import com.xl.projectno.data.protocol.ResponseHeaderBean;
import com.xl.projectno.model.BaseDataModel;
import com.xl.projectno.model.UserBean;


/**
 * 数据层解析接口
 */
public interface IDataParser<T> {
	public ResponseHeaderBean parseGenerateCodeJSON(byte[] json,boolean isZipData) throws ParseError;

	public ResponseHeaderBean parseResetPsJSON(byte[] json,boolean isZipData) throws ParseError;

	public ResponseHeaderBean parseAlterPsJSON(byte[] json,boolean isZipData) throws ParseError;

	public ResponseHeaderBean parseChangeInfoJSON(byte[] json,boolean isZipData) throws ParseError;

	public ResponseHeaderBean parseUploadImageJSON(byte[] json,boolean isZipData) throws ParseError;

	public ResponseHeaderBean parseKeepLiveJSON(byte[] json,boolean isZipData) throws ParseError;

	public ResponseHeaderBean parseLogoutJSON(byte[] json,boolean isZipData) throws ParseError;

	public UserBean parseLoginJSON(byte[] json, boolean isZipData) throws ParseError;

	public UserBean parseRegisterJSON(byte[] json,boolean isZipData) throws ParseError;

	public BaseDataModel parseDataJSON(byte[] json, boolean isZipData) throws ParseError;

	public boolean parseUploadFlowJSON(byte[] json, boolean isZipData) throws ParseError;

	public boolean parseUploadLogJSON(byte[] json, boolean isZipData) throws ParseError;

	public ResponseHeaderBean parseCheckVersionJSON(byte[] json, boolean isZipData) throws ParseError;

	public ResponseHeaderBean parsePayJSON(byte[] json, boolean isZipData) throws ParseError;
}
