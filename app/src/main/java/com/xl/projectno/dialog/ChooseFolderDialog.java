package com.xl.projectno.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.xl.projectno.R;
import com.xl.projectno.adapter.FolderListAdapter;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.FolderBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/27.
 */
public class ChooseFolderDialog extends Dialog implements View.OnClickListener, DataManager.IDataChangeListener {
    private ListView listView;
    private FolderListAdapter listAdapter;
    ArrayList<BaseBean> data;
    private String checkName;

    public void setCheckName(String checkName){this.checkName=checkName;}

    public ChooseFolderDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choosefolder);
        setCanceledOnTouchOutside(true);
        findViewById(R.id.addfolder).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listview);
        initData();
    }

    private void initData() {
        DataManager.getInstance().registerDataChangeListener(DataManager.TYPE_DATA_FOLDER,this);
        DataManager.getInstance().updateListFromDb();
        data = DataManager.getInstance().getListDirect(DataManager.TYPE_DATA_FOLDER);
        listAdapter = new FolderListAdapter(data,getContext());
        listAdapter.setOnChooseListener(new FolderListAdapter.OnChooseListener() {
            @Override
            public void onchoose(String folderName) {
                for (BaseBean baseBean:DataManager.getInstance().getListDirect(DataManager.TYPE_DATA_FOLDER)){
                    FolderBean bean = (FolderBean)baseBean;
                    bean.isCheck=false;
                    if (folderName.equals(bean.name)){
                        bean.isCheck=true;
                    }
                    DataManager.getInstance().updateItem(bean,bean);
                }
                if (onChooseListener!=null){
                    onChooseListener.onChoose(folderName);
                }
            }
        });
        listAdapter.setCheck(checkName);
        listView.setAdapter(listAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addfolder:
                AddFolderDialog dialog = new AddFolderDialog(getContext(),R.style.add_dialog);
                dialog.setOnAddListener(new AddFolderDialog.OnAddListener() {
                    @Override
                    public void onAdd(String name) {
                        if (onChooseListener!=null){
                            onChooseListener.onChoose(name);
                            dismiss();
                        }
                    }
                });
                dialog.show();
                break;
            case R.id.cancel:
                dismiss();
                break;
        }
    }

    private OnChooseListener onChooseListener;

    public void setOnChooseListener(OnChooseListener onChooseListener){
        this.onChooseListener=onChooseListener;
    }

    @Override
    public void onDataChange(int msgId, int arg) {
        if (msgId>0){
            listAdapter.setData(DataManager.getInstance().getListDirect(DataManager.TYPE_DATA_FOLDER));
        }
    }

    public interface OnChooseListener{
        void onChoose(String name);
    }
}
