package com.xl.projectno.data.imageload;


import com.xl.projectno.volley.RequestManager;

/**
 * <br>类描述:
 * <br>功能详细描述:
 */
public class ImageLoadManager {
	private static ImageLoadManager sInstance;

	private CustomImageLoader mImageLoader;
	private ImageCacheManager mImageCache;

	public ImageLoadManager() {
		mImageCache = new ImageCacheManager();
		mImageLoader = new CustomImageLoader(RequestManager.getRequestQueue(), mImageCache);
	}

	public static ImageLoadManager getInstance() {
		if (sInstance == null) {
			sInstance = new ImageLoadManager();
		}
		return sInstance;
	}

	public CustomImageLoader getImageLoader() {
		return mImageLoader;
	}

	public ImageCacheManager getImageCacheManager() {
		return mImageCache;
	}
}
