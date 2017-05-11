package com.xl.projectno.data.imageload;

import android.content.Context;


import com.xl.projectno.utils.SharedPreferencesUtils;
import com.xl.projectno.volley.GoPluginEnv;

import java.io.File;

/**
 * 用于清除本地图片缓存
 */
public class DeleteLocalImageCacheRunable implements Runnable {
	/**
	 * 最大缓存
	 */
	public static final long CACHE_MAX_SIZE = 200 * 1024 * 1024;
	/**
	 * 检查时间间隔
	 */
	public static final long CHECK_CACHE_TIME = 4 * 60 * 60 * 1000;
	
	/**
	 * 检查是否需要去清除本地图片缓存 8小时检查一次
	 */
	public void start(Context context) {
		SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(context, GoPluginEnv.SharedPreferencesEnv.GOPLUGIN_REQUEST_INFO);
		long time = sharedPreferencesUtils.getLong(GoPluginEnv.SharedPreferencesEnv.IMAGE_CHCHE_TIME_ID, 0);
		long currentTime = System.currentTimeMillis();
		if ((currentTime - time) >  CHECK_CACHE_TIME) {
			/**
			 * 间隔时间到了
			 */
			sharedPreferencesUtils.putLong(GoPluginEnv.SharedPreferencesEnv.IMAGE_CHCHE_TIME_ID, currentTime);
			sharedPreferencesUtils.commit();
			
			Thread thread = new Thread(this);
			thread.setName("DeleteLocalImageCacheRunable");
			thread.start();
		}
	}
	
	/**
	 * 获取目录文件大小
	 * @param f
	 * @return
	 */
	public long getFolderSize(File f) {
	    long size = 0;
	    if (f.isDirectory()) {
	        for (File file : f.listFiles()) {    
	            size += getFolderSize(file);
	        }
	    } else {
	        size = f.length();
	    }
	    return size;
	}
	
	@Override
	public void run() {
		/**
		 * 超过了最大缓存 清除一半的缓存
		 */
		File rootDirectory =  new File(GoPluginEnv.Path.IMAGES_DIR);
		if (rootDirectory.exists()) {
			long totalSpace = getFolderSize(rootDirectory);
			if (totalSpace >= CACHE_MAX_SIZE) {
				File[] fileList = rootDirectory.listFiles();
				if (fileList == null) {
					return;
				}
				for (int  i = 0; i < fileList.length / 2; i++) {
					File file = fileList[i];
					if (file != null) {
						file.delete();
					}
				}
			}
		}
	
	}

}
