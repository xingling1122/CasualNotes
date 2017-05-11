package com.xl.projectno.data.imageload;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.xl.projectno.utils.LogUtil;
import com.xl.projectno.volley.GoPluginEnv;

import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

/**
 */

public class MemoryImageCache implements ImageCache {
	/**
	 * 强引用缓存，线程安全
	 * 当缓存超过限定大小时，该缓存会把最久没有使用的图片从缓存中移除，直到小于限制值为止
	 */
	private LruCache<String, Bitmap> mLruCache = null;

	private static volatile MemoryImageCache sINSTANCE;
	
	/**
	 * 弱引用缓存
	 */
	private ConcurrentHashMap<String, SoftReference<Bitmap>> mSoftCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>();

	private MemoryImageCache(int maxMemorySize) {
		mLruCache = new LruCache<String, Bitmap>(maxMemorySize) {

			@Override
			protected void entryRemoved(boolean evicted, String key, Bitmap oldValue,
					Bitmap newValue) {
				if (evicted) {
					//如果超过了大小，就把从强引用移除的图片加入到弱引用中
					mSoftCache.put(key, new SoftReference<Bitmap>(oldValue));
				}
			}

			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}

		};
	}

	public static MemoryImageCache getInstance() {
		if (sINSTANCE == null) {
			synchronized (MemoryImageCache.class) {
				if (null == sINSTANCE) {
					int maxMemory = (int) (Runtime.getRuntime().maxMemory());
					// 使用最大可用内存值的1/8作为缓存的大小。 
					int cacheSize = maxMemory / 8;
//					// 最大能使用5Ｍ
//					if (cacheSize > DEFAULT_MAX_MEMORY_SIZE) {
//						cacheSize = DEFAULT_MAX_MEMORY_SIZE;
//					}

					if (GoPluginEnv.DEBUG) {
						LogUtil.d("RssImageCache", "maxMemory=" + maxMemory + ",cacheSize="
								+ cacheSize);
					}
					sINSTANCE = new MemoryImageCache(cacheSize);
				}
			}
		}
		return sINSTANCE;
	}

	public void clear() {
		if (mLruCache != null) {
			mLruCache.evictAll();
		}
		if (mSoftCache != null) {
			mSoftCache.clear();
		}
	}

	@Override
	public Bitmap getBitmap(String key) {
		if (key == null) {
			return null;
		}
		//先从强引用缓存中取，如果取不到的话，再从弱引用缓存里面取
		Bitmap bitmap = mLruCache.get(key);

		if (bitmap == null) {
			SoftReference<Bitmap> softReference = mSoftCache.get(key);
			if (softReference != null) {
				bitmap = softReference.get();
			}
		}

		return bitmap;
	}

	@Override
	public void putBitmap(String key, Bitmap data) {
		if (key == null || data == null) {
			return;
		}
		if (mLruCache != null) {
			mLruCache.put(key, data);
		}

	}
}
