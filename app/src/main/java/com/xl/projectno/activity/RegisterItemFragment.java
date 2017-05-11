package com.xl.projectno.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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
import com.xl.projectno.data.protocol.ResponseHeaderBean;
import com.xl.projectno.model.ThemeBean;
import com.xl.projectno.model.UserBean;
import com.xl.projectno.utils.DialogUtil;
import com.xl.projectno.utils.LanguageUtils;
import com.xl.projectno.utils.ValidateUtils;
import com.xl.projectno.volley.ILoadDataListener;
import com.xl.projectno.widget.DynamicButton;
import com.xl.projectno.widget.ProgressButton;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/4/25.
 */
public class RegisterItemFragment extends Fragment implements View.OnClickListener{
    RegisterChildViewHolder viewHolder;

    private int current_language;

    private Timer mTimer;
    int progress=0;
    private boolean registerstatus = false;
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
            Intent intent = new Intent(getContext(),MainActivity.class);
            startActivity(intent);
            getActivity().finish();
            Toast.makeText(getActivity(), getString(R.string.registersuccess), Toast.LENGTH_SHORT).show();
        }
    }

    private class WaitTask2 implements Runnable {
        public void run() {
            morphToSquare(viewHolder.register,0);
            viewHolder.register.setClickable(true);
            Toast.makeText(getActivity(),getString(R.string.registerfail)+"--" + resultbean.message, Toast.LENGTH_SHORT).show();
        }
    }

    private WaitTask1 myTask1 = new WaitTask1();
    private WaitTask2 myTask2 = new WaitTask2();

    ThemeBean theme;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        theme = DataManager.getInstance().getCurrentTheme();
        View view = inflater.inflate(R.layout.register_item, container, false);
        viewHolder = new RegisterChildViewHolder(view);
        viewHolder.register.setOnClickListener(this);
        viewHolder.login.setOnClickListener(this);
        LanguageUtils.ChangeLanguage(getContext(),current_language);
        initData();

        viewHolder.register.setProgressListener(new ProgressButton.ProgressListener() {
            @Override
            public void onComplete(ProgressButton button) {
                if (registerstatus){
                    morphToEnd(button,R.drawable.ic_done,theme.color);
                    handler.postDelayed(myTask1,1000);
                }else{
                    morphToEnd(button,R.drawable.ic_nodone,R.color.red_color);
                    handler.postDelayed(myTask2,1000);
                }
                mTimer.cancel();
            }
        });
        morphToSquare(viewHolder.register, 0);
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
                .text(getString(R.string.register));
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

    private void initData(){
        viewHolder.username.setHint(R.string.username);
        viewHolder.password.setHint(R.string.password);
        viewHolder.register.setText(R.string.register);
        viewHolder.login.setText(R.string.own);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                final String username = viewHolder.username.getText().toString();
                final String password = viewHolder.password.getText().toString();
                ValidateUtils validateUtils = ValidateUtils.getInstance(getContext());
                if(!validateUtils.validate(username,password)){
                    return;
                }
                viewHolder.register.setClickable(false);
                morphToProgress(viewHolder.register);
                mTimer=new Timer();
                mTimer.schedule(new TimerTask(){
                    @Override
                    public void run() {
                        progress+=10;
                        if(progress>ProgressButton.MAX_PROGRESS){
                            //mTimer.cancel();
                            progress=0;
                        }
                        if (getActivity()!=null){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    viewHolder.register.setProgress(progress,network);
                                }
                            });
                        }
                    }
                },1000,100);
                LanguageUtils.ChangeLanguage(getContext(),current_language);
                DataManager.getInstance().register(username, password, new ILoadDataListener<UserBean>() {
                            @Override
                            public void onDataListner(UserBean result) {
                                network = true;
                                if (result != null&& !TextUtils.isEmpty(result.token)) {
                                    result.name=username;
                                    result.password=password;
                                    DataManager.getInstance().updateCurrentUser(result);
                                    DataManager.getInstance().addItem(result);
                                    registerstatus = true;
                                } else {
                                    resultbean = result;
                                }
                            }

                            @Override
                            public void onErrorResponse(VolleyError arg0) {
                                Log.d("xingling", arg0.getMessage());
                                if (getActivity()!=null)
                                    Toast.makeText(getActivity(), getString(R.string.registerfail)+"--" + arg0.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                break;
            case R.id.login:
                if (onChangeListener!=null){
                    onChangeListener.onChange();
                }
                break;
        }
    }
}
