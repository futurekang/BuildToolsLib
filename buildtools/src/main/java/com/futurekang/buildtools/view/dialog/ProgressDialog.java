package com.futurekang.buildtools.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.futurekang.buildtools.view.ProgressBar;

public class ProgressDialog extends BaseDialog {
    ProgressBar progressBar;

    public ProgressDialog(Context context) {
        super(context, null);
        progressBar = new ProgressBar(context);
        progressBar.setStyle(ProgressBar.Style.LOOP);
        progressBar.start();
        initDialog(progressBar);
    }

    public ProgressDialog(Context context, ProgressBar.Style style) {
        super(context, null);
        progressBar = new ProgressBar(context);
        progressBar.setStyle(style);
        progressBar.start();
        initDialog(progressBar);
    }

    @Override
    protected void setChildView(View v) {

    }


    public void setProgress(int progress) {
        progressBar.setProgress(progress);
    }

    public void setMax(int max) {
        progressBar.setMax(max);
        progressBar.postInvalidate();
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        alterDialog.setOnDismissListener(onDismissListener);
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener cancelListener) {
        alterDialog.setOnCancelListener(cancelListener);
    }
}
