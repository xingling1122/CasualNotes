package com.xl.projectno.volley;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.Build;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.xl.projectno.base.MposApp;

import java.io.File;

/**
 * <br>类描述:
 * <br>功能详细描述:
 */
public class RequestManager {
	/** Default on-disk cache directory. */
	// Default maximum disk usage in bytes
	private static final int DEFAULT_DISK_USAGE_BYTES = 5 * 1024 * 1024;

	private static RequestQueue sRequestQueue;

	private RequestManager() {

	}

	/**
	 * @param context
	 * 			application context
	 */
	public static void init(Context context) {
		sRequestQueue = newRequestQueue(context, null);
	}

	public static RequestQueue getRequestQueue() {
		if(sRequestQueue == null){
			 sRequestQueue = newRequestQueue(MposApp.getApp().getApplicationContext());
		}
		return sRequestQueue;
	}
	
	public static Bitmap getBitmapFromCache(String url) {
		Bitmap bitmap = null;
		if (sRequestQueue != null) {
			Cache cache = sRequestQueue.getCache();
			if (cache != null) {
				Entry bitmapEntry = cache.get(url);
				if (bitmapEntry != null) {
					byte[] data = bitmapEntry.data;
					BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
					decodeOptions.inJustDecodeBounds = true;
					BitmapFactory.decodeByteArray(data, 0, data.length,
							decodeOptions);
					int actualWidth = decodeOptions.outWidth;
					int actualHeight = decodeOptions.outHeight;
					decodeOptions.inJustDecodeBounds = false;
					decodeOptions.inSampleSize = findBestSampleSize(
							actualWidth, actualHeight, actualWidth,
							actualHeight);
					Bitmap tempBitmap = BitmapFactory.decodeByteArray(data, 0,
							data.length, decodeOptions);
					if (tempBitmap != null
							&& (tempBitmap.getWidth() > actualWidth || tempBitmap
									.getHeight() > actualHeight)) {
						bitmap = Bitmap.createScaledBitmap(tempBitmap,
								actualWidth, actualHeight, true);
						tempBitmap.recycle();
					} else {
						bitmap = tempBitmap;
					}
				}
			}
		}
		return bitmap;
	}	
	
	static int findBestSampleSize(int actualWidth, int actualHeight, int desiredWidth,
			int desiredHeight) {
		double wr = (double) actualWidth / desiredWidth;
		double hr = (double) actualHeight / desiredHeight;
		double ratio = Math.min(wr, hr);
		float n = 1.0f;
		while ((n * 2) <= ratio) {
			n *= 2;
		}
		return (int) n;
	}

	/**
	 * Creates a default instance of the worker pool and calls {@link RequestQueue#start()} on it.
	 *
	 * @param context A {@link Context} to use for creating the cache dir.
	 * @return A started {@link RequestQueue} instance.
	 */
	private static RequestQueue newRequestQueue(Context context, HttpStack stack) {
		File cacheDir = new File(GoPluginEnv.Path.IMAGES_DIR);
		String userAgent = "volley/0";
		try {
			String packageName = context.getPackageName();
			PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
			userAgent = packageName + "/" + info.versionCode;
		} catch (NameNotFoundException e) {
		}

		if (stack == null) {
			if (Build.VERSION.SDK_INT >= 9) {
				stack = new HurlStack();
			} else {
				// Prior to Gingerbread, HttpUrlConnection was unreliable.
				// See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
				stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
			}
		}

		Network network = new BasicNetwork(stack);

		RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir, DEFAULT_DISK_USAGE_BYTES), network);
		queue.start();

		return queue;
	}
	
	/**
	 * Creates a default instance of the worker pool and calls {@link RequestQueue#start()} on it.
	 *
	 * @param context A {@link Context} to use for creating the cache dir.
	 * @return A started {@link RequestQueue} instance.
	 */
	private static RequestQueue newRequestQueue(Context context) {
		return newRequestQueue(context, null);
	}
}
