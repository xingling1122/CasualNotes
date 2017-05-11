package com.xl.projectno.volley;


import com.xl.projectno.data.bean.PageDataBean;

/**
 * 数据层缓存接口
 */
public interface IDataCache<T> extends ICache<T> {

	/**
	 * <br>功能简述:获取第一页数据
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param cacheKey
	 * @return
	 */
	public PageDataBean getPageCache(String cacheKey);


	/**
	 * <br>功能简述:本地是否有缓存
	 * <br>功能详细描述: 
	 * <br>注意:
	 * @param cacheKey
	 * @return
	 */
	public boolean isLocalCached(String cacheKey);
}
