package com.xl.projectno.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xl.projectno.R;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.FolderBean;
import com.xl.projectno.model.InventoryBean;
import com.xl.projectno.utils.LanguageUtils;
import com.xl.projectno.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/28.
 */
public class InventoryExListAdapter extends BaseExListAdapter {
    private Context context;

    public InventoryExListAdapter(Context context,ArrayList<BaseBean> dataParent){
        super(dataParent);
        this.context=context;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        FolderBean folderBean = (FolderBean)getGroup(i);
        if (view == null) {
            view = View.inflate(context,R.layout.inventory_groupitem, null);
        }
        TextView folderName = (TextView)view.findViewById(R.id.folderName);
        ImageView arrow = (ImageView) view.findViewById(R.id.arrow);
        folderName.setText(folderBean.name);
        if (b){
            arrow.setBackground(context.getResources().getDrawable(R.drawable.arrow_up));
            if (dataChild.get(i).size()==0){
                arrow.setBackground(context.getResources().getDrawable(R.drawable.arrow_up_lighter));
            }
        }else{
            arrow.setBackground(context.getResources().getDrawable(R.drawable.arrow_down));
            if (dataChild.get(i).size()==0){
                arrow.setBackground(context.getResources().getDrawable(R.drawable.arrow_down_lighter));
            }
        }
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final InventoryBean inventoryBean = (InventoryBean) getChild(i,i1);
        if (view==null){
            view = View.inflate(context,R.layout.inventory_childitem,null);
        }
        TextView inventoryName = (TextView) view.findViewById(R.id.inventoryName);
        inventoryName.setText(inventoryBean.name);
        inventoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onMClickListener!=null){
                    onMClickListener.onclick(inventoryBean.name);
                }
            }
        });
        return view;
    }

}
