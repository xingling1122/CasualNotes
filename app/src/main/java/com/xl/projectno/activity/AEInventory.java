package com.xl.projectno.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xl.projectno.R;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.dialog.ChooseColorDialog;
import com.xl.projectno.dialog.ChooseFolderDialog;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.FolderBean;
import com.xl.projectno.model.InventoryBean;
import com.xl.projectno.model.ThemeBean;
import com.xl.projectno.utils.LanguageUtils;
import com.xl.projectno.utils.StatusBarUtils;
import com.xl.projectno.utils.TimeUtils;
import com.xl.projectno.utils.ValidateUtils;
import com.xl.projectno.widget.MyToggleButton;

/**
 * Created by Administrator on 2017/3/27.
 */
public class AEInventory extends AppCompatActivity implements MyToggleButton.OnChangeListener, View.OnClickListener {
    ViewHolder viewHolder;
    private boolean isusing=false;

    private boolean flag = false;
    private String inventoryName = null;
    private InventoryBean inventoryBean;
    private int color;
    private String folderName;

    public static final String action = "jason.broadcast.action";
    ThemeBean theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme = DataManager.getInstance().getCurrentTheme();
        setTheme(theme.style);
        setContentView(R.layout.inventory_add_main);
        LanguageUtils.ChangeLanguage(this,LanguageUtils.current_language);
        inventoryName = getIntent().getStringExtra("name");
        if (inventoryName!=null){
            flag=true;
        }
        initItem();
        if (flag){
            initData();
        }
    }

    private void initData() {
        inventoryBean = (InventoryBean) DataManager.getInstance().getItemByName(inventoryName,DataManager.TYPE_DATA_INVENTORY);
        viewHolder.input.setText(inventoryName);

        viewHolder.folder.setText(DataManager.getInstance().getFolderBeanById(inventoryBean.folderId).name);
        viewHolder.color.setBackground(this.getResources().getDrawable(inventoryBean.colorDrawableId));

        viewHolder.isusing.setOpen(inventoryBean.isUsing);
        color = inventoryBean.color;
    }

    private void initItem() {
        viewHolder = new ViewHolder(getWindow().getDecorView());
        viewHolder.toolbar.setTitle("");
        StatusBarUtils.setWindowStatusBarColor(this, theme.color);
        setSupportActionBar(viewHolder.toolbar);
        viewHolder.desc.setVisibility(View.GONE);
        viewHolder.isusing.setOnChangeListener(this);
        viewHolder.check.setOnClickListener(this);
        viewHolder.colorclick.setOnClickListener(this);
        viewHolder.folderclick.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inventory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_cancel:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void click(boolean isOpen) {
        isusing=isOpen;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.check:
                String name = viewHolder.input.getText().toString();
                if (!ValidateUtils.getInstance(this).validate(inventoryName,name, DataManager.TYPE_DATA_INVENTORY)){
                    viewHolder.desc.setText(ValidateUtils.getInstance(this).getDesc());
                    viewHolder.desc.setVisibility(View.VISIBLE);
                    return;
                }
                folderName = viewHolder.folder.getText().toString();
                int folderId = DataManager.getInstance().getItemByName(folderName,DataManager.TYPE_DATA_FOLDER).id;
                InventoryBean bean = new InventoryBean(TimeUtils.getNowTime(),name,0,color,folderId,isusing,false,TimeUtils.getDefaultTimeTick());
                if (flag){
                    inventoryBean.name = name;
                    inventoryBean.color=color;
                    inventoryBean.folderId=folderId;
                    inventoryBean.isUsing=isusing;
                    DataManager.getInstance().updateItem(inventoryBean,inventoryBean);
                    Toast.makeText(this,getString(R.string.updatesuccess),Toast.LENGTH_SHORT).show();
                }else{
                    DataManager.getInstance().addItem(bean);
                    Toast.makeText(this,getString(R.string.addsuccess),Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(action);
                intent.putExtra("name", name);
                sendBroadcast(intent);
                finish();
                break;
            case R.id.colorclick:
                final ChooseColorDialog dialog1 =new ChooseColorDialog(this,R.style.add_dialog);
                dialog1.setOnChooseListener(new ChooseColorDialog.OnChooseListener() {
                    @Override
                    public void onChoose(Drawable color,int id) {
                        AEInventory.this.color = id;
                        viewHolder.color.setBackground(color);
                        dialog1.dismiss();
                    }
                });
                dialog1.show();
                break;
            case R.id.folderclick:
                final ChooseFolderDialog dialog =new ChooseFolderDialog(this,R.style.add_dialog);
                dialog.setOnChooseListener(new ChooseFolderDialog.OnChooseListener() {
                    @Override
                    public void onChoose(String name) {
                        viewHolder.folder.setText(name);
                        dialog.dismiss();
                    }
                });
                if (inventoryBean!=null){
                    FolderBean bean1 = DataManager.getInstance().getFolderBeanById(inventoryBean.folderId);
                    dialog.setCheckName(bean1.name);
                }else{
                    dialog.setCheckName(getString(R.string.defaultfolder));
                }
                dialog.show();
                break;
        }
    }

    class ViewHolder{
        public Toolbar toolbar;
        public EditText input;
        public TextView desc;
        public ImageView color;
        public TextView folder;
        public ImageView check;
        public MyToggleButton isusing;

        public LinearLayout colorclick;
        public LinearLayout folderclick;

        public ViewHolder(View view){
            toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            input = (EditText) view.findViewById(R.id.input);
            desc = (TextView) view.findViewById(R.id.desc);
            color = (ImageView) view.findViewById(R.id.color);
            folder = (TextView) view.findViewById(R.id.folder);
            check = (ImageView) view.findViewById(R.id.check);
            isusing = (MyToggleButton) view.findViewById(R.id.isusing);
            colorclick = (LinearLayout) view.findViewById(R.id.colorclick);
            folderclick = (LinearLayout) view.findViewById(R.id.folderclick);
        }
    }
}
