package com.xl.projectno.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xl.projectno.R;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.ThemeBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/1.
 */

public class ThemeGridViewAdapter extends BaseListAdapter {
    Context context;
    ArrayList<BaseBean> data;

    public ThemeGridViewAdapter(ArrayList<BaseBean> data, Context context) {
        super(data);
        this.data = data;
        this.context = context;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ThemeBean bean = (ThemeBean)getItem(i);
        ViewHolder viewHolder;
        if (view==null){
            view = View.inflate(context, R.layout.item_theme,null);
            viewHolder = new ViewHolder();
            viewHolder.color = (RelativeLayout) view.findViewById(R.id.color);
            viewHolder.choose = (ImageView) view.findViewById(R.id.choose);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name.setText(bean.name);
        viewHolder.color.setBackground(context.getResources().getDrawable(bean.drawable));
        viewHolder.choose.setVisibility(bean.choose?View.VISIBLE:View.GONE);
        return view;
    }

    class ViewHolder{
        public RelativeLayout color;
        public ImageView choose;
        public TextView name;
    }
}
