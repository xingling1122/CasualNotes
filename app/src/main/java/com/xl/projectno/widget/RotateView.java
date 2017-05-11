package com.xl.projectno.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.xl.projectno.R;


/**
 * 360度旋转动画View
 */
public class RotateView extends ImageView {

	private Animation mRateAnimation;
	private int mDuration = 1000;



	/**
	 * @param context
	 * @param attrs
	 */
	public RotateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}



	private void init() {
		setBackgroundColor(0x33333333);
		setImageResource(R.drawable.goplay_go_progressbar);
		mRateAnimation = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mRateAnimation.setInterpolator(new LinearInterpolator());
		mRateAnimation.setDuration(mDuration);
		mRateAnimation.setRepeatCount(-1);
		mRateAnimation.setFillAfter(true);
		this.startAnimation(mRateAnimation);
	}

	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
		if (visibility == View.VISIBLE) {
			this.clearAnimation();
			this.startAnimation(mRateAnimation);
		} else {
			this.clearAnimation();
		}
	}
	
	public void setDuration(int duration){
		mDuration = duration;
		mRateAnimation.setDuration(mDuration);
	}
	
	public void startLoadingAnimaition() {
		this.startAnimation(mRateAnimation);
	}
	
	public void cleanLoadingAnimation() {
		this.clearAnimation();
	}
}
