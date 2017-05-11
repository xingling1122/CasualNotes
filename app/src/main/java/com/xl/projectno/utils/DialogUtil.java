package com.xl.projectno.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;

import com.xl.projectno.R;

/**
 * Created by huanglun on 16-9-29.
 */
public class DialogUtil {

    public static final AlertDialog showDialog(Context context, DialogInterface.OnClickListener onClickListener, String message, String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.app_name)).setMessage(message).setPositiveButton(text, onClickListener);
        AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogPush_anim;
        try {
            dialog.show();
            print(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dialog;
    }

    public static final AlertDialog showDialog(Context context, String message, int printNo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.app_name)).setMessage(message).setPositiveButton(context.getString(R.string.OK), null);
        AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogPush_anim;
        try {
            dialog.show();
            print(printNo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dialog;
    }

    public static final void showDialog(Context context, String message, int printNo, int lTitleResId, int rTitleResId, DialogInterface.OnClickListener onLeftClickListener, DialogInterface.OnClickListener onRightClickListener) {
        String str = context.getString(R.string.app_name);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(str).setMessage(message).setPositiveButton(context.getString(lTitleResId), onLeftClickListener).setNegativeButton(context.getString(rTitleResId), onRightClickListener);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogPush_anim;
        try {
            dialog.show();
            print(printNo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static final void showDialog(Context context, String message, int printNo, DialogInterface.OnClickListener onRightClickListener) {
        showDialog(context, message, printNo, R.string.OK, R.string.NO, null, onRightClickListener);
    }

    public static final void showDialog(Context context, String message, int printNo, DialogInterface.OnClickListener onLeftClickListener, String lBtnTitle, DialogInterface.OnClickListener onRightClickListener, String rBtnTitle) {
        String str = context.getString(R.string.app_name);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(str).setMessage(message).setPositiveButton(lBtnTitle, onLeftClickListener).setNegativeButton(rBtnTitle, onRightClickListener);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogPush_anim;
        try {
            dialog.show();
            dialog.getButton(-1).setBackgroundResource(R.drawable.btn_pos_blue_bg);
            print(printNo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static final void showDialog(Context context, String message, DialogInterface.OnClickListener onLClickListener, DialogInterface.OnClickListener onCClickListener, DialogInterface.OnClickListener onRClickListener) {
        String str = context.getString(R.string.app_name);
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
        localBuilder.setTitle(str).setMessage(message).setPositiveButton(R.string.wait, onLClickListener).setNeutralButton(R.string.NO, onCClickListener).setNegativeButton(R.string.tryagain, onRClickListener);
        AlertDialog localAlertDialog = localBuilder.create();
        localAlertDialog.setCanceledOnTouchOutside(false);
        localAlertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogPush_anim;
        try {
            localAlertDialog.show();
            print(2);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    public static final void showDialog(Context context, String message, DialogInterface.OnClickListener onLClickListener, String lBtnTitle, DialogInterface.OnClickListener onCClickListener, String cBtnTitle, DialogInterface.OnClickListener onRClickListener, String rBtnTitle) {
        String str = context.getString(R.string.app_name);
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
        localBuilder.setTitle(str).setMessage(message).setPositiveButton(lBtnTitle, onLClickListener).setNeutralButton(cBtnTitle, onCClickListener).setNegativeButton(rBtnTitle, onRClickListener);
        AlertDialog localAlertDialog = localBuilder.create();
        localAlertDialog.setCanceledOnTouchOutside(false);
        localAlertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogPush_anim;
        try {
            localAlertDialog.show();
            localAlertDialog.getButton(-1).setBackgroundResource(R.drawable.btn_pos_blue_bg);
            print(0);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }


    public static void showDialogOnUiThread(final String message, final Activity activity, final int printNo) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showDialog(activity, message, printNo);
            }
        });
    }


    private static void print(int printNo) {
        switch (printNo) {
            case 1:
//                j.c();
                break;
            case 2:
//                j.d();
                break;
            case 3:
//                j.e();
                break;
            default:
        }
    }

    public static final Dialog showLoadingDialog(Context context, CharSequence errMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(LayoutInflater.from(context).inflate(R.layout.loading_view, null));
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        try {
            dialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dialog;
    }
}
