package com.xl.projectno.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xl.projectno.R;
import com.xl.projectno.adapter.ThemeGridViewAdapter;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.base.SettingManager;
import com.xl.projectno.data.protocol.ResponseHeaderBean;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.ThemeBean;
import com.xl.projectno.model.UserBean;
import com.xl.projectno.utils.DialogUtil;
import com.xl.projectno.utils.DrawCircle;
import com.xl.projectno.utils.LanguageUtils;
import com.xl.projectno.utils.StatusBarUtils;
import com.xl.projectno.volley.ILoadDataListener;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/1.
 */

public class ThemeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    GridView themegridview ;
    ThemeGridViewAdapter adapter;
    ArrayList<BaseBean> data;
    ThemeBean theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme = DataManager.getInstance().getCurrentTheme();
        setTheme(theme.style);
        LanguageUtils.ChangeLanguage(this,LanguageUtils.current_language);
        setContentView(R.layout.theme_main_layout);
        initItem();
        initData();
    }

    private void initData() {
        data = DataManager.getInstance().getThemes();
        adapter = new ThemeGridViewAdapter(data,this);
        themegridview.setAdapter(adapter);
    }

    private void initItem() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        StatusBarUtils.setWindowStatusBarColor(this, theme.color);
        setSupportActionBar(toolbar);
        ImageView back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        themegridview = (GridView) findViewById(R.id.themegridview);
        themegridview.setOnItemClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                Intent intent1 = new Intent(this,SettingActivity.class);
                startActivity(intent1);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(this,SettingActivity.class);
        startActivity(intent1);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int i=0;
        for (BaseBean baseBean:data){
            ThemeBean bean = (ThemeBean)baseBean;
            if (i==position){
                bean.choose=true;
            }else{
                bean.choose=false;
            }
            DataManager.getInstance().updateItem(bean,bean);
            i++;
        }
        adapter.notifyDataSetChanged();
    }
}
