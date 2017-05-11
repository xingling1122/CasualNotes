package com.xl.projectno.activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.xl.projectno.data.protocol.ResponseHeaderBean;
import com.xl.projectno.model.ThemeBean;
import com.xl.projectno.model.UserBean;
import com.xl.projectno.utils.LanguageUtils;
import com.xl.projectno.utils.ValidateUtils;
import com.xl.projectno.volley.ILoadDataListener;
import com.xl.projectno.widget.DynamicButton;
import com.xl.projectno.widget.ProgressButton;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/5/8.
 */

public class ResetItemFragment extends Fragment implements View.OnClickListener{
    ViewHolder viewHolder;

    private Timer mTimer;
    int progress=0;
    private boolean resetstatus = false;
    private ResponseHeaderBean resultbean;
    private boolean network = false;

    Handler handler = new Handler();

    public interface OnfinishListener{
        void finish();
        void nofinish();
    }

    private OnfinishListener onfinishListener;

    public void setOnfinishListener(OnfinishListener onfinishListener){
        this.onfinishListener = onfinishListener;
    }

    private class WaitTask1 implements Runnable {
        public void run() {
            UserBean user = resultbean.getUser();
            DataManager.getInstance().updateCurrentUser(user);
            if (onfinishListener!=null){
                onfinishListener.finish();
            }
        }
    }

    private class WaitTask2 implements Runnable {
        public void run() {
            morphToSquare(viewHolder.reset, 0);
            Toast.makeText(getActivity(),getString(R.string.resetfail)+ "--" + resultbean.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private WaitTask1 myTask1 = new WaitTask1();
    private WaitTask2 myTask2 = new WaitTask2();

    public static final int TYPE_LOGIN = 0;
    public static final int TYPE_CREATE = 1;

    ThemeBean theme;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        theme = DataManager.getInstance().getCurrentTheme();
        LanguageUtils.ChangeLanguage(getContext(),LanguageUtils.current_language);
        View view = inflater.inflate(R.layout.reset_item, container, false);
        viewHolder = new ViewHolder(view);
        initItem();
        initData();
        return view;
    }

    private void initItem() {
        viewHolder.login.setOnClickListener(this);
        viewHolder.create.setOnClickListener(this);
        viewHolder.reset.setOnClickListener(this);
        viewHolder.getcheckcode.setOnClickListener(this);
        viewHolder.change.setOnClickListener(this);

        viewHolder.reset.setProgressListener(new ProgressButton.ProgressListener() {
            @Override
            public void onComplete(ProgressButton button) {
                if (resetstatus){
                    morphToEnd(button,R.drawable.ic_done,theme.color);
                    handler.postDelayed(myTask1,1000);
                }else{
                    morphToEnd(button,R.drawable.ic_nodone,R.color.red_color);
                    viewHolder.reset.setClickable(true);
                    handler.postDelayed(myTask2,1000);
                }
                mTimer.cancel();
                mTimer=null;
            }
        });
        morphToSquare(viewHolder.reset, 0);
    }

    private void initData(){

    }

    private void morphToSquare(final DynamicButton btnMorph, long duration) {
        DynamicButton.PropertyParam square = DynamicButton.PropertyParam.build()
                .duration(duration)
                .setCornerRadius(dimen(R.dimen.mb_corner_radius_2))
                .setWidth(dimen(R.dimen.mb_width_300))
                .setHeight(dimen(R.dimen.mb_height_56))
                .setColor(color(theme.color))
                .setPressedColor(color(theme.color))
                .text(getString(R.string.reset));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                if (listener!=null){
                    listener.onClick(TYPE_LOGIN);
                }
                break;
            case R.id.create:
                if (listener!=null){
                    listener.onClick(TYPE_CREATE);
                }
                break;
            case R.id.reset:
                String phone = viewHolder.phone.getText().toString();
                String checkcode = viewHolder.checkcode.getText().toString();
                ValidateUtils validateUtils = ValidateUtils.getInstance(getContext());
                if(!validateUtils.validaters(phone,checkcode)){
                    return;
                }
                viewHolder.reset.setClickable(false);
                morphToProgress(viewHolder.reset);
                mTimer =new Timer();
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
                                    viewHolder.reset.setProgress(progress,network);
                                }
                            });
                        }
                    }
                },1000,100);
                LanguageUtils.ChangeLanguage(getContext(),LanguageUtils.current_language);
                DataManager.getInstance().resetps(null,phone, checkcode, new ILoadDataListener<ResponseHeaderBean>() {
                            @Override
                            public void onDataListner(ResponseHeaderBean result) {
                                network = true;
                                if (result != null && result.getStatus()) {
                                    resultbean = result;
                                    resetstatus=true;
                                } else {
                                    resetstatus=false;
                                    resultbean = result;
                                }
                            }

                            @Override
                            public void onErrorResponse(VolleyError arg0) {
                                Log.d("xingling", arg0.getMessage());
                                if (getActivity()!=null){
                                    Toast.makeText(getActivity(), getString(R.string.resetfail)+ "--" + arg0.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
                break;
            case R.id.getcheckcode:
                String phone1 = viewHolder.phone.getText().toString();
                LanguageUtils.ChangeLanguage(getContext(),LanguageUtils.current_language);
                if (phone1.equals("")){
                    Toast.makeText(getContext(),getString(R.string.inputphone),Toast.LENGTH_SHORT).show();
                    return;
                }
                DataManager.getInstance().generatecode(null, phone1, new ILoadDataListener<ResponseHeaderBean>() {
                    @Override
                    public void onDataListner(ResponseHeaderBean result) {
                        if (result.getStatus()){
                            Toast.makeText(getContext(),getString(R.string.generatecodesuccess),Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(),result.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getContext(),volleyError.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.change:
                break;
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
        public ImageView change;
        public EditText phone;
        public EditText checkcode;
        public TextView getcheckcode;
        public ProgressButton reset;
        public TextView login;
        public TextView create;
        public ViewHolder(View view){
            change = (ImageView) view.findViewById(R.id.change);
            phone = (EditText) view.findViewById(R.id.phone);
            checkcode = (EditText)view.findViewById(R.id.checkcode);
            getcheckcode = (TextView)view.findViewById(R.id.getcheckcode);
            reset = (ProgressButton)view.findViewById(R.id.reset);
            login = (TextView)view.findViewById(R.id.login);
            create = (TextView)view.findViewById(R.id.create);
        }
    }
}
