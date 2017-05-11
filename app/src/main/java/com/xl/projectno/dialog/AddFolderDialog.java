package com.xl.projectno.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.xl.projectno.R;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.model.FolderBean;
import com.xl.projectno.utils.TimeUtils;
import com.xl.projectno.utils.ValidateUtils;

/**
 * Created by Administrator on 2017/3/27.
 */
public class AddFolderDialog extends Dialog implements View.OnClickListener{
    EditText newName;

    public AddFolderDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_addfolder);
        setCanceledOnTouchOutside(true);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.sure).setOnClickListener(this);
        newName = (EditText) findViewById(R.id.newName);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel:
                dismiss();
                break;
            case R.id.sure:
                String name = newName.getText().toString();
                if (!ValidateUtils.getInstance(getContext()).validate("",name, DataManager.TYPE_DATA_FOLDER)){
                   return;
                }
                FolderBean folderBean = new FolderBean(TimeUtils.getNowTime(),name,true);
                DataManager.getInstance().addItem(folderBean);
                if (onAddListener!=null){
                    onAddListener.onAdd(name);
                }
                dismiss();
                break;
        }
    }

    private OnAddListener onAddListener;

    public void setOnAddListener(OnAddListener onAddListener){
        this.onAddListener=onAddListener;
    }

    public interface OnAddListener{
        void onAdd(String name);
    }
}
