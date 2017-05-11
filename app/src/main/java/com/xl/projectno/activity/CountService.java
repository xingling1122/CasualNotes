package com.xl.projectno.activity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xl.projectno.ICountInterface;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.InventoryBean;
import com.xl.projectno.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/29.
 */
public class CountService extends Service {
    long s = 1 * 1 * 30;
    ArrayList<TimeUtils> timeUtils;
    ArrayList<String> times;

    ArrayList<BaseBean> data;

    Handler handler;

    boolean flag = false;

    public static final String action = "com.xl.counservice";

    private CounterTask myTask = new CounterTask();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

//    private CountBinder countBinder;

    @Override
    public void onCreate() {
        super.onCreate();
//        countBinder = new CountBinder();
        data = DataManager.getInstance().getDropInventorys();
        timeUtils = new ArrayList<>();
        times = new ArrayList<>();
        handler = new Handler();
        handler.postDelayed(myTask, 0);

        IntentFilter intentFilter = new IntentFilter(action);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private class CounterTask implements Runnable {
        public void run() {
            try {
                refresh();
            } catch (Exception e) {
            }
            handler.postDelayed(myTask, 1000);
        }
    }

    public void refresh() {
        times.clear();
        if (timeUtils.size() < data.size()) {
            for (int i = timeUtils.size(); i < data.size(); i++) {
                TimeUtils utils = new TimeUtils(s);
                timeUtils.add(i, utils);
            }
        } else if (timeUtils.size() > data.size()) {
//            for (int i = data.size(); i < timeUtils.size(); i++) {
//                timeUtils.remove(i);
//            }
            ArrayList<Integer> ndels = new ArrayList<>();
            for(int i=0;i<data.size();i++){
                InventoryBean inventoryBean = (InventoryBean) data.get(i);
                ndels.add(inventoryBean.position);
            }
            int size = timeUtils.size();
            for (int i=0;i<size;i++){
                boolean flag =false;
                for(int j=0;j<ndels.size();j++){
                    if (i==j){
                        flag =true;
                        break;
                    }
                }
                if (flag){
                    ndels.remove(i);
                    timeUtils.remove(i);
                }
            }
        }
        if (timeUtils.size()==data.size()){
            for (int i=0;i<timeUtils.size();i++){
                ((InventoryBean)data.get(i)).position=i;
            }
            for (int i=0;i<data.size();i++) {
                times.add(new String(timeUtils.get(i).getTimeTick()));
            }
        }else if(data.size()==0){
            timeUtils.clear();
        }
        for (int i = 0; i < times.size(); i++) {
            Log.d("xingling", i + ": " + times.get(i));
        }
    }

//    public class CountBinder extends Binder{
//        public ArrayList<String> getTimes(){
//            return times;
//        }
//    }


    private final ICountInterface.Stub mBinder = new ICountInterface.Stub() {

        @Override
        public List getTimes() throws RemoteException {
            return times;
        }
    };

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            data = DataManager.getInstance().getDropInventorysFM();
            refresh();
        }
    };

    @Override
    public void onDestroy() {
//        for (int i=0;i<data.size();i++){
//            InventoryBean inventoryBean = (InventoryBean)data.get(i);
//            inventoryBean.delTime = times.get(i);
//            DataManager.getInstance().updateItem(inventoryBean,inventoryBean);
//        }
        super.onDestroy();
    }
}
