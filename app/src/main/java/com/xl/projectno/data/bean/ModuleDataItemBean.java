package com.xl.projectno.data.bean;

import android.text.TextUtils;

import com.xl.projectno.volley.IDataBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <br>
 * 类描述:模块数据项单元 <br>
 * 功能详细描述:
 */
public class ModuleDataItemBean implements IDataBean {

	/**
	 * 子模块
	 */
	public static final int TYPE_CHILD_MODULE = 1;

	/**
	 * 内容资源
	 */
	public static final int TYPE_CONTENT = 2;

	/**
	 * 真实的模块id 统计的时候使用该值，虚拟id不能用作统计
	 */
	private int mModuleId;

	/**
	 * 模块名称
	 */
	private String mModuleName;

	/**
	 * 该模块下挂载的内容类型 1:下挂子模块； 2:下挂内容资源；
	 */
	private int mDataType;

	/**
	 * 该模块的布局
	 */
	private int mLayout;

	/**
	 * 总页数 总页数pages是不准确的；只要pages>pageid则代表还有下一页数据；否则说明已是最后一页。
	 */
	private int mPageCount;

	/**
	 * 当前分页 客户端可根据“模块id+分隔符+页码”作为key来缓存模块列表数据。
	 */
	private int mPageId;

	/* 依赖的bannerId */
	private int mObeymoduleid;

	/**
	 * 是否为首屏 默认:0；1:首屏；
	 */
	private int mIsHome;

	/**
	 * 挂载的子模块数据, 仅当datatype=1时有数据
	 */
	private List<ModuleInfoBean> mModuleInfos = new ArrayList<ModuleInfoBean>();

	/**
	 * 挂载的内容资源数据, 仅当datatype=2时有数据
	 */
//	private List<ContentResourcesInfoBean> mContentResourcesInfos = new ArrayList<ContentResourcesInfoBean>();

	//Abtest的数据

	public int getModuleId() {
		return mModuleId;
	}

	public void setModuleId(int moduleId) {
		this.mModuleId = moduleId;
	}

	public String getModuleName() {
		return mModuleName;
	}

	public void setModuleName(String moduleName) {
		this.mModuleName = moduleName;
	}

	public int getDataType() {
		return mDataType;
	}

	public void setObeymoduleid(int obeymoduleid) {
		mObeymoduleid = obeymoduleid;
	}

	public int getObeymoduleid() {
		return mObeymoduleid;
	}

	public int getIsHome() {
		return mIsHome;
	}

	public void setDataType(int dataType) {
		this.mDataType = dataType;
	}

	public int getLayout() {
		return mLayout;
	}

	public void setLayout(int layout) {
		this.mLayout = layout;
	}

	public int getPageCount() {
		return mPageCount;
	}

	public void setPageCount(int pageCount) {
		this.mPageCount = pageCount;
	}

	public int getPageId() {
		return mPageId;
	}

	public void setPageId(int pageId) {
		this.mPageId = pageId;
	}

	public List<ModuleInfoBean> getModuleInfos() {
		return mModuleInfos;
	}

	public void setModuleInfos(List<ModuleInfoBean> moduleInfos) {
		this.mModuleInfos = moduleInfos;
	}


	/**
	 * <br>
	 * 功能简述:是否为子模块 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @return
	 */
	public boolean isCacheChildModuleInfoBean() {
		return mDataType == TYPE_CHILD_MODULE;
	}

	/**
	 * <br>
	 * 功能简述:是否为内容数据 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @return
	 */
	public boolean isCacheContentResourcesInfoBean() {
		return mDataType == TYPE_CONTENT;
	}

	/**
	 * <br>
	 * 功能简述:获取子模块的数量 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @return
	 */
	public int getChildModuleInfoBeanSize() {
		if (mModuleInfos != null) {
			return mModuleInfos.size();
		}

		return 0;
	}

	/**
	 * <br>
	 * 功能简述:获取内容资源的数量 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @return
	 */
	public int getContentResourcesInfoBeanSize() {
//		if (mContentResourcesInfos != null) {
//			return mContentResourcesInfos.size();
//		}

		return 0;
	}

