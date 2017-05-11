package com.xl.projectno.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.xl.projectno.R;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.base.OnChangeListener;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.ThemeBean;
import com.xl.projectno.model.UserBean;
import com.xl.projectno.utils.LanguageUtils;
import com.xl.projectno.widget.CustomViewPager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/18.
 */
public class LoginActivity  extends FragmentActivity {
    private CustomViewPager mViewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeBean theme = DataManager.getInstance().getCurrentTheme();
        setTheme(theme.style);
        setContentView(R.layout.login_main);
        mViewPager = (CustomViewPager) findViewById(R.id.main_viewpager);
        FragmentManager mFragMgr = getSupportFragmentManager();
        FragmentAdapter mAdapter = new FragmentAdapter(mFragMgr);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCanScroll(false);
        UserBean user = DataManager.getInstance().getUser();
        if (user!=null&&user.token!=null&&!user.token.equals("")){
            DataManager.getInstance().updateCurrentUser(user);
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        if (LanguageUtils.getCurrent_language()==LanguageUtils.CHINESE){
            mViewPager.setCurrentItem(1);
        }else{
            mViewPager.setCurrentItem(2);
        }
    }

    class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position==0){
                ResetPsFragment fragment = new ResetPsFragment();
                fragment.setLoCListener(new ResetPsFragment.LoCListener() {
                    @Override
                    public void onClick(int which) {
                        switch (which){
                            case ResetPsFragment.TYPE_LOGIN:
                                if (LanguageUtils.getCurrent_language()==LanguageUtils.CHINESE){
                                    mViewPager.setCurrentItem(1);
                                    LoginFragment fragment1 = (LoginFragment) getSupportFragmentManager().getFragments().get(0);
                                    fragment1.setViewpager(0);
                                }else{
                                    mViewPager.setCurrentItem(2);
                                    LoginENFragment fragment1 = (LoginENFragment) getSupportFragmentManager().getFragments().get(2);
                                    fragment1.setViewpager(0);
                                }
                                break;
                            case ResetPsFragment.TYPE_CREATE:
                                if (LanguageUtils.getCurrent_language()==LanguageUtils.CHINESE){
                                    mViewPager.setCurrentItem(1);
                                    LoginFragment fragment2 = (LoginFragment) getSupportFragmentManager().getFragments().get(0);
                                    fragment2.setViewpager(1);
                                }else{
                                    mViewPager.setCurrentItem(2);
                                    LoginENFragment fragment2 = (LoginENFragment) getSupportFragmentManager().getFragments().get(2);
                                    fragment2.setViewpager(1);
                                }
                                break;
                        }
                    }
                });
                return fragment;
            } else if (position == 1) {
                LoginFragment fragment = new LoginFragment();
                fragment.setOnChangeListener(new OnChangeListener() {
                    @Override
                    public void onChange() {
                        LanguageUtils.setCurrent_language(LanguageUtils.ENGLISH);
                        mViewPager.setCurrentItem(2);
                    }
                });
                fragment.setForgotPswListener(new LoginFragment.ForgotPswListener() {
                    @Override
                    public void forget() {
                        mViewPager.setCurrentItem(0);
                    }
                });
                return fragment;
            }else{
                LoginENFragment fragment = new LoginENFragment();
                fragment.setOnChangeListener(new OnChangeListener() {
                    @Override
                    public void onChange() {
                        LanguageUtils.setCurrent_language(LanguageUtils.CHINESE);
                        mViewPager.setCurrentItem(1);
                    }
                });
                fragment.setForgotPswListener(new LoginFragment.ForgotPswListener() {
                    @Override
                    public void forget() {
                        mViewPager.setCurrentItem(0);
                    }
                });
                return fragment;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
