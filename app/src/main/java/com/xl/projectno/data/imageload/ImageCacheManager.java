package com.xl.projectno.data.imageload;

import android.graphics.Bitmap;


/**
 * <br>类描述:
 * <br>功能详细描述:
 */
public class ImageCacheManager implements CustomImageLoader.ImageCache {
	private MemoryImageCache mMemoryImageCache;

	public ImageCacheManager() {
		mMemoryImageCache = MemoryImageCache.getInstance();
	}

	@Override
	public Bitmap getBitmap(String key) {
		Bitmap bitmap = mMemoryImageCache.getBitmap(key);
		return bitmap;
	}

	@Override
	public void putBitmap(String key, Bitmap data) {
		mMemoryImageCache.putBitmap(key, data);

	}

}
