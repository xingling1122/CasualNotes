package com.xl.projectno.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.xl.projectno.R;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.base.OnChangeListener;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.ThemeBean;
import com.xl.projectno.model.UserBean;
import com.xl.projectno.utils.DialogUtil;
import com.xl.projectno.utils.LanguageUtils;
import com.xl.projectno.utils.ValidateUtils;
import com.xl.projectno.volley.ILoadDataListener;
import com.xl.projectno.widget.DynamicButton;
import com.xl.projectno.widget.ProgressButton;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/4/25.
 */
public class LoginItemFragment extends Fragment implements View.OnClickListener {
    LoginChildViewHolder viewHolder;
    private int current_language;

    private Timer mTimer;
    int progress=0;
    private boolean loginstatus = false;
    private UserBean resultbean;
    private boolean network = false;

    private OnChangeListener onChangeListener;

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    public void setCurrent_language(int current_language){
        this.current_language = current_language;
    }

    Handler handler = new Handler();

    private class WaitTask1 implements Runnable {
        public void run() {
            Intent intent = new Intent();
            intent.setClass(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    private class WaitTask2 implements Runnable {
        public void run() {
            viewHolder.login.setClickable(true);
            morphToSquare(viewHolder.login,0);
            Toast.makeText(getActivity(),getString(R.string.loginfail)+ "--" + resultbean.message, Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent();
//            intent.setClass(getActivity(),LoginActivity.class);
//            startActivity(intent);
//            getActivity().finish();
        }
    }

    private WaitTask1 myTask1 = new WaitTask1();
    private WaitTask2 myTask2 = new WaitTask2();

    ThemeBean theme;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        theme = DataManager.getInstance().getCurrentTheme();
        View view = inflater.inflate(R.layout.login_item, container, false);
        viewHolder = new LoginChildViewHolder(view);
        viewHolder.forget.setOnClickListener(this);
//        viewHolder.login.setOnClickListener(this);
        viewHolder.create.setOnClickListener(this);
        LanguageUtils.ChangeLanguage(getContext(),current_language);
        initData();

        viewHolder.login.setProgressListener(new ProgressButton.ProgressListener() {
            @Override
            public void onComplete(ProgressButton button) {
                if (loginstatus){
                    morphToEnd(button,R.drawable.ic_done,theme.color);
                    handler.postDelayed(myTask1,1000);
                }else{
                    morphToEnd(button,R.drawable.ic_nodone,R.color.red_color);
                    handler.postDelayed(myTask2,1000);
                }
                mTimer.cancel();
            }
        });
        viewHolder.login.setOnClickListener(this);
        morphToSquare(viewHolder.login, 0);
        return view;
    }

    private void morphToSquare(final DynamicButton btnMorph, long duration) {
        DynamicButton.PropertyParam square = DynamicButton.PropertyParam.build()
                .duration(duration)
                .setCornerRadius(dimen(R.dimen.mb_corner_radius_2))
                .setWidth(dimen(R.dimen.mb_width_300))
                .setHeight(dimen(R.dimen.mb_height_56))
                .setColor(color(theme.color))
                .setPressedColor(color(theme.color))
                .text(getString(R.string.login));
        btnMorph.startChange(square);
    }

    private void morphToEnd(final DynamicButton btnMorph,int picture,int color) {
        DynamicButton.PropertyParam circle = DynamicButton.PropertyParam.build()
                .duration(500)
                .setCornerRadius(dimen(R.dimen.mb_height_56))
                .setWidth(dimen(R.dimen.mb_height_56))
                .setHeight(dimen(R.dimen.mb_height_56))
                .setColor(color(color))
                .icon(drawable(picture))
                .setPressedColor(color(theme.color));

        btnMorph.startChange(circle);
    }

    public void morphToProgress(final ProgressButton btnMorph){
        DynamicButton.PropertyParam progress=DynamicButton.PropertyParam.build()
                .duration(500)
                .setCornerRadius(dimen(R.dimen.mb_height_56))
                .setWidth(dimen(R.dimen.mb_width_200))
                .setHeight(dimen(R.dimen.mb_height_8))
                .setColor(color(R.color.gray))
                .setStrokeColor(color(theme.color))
                .setStrokeWidth(0)
                .setPressedColor(theme.color);

        btnMorph.startChange(progress);


    }

    public int dimen(@DimenRes int resId) {
        return (int) getResources().getDimension(resId);
    }

    public int color(@ColorRes int resId) {
        return getResources().getColor(resId);
    }

    public int integer(@IntegerRes int resId) {
        return getResources().getInteger(resId);
    }

    public Drawable drawable(int resId){
        return getResources().getDrawable(resId);
    }

    private void initData() {
        viewHolder.username.setHint(R.string.username);
        viewHolder.password.setHint(R.string.password);
        viewHolder.login.setText(R.string.login);
        viewHolder.forget.setText(R.string.forgetpsw);
        viewHolder.create.setText(R.string.create);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                String username = viewHolder.username.getText().toString();
                String password = viewHolder.password.getText().toString();
                ValidateUtils validateUtils = ValidateUtils.getInstance(getContext());
                if(!validateUtils.validate(username,password)){
                    return;
                }
                viewHolder.login.setClickable(false);
                morphToProgress(viewHolder.login);
                mTimer=new Timer();
                mTimer.schedule(new TimerTask(){
                    @Override
                    public void run() {
                        progress+=10;
                        if(progress>ProgressButton.MAX_PROGRESS){
                            progress=0;
                            //mTimer.cancel();
                        }
                        if (getActivity()!=null){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    viewHolder.login.setProgress(progress,network);
                                }
                            });
                        }
                    }
                },1000,100);
                LanguageUtils.ChangeLanguage(getContext(),current_language);
                DataManager.getInstance().login(username, password, new ILoadDataListener<UserBean>() {
                            @Override
                            public void onDataListner(UserBean result) {
                                network = true;
                                if (result != null && !TextUtils.isEmpty(result.token)) {
                                    DataManager.getInstance().updateCurrentUser(result);
                                    DataManager.getInstance().addItem(result);
                                    loginstatus=true;
                                } else {
                                    loginstatus=false;
                                    resultbean = result;
                                }
                            }

                            @Override
                            public void onErrorResponse(VolleyError arg0) {
                                Log.d("xingling", arg0.getMessage());
                                if (getActivity()!=null){
                                    Toast.makeText(getActivity(), getString(R.string.loginfail)+ "--" + arg0.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
                break;
            case R.id.forgetpsw:
                if (forgotPswListener!=null){
                    forgotPswListener.forget();
                }
                break;
            case R.id.create:
                if (onChangeListener!=null){
                    onChangeListener.onChange();
                }
                break;
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
