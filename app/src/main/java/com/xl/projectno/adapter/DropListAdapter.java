package com.xl.projectno.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xl.projectno.R;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.InventoryBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/28.
 */
public class DropListAdapter extends BaseListAdapter {
    Context context;

    public DropListAdapter(ArrayList<BaseBean> data, Context context) {
        super(data);
        this.context=context;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final InventoryBean inventoryBean = (InventoryBean)getItem(i);
        if (view==null){
            view = View.inflate(context,R.layout.inventory_drop_item,null);
        }
        ImageView drop = (ImageView)view.findViewById(R.id.drop);
        TextView inventoryName = (TextView)view.findViewById(R.id.inventoryName);
        inventoryName.setText(inventoryBean.name);
        drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDeleteListener!=null){
                    onDeleteListener.delete(inventoryBean.name);
                }
            }
        });
        return view;
    }

    private OnDeleteListener onDeleteListener;

    public void setOnDeleteListener(OnDeleteListener onDeleteListener){
        this.onDeleteListener=onDeleteListener;
    }

    public interface OnDeleteListener{
        void delete(String name);
    }
}
