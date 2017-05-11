package com.xl.projectno.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.xl.projectno.R;
import com.xl.projectno.adapter.BaseExListAdapter;
import com.xl.projectno.adapter.EInventoryExListAdapter;
import com.xl.projectno.adapter.InventoryExListAdapter;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.dialog.ChooseTEDialog;
import com.xl.projectno.dialog.EditFolderDialog;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.InventoryBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/28.
 */
public class UseInventoryFragment extends Fragment implements DataManager.IDataChangeListener {
    ExpandableListView listView;
    EInventoryExListAdapter exListAdapter;

    ArrayList<BaseBean> data;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.inventory_using_main,null);
        listView = (ExpandableListView)view.findViewById(R.id.listview);
        initItem();

        return view;
    }

    private void initItem() {
        DataManager.getInstance().registerDataChangeListener(DataManager.TYPE_DATA_INVENTORY,this);
        DataManager.getInstance().registerDataChangeListener(DataManager.TYPE_DATA_FOLDER,this);
        DataManager.getInstance().updateListFromDb();
        data = DataManager.getInstance().getListDirect(DataManager.TYPE_DATA_FOLDER);
        exListAdapter = new EInventoryExListAdapter(getContext(),data);
        listView.setAdapter(exListAdapter);
        listView.setGroupIndicator(null);
        for(int i=0;i<data.size();i++){
            listView.expandGroup(i);
        }
        exListAdapter.setOnGroupListener(new EInventoryExListAdapter.OnGroupListener() {
            @Override
            public void onclick(String name) {
                EditFolderDialog dialog = new EditFolderDialog(getContext(),R.style.add_dialog,name);
                dialog.show();
            }
        });
        exListAdapter.setOnMClickListener(new BaseExListAdapter.OnMClickListener() {
            @Override
            public void onclick(String name) {
                Intent intent = new Intent(getContext(),AEInventory.class);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
        exListAdapter.setOnLongClickItemListener(new EInventoryExListAdapter.OnLongClickItemListener() {
            @Override
            public void onLongClick(final String name, final int position) {
                if (!name.equals(getString(R.string.collectbox))){
                    ChooseTEDialog dialog = new ChooseTEDialog(getContext(),R.style.add_dialog,name);
                    dialog.setCallBackListener(new ChooseTEDialog.OnCallBackListener() {
                        @Override
                        public void callback() {
                            exListAdapter.setData(DataManager.getInstance().getListDirect(DataManager.TYPE_DATA_FOLDER));
                            listView.collapseGroup(position);
                            listView.expandGroup(position);
                        }
                    });
                    dialog.show();
                }
            }
        });
    }

    @Override
    public void onDataChange(int msgId, int arg) {
        switch (msgId){
            case DataManager.TYPE_DATA_INVENTORY:
                data = DataManager.getInstance().getListDirect(DataManager.TYPE_DATA_FOLDER);
                exListAdapter.setData(data);
                for (int i=0;i<data.size();i++){
                    listView.collapseGroup(i);
                    listView.expandGroup(i);
                }
                break;
            case DataManager.TYPE_DATA_FOLDER:
                break;
        }
    }
}
