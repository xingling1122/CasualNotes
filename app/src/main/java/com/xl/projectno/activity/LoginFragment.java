package com.xl.projectno.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xl.projectno.R;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.base.OnChangeListener;
import com.xl.projectno.model.ThemeBean;
import com.xl.projectno.utils.LanguageUtils;

/**
 * Created by Administrator on 2017/4/18.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    ViewHolder viewHolder;

    private OnChangeListener onChangeListener;

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LanguageUtils.ChangeLanguage(getContext(),LanguageUtils.CHINESE);
        View view = inflater.inflate(R.layout.login_main_layout, container, false);
        viewHolder = new ViewHolder(view);
        viewHolder.changeversion.setOnClickListener(this);
        FragmentManager mFragMgr = getChildFragmentManager();
        FragmentAdapter mAdapter = new FragmentAdapter(mFragMgr);
        viewHolder.viewPager.setAdapter(mAdapter);
        viewHolder.viewPager.setCanScroll(false);
        viewHolder.viewPager.setCurrentItem(0);
        initData();
        return view;
    }

    public void setViewpager(int position){
        viewHolder.viewPager.setCurrentItem(position);
    }

    private void initData(){
        viewHolder.logo.setText(R.string.name_logo);
        viewHolder.changeversion.setText(R.string.changeversion);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changeversion:
                if (onChangeListener != null) {
                    onChangeListener.onChange();
                }
                break;
        }
    }

    class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                LoginItemFragment fragment = new LoginItemFragment();
                fragment.setCurrent_language(LanguageUtils.CHINESE);
                fragment.setOnChangeListener(new OnChangeListener() {
                    @Override
                    public void onChange() {
                        viewHolder.viewPager.setCurrentItem(1);
                    }
                });
                fragment.setForgotPswListener(new LoginItemFragment.ForgotPswListener() {
                    @Override
                    public void forget() {
                        if (forgotPswListener!=null){
                            forgotPswListener.forget();
                        }
                    }
                });
                return fragment;
            }else{
                RegisterItemFragment fragment = new RegisterItemFragment();
                fragment.setCurrent_language(LanguageUtils.CHINESE);
                fragment.setOnChangeListener(new OnChangeListener() {
                    @Override
                    public void onChange() {
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

    private ForgotPswListener forgotPswListener;

    public void setForgotPswListener(ForgotPswListener forgotPswListener){
        this.forgotPswListener=forgotPswListener;
    }

    public interface ForgotPswListener{
        void forget();
    }
}
