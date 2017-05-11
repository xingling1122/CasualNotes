package com.xl.projectno.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.xl.projectno.R;
import com.xl.projectno.adapter.InventoryExListAdapter;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.utils.LanguageUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/28.
 */
public class ChooseInventoryDialog extends Dialog implements View.OnClickListener{
    private ExpandableListView listView;
    private InventoryExListAdapter listAdapter;

    public ChooseInventoryDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_chooseinventory);
        setCanceledOnTouchOutside(true);
        findViewById(R.id.cancel).setOnClickListener(this);
        listView = (ExpandableListView) findViewById(R.id.listview);
        initData();
    }

    private void initData() {
        ArrayList<BaseBean> data = DataManager.getInstance().queryList(DataManager.TYPE_DATA_FOLDER);
        listAdapter = new InventoryExListAdapter(getContext(),data);
        listAdapter.setOnMClickListener(new InventoryExListAdapter.OnMClickListener() {
            @Override
            public void onclick(String name) {
                if (onChooseListener!=null){
                    onChooseListener.onChoose(name);
                }
                dismiss();
            }
        });
        listView.setAdapter(listAdapter);
        listView.setGroupIndicator(null);
        listView.setDivider(null);
        for(int i=0;i<data.size();i++){
            listView.expandGroup(i);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel:
                dismiss();
                break;
        }
    }

    private OnChooseListener onChooseListener;

    public void setOnChooseListener(OnChooseListener onChooseListener){
        this.onChooseListener=onChooseListener;
    }

    public interface OnChooseListener{
        void onChoose(String name);
    }
}
