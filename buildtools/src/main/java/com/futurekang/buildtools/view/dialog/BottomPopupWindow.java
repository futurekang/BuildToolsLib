package com.futurekang.buildtools.view.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public abstract class BottomPopupWindow extends BaseDialog {

    public BottomPopupWindow(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void initDialog(View view) {
        super.initDialog(view);
        //点击屏幕关闭
        alterDialog.setCanceledOnTouchOutside(true);
        Window window = alterDialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
