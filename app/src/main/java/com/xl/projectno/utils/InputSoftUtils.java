package com.xl.projectno.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/3/23.
 */
public class InputSoftUtils {
    public static void closeInputSoft(TextView textView) {
        InputMethodManager imm = (InputMethodManager) textView
                .getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(
                    textView.getApplicationWindowToken(), 0);
        }
    }

    public static void openInputSoft(TextView textView) {
        InputMethodManager imm = (InputMethodManager) textView
                .getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        if (!imm.isActive()) {
            imm.toggleSoftInputFromWindow(
                    textView.getApplicationWindowToken(), 0,0);
        }
    }
}
