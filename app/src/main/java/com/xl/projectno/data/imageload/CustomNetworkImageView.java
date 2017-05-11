package com.xl.projectno.data.imageload;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.xl.projectno.utils.DrawCircle;

/**
 * <br>类描述:
 * <br>功能详细描述:
 */
public class CustomNetworkImageView extends ImageView {

	/** The URL of the network image to load */
	protected String mUrl;

	/**
	 * Resource ID of the image to be used as a placeholder until the network image is loaded.
	 */
	private int mDefaultImageId;

	/**
	 * Resource ID of the image to be used if the network response fails.
	 */
	protected int mErrorImageId;

	/** Local copy of the ImageLoader. */
	protected CustomImageLoader mImageLoader;

	/** Current ImageContainer. (either in-flight or finished) */
	protected CustomImageLoader.ImageContainer mImageContainer;

	protected boolean mIsMemoryCache;
	protected boolean mIsDiskCache;

	protected boolean mAutoCompression = true;

	public CustomNetworkImageView(Context context) {
		this(context, null);
	}

	public CustomNetworkImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Sets URL of the image that should be loaded into this view. Note that calling this will
	 * immediately either set the cached image (if available) or the default image specified by
	 * {@link NetworkImageView#setDefaultImageResId(int)} on the view.
	 *
	 * NOTE: If applicable, {@link NetworkImageView#setDefaultImageResId(int)} and
	 * {@link NetworkImageView#setErrorImageResId(int)} should be called prior to calling
	 * this function.
	 *
	 * @param url The URL that should be loaded into this ImageView.
	 */
	public void setImageUrl(String url) {
		setImageUrl(url, ImageLoadManager.getInstance().getImageLoader(), true, true);
	}

	/**
	 * Sets URL of the image that should be loaded into this view. Note that calling this will
	 * immediately either set the cached image (if available) or the default image specified by
	 * {@link NetworkImageView#setDefaultImageResId(int)} on the view.
	 *
	 * NOTE: If applicable, {@link NetworkImageView#setDefaultImageResId(int)} and
	 * {@link NetworkImageView#setErrorImageResId(int)} should be called prior to calling
	 * this function.
	 *
	 * @param url The URL that should be loaded into this ImageView.
	 * @param imageLoader ImageLoader that will be used to make the request.
	 */
//	public void setImageUrl(String url, CustomImageLoader imageLoader) {
//		setImageUrl(url, imageLoader, true, true);
//	}

	/**
	 * Sets URL of the image that should be loaded into this view. Note that calling this will
	 * immediately either set the cached image (if available) or the default image specified by
	 * {@link NetworkImageView#setDefaultImageResId(int)} on the view.
	 *
	 * NOTE: If applicable, {@link NetworkImageView#setDefaultImageResId(int)} and
	 * {@link NetworkImageView#setErrorImageResId(int)} should be called prior to calling
	 * this function.
	 *
	 * @param url The URL that should be loaded into this ImageView.
	 * @param imageLoader ImageLoader that will be used to make the request.
	 * @param isMemoryCache if true cache to memory
	 * @param isDiskCahce if true cache to file
	 */
	public void setImageUrl(String url, CustomImageLoader imageLoader, boolean isMemoryCache,
							boolean isDiskCahce) {
		mUrl = url;
		mImageLoader = imageLoader;
		mIsMemoryCache = isMemoryCache;
		mIsDiskCache = isDiskCahce;
		// The URL has potentially changed. See if we need to load it.
		loadImageIfNecessary(false);
	}

	/**
	 * Sets the default image resource ID to be used for this view until the attempt to load it
	 * completes.
	 */
	public void setDefaultImageResId(int defaultImage) {
		mDefaultImageId = defaultImage;
	}

	/**
	 * Sets the error image resource ID to be used for this view in the event that the image
	 * requested fails to load.
	 */
	public void setErrorImageResId(int errorImage) {
		mErrorImageId = errorImage;
	}

	/**
	 * Loads the image for the view if it isn't already loaded.
	 * @param isInLayoutPass True if this was invoked from a layout pass, false otherwise.
	 */
	protected void loadImageIfNecessary(final boolean isInLayoutPass) {
		int width = getWidth();
		int height = getHeight();

		boolean isFullyWrapContent = getLayoutParams() != null
				&& getLayoutParams().height == LayoutParams.WRAP_CONTENT
				&& getLayoutParams().width == LayoutParams.WRAP_CONTENT;
		// if the view's bounds aren't known yet, and this is not a wrap-content/wrap-content
		// view, hold off on loading the image.
		if (width == 0 && height == 0 && !isFullyWrapContent) {
			return;
		}
		// if the URL to be loaded in this view is empty, cancel any old requests and clear the
		// currently loaded image.
		if (TextUtils.isEmpty(mUrl) || mImageLoader == null || Uri.parse(mUrl).getHost() == null) {
			if (mImageContainer != null) {
				mImageContainer.cancelRequest();
				mImageContainer = null;
			}
//			setDefaultImageOrNull();
			return;
		}

		// if there was an old request in this view, check if it needs to be canceled.
		if (mImageContainer != null && mImageContainer.getRequestUrl() != null) {
			if (mImageContainer.getRequestUrl().equals(mUrl)) {
				// if the request is from the same URL, return.
				return;
			} else {
				// if there is a pre-existing request, cancel it if it's fetching a different URL.
				mImageContainer.cancelRequest();
				setDefaultImageOrNull();
			}
		}

		CustomImageLoader.ImageContainer newContainer = null;
		if (mAutoCompression) {
			newContainer = mImageLoader.get(mUrl, new CustomImageLoader.ImageListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					if (mErrorImageId != 0) {
						setImageResource(mErrorImageId);
					}
					if (mOnImageLoadfailedListener != null) {
						mOnImageLoadfailedListener.onImageLoadFailed();
					}
				}

				@Override
				public void onResponse(final CustomImageLoader.ImageContainer response, boolean isImmediate) {
					if (isImmediate && isInLayoutPass) {
						post(new Runnable() {
							@Override
							public void run() {
								onResponse(response, false);
							}
						});
						return;
					}

					Bitmap bmp = response.getBitmap();
					if (bmp!=null)
						bmp = DrawCircle.getCircleBitmap(bmp);
					setBitmap(bmp);
				}
			}, width, height, mIsMemoryCache, mIsDiskCache);
		} else {
			newContainer = mImageLoader.get(mUrl, new CustomImageLoader.ImageListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					if (mErrorImageId != 0) {
						setImageResource(mErrorImageId);
					}
					if (mOnImageLoadfailedListener != null) {
						mOnImageLoadfailedListener.onImageLoadFailed();
					}
				}

				@Override
				public void onResponse(final CustomImageLoader.ImageContainer response, boolean isImmediate) {
					// If this was an immediate response that was delivered inside of a layout
					// pass do not set the image immediately as it will trigger a requestLayout
					// inside of a layout. Instead, defer setting the image by posting back to
					// the main thread.
					if (isImmediate && isInLayoutPass) {
						post(new Runnable() {
							@Override
							public void run() {
								onResponse(response, false);
							}
						});
						return;
					}

					Bitmap bmp = response.getBitmap();
					if (bmp!=null)
						bmp = DrawCircle.getCircleBitmap(bmp);
					setBitmap(bmp);
				}
			}, mIsMemoryCache, mIsDiskCache);
		}
		// update the ImageContainer to be the new bitmap container.
		mImageContainer = newContainer;
	}

	protected void setBitmap(Bitmap bmp) {
		if (bmp != null) {
			boolean hanlded = false;
			if (mImageLoadedListener != null) {
				hanlded = mImageLoadedListener.onHandleImageLoaded(bmp);
			}

			if (!hanlded) {
				setImageBitmap(bmp);
			}
			setBackgroundDrawable(null);
		} else if (mDefaultImageId != 0) {
			setImageResource(mDefaultImageId);
		} else {
			setImageDrawable(null);
		}
	}

	protected void setDefaultImageOrNull() {
		if (mDefaultImageId != 0) {
			setImageResource(mDefaultImageId);
		} else {
			setImageDrawable(null);
		}
	}

	/**
	 * 功能简述: 是否开启自动压缩图片功能
	 * @param isEnable
	 */
	public void setAutoCompression(boolean isEnable) {
		mAutoCompression = isEnable;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		loadImageIfNecessary(true);
	}

	@Override
	protected void onDetachedFromWindow() {
		if (mImageContainer != null) {
			// If the view was bound to an image request, cancel it and clear
			// out the image from the view.
			mImageContainer.cancelRequest();
			setImageBitmap(null);
			// also clear out the container so we can reload the image if necessary.
			mImageContainer = null;
		}
		super.onDetachedFromWindow();
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		invalidate();
	}

	private OnImageLoadedListener mImageLoadedListener;
	private OnImageLoadfailedListener mOnImageLoadfailedListener;

	/**
	 *  图片加载完成监听,
	 * <br>类描述: 
	 * <br>功能详细描述:
	 * 
	 * @author  xiongsheng
	 * @date  [2014年3月7日]
	 */
	public static interface OnImageLoadedListener {
		/**
		 * <br>功能简述: 如果在该监听方法中返回true,那么KPNetworkImageView就不会对图片进行设置,返回false,则会进行设置
		 * <br>功能详细描述:
		 * <br>注意:
		 * @return
		 */
		public boolean onHandleImageLoaded(Bitmap bmp);
	}

	public void setImageLoadedListener(OnImageLoadedListener l) {
		mImageLoadedListener = l;
	}
	
	/**
	 * 图片加载失败监听
	 * <br>类描述:
	 * <br>功能详细描述:
	 * 
	 * @author  dengchengtian
	 * @date  [2016年3月18日]
	 */
	public static interface OnImageLoadfailedListener {
		public void onImageLoadFailed();
	}

	public void setOnImageLoadfailedListener(OnImageLoadfailedListener onImageLoadfailedListener) {
		mOnImageLoadfailedListener = onImageLoadfailedListener;
	}

}
