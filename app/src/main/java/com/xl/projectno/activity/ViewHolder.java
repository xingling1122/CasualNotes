package com.xl.projectno.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xl.projectno.R;
import com.xl.projectno.widget.CustomViewPager;

/**
 * Created by Administrator on 2017/4/25.
 */
public class ViewHolder {
    public TextView logo;
    public CustomViewPager viewPager;
    public TextView changeversion;

    public ViewHolder(View view) {
        logo = (TextView) view.findViewById(R.id.logo);
        viewPager = (CustomViewPager) view.findViewById(R.id.viewPager);
        changeversion = (TextView) view.findViewById(R.id.changeversion);
    }
}
