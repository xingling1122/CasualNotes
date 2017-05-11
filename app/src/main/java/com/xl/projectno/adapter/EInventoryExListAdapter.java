package com.xl.projectno.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xl.projectno.R;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.FolderBean;
import com.xl.projectno.model.InventoryBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/28.
 */
public class EInventoryExListAdapter extends BaseExListAdapter {
    Context context;

    public EInventoryExListAdapter(Context context,ArrayList<BaseBean> dataParent) {
        super(dataParent);
        this.context=context;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (i>getGroupCount()-1){
            i--;
            view.setVisibility(View.GONE);
        }else{
            if (view!=null){
                view.setVisibility(View.VISIBLE);
            }
        }
        final FolderBean folderBean = (FolderBean)getGroup(i);
        if (view == null) {
            view = View.inflate(context, R.layout.edit_inventory_groupitem, null);
        }
        ImageView logo = (ImageView)view.findViewById(R.id.logo);
        TextView folderName = (TextView)view.findViewById(R.id.folderName);
        ImageView arrow = (ImageView) view.findViewById(R.id.arrow);
        if (folderBean!=null){
            folderName.setText(folderBean.name);
            if (b){
                arrow.setBackground(context.getResources().getDrawable(R.drawable.arrow_up));
                logo.setBackground(context.getResources().getDrawable(R.drawable.logo_folder_open));
                if (dataChild.get(i).size()==0){
                    arrow.setBackground(context.getResources().getDrawable(R.drawable.arrow_up_lighter));
                }
            }else{
                logo.setBackground(context.getResources().getDrawable(R.drawable.logo_folder));
                arrow.setBackground(context.getResources().getDrawable(R.drawable.arrow_down));
                if (dataChild.get(i).size()==0){
                    arrow.setBackground(context.getResources().getDrawable(R.drawable.arrow_down_lighter));
                }
            }
            folderName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onGroupListener!=null){
                        onGroupListener.onclick(folderBean.name);
                    }
                }
            });
        }else{
            view.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final int j = i;
        final InventoryBean inventoryBean = (InventoryBean) getChild(i,i1);
        if (view==null){
            view = View.inflate(context,R.layout.edit_inventory_childitem,null);
        }
        TextView inventoryName = (TextView) view.findViewById(R.id.inventoryName);
        if (inventoryBean!=null&&inventoryName!=null){
            inventoryName.setText(inventoryBean.name);
            inventoryName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onMClickListener!=null){
                        onMClickListener.onclick(inventoryBean.name);
                    }
                }
            });
            inventoryName.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (onLongClickItemListener!=null){
                        onLongClickItemListener.onLongClick(inventoryBean.name,j);
                    }
                    return true;
                }
            });
            view.setVisibility(View.VISIBLE);
        }else{
            view.setVisibility(View.GONE);
        }
        return view;
    }

    private OnGroupListener onGroupListener;

    public void setOnGroupListener(OnGroupListener onGroupListener){
        this.onGroupListener=onGroupListener;
    }

    public interface OnGroupListener{
        void onclick(String name);
    }

    private OnLongClickItemListener onLongClickItemListener;

    public void setOnLongClickItemListener(OnLongClickItemListener onLongClickItemListener){
        this.onLongClickItemListener=onLongClickItemListener;
    }

    public interface OnLongClickItemListener{
        void onLongClick(String name,int position);
    }
}
