package com.xl.projectno.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xl.projectno.R;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.FolderBean;
import com.xl.projectno.model.InventoryBean;
import com.xl.projectno.utils.ValidateUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/28.
 */
public class EditFolderDialog extends Dialog implements View.OnClickListener {
    private String name;
    private FolderBean folderBean;

    public EditFolderDialog(Context context, int themeResId,String name) {
        super(context, themeResId);
        this.name=name;
        setContentView(R.layout.dialog_editfolder);
        setCanceledOnTouchOutside(true);
        findViewById(R.id.dismiss).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.sure).setOnClickListener(this);
        initData();
    }

    private void initData() {
        folderBean = (FolderBean) DataManager.getInstance().getItemByName(name,DataManager.TYPE_DATA_FOLDER);
        ((TextView)findViewById(R.id.name)).setText(folderBean.name);
        if(name.equals("默认文件夹")){
            findViewById(R.id.dismiss).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dismiss:
                DataManager.getInstance().delItem(folderBean);
                ArrayList<BaseBean> beans = DataManager.getInstance().getInventoryBeansByFolderId(folderBean.id);
                for(BaseBean baseBean:beans){
                    InventoryBean inventoryBean = (InventoryBean) baseBean;
                    inventoryBean.folderId = 1;
                    DataManager.getInstance().updateItem(inventoryBean,inventoryBean);
                }
                dismiss();
                break;
            case R.id.cancel:
                dismiss();
                break;
            case R.id.sure:
                String name = ((TextView)findViewById(R.id.name)).getText().toString();
                if (!ValidateUtils.getInstance(getContext()).validate(this.name,name,DataManager.TYPE_DATA_FOLDER)){
                    return;
                }
                folderBean.name = name;
                DataManager.getInstance().updateItem(folderBean,folderBean);
                dismiss();
                break;
        }
    }
}
