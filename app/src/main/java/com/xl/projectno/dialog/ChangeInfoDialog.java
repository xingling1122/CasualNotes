package com.xl.projectno.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xl.projectno.R;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.FolderBean;
import com.xl.projectno.model.InventoryBean;
import com.xl.projectno.model.UserBean;
import com.xl.projectno.utils.ValidateUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/3.
 */

public class ChangeInfoDialog extends Dialog implements View.OnClickListener  {
    private int name;
    private int type;
    public static final int TYPE_NICKNAME =0;
    public static final int TYPE_PHONE = 1;
    public static final int TYPE_IMAIL = 2;
    public static final int TYPE_PASSWORD = 3;

    private ViewHolder viewHolder;

    public ChangeInfoDialog(Context context, int themeResId, int name,int type) {
        super(context, themeResId);
        this.name=name;
        this.type = type;
        setContentView(R.layout.dialog_changeinfo);
        setCanceledOnTouchOutside(true);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.sure).setOnClickListener(this);
        initData();
    }

    private void initData() {
        UserBean user = DataManager.getInstance().getCurrentUser();
        viewHolder = new ViewHolder();
        viewHolder.text.setText(getContext().getString(R.string.change)+getContext().getString(name));
        switch (type){
            case TYPE_NICKNAME:
                viewHolder.name.setHint(user.nickname);
                viewHolder.newname.setVisibility(View.GONE);
                break;
            case TYPE_PHONE:
                viewHolder.name.setHint(user.phone);
                viewHolder.newname.setVisibility(View.GONE);
                break;
            case TYPE_IMAIL:
                viewHolder.name.setHint(user.imail);
                viewHolder.newname.setVisibility(View.GONE);
                break;
            case TYPE_PASSWORD:
                viewHolder.name.setHint(getContext().getString(R.string.oldfirst));
                viewHolder.newname.setVisibility(View.VISIBLE);
                viewHolder.newname.setHint(getContext().getString(R.string.newpassword));
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel:
                dismiss();
                break;
            case R.id.sure:
                if (onSaveListener!=null){
                    if (type==TYPE_PASSWORD){
                        onSaveListener.onSave(viewHolder.name.getText().toString(),viewHolder.newname.getText().toString());
                    }else{
                        onSaveListener.onSave(viewHolder.name.getText().toString(),null);
                    }
                }
                dismiss();
                break;
        }
    }
    class ViewHolder{
        public TextView text;
        public EditText name;
        public EditText newname;
        public ViewHolder(){
            text = (TextView) findViewById(R.id.text);
            name = (EditText) findViewById(R.id.name);
            newname = (EditText) findViewById(R.id.newname);
        }
    }

    private OnSaveListener onSaveListener;

    public void setOnSaveListener(OnSaveListener onSaveListener){
        this.onSaveListener = onSaveListener;
    }

    public interface OnSaveListener{
        void onSave(String name,String newname);
    }
}
