package com.xl.projectno.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xl.projectno.R;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.InventoryBean;
import com.xl.projectno.utils.TimeUtils;
import com.xl.projectno.widget.ItemThouchHelperAdapterCallback;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/29.
 */
public class DropRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemThouchHelperAdapterCallback {
    Context context;
    ArrayList<BaseBean> data;
    LayoutInflater layoutInflater;
    ArrayList<String> times;


    public void setTimes(ArrayList<String> times){
        this.times=times;
        notifyDataSetChanged();
    }

    public DropRecyclerAdapter(ArrayList<BaseBean> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void setData(ArrayList<BaseBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(context);
        return new ViewHolder(layoutInflater.inflate(R.layout.inventory_drop_item, null));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final InventoryBean inventoryBean = (InventoryBean) data.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.inventoryName.setText(inventoryBean.name);
        viewHolder.drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDeleteListener != null) {
                    onDeleteListener.delete(inventoryBean.name);
                }
            }
        });
        if (times!=null&&position<times.size()){
            if (times.get(position).equals("00:00:00")){
                DataManager.getInstance().delItem(data.get(position));
                times.remove(position);
            }else{
                viewHolder.delTime.setText(times.get(position));
            }
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemSwiped(int adapterPosition) {
        InventoryBean inventoryBean = (InventoryBean) data.get(adapterPosition);
        DataManager.getInstance().delItem(inventoryBean);
        inventoryBean.isDrop = false;
        DataManager.getInstance().addItem(inventoryBean);
        if (onSwipeListener != null) {
            onSwipeListener.swipe();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView inventoryName;
        TextView delTime;
        ImageView drop;

        public ViewHolder(View itemView) {
            super(itemView);
            inventoryName = (TextView) itemView.findViewById(R.id.inventoryName);
            delTime = (TextView) itemView.findViewById(R.id.delTime);
            drop = (ImageView) itemView.findViewById(R.id.drop);
        }
    }

    private OnDeleteListener onDeleteListener;

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    public interface OnDeleteListener {
        void delete(String name);
    }

    private OnSwipeListener onSwipeListener;

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }

    public interface OnSwipeListener {
        void swipe();
    }
}
