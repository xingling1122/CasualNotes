package com.xl.projectno.volley;


import com.xl.projectno.data.bean.ModuleDataItemBean;

/**
 * 
 * 类描述:缓存数据的统一接口
 */
public interface IDataBeanCache {
	
	/**
	 * 功能简述: 缓存一个分类项单元
	 *
	 * @param classificationItemBean
	 * @return
	 */
	public boolean saveDataBean(String key, ModuleDataItemBean moduleDataItemBean);
	
	/**
	 * 功能简述: 加载一个分类项单元
	 *
	 * @param key
	 * @return
	 */
	public ModuleDataItemBean loadDataBean(String key);
	
	/**
	 * 功能简述: 删除一个分类项单元
	 *
	 * @param key
	 * @return
	 */
	public boolean removeDataBean(String key);
	
	/**
	 * 功能简述: 分类项单元是否存在
	 *
	 * @param key
	 * @return
	 */
	public boolean isCache(String key);
	
}
