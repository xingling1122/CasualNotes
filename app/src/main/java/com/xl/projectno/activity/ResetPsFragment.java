package com.xl.projectno.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.xl.projectno.R;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.base.OnChangeListener;
import com.xl.projectno.data.protocol.ResponseHeaderBean;
import com.xl.projectno.model.ThemeBean;
import com.xl.projectno.model.UserBean;
import com.xl.projectno.utils.LanguageUtils;
import com.xl.projectno.utils.ValidateUtils;
import com.xl.projectno.volley.ILoadDataListener;
import com.xl.projectno.widget.CustomViewPager;
import com.xl.projectno.widget.DynamicButton;
import com.xl.projectno.widget.ProgressButton;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/5/5.
 */

public class ResetPsFragment extends Fragment{
    ViewHolder viewHolder;

    public static final int TYPE_LOGIN = 0;
    public static final int TYPE_CREATE = 1;

    ThemeBean theme;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        theme = DataManager.getInstance().getCurrentTheme();
        LanguageUtils.ChangeLanguage(getContext(),LanguageUtils.current_language);
        View view = inflater.inflate(R.layout.resetps_main_layout, container, false);
        viewHolder = new ViewHolder(view);
        FragmentManager mFragMgr = getChildFragmentManager();
        FragmentAdapter mAdapter = new FragmentAdapter(mFragMgr);
        viewHolder.viewPager.setAdapter(mAdapter);
        viewHolder.viewPager.setCanScroll(false);
        viewHolder.viewPager.setCurrentItem(0);
        return view;
    }

    class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                ResetItemFragment fragment = new ResetItemFragment();
                fragment.setOnfinishListener(new ResetItemFragment.OnfinishListener() {
                    @Override
                    public void finish() {
                        viewHolder.viewPager.setCurrentItem(1);
                    }

                    @Override
                    public void nofinish() {
                        viewHolder.viewPager.setCurrentItem(0);
                    }
                });
                fragment.setLoCListener(new ResetItemFragment.LoCListener() {
                    @Override
                    public void onClick(int which) {
                        if (listener!=null){
                            listener.onClick(which);
                        }
                    }
                });
                return fragment;
            }else{
                PsItemFragment fragment = new PsItemFragment();
                fragment.setOnfinishListener(new PsItemFragment.OnfinishListener() {
                    @Override
                    public void finish() {
                        if (listener!=null){
                            listener.onClick(ResetPsFragment.TYPE_LOGIN);
                        }
                    }

                    @Override
                    public void nofinish() {
                        viewHolder.viewPager.setCurrentItem(0);
                    }
                });
                return fragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private LoCListener listener;

    public void setLoCListener(LoCListener listener){
        this.listener = listener;
    }

    public interface LoCListener{
        void onClick(int which);
    }

    class ViewHolder {
        CustomViewPager viewPager;
        public ViewHolder(View view){
            viewPager = (CustomViewPager)view.findViewById(R.id.view_pager);
        }
    }
}
