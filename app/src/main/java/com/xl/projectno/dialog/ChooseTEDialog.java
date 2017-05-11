package com.xl.projectno.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xl.projectno.R;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.model.InventoryBean;

/**
 * Created by Administrator on 2017/3/28.
 */
public class ChooseTEDialog extends Dialog implements View.OnClickListener {
    private String name;
    private InventoryBean inventoryBean;
    private ViewHolder viewHolder;

    public ChooseTEDialog(Context context, int themeResId,String name) {
        super(context, themeResId);
        this.name=name;
        setContentView(R.layout.dialog_edit_choose);
        initItem();
    }

    private void initItem() {
        inventoryBean = (InventoryBean) DataManager.getInstance().getItemByName(name,DataManager.TYPE_DATA_INVENTORY);
        viewHolder = new ViewHolder(getWindow().getDecorView());
        viewHolder.inventoryName.setText(inventoryBean.name);
        viewHolder.delete.setOnClickListener(this);
        viewHolder.callback.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.delete:
                DataManager.getInstance().delItem(inventoryBean);
                if (onCallBackListener!=null){
                    onCallBackListener.callback();
                }
                dismiss();
                break;
            case R.id.callback:
                InventoryBean inventoryBean = (InventoryBean) DataManager.getInstance().getItemByName(name,DataManager.TYPE_DATA_INVENTORY);
                DataManager.getInstance().delItem(inventoryBean);
                inventoryBean.isDrop=true;
                DataManager.getInstance().addItem(inventoryBean);
                if (onCallBackListener!=null){
                    onCallBackListener.callback();
                }
                dismiss();
                break;
        }
    }

    class ViewHolder{
        public TextView inventoryName;
        public LinearLayout delete;
        public LinearLayout callback;
        public ViewHolder(View view){
            inventoryName = (TextView)view.findViewById(R.id.inventoryName);
            delete = (LinearLayout) view.findViewById(R.id.delete);
            callback = (LinearLayout) view.findViewById(R.id.callback);
        }
    }

    private OnCallBackListener onCallBackListener;

    public void setCallBackListener(OnCallBackListener onCallBackListener){
        this.onCallBackListener=onCallBackListener;
    }

    public interface OnCallBackListener{
        void callback();
    }
}
