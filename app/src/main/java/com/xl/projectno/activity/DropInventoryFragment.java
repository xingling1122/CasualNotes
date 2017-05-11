package com.xl.projectno.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.xl.projectno.ICountInterface;
import com.xl.projectno.R;
import com.xl.projectno.adapter.DropListAdapter;
import com.xl.projectno.adapter.DropRecyclerAdapter;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.InventoryBean;
import com.xl.projectno.utils.TimeUtils;
import com.xl.projectno.widget.WhItemTouchCallback;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/28.
 */
public class DropInventoryFragment extends Fragment implements DataManager.IDataChangeListener {
    RecyclerView listView;
//    DropListAdapter listAdapter;
    DropRecyclerAdapter recyclerAdapter;
    ArrayList<BaseBean> data;

    private ItemTouchHelper itemtouchhelper;
    WhItemTouchCallback callback;

    ArrayList<String> times;

    ServiceConnection sc;

//    CountService.CountBinder countBinder =null;
    ICountInterface countBinder=null;

    Handler handler = new Handler();
    private class CounterTask implements Runnable{
        public void run() {
            invoke();
            handler.postDelayed(myTask,1000);
        }
    }
    private CounterTask myTask = new CounterTask();

    public void invoke(){
        if (sc!=null&&countBinder!=null){
            try {
                times=(ArrayList<String>) countBinder.getTimes();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (times.size()==data.size()){
                recyclerAdapter.setTimes(times);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.inventory_drop_main,null);
        listView = (RecyclerView)view.findViewById(R.id.listview);
        startService();
        initItem();
        return view;
    }

    private void startService(){
        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                countBinder = ICountInterface.Stub.asInterface(iBinder);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        getActivity().startService(new Intent(getContext(),CountService.class));
        getActivity().bindService(new Intent(getContext(), CountService.class), sc, Context.BIND_AUTO_CREATE);
//        if (sc!=null){
//            recyclerAdapter.notifyDataSetChanged();
//        }
        handler.postDelayed(myTask,1000);
    }

    private void initItem() {
        DataManager.getInstance().registerDataChangeListener(DataManager.TYPE_DATA_INVENTORY,this);
        DataManager.getInstance().updateListFromDb();
        data = DataManager.getInstance().getDropInventorys();
        recyclerAdapter = new DropRecyclerAdapter(data,getContext());
        recyclerAdapter.setTimes(times);
        listView.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnDeleteListener(new DropRecyclerAdapter.OnDeleteListener() {
            @Override
            public void delete(String name) {
                InventoryBean inventoryBean = (InventoryBean) DataManager.getInstance().getItemByName(name,DataManager.TYPE_DATA_INVENTORY);
                DataManager.getInstance().delItem(inventoryBean);
            }
        });
        listView.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager gridlayoutmanager = new GridLayoutManager(getContext(), 1, OrientationHelper.VERTICAL, false);
        listView.setLayoutManager(gridlayoutmanager);

        callback = new WhItemTouchCallback(recyclerAdapter);//初始化ItemTouchHelper.
        itemtouchhelper = new ItemTouchHelper(callback);
        itemtouchhelper.attachToRecyclerView(listView);//这个必须加上.让辅助类和recycleview关联上
        callback.setDragEnable(false);
        callback.setXdirection(ItemTouchHelper.LEFT);
    }

    @Override
    public void onDataChange(int msgId, int arg) {
        data = DataManager.getInstance().getDropInventorys();
        recyclerAdapter.setData(data);
        Intent intent = new Intent(CountService.action);
        if (getContext()!=null){
            getContext().sendBroadcast(intent);
        }
        handler.postDelayed(myTask,0);
    }

}
