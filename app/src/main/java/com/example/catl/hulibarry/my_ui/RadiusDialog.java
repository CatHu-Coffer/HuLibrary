package com.example.catl.hulibarry.my_ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.catl.hulibarry.R;

import static com.example.catl.hulibarry.BaseAppActivity.isStrEmpty;

/**
 * Created by HSL on 2020/12/23.
 * 圆脚弹框
 */

public class RadiusDialog extends Dialog {

    private TextView dialogTitle;
    private TextView dialogMessage;
    private TextView dialogButton;
    private TextView dialogButtonS;
    private Context context;

    //构造方法
    public RadiusDialog(Context context) {

        //设置对话框样式
        super(context, R.style.radiusDialog);
        this.context = context;
        //通过LayoutInflater获取布局
        View view = LayoutInflater.from(getContext()).inflate(R.layout.default_dialog_layout, null);
        dialogTitle = view.findViewById(R.id.dialog_title);
        dialogMessage = view.findViewById(R.id.dialog_message);
        dialogButton = view.findViewById(R.id.dialog_button);
        dialogButtonS = view.findViewById(R.id.dialog_button_s);
        setContentView(view);  //设置显示的视图
    }

    //设置标题
    public void setTitleText(String title) {
        dialogTitle.setText(title);
    }  //设置标题

    public void setMessageText(String message) {
        if (isStrEmpty(message)) {
            dialogMessage.setText(message);
        } else {
            dialogTitle.setPadding(10, 50, 10, 50);
            dialogMessage.setVisibility(View.GONE);
        }
    }

    public void setButtonText(String confirm, int confirmColor) {
        dialogButton.setText(confirm);
        if (confirmColor != -1) {
            dialogButton.setTextColor(confirmColor);
        }
    }

    @SuppressLint("NewApi")
    public void setButtonSText(String cancel, int cancelColor) {
        if (isStrEmpty(cancel)) {
            dialogButtonS.setText(cancel);
            if (cancelColor != -1) {
                dialogButtonS.setTextColor(cancelColor);
            }
        } else {
            dialogButton.setTextColor(ContextCompat.getColor(context, R.color.common_bg));
            dialogButtonS.setVisibility(View.GONE);
        }
    }

    //取消监听
    public void setOnCancelListener(View.OnClickListener listener) {
        dialogButtonS.setOnClickListener(listener);
    }

    //退出监听
    public void setOnExitListener(View.OnClickListener listener) {
        dialogButton.setOnClickListener(listener);
    }
}
