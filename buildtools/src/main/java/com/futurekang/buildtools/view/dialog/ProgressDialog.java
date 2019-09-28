package com.futurekang.buildtools.view.dialog;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.futurekang.buildtools.util.Utils;
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
        progressBar.setProgress(progress);
    }

    public void setMax(int max) {
        progressBar.setMax(max);
        progressBar.postInvalidate();
    }
}
