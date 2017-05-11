package com.xl.projectno.volley;
import com.xl.projectno.data.bean.ModuleDataItemBean;
import com.xl.projectno.data.bean.ModuleInfoBean;
import com.xl.projectno.data.bean.PageDataBean;
import com.xl.projectno.data.protocol.ProtocolManager;
import com.xl.projectno.data.protocol.RequestBean;
import com.xl.projectno.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据层缓存实现类
 */
public class DataCache implements IDataCache<ModuleDataItemBean> {
	
	/**
	 * 模块的内存数据
	 */
	private Map<String, ModuleDataItemBean> mCache = new HashMap<String, ModuleDataItemBean>();
	
	/**
	 * 文件操作接口
	 */
	private IDataBeanCache mDataBeanCache;

	public DataCache(IDataBeanCache dataBeanCache) {
		mDataBeanCache = dataBeanCache;
	}

	@Override
	public boolean isCached(String cacheKey) {
		return mCache.containsKey(cacheKey);
	}

	@Override
	public ModuleDataItemBean getCache(String cacheKey) {
		ModuleDataItemBean cache = mCache.get(cacheKey);
//		if (cache == null) {
//			cache = mDataBeanCache.loadDataBean(cacheKey);
//			if(cache != null){
//				mCache.put(cacheKey, cache);
//			}
//		}
		return cache;
	}
 
	@Override
	public void saveCache(final String cacheKey, final ModuleDataItemBean t) {
		if (GoPluginEnv.DEBUG) {
			LogUtil.d(GoPluginEnv.TAG, String.format(
					"saveCache[cacheKey=%s,cache=%s]", cacheKey, t));
		}
		mCache.put(cacheKey, t);

		mDataBeanCache.saveDataBean(cacheKey, t);
	}

	@Override
	public void clearCache(String cacheKey, boolean deleteLocal) {
		if (GoPluginEnv.DEBUG) {
			LogUtil.d(GoPluginEnv.TAG, "cacheKey=" + cacheKey);
		}
		mCache.remove(cacheKey);
		if (deleteLocal) {
			mDataBeanCache.removeDataBean(cacheKey);
		}
	}

	@Override
	public void clearAll() {
		mCache.clear();
	}

	@Override
	public PageDataBean getPageCache(String cacheKey) {
		return getHomePage(cacheKey, new PageDataBean());
	}

	/**
	 * <br>功能简述:递归拿首页数据
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param cacheKey
	 * @param pageDataBean
	 * @return
	 */
	public PageDataBean getHomePage(String cacheKey, PageDataBean pageDataBean) {
		ModuleDataItemBean cache = this.getCache(cacheKey);
		if (cache != null) {
			int pageId = 0;
			int entranceId = GoPluginEnv.ENTRANCE_ENTER_GO_PLUGIN_STORE;
			RequestBean keyBean = ProtocolManager.parseCacheKey(cacheKey);
			if (keyBean != null) {
				pageId = keyBean.getPageId();
				entranceId = keyBean.getEntranceId();
			}
			
			if (pageDataBean.getRecursionCount() == 0) {
				if (keyBean.getModuleId() == GoPluginEnv.GO_PLUGIN_STORE_ROOT_VIRTUAL_ID
						|| keyBean.getModuleId() == GoPluginEnv.GO_STORE_STORE_ROOT_VIRTUAL_ID) {
					pageDataBean.setModuleId(cache.getModuleId());
				} else {
					pageDataBean.setModuleId(cache.getModuleId());
				}
				pageDataBean.setPageId(pageId);
			}
			pageDataBean.setRecursionCount(pageDataBean.getRecursionCount() + 1);
			
			if (cache.isCacheChildModuleInfoBean()) {
				for (int i = 0; i < cache.getModuleInfos().size(); i++) {
					ModuleInfoBean infoBean = cache.getModuleInfos().get(i);
					if (infoBean != null) {
						String infoBeanKey = ProtocolManager.getCacheKey(infoBean.getModuleId(), pageId, entranceId);
						this.getHomePage(infoBeanKey, pageDataBean);
					}
				}
			} 
			pageDataBean.addModuleItem(cache);
		}
		
		return pageDataBean;
	}
	
	@Override
	public boolean isLocalCached(String cacheKey) {
		return mDataBeanCache.isCache(cacheKey);
	}
}
