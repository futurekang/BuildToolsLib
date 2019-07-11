package com.futurekang.buildtools.view.dialog;

import android.content.Context;
import android.view.View;

import com.futurekang.buildtools.view.ProgressBar;

public class ProgressDialog extends BaseDialog {
    ProgressBar progressBar;
    volatile ProgressBar.Style defaultStyle = ProgressBar.Style.LOOP;

    public ProgressDialog(Context context) {
        super(context, null);
        progressBar = new ProgressBar(context);
        progressBar.setStyle(defaultStyle);
        progressBar.start();
        initDialog(progressBar);
    }

    public ProgressDialog(Context context, ProgressBar.Style style) {
        super(context, null);
        progressBar = new ProgressBar(context);
        if (null != style) {
            this.defaultStyle = style;
        }
        progressBar.setStyle(defaultStyle);
        progressBar.start();
        initDialog(progressBar);
    }

    @Override
    protected void setChildView(View v) {

    }


    public void setProgress(int progress) {
        progressBar.setStyle(ProgressBar.Style.ARC_TEXT);
        progressBar.setProgress(progress);
    }

    public void setMax(int max) {
        progressBar.setStyle(ProgressBar.Style.ARC_TEXT);
        progressBar.setMax(max);
        progressBar.postInvalidate();
    }
}
