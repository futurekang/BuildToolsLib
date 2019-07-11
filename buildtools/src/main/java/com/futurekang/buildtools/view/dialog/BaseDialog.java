package com.futurekang.buildtools.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public abstract class BaseDialog {
    protected Dialog dialog;
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
        setChildView(view);
        containerView = view;
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.hide();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
    }

    public View findViewById(int layoutId) {
        return containerView.findViewById(layoutId);
    }

    protected abstract void setChildView(View v);

    protected boolean setCancelable() {
        return false;
    }

    ;

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void hide() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void dismiss() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

}
