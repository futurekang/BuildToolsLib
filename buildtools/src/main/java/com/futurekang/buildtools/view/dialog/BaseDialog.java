package com.futurekang.buildtools.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public abstract class BaseDialog {
    protected Dialog alterDialog;
    protected Context context;
    protected int layoutId;
    protected View containerView;

    public BaseDialog(Context context, int layoutId) {
        this.context = context;
        this.layoutId = layoutId;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, null);
        initDialog(view);
    }

    public BaseDialog(Context context, View view) {
        this.context = context;
        initDialog(view);
    }


    protected void initDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        alterDialog = builder.create();
        containerView = view;
        setChildView(view);
        alterDialog.setCanceledOnTouchOutside(false);
        alterDialog.create();
        alterDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        alterDialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
    }


    public View findViewById(int layoutId) {
        return containerView.findViewById(layoutId);
    }

    protected abstract void setChildView(View v);

    public void setCanceledOnTouchOutside(boolean enable) {
        alterDialog.setCanceledOnTouchOutside(enable);
    }

    public void show() {
        if (alterDialog != null) {
            alterDialog.show();
        }
    }

    public void hide() {
        if (alterDialog != null) {
            alterDialog.dismiss();
        }
    }

    public void dismiss() {
        if (alterDialog != null) {
            if (alterDialog.isShowing()) {
                alterDialog.dismiss();
            }
        }
    }

}
