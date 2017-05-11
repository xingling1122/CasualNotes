package com.xl.projectno.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xl.projectno.R;

/**
 * Created by Administrator on 2017/3/28.
 */
public class TabViewPager extends LinearLayout {
    /* 数据段begin */
    public final static String TAG = "TabViewPager";
    private Context mContext;

    private LinearLayout mTabHost;
    private ImageView mUnderline;
    private ViewPager mViewPager;

    //tab及underline宽度，也是underline的最小移动距离
    private int mTabWidth;
    /* 数据段end */

    /* 函数段begin */
    public TabViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        inflate(mContext, R.layout.tab_view_pager, this);
        initViews();
    }

    private void initViews() {
        mTabHost = (LinearLayout) findViewById(R.id.tab_host);
        mUnderline = (ImageView) findViewById(R.id.tab_underline);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    public void initTabs(String[] tabTitles, int parentWidth) {
        LinearLayout.LayoutParams tabHostLayoutParams;
        TextView tab;

        mTabWidth = parentWidth / tabTitles.length;

        //设置宽度
        if (tabTitles.length > 0) {
            tabHostLayoutParams = new LinearLayout.LayoutParams(mTabWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        } else {
            return;
        }

        //动态添加tab
        for (int loopVal = 0; loopVal < tabTitles.length; loopVal++) {
            tab = new TextView(mContext);
            tab.setText(tabTitles[loopVal]);
            tab.setTextSize(22);
            tab.setTextColor(getResources().getColor(R.color.white));

            tabHostLayoutParams.weight = 1;
            tabHostLayoutParams.gravity = Gravity.CENTER_VERTICAL;
            tab.setLayoutParams(tabHostLayoutParams);
            tab.setGravity(Gravity.CENTER);

            tab.setOnClickListener(new TabOnClickListener(loopVal));

            mTabHost.addView(tab);
        }

        //设置underline宽度，使得下划线与tab宽度保持一致
        FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(mTabWidth, 20);
        frameLayoutParams.gravity = Gravity.BOTTOM;
        mUnderline.setLayoutParams(frameLayoutParams);
        mUnderline.setBackgroundDrawable(getResources().getDrawable(R.drawable.henxian));
    }

    public void setAdapter(PagerAdapter pagerAdapter) {
        mViewPager.setAdapter(pagerAdapter);
        //滑动viewPager时也要执行mUnderline的移动动画
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int currentPosition = -1;
            private int nextPosition = -1;

            @Override
            public void onPageSelected(int position) {
                nextPosition = position;
                //mUnderline的移动动画
                mUnderline.startAnimation(new UnderlineTranslateAnimation(currentPosition * mTabWidth, nextPosition * mTabWidth, 0, 0));
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void setCurrentItem(int position) {
        //记录当前的位置后再设置选中位置
        int currentPosition = mViewPager.getCurrentItem();
        mViewPager.setCurrentItem(position);
        int nextPosition = mViewPager.getCurrentItem();

        //mUnderline的移动动画
        mUnderline.startAnimation(new UnderlineTranslateAnimation(currentPosition * mTabWidth, nextPosition * mTabWidth, 0, 0));
    }
    /* 函数段end */

    /* 内部类begin */
    private class TabOnClickListener implements OnClickListener {
        private int viewPosition = -1;

        public TabOnClickListener(int position) {
            viewPosition = position;
        }

        @Override
        public void onClick(View v) {

            setCurrentItem(viewPosition);
        }
    }

    private class UnderlineTranslateAnimation extends TranslateAnimation {
        public UnderlineTranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
            super(fromXDelta, toXDelta, fromYDelta, toYDelta);

            setFillAfter(true);
        }

    }
    /* 内部类end */
}
