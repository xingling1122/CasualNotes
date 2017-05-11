package com.xl.projectno.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xl.projectno.model.BaseBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/27.
 */
public class BaseListAdapter extends BaseAdapter {
    ArrayList<BaseBean> data;

    public BaseListAdapter(ArrayList<BaseBean> data){
        this.data=data;
    }

    public void setData(ArrayList<BaseBean> data){
        this.data=data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data==null?0:data.size();
    }

    @Override
    public Object getItem(int i) {
        return data==null?null:data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
