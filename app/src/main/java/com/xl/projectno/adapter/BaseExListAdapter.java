package com.xl.projectno.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xl.projectno.R;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.FolderBean;
import com.xl.projectno.model.InventoryBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/28.
 */
public class BaseExListAdapter extends BaseExpandableListAdapter {
    protected ArrayList<BaseBean> dataParent;
    protected ArrayList<ArrayList<BaseBean>> dataChild;

    public BaseExListAdapter(ArrayList<BaseBean> dataParent){
        setData(dataParent);
    }

    public void setData(ArrayList<BaseBean> dataParent){
        this.dataParent=dataParent;
        dataChild= new ArrayList<>();
        for (BaseBean baseBean:dataParent){
            FolderBean folderBean = (FolderBean) baseBean;
            ArrayList<BaseBean> data = DataManager.getInstance().getInventoryItemsByFId(folderBean.id);
            dataChild.add(data);
        }
        notifyDataSetChanged();
    }


    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getGroupCount() {
        Log.d("xingling","groupsize:"+dataParent.size());
        return dataParent==null?0:dataParent.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return dataChild.get(i)==null?0:dataChild.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        Log.d("xingling","data.size:"+dataParent.size()+",i:"+i);
        return dataParent==null||(i>(dataParent.size()-1))?null:dataParent.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return dataChild==null||(i>(dataChild.size()-1))?null:dataChild.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int i) {

    }

    @Override
    public void onGroupCollapsed(int i) {

    }

    @Override
    public long getCombinedChildId(long l, long l1) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long l) {
        return 0;
    }

    protected OnMClickListener onMClickListener;

    public void setOnMClickListener(OnMClickListener onMClickListener){
        this.onMClickListener=onMClickListener;
    }

    public interface OnMClickListener{
        void onclick(String name);
    }
}
