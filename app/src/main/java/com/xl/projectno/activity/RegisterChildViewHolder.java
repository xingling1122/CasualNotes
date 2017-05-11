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
public class RegisterChildViewHolder {
    public EditText username;
    public EditText password;
    public ProgressButton register;
    public TextView login;

    public RegisterChildViewHolder(View view){
        username = (EditText)view.findViewById(R.id.username);
        password = (EditText)view.findViewById(R.id.password);
        register = (ProgressButton)view.findViewById(R.id.register);
        login = (TextView)view.findViewById(R.id.login);
    }
}
