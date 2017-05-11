package com.xl.projectno.volley;


import com.xl.projectno.data.bean.ModuleDataItemBean;
import com.xl.projectno.utils.FileUtils;

/**
 * 类描述: 本地缓冲数据实现
 */
public class DataFileCache implements IDataBeanCache {

	@Override
	public boolean saveDataBean(String key, ModuleDataItemBean moduleDataItemBean) {
		boolean success = true;
		try {
			String json = moduleDataItemBean.toJSON().toString();
			FileUtils.saveByteToSDFile(json.getBytes(), key);
		} catch (Exception e) {
			success = false;
			e.printStackTrace();
		}
		return success;
	}

	@Override
	public ModuleDataItemBean loadDataBean(String key) {
		ModuleDataItemBean moduleDataItemBean = null;
		try {
			String json = new String(FileUtils.readFileToByte(key));
			moduleDataItemBean = new ModuleDataItemBean();
			moduleDataItemBean.parseJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return moduleDataItemBean;
	}

	@Override
	public boolean removeDataBean(String key) {
		return FileUtils.deleteFile(key);
	}

	@Override
	public boolean isCache(String key) {
		return FileUtils.isFileExists(key);
	}

}