	/**
	 * <br>
	 * 功能简述:获取子模块id <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @param position
	 * @return
	 */
	public int getChildModuleId(int position) {
		if (position >= 0 && position < getChildModuleInfoBeanSize()) {
			return mModuleInfos.get(position).getModuleId();
		}
		return -1;
	}

	@Override
	public void parseJSON(String json) {
		if (!TextUtils.isEmpty(json)) {
			try {
				JSONObject jsonObject = new JSONObject(json);
				mModuleId = jsonObject.optInt("moduleId");
				mModuleName = jsonObject.optString("moduleName");
				mDataType = jsonObject.optInt("dataType");
				mLayout = jsonObject.optInt("layout");
				mPageCount = jsonObject.optInt("pages");
				mPageId = jsonObject.optInt("pageid");
				mObeymoduleid = jsonObject.optInt("obeymoduleid");
				mIsHome = jsonObject.optInt("ishome", 0);
				JSONObject adTestJsonObject = jsonObject
						.optJSONObject("abtest");
				JSONArray jsonArray = null;
				int length = 0;
				if (mDataType == TYPE_CHILD_MODULE) {
					jsonArray = jsonObject.optJSONArray("childmodules");
					if (jsonArray != null) {
						length = jsonArray.length();
						if (length > 0) {
							for (int i = 0; i < length; i++) {
								ModuleInfoBean moduleInfo = new ModuleInfoBean();
								moduleInfo.parseJSON(jsonArray.optString(i));
								mModuleInfos.add(moduleInfo);
							}
						}
					}
				} else if (mDataType == TYPE_CONTENT) {
					jsonArray = jsonObject.optJSONArray("contents");
					if (jsonArray != null) {
						length = jsonArray.length();
						if (length > 0) {
							for (int i = 0; i < length; i++) {
//								ContentResourcesInfoBean contentResourcesInfo = new ContentResourcesInfoBean();
//								contentResourcesInfo.parseJSON(jsonArray
//										.optString(i));
//								if (contentResourcesInfo.getType() == ContentResourcesInfoBean.TYPE_FACEBOOK_ADMOD) {
//									continue;
//								}
//								if(mAbTestData!=null){
//									AbTestData abTestData = contentResourcesInfo.getAbTestData();
//									if(abTestData!=null ){
//										abTestData.setFunctionId(mAbTestData.getFunctionId());
//										abTestData.setUserGroup(mAbTestData.getUserGroup());
//									}
//								}
//								mContentResourcesInfos
//										.add(contentResourcesInfo);
							}
						}
					}
				}
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
			jsonObject.put("dataType", mDataType);
			jsonObject.put("layout", mLayout);
			jsonObject.put("pages", mPageCount);
			jsonObject.put("pageid", mPageId);
			jsonObject.put("obeymoduleid", mObeymoduleid);
			jsonObject.put("ishome", mIsHome);
			JSONArray jsonArray = null;
			if (mDataType == TYPE_CHILD_MODULE) {
				if (mModuleInfos != null) {
					jsonArray = new JSONArray();
					for (int i = 0; i < mModuleInfos.size(); i++) {
						ModuleInfoBean moduleInfo = mModuleInfos.get(i);
						jsonArray.put(moduleInfo.toJSON());
					}
				}

				jsonObject.put("childmodules", jsonArray);
			} else if (mDataType == TYPE_CONTENT) {
//				if (mContentResourcesInfos != null) {
//					jsonArray = new JSONArray();
//					for (int i = 0; i < mContentResourcesInfos.size(); i++) {
////						ContentResourcesInfoBean contentResourcesInfo = mContentResourcesInfos
////								.get(i);
////						jsonArray.put(contentResourcesInfo.toJSON());
//					}
//				}

				jsonObject.put("contents", jsonArray);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObject;
	}
	
	//在一个页面已经展示过对话框了
	private boolean mHasShowDialogInPage;

	public boolean isHasShowDialogInPage() {
		return mHasShowDialogInPage;
	}

	public void setHasShowDialogInPage(boolean hasShowDialogInPage) {
		mHasShowDialogInPage = hasShowDialogInPage;
	}

}
