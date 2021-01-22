package com.futurekang.buildtools.util;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;

import androidx.annotation.Nullable;


public class NotificationUtil {
    private static final String NOTIFICATION_CHANNEL_NAME = "BackgroundLocation";
    private NotificationManager notificationManager = null;
    private Integer notificationId;
    private boolean isCreateChannel = false;
    private Notification notification = null;
    private String contentText;
    private String contentTitle;
    private Intent intent;
    private Intent deleteIntent;

    private NotificationUtil() {
    }

    @SuppressLint("NewApi")
    private NotificationUtil buildNotification(Context context, int drawbleId) {
        Notification.Builder builder = null;
        if (null == notificationManager) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            //Android O上对Notification进行了修改，如果设置的targetSDKVersion>=26建议使用此种方式创建通知栏

            String channelId = context.getPackageName();
            if (!isCreateChannel) {
                NotificationChannel notificationChannel = new NotificationChannel(channelId,
                        NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.enableLights(true);//是否在桌面icon右上角展示小圆点
                notificationChannel.setLightColor(Color.BLUE); //小圆点颜色
                notificationChannel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
                notificationManager.createNotificationChannel(notificationChannel);
                isCreateChannel = true;
            }
            builder = new Notification.Builder(context.getApplicationContext(), channelId);
        } else {
            builder = new Notification.Builder(context.getApplicationContext());
        }

        builder.setSmallIcon(drawbleId)
                .setWhen(System.currentTimeMillis());
        if (!TextUtils.isEmpty(contentText)) {
            builder.setContentText(contentText);
        }
        if (!TextUtils.isEmpty(contentTitle)) {
            builder.setContentTitle(contentTitle);
        }
        if (intent != null) {
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
        }

        if (android.os.Build.VERSION.SDK_INT >= 16) {
            notification = builder.build();
        } else {
            notification = builder.getNotification();
        }
        return this;
    }

    private Notification getNotification() {
        if (notification != null) {
            return notification;
        }
        return null;
    }

    public static class Builder {

        private NotificationUtil instance;
        private Context mContext;
        private Integer id = -1;


        public Builder(@Nullable Context context) {
            this.mContext = context;
            this.instance = new NotificationUtil();
        }

        public Builder setSmallIcon(@Nullable Integer id) {
            if (id != null) {
                this.id = id;
            }
            return this;
        }

        public Builder setContentText(@Nullable String contentText) {
            instance.contentText = contentText;
            return this;
        }

        public Builder setContentTitle(@Nullable String contentTitle) {
            instance.contentTitle = contentTitle;
            return this;
        }


        public Builder setIntent(@Nullable Intent intent) {
            instance.intent = intent;
            return this;
        }

        public Builder build() {
            instance.buildNotification(mContext, id);
            return this;
        }

        public void show(@Nullable Integer notificationId) {
            instance.notificationId = notificationId;
            instance.notificationManager.notify(notificationId, instance.notification);
        }
    }
}
