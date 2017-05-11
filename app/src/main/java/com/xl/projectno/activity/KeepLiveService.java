package com.xl.projectno.activity;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.xl.projectno.IKeepLiveInterface;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.data.protocol.ResponseHeaderBean;
import com.xl.projectno.model.UserBean;
import com.xl.projectno.volley.ILoadDataListener;


/**
 * Created by Administrator on 2017/4/27.
 */
public class KeepLiveService extends Service{

    private KeepLiveTask myTask = new KeepLiveTask();
    Handler handler;

    int code;

    private final IKeepLiveInterface.Stub mBinder = new IKeepLiveInterface.Stub() {

        @Override
        public int keepLive() throws RemoteException {
            return code;
        }

        @Override
        public void removeCallBack() throws RemoteException {
            handler.removeCallbacks(myTask);
        }

        @Override
        public void sendCallBack() throws RemoteException {
            handler.postDelayed(myTask,0);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        handler.postDelayed(myTask, 0);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        handler.postDelayed(myTask, 0);
    }

    private class KeepLiveTask implements Runnable {
        public void run() {
            try {
                refresh();
            } catch (Exception e) {
            }
            handler.postDelayed(myTask, 2000);
        }
    }

    private void refresh() {
        UserBean user = DataManager.getInstance().getCurrentUser();
        DataManager.getInstance().keepLive(user.name, user.token, new ILoadDataListener<ResponseHeaderBean>() {
            @Override
            public void onDataListner(ResponseHeaderBean result) {
                code = result.getRetCode();
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }
}
