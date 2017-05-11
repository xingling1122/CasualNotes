package com.xl.projectno.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xl.projectno.R;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.data.protocol.ResponseHeaderBean;
import com.xl.projectno.dialog.ChangeInfoDialog;
import com.xl.projectno.model.ThemeBean;
import com.xl.projectno.model.UserBean;
import com.xl.projectno.utils.DialogUtil;
import com.xl.projectno.utils.DrawCircle;
import com.xl.projectno.utils.LanguageUtils;
import com.xl.projectno.utils.StatusBarUtils;
import com.xl.projectno.volley.ILoadDataListener;

/**
 * Created by Administrator on 2017/5/3.
 */

public class PersonalInfoActivity extends AppCompatActivity implements View.OnClickListener  {
    ViewHolder viewHolder;
    ThemeBean theme;
    UserBean user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme = DataManager.getInstance().getCurrentTheme();
        setTheme(theme.style);
        LanguageUtils.ChangeLanguage(this,LanguageUtils.current_language);
        setContentView(R.layout.personalinfo_main_layout);
        initItem();
        initData();
    }

    private void initData() {
        user = DataManager.getInstance().getCurrentUser();
        if (user!=null){
            viewHolder.textnickname.setText(user.nickname);
            viewHolder.textimail.setText(user.imail);
            viewHolder.textphone.setText(user.phone);
        }
    }

    private void initItem() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        StatusBarUtils.setWindowStatusBarColor(this, theme.color);
        setSupportActionBar(toolbar);
        ImageView back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        viewHolder = new ViewHolder();
        viewHolder.nickname.setOnClickListener(this);
        viewHolder.phone.setOnClickListener(this);
        viewHolder.imail.setOnClickListener(this);
        viewHolder.changepassword.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        ChangeInfoDialog dialog;
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.nickname:
                dialog = new ChangeInfoDialog(this,R.style.add_dialog,R.string.nickname,ChangeInfoDialog.TYPE_NICKNAME);
                dialog.setOnSaveListener(new ChangeInfoDialog.OnSaveListener() {
                    @Override
                    public void onSave(final String name,String newname) {
                        DataManager.getInstance().changeinfo(user.name, user.token, name, null, null, new ILoadDataListener<ResponseHeaderBean>() {
                            @Override
                            public void onDataListner(ResponseHeaderBean result) {
                                if (result.getStatus()){
                                    user.nickname=name;
                                    DataManager.getInstance().updateCurrentUser(user);
                                    initData();
                                    DataManager.getInstance().updateItem(user,user);
                                    Toast.makeText(PersonalInfoActivity.this,getString(R.string.savesuccess),Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(PersonalInfoActivity.this,getString(R.string.savefail)+"--"+result.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(PersonalInfoActivity.this,getString(R.string.savefail)+"--"+volleyError.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                dialog.show();
                break;
            case R.id.phone:
                dialog = new ChangeInfoDialog(this,R.style.add_dialog,R.string.phone,ChangeInfoDialog.TYPE_PHONE);
                dialog.setOnSaveListener(new ChangeInfoDialog.OnSaveListener() {
                    @Override
                    public void onSave(final String name,String newname) {
                        DataManager.getInstance().changeinfo(user.name, user.token,null, name, null, new ILoadDataListener<ResponseHeaderBean>() {
                            @Override
                            public void onDataListner(ResponseHeaderBean result) {
                                if (result.getStatus()){
                                    user.phone=name;
                                    DataManager.getInstance().updateCurrentUser(user);
                                    initData();
                                    DataManager.getInstance().updateItem(user,user);
                                    Toast.makeText(PersonalInfoActivity.this,getString(R.string.savesuccess),Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(PersonalInfoActivity.this,getString(R.string.savefail)+"--"+result.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(PersonalInfoActivity.this,getString(R.string.savefail)+"--"+volleyError.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                dialog.show();
                break;
            case R.id.imail:
                dialog = new ChangeInfoDialog(this,R.style.add_dialog,R.string.imail,ChangeInfoDialog.TYPE_IMAIL);
                dialog.setOnSaveListener(new ChangeInfoDialog.OnSaveListener() {
                    @Override
                    public void onSave(final String name,String newname) {
                        DataManager.getInstance().changeinfo(user.name, user.token, null, null, name, new ILoadDataListener<ResponseHeaderBean>() {
                            @Override
                            public void onDataListner(ResponseHeaderBean result) {
                                if (result.getStatus()){
                                    user.imail=name;
                                    DataManager.getInstance().updateCurrentUser(user);
                                    initData();
                                    DataManager.getInstance().updateItem(user,user);
                                    Toast.makeText(PersonalInfoActivity.this,getString(R.string.savesuccess),Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(PersonalInfoActivity.this,getString(R.string.savefail)+"--"+result.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(PersonalInfoActivity.this,getString(R.string.savefail)+"--"+volleyError.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                dialog.show();
                break;
            case R.id.changepassword:
                dialog = new ChangeInfoDialog(this,R.style.add_dialog,R.string.password,ChangeInfoDialog.TYPE_PASSWORD);
                dialog.setOnSaveListener(new ChangeInfoDialog.OnSaveListener() {
                    @Override
                    public void onSave(final String name, final String newname) {
                        DataManager.getInstance().alterps(user.name, name, newname,new ILoadDataListener<ResponseHeaderBean>() {
                            @Override
                            public void onDataListner(ResponseHeaderBean result) {
                                if (result.getStatus()){
                                    user.password=newname;
                                    DataManager.getInstance().updateCurrentUser(user);
                                    initData();
                                    DataManager.getInstance().updateItem(user,user);
                                    Toast.makeText(PersonalInfoActivity.this,getString(R.string.savesuccess),Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(PersonalInfoActivity.this,getString(R.string.savefail)+"--"+result.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(PersonalInfoActivity.this,getString(R.string.savefail)+"--"+volleyError.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                dialog.show();
                break;
        }
    }

    class ViewHolder {
        public LinearLayout nickname;
        public LinearLayout phone;
        public LinearLayout imail;
        public LinearLayout changepassword;
        public TextView textnickname;
        public TextView textphone;
        public TextView textimail;
        public ViewHolder(){
            nickname = (LinearLayout) findViewById(R.id.nickname);
            phone = (LinearLayout) findViewById(R.id.phone);
            imail = (LinearLayout) findViewById(R.id.imail);
            changepassword = (LinearLayout) findViewById(R.id.changepassword);
            textnickname = (TextView) findViewById(R.id.textnickname);
            textphone = (TextView) findViewById(R.id.textphone);
            textimail = (TextView) findViewById(R.id.textimail);
        }
    }
}
