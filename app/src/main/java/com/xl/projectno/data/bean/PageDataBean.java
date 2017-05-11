package com.xl.projectno.data.bean;

import android.text.TextUtils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 数据组装单元.用户界面用,缓存了一个界面的数据
 *
 */
public class PageDataBean {

	/**
	 * 插件商店根节点的moduleid
	 */
	private int mModuleId = -1;

	/**
	 * 插件商店根节点的pageid
	 */
	private int mPageId;

	/**
	 * 递归次数
	 */
	private int mRecursionCount;

	// 是插件商店还是主题商店
	private int mDataType;
	
	private int mShopType;

	// 标识商店不同风格的展示类型
	private int mShowStyle;

	// 分类项数据
	private LinkedHashMap<Integer, ModuleDataItemBean> mData = new LinkedHashMap<Integer, ModuleDataItemBean>();

	public int getSize() {
		return mData.size();
	}

	public Set<Entry<Integer, ModuleDataItemBean>> entrySet() {
		return mData.entrySet();
	}

	public ModuleDataItemBean get(int moduleId) {
		return mData.get(moduleId);
	}

	public void addModuleItem(ModuleDataItemBean item) {
		mData.put(item.getModuleId(), item);
	}

	public int getModuleId() {
		return mModuleId;
	}

	public void setModuleId(int moduleId) {
		this.mModuleId = moduleId;
	}

	public int getPageId() {
		return mPageId;
	}

	public void setPageId(int pageId) {
		this.mPageId = pageId;
	}

	public void setDataType(int dataType) {
		mDataType = dataType;
	}

	public int getDataType() {
		return mDataType;
	}
	
	public void setShopType(int shopType) {
		mShopType = shopType;
	}

	public int getShopType() {
		return mShopType;
	}

	public void setShowStyle(int showStyle) {
		mShowStyle = showStyle;
	}

	public int getShowStyle() {
		return mShowStyle;
	}

	public void setRecursionCount(int recursionCount) {
		mRecursionCount = recursionCount;
	}

	public int getRecursionCount() {
		return mRecursionCount;
	}
	
	public String getAllChildModuleIds() {
		Set<Integer> keySet = mData.keySet();
		if (keySet == null || keySet.size() <= 0) {
			return "";
		}
		Iterator<Integer> iter = keySet.iterator();
		if (iter == null) {
			return "";
		}
		String mudouleIds = "";
		while (iter.hasNext()) {
			Integer i = iter.next();
			if (i == null) {
				continue;
			}
			mudouleIds += i+",";
		}
		if(!TextUtils.isEmpty(mudouleIds)){
			//去掉最后的逗号
			mudouleIds  = mudouleIds.substring(0, mudouleIds.length()-1);			
		}
		return mudouleIds;
	}
	

	@Override
	public String toString() {
		return "PageDataBean [mModuleId=" + mModuleId + ", mPageId=" + mPageId
				+ ", mData=" + mData + "]";
	}
}
