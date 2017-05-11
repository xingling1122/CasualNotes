package com.xl.projectno.volley;
/**
 * 
 * <br>类描述: 缓存接口
 * <br>功能详细描述:
 * @param <T>
 * @author  tempuser
 * @date  [2014年1月18日]
 */
public interface ICache<T> {
	/**
	 * <br>功能简述: 内存中是否有缓存
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param cacheKey
	 * @return
	 */
	public boolean isCached(String cacheKey);
	
	public T getCache(String cacheKey);
	
	public void saveCache(String cacheKey, T t);
	
	public void clearCache(String cacheKey, boolean deleteLocal);
	
	public void clearAll();
}
