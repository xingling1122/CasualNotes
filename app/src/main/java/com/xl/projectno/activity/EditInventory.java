package com.xl.projectno.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.xl.projectno.R;
import com.xl.projectno.adapter.FragmentAdapter;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.model.ThemeBean;
import com.xl.projectno.utils.LanguageUtils;
import com.xl.projectno.utils.StatusBarUtils;
import com.xl.projectno.utils.SystemUtils;
import com.xl.projectno.widget.TabViewPager;

/**
 * Created by Administrator on 2017/3/28.
 */
public class EditInventory extends AppCompatActivity implements View.OnClickListener {
    ThemeBean theme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theme = DataManager.getInstance().getCurrentTheme();
        setTheme(theme.style);
        LanguageUtils.ChangeLanguage(this,LanguageUtils.current_language);
        setContentView(R.layout.inventory_edit_main);
        initItem();
        initData();
    }

    private void initItem() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        StatusBarUtils.setWindowStatusBarColor(this, theme.color);
        setSupportActionBar(toolbar);
        ImageView back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);

        TabViewPager tabViewPager = (TabViewPager)findViewById(R.id.tabViewPager);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new UseInventoryFragment());
        adapter.addFragment(new DropInventoryFragment());
        String[] arrays = new String[]{getString(R.string.useinventory),getString(R.string.dropinventory)};
        tabViewPager.initTabs(arrays, SystemUtils.getPhoneWidth(this));
        tabViewPager.setAdapter(adapter);
    }

    private void initData() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}
