package com.xl.projectno.data.protocol;

/**
 * 
 * <br>类描述:模块请求参数单元
 * <br>功能详细描述:
 * 
 * @author  zhangjinfa
 * @date  [2014年9月1日]
 */
public class RequestBean {

	/**
	 * 模块id:分为虚拟id和真实id两种；
	 * 小于100000的id都是虚拟id
	 */
	private int mModuleId;
	
	/**
	 * 请求的页码
	 * 首次请求传1;
	 * 如果想一次获取列表所有数据请传0;
	 */
	private int mPageId;
	
	/**
	 * 入口id
	 */
	private int mEntranceId;
	
	/**
	 * 
	 */
	private int mPosition;

	/**
	 * <默认构造函数>
	 */
	public RequestBean(int moduleId, int pageId, int entranceId) {
		mModuleId = moduleId;
		mPageId = pageId;
		mEntranceId = entranceId;
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

	public int getEntranceId() {
		return mEntranceId;
	}

	public void setPosition(int position) {
		this.mPosition = position;
	}
	
	public int getPosition() {
		return mPosition;
	}

	public void setEntranceId(int entranceId) {
		this.mEntranceId = entranceId;
	}

	@Override
	public String toString() {
		return "RequestBean [mModuleId=" + mModuleId + ", mPageId=" + mPageId + ", mEntranceId=" + mEntranceId + "]";
	}
}
