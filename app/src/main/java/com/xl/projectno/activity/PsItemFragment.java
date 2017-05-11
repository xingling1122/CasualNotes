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

public class PsItemFragment extends Fragment implements View.OnClickListener {

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
            DataManager.getInstance().delUser();
            if (onfinishListener!=null){
                onfinishListener.finish();
            }
        }
    }

    private class WaitTask2 implements Runnable {
        public void run() {
            DataManager.getInstance().delUser();
            viewHolder.reset.setClickable(true);
            morphToSquare(viewHolder.reset,0);
            Toast.makeText(getActivity(),getString(R.string.resetfail)+ "--" + resultbean.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private WaitTask1 myTask1 = new WaitTask1();
    private WaitTask2 myTask2 = new WaitTask2();

    ThemeBean theme;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        theme = DataManager.getInstance().getCurrentTheme();
        LanguageUtils.ChangeLanguage(getContext(), LanguageUtils.current_language);
        View view = inflater.inflate(R.layout.ps_item, container, false);
        viewHolder = new ViewHolder(view);
        initItem();
        initData();
        return view;
    }

    private void initItem() {
        viewHolder.reset.setOnClickListener(this);
        viewHolder.reset.setProgressListener(new ProgressButton.ProgressListener() {
            @Override
            public void onComplete(ProgressButton button) {
                if (resetstatus){
                    morphToEnd(button,R.drawable.ic_done,theme.color);
                    handler.postDelayed(myTask1,1000);
                }else{
                    morphToEnd(button,R.drawable.ic_nodone,R.color.red_color);
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
            case R.id.reset:
                String psone = viewHolder.psone.getText().toString();
                String pstwo = viewHolder.pstwo.getText().toString();
                ValidateUtils validateUtils = ValidateUtils.getInstance(getContext());
                if(!validateUtils.validateps(psone,pstwo)){
                    return;
                }
                viewHolder.reset.setClickable(false);
                morphToProgress(viewHolder.reset);
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
                                    viewHolder.reset.setProgress(progress,network);
                                }
                            });
                        }
                    }
                },1000,100);
                LanguageUtils.ChangeLanguage(getContext(),LanguageUtils.current_language);
                UserBean user = DataManager.getInstance().getCurrentUser();
                DataManager.getInstance().alterps(user.name,user.password, psone, new ILoadDataListener<ResponseHeaderBean>() {
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
        }
    }

    class ViewHolder {
        public EditText psone;
        public EditText pstwo;
        public ProgressButton reset;
        public ViewHolder(View view){
            psone = (EditText) view.findViewById(R.id.psone);
            pstwo = (EditText)view.findViewById(R.id.pstwo);
            reset = (ProgressButton)view.findViewById(R.id.reset);
        }
    }
}
