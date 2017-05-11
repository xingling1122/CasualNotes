package com.xl.projectno.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/3/17.
 */
public class BottomInput extends RelativeLayout {

    View downinput;

    private int downinputHeight;
    private int parentHeight;

    public BottomInput(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        downinput = getChildAt(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        contentWidth = getMeasuredWidth();
        downinputHeight = downinput.getMeasuredHeight();
        parentHeight = getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        downinput.layout(0, parentHeight-downinputHeight, getMeasuredWidth(), parentHeight-10);
    }

}
