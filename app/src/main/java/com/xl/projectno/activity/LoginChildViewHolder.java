package com.xl.projectno.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xl.projectno.R;
import com.xl.projectno.widget.ProgressButton;

/**
 * Created by Administrator on 2017/4/25.
 */
public class LoginChildViewHolder {
    public EditText username;
    public EditText password;
    public ProgressButton login;
    public TextView forget;
    public TextView create;

    public LoginChildViewHolder(View view){
        username = (EditText)view.findViewById(R.id.username);
        password = (EditText)view.findViewById(R.id.password);
        login = (ProgressButton)view.findViewById(R.id.login);
        forget = (TextView)view.findViewById(R.id.forgetpsw);
        create = (TextView)view.findViewById(R.id.create);
    }
}
