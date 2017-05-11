package com.xl.projectno.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.xl.projectno.adapter.FragmentAdapter;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.data.protocol.ResponseHeaderBean;
import com.xl.projectno.model.ThemeBean;
import com.xl.projectno.model.UserBean;
import com.xl.projectno.utils.DialogUtil;
import com.xl.projectno.utils.DrawCircle;
import com.xl.projectno.utils.ImageCompress;
import com.xl.projectno.utils.LanguageUtils;
import com.xl.projectno.utils.StatusBarUtils;
import com.xl.projectno.utils.SystemUtils;
import com.xl.projectno.volley.ILoadDataListener;
import com.xl.projectno.widget.TabViewPager;

import java.util.List;

/**
 * Created by Administrator on 2017/4/26.
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    ViewHolder viewHolder;
    ThemeBean theme;

    boolean status=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme = DataManager.getInstance().getCurrentTheme();
        setTheme(theme.style);
        LanguageUtils.ChangeLanguage(this, LanguageUtils.current_language);
        setContentView(R.layout.setting_main_layout);
        initItem();
        initData();
    }

    private void initData() {
        UserBean user = DataManager.getInstance().getCurrentUser();
        if (user==null){
            status=true;
            viewHolder.username.setText(getString(R.string.loginorregister));
            viewHolder.image_user.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.icon_user));
        }else{
            viewHolder.username.setText(user.name);
            if (user.imageUrl != null && !user.imageUrl.equals("null")) {
                DataManager.getInstance().setImage(user.imageUrl, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        Bitmap circleBitmap = DrawCircle.getCircleBitmap(bitmap);
                        viewHolder.image_user.setImageBitmap(circleBitmap);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
            }
        }
    }

    private void initItem() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        StatusBarUtils.setWindowStatusBarColor(this, theme.color);
        setSupportActionBar(toolbar);
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        viewHolder = new ViewHolder();
        viewHolder.loginorregister.setOnClickListener(this);
        viewHolder.theme.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.logout:
                UserBean user = DataManager.getInstance().getCurrentUser();
                if (user!=null){
                    final Dialog dlg = DialogUtil.showLoadingDialog(this, null);
                    DataManager.getInstance().logout(user.name, user.password, new ILoadDataListener<ResponseHeaderBean>() {
                        @Override
                        public void onDataListner(ResponseHeaderBean result) {
                            if (result.getStatus()) {
//                            Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                            finish();
                                Toast.makeText(SettingActivity.this, getString(R.string.logoutsuccess), Toast.LENGTH_SHORT).show();
                                DataManager.getInstance().delUser();
                                DataManager.getInstance().updateCurrentUser(null);
                                initData();
                            } else {
                                Toast.makeText(SettingActivity.this, getString(R.string.logoutfail) + "：" + result.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            dlg.dismiss();
                        }

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(SettingActivity.this, getString(R.string.logoutfail) + "：" + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(SettingActivity.this,getString(R.string.alreadyloyout),Toast.LENGTH_SHORT).show();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.loginorregister:
                Intent intent2 = new Intent();
                if (status){
                    intent2.setClass(this, LoginActivity.class);
                    startActivity(intent2);
                    finish();
                }else{
                    intent2.setClass(this, PersonalInfoActivity.class);
                    startActivity(intent2);
                }
                break;
            case R.id.theme:
                Intent intent3 = new Intent(this, ThemeActivity.class);
                startActivity(intent3);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(this, MainActivity.class);
        startActivity(intent1);
        finish();
    }

    class ViewHolder {
        public LinearLayout loginorregister;
        public ImageView image_user;
        public TextView username;
        public LinearLayout theme;

        public ViewHolder() {
            loginorregister = (LinearLayout) findViewById(R.id.loginorregister);
            image_user = (ImageView) findViewById(R.id.image_user);
            username = (TextView) findViewById(R.id.username);
            theme = (LinearLayout) findViewById(R.id.theme);
        }
    }
}
