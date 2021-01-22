package com.futurekang.buildtools.system;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.futurekang.buildtools.adapter.CommAdapter;
import com.futurekang.buildtools.util.ToastUtils;
import com.futurekang.buildtools.util.Utils;
import com.futurekang.buildtools.view.dialog.BottomPopupWindow;
import com.futurekang.fastbuild.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ShareTools {
    public static void sendMail(Activity activity, String subject, String text, String mail, Uri fileUri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String[] tos = {mail};
        intent.putExtra(Intent.EXTRA_EMAIL, tos);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_STREAM, fileUri);
        intent.setType("message/rfc882");
        Intent.createChooser(intent, "选择邮箱");
        activity.startActivity(intent);
    }

    public static void share(Activity activity, Uri fileUri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, fileUri);
        intent.setType("application/pdf");
        Intent.createChooser(intent, "分享到");
        activity.startActivity(intent);
    }

    /**
     * 查询微信时候以安装
     *
     * @param context
     * @return
     */
    public static boolean installWeChat(Context context) {
        if (context == null) throw new RuntimeException("上下文对象不能为空");
        PackageManager packageManager = context.getPackageManager();
        String packageName = "com.tencent.mm";
        boolean hasInstallWx;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_GIDS);
            hasInstallWx = packageInfo != null;
        } catch (PackageManager.NameNotFoundException e) {
            hasInstallWx = false;
            e.printStackTrace();
        }
        return hasInstallWx;
    }

    /**
     * 分享文件到微信
     *
     * @param activity
     * @param fileUri
     * @return
     */
    public static void shareToWeChat(Activity activity, Uri fileUri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI"));
        intent.putExtra(Intent.EXTRA_TEXT, "你的合同");
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, fileUri);
        activity.startActivity(intent);
    }

    @SuppressLint("WrongConstant")
    public static List<ResolveInfo> getShareApplicationList(Context context) {
        if (context == null) throw new RuntimeException("上下文对象不能为空");
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.setType("*/*");
        return packageManager.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
    }

    public static void sendToMail(Activity activity, String subject, String text, String mail, Uri fileUri) {
        List<ResolveInfo> resolveInfos = filter(getShareApplicationList(activity), resolveInfo -> {
            if (resolveInfo.activityInfo.packageName.contains("mail") ||
                    resolveInfo.activityInfo.name.equals("com.tencent.mm.ui.tools.ShareImgUI")) {
                return true;
            } else {
                return false;
            }
        });
        if (resolveInfos.size() == 0) {
            ToastUtils.ShowToast(activity, "未找到相关应用，请安装后重试");
            return;
        }
        //将微信放到第一位
        for (int index = 0, length = resolveInfos.size(); index < length; index++) {
            if ("com.tencent.mm.ui.tools.ShareImgUI".equals(resolveInfos.get(index).activityInfo.name)) {
                ResolveInfo temp = resolveInfos.get(0);
                resolveInfos.set(0, resolveInfos.get(index));
                resolveInfos.set(index, temp);
                break;
            }
        }

        CommAdapter<ResolveInfo> commAdapter = new CommAdapter<ResolveInfo>(resolveInfos, R.layout.item_share_app_layout) {
            @Override
            public void setView(ViewHolder viewHolder, int position, Context context) {
                viewHolder.getConverView().setLayoutParams(
                        new LinearLayout.LayoutParams(
                                Utils.getScreenWidth(activity) / 4,
                                Utils.dip2px(activity, 250) / 2));
                ResolveInfo resolveInfo = resolveInfos.get(position);
                TextView tvAppName = viewHolder.getItemView(R.id.tv_app_name);
                ImageView ivAppIcon = viewHolder.getItemView(R.id.iv_app_icon);
                tvAppName.setText(resolveInfo.loadLabel(activity.getPackageManager()));
                ivAppIcon.setImageDrawable(resolveInfo.loadIcon(activity.getPackageManager()));
                tvAppName.setTag(tvAppName.getId(), resolveInfo);
            }
        };
        BottomPopupWindow baseDialog = new BottomPopupWindow(activity, R.layout.popup_window_share_layout) {
            @Override
            protected void setChildView(View v) {
                GridView gridView = (GridView) findViewById(R.id.gv_app_list);
                gridView.setAdapter(commAdapter);
                int size = resolveInfos.size();
                int length = 100;
                DisplayMetrics dm = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
                float density = dm.density;
                int gridViewWidth = (int) (size * (length + 4) * density);
                int itemWidth = (int) (length * density);


                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        gridViewWidth + 100, LinearLayout.LayoutParams.MATCH_PARENT);
                gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
                gridView.setColumnWidth(itemWidth); // 设置列表项宽
                gridView.setStretchMode(GridView.NO_STRETCH);
                gridView.setNumColumns(size); // 设置列数量=列表集合数
                gridView.setOnItemClickListener((parent, view, position, id) -> {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    TextView tvAppName = view.findViewById(R.id.tv_app_name);
                    ResolveInfo resolveInfo = (ResolveInfo) tvAppName.getTag(tvAppName.getId());
                    intent.setComponent(new ComponentName(resolveInfo.activityInfo.packageName,
                            resolveInfo.activityInfo.name));
                    String[] tos = {mail};
                    intent.putExtra(Intent.EXTRA_EMAIL, tos);
                    intent.putExtra(Intent.EXTRA_TEXT, text);
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    intent.putExtra(Intent.EXTRA_STREAM, fileUri);
                    intent.setType("message/rfc882");
                    activity.startActivity(intent);
                    dismiss();
                });
                findViewById(R.id.btn_cancel).setOnClickListener(v1 -> dismiss());
            }
        };
        baseDialog.setCanceledOnTouchOutside(true);
        baseDialog.show();

    }

    /**
     * 过滤器
     *
     * @param <T>
     * @author puyf
     */
    public interface Filter<T> {
        /**
         * 筛选是否通过
         *
         * @param t
         * @return true 表示通过
         */
        boolean pass(T t);
    }

    /**
     * @param datas     数据源
     * @param condition 过滤条件
     * @return 返回过滤后的集合
     * @author puyf
     * @Description:过滤集合
     */
    public static <T> List<T> filter(Collection<T> datas, Filter<T> condition) {
        List<T> result = new ArrayList<>();
        if (condition != null) {
            for (T t : datas) {
                if (condition.pass(t)) {
                    result.add(t);
                }
            }
        } else {
            return new ArrayList<>(datas);
        }
        return result;
    }
}
