package com.xl.projectno.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.xl.projectno.R;
import com.xl.projectno.adapter.FolderListAdapter;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.FolderBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/17.
 */
public class ChooseColorDialog extends Dialog implements View.OnClickListener {
    ImageButton[] buttons;

    public ChooseColorDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choosecolor);
        setCanceledOnTouchOutside(true);
        initButton();
    }

    private void initButton() {
        buttons = new ImageButton[15];
        buttons[0] = (ImageButton) findViewById(R.id.blue);
        buttons[1] = (ImageButton) findViewById(R.id.lightblue);
        buttons[2] = (ImageButton) findViewById(R.id.green);
        buttons[3] = (ImageButton) findViewById(R.id.lightgreen);
        buttons[4] = (ImageButton) findViewById(R.id.yellow);
        buttons[5] = (ImageButton) findViewById(R.id.lightyellow);
        buttons[6] = (ImageButton) findViewById(R.id.minyellow);
        buttons[7] = (ImageButton) findViewById(R.id.purple);
        buttons[8] = (ImageButton) findViewById(R.id.lightpurple);
        buttons[9] = (ImageButton) findViewById(R.id.coral);
        buttons[10] = (ImageButton) findViewById(R.id.grey);
        buttons[11] = (ImageButton) findViewById(R.id.lightgrey);
        buttons[12] = (ImageButton) findViewById(R.id.seagreen);
        buttons[13] = (ImageButton) findViewById(R.id.khaki);
        buttons[14] = (ImageButton) findViewById(R.id.colorno);
        for (ImageButton i : buttons) {
            i.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        int id = -1;
        if (onChooseListener!=null){
            switch (view.getId()){
                case R.id.colorno:
                    id = 0;
                    break;
                case R.id.blue:
                    id = 1;
                    break;
                case R.id.lightblue:
                    id = 2;
                    break;
                case R.id.green:
                    id = 3;
                    break;
                case R.id.lightgreen:
                    id = 4;
                    break;
                case R.id.yellow:
                    id = 5;
                    break;
                case R.id.lightyellow:
                    id = 6;
                    break;
                case R.id.minyellow:
                    id = 7;
                    break;
                case R.id.purple:
                    id = 8;
                    break;
                case R.id.lightpurple:
                    id = 9;
                    break;
                case R.id.coral:
                    id = 10;
                    break;
                case R.id.grey:
                    id = 11;
                    break;
                case R.id.lightgrey:
                    id = 12;
                    break;
                case R.id.seagreen:
                    id = 13;
                    break;
                case R.id.khaki:
                    id = 14;
                    break;
            }
            onChooseListener.onChoose(view.getBackground(),id);
        }
    }

    private OnChooseListener onChooseListener;

    public void setOnChooseListener(OnChooseListener onChooseListener) {
        this.onChooseListener = onChooseListener;
    }

    public interface OnChooseListener {
        void onChoose(Drawable color,int id);
    }
}
