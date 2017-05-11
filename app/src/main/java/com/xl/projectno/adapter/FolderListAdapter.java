package com.xl.projectno.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xl.projectno.R;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.FolderBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/27.
 */
public class FolderListAdapter extends BaseListAdapter {
    Context context;
    public FolderListAdapter(ArrayList<BaseBean> data, Context context) {
        super(data);
        this.context=context;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final FolderBean bean = (FolderBean) getItem(i);
        ViewHolder viewHolder=null;
        if (view==null){
            view = View.inflate(context, R.layout.folder_item,null);
            viewHolder = new ViewHolder();
            viewHolder.box= (RadioButton) view.findViewById(R.id.box);
            viewHolder.folderName = (TextView)view.findViewById(R.id.folderName);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.folderName.setText(bean.name);
        viewHolder.box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onChooseListener!=null){
                    onChooseListener.onchoose(bean.name);
                }
            }
        });
        if (bean.name.equals(name)){
            viewHolder.box.setChecked(true);
        }else{
            viewHolder.box.setChecked(false);
        }
        return view;
    }

    class ViewHolder{
        public RadioButton box;
        public TextView folderName;
    }

    private String name;
    public void setCheck(String name){
        this.name = name;
    }

    private OnChooseListener onChooseListener;

    public void setOnChooseListener(OnChooseListener onChooseListener){
        this.onChooseListener=onChooseListener;
    }

    public interface OnChooseListener{
        void onchoose(String folderName);
    }
}
