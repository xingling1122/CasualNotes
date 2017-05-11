package com.xl.projectno.utils;

import android.content.Context;
import android.widget.Toast;

import com.xl.projectno.base.DataManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/3/24.
 */
public class ValidateUtils {
    private static ValidateUtils instance = new ValidateUtils();
    private static Context context;

    private String desc;

    private ValidateUtils(){}
    public static ValidateUtils getInstance(Context context){
        ValidateUtils.context=context;
        return instance;
    }
    public boolean validate(String oldname,String name,int dataType){
        if (name.equals("")){
            Toast.makeText(context,"名字不能为空",Toast.LENGTH_SHORT).show();
            desc = "名字不能为空";
            return false;
        }
        if(DataManager.getInstance().isExit(oldname,name,dataType)){
            Toast.makeText(context,"名字已经存在",Toast.LENGTH_SHORT).show();
            desc = "名字已经存在";
            return false;
        }
        return true;
    }

    public boolean validate(String name,String password){
        if (name.equals("")){
            Toast.makeText(context,"名字不能为空",Toast.LENGTH_SHORT).show();
            desc = "名字不能为空";
            return false;
        }
        if (password.equals("")){
            Toast.makeText(context,"密码不能为空",Toast.LENGTH_SHORT).show();
            desc = "密码不能为空";
            return false;
        }
        return true;
    }

    public boolean validaters(String phone,String valicode){
        if (phone.equals("")){
            Toast.makeText(context,"电话不能为空",Toast.LENGTH_SHORT).show();
            desc = "电话不能为空";
            return false;
        }else if (!isMobileNO(phone)){
            Toast.makeText(context,"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
            desc = "请输入正确的手机号码";
            return false;
        }
        if (valicode.equals("")){
            Toast.makeText(context,"验证码不能为空",Toast.LENGTH_SHORT).show();
            desc = "验证码不能为空";
            return false;
        }else if(!isValicode(valicode)){
            Toast.makeText(context,"请输入正确的验证码",Toast.LENGTH_SHORT).show();
            desc = "请输入正确的验证码";
            return false;
        }
        return true;
    }

    public boolean validateps(String psone,String pstwo){
        if (psone.equals("")){
            Toast.makeText(context,"密码不能为空",Toast.LENGTH_SHORT).show();
            desc = "密码不能为空";
            return false;
        }
        if (pstwo.equals("")){
            Toast.makeText(context,"确认密码不能为空",Toast.LENGTH_SHORT).show();
            desc = "确认密码不能为空";
            return false;
        }else if(!psone.equals(pstwo)){
            Toast.makeText(context,"请输入相同的密码",Toast.LENGTH_SHORT).show();
            desc = "请输入相同的密码";
            return false;
        }
        return true;
    }

    public boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public boolean isValicode(String valicode) {
        try{
            int i = Integer.valueOf(valicode);
            if (i>9999||i<1000){
                return false;
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public String getDesc(){
        return desc;
    }
}
