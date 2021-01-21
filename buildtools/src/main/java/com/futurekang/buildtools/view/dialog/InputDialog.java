package com.futurekang.buildtools.view.dialog;

import android.content.Context;
import android.view.WindowManager;

public abstract class InputDialog extends BaseDialog {
    public InputDialog(Context context, int layoutId) {
        super(context, layoutId);
        alterDialog.setCanceledOnTouchOutside(true);
        alterDialog.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alterDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        show();
        hide();
    }
}
