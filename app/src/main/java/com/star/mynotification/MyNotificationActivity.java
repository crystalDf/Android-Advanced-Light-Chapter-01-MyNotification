package com.star.mynotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RemoteViews;

public class MyNotificationActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 0;
    public static final int NOTIFICATION_ID_0 = 0;
    public static final int NOTIFICATION_ID_1 = 1;
    public static final int NOTIFICATION_ID_2 = 2;

    private Button mButtonNormal;
    private Button mButtonFolded;
    private Button mButtonHung;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonPublic;
    private RadioButton mRadioButtonPrivate;
    private RadioButton mRadioButtonSecret;

    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notification);

        mButtonNormal = findViewById(R.id.normal);
        mButtonFolded = findViewById(R.id.folded);
        mButtonHung = findViewById(R.id.hung);
        mRadioGroup = findViewById(R.id.radio_group);
        mRadioButtonPublic = findViewById(R.id.radio_button_public);
        mRadioButtonPrivate = findViewById(R.id.radio_button_private);
        mRadioButtonSecret = findViewById(R.id.radio_button_secret);

        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        mButtonNormal.setOnClickListener(v -> {

            Notification.Builder builder = getNotificationBuilder("普通通知");

            mNotificationManager.notify(NOTIFICATION_ID_0, builder.build());
        });

        mButtonFolded.setOnClickListener(v -> {

            Notification.Builder builder = getNotificationBuilder("折叠式通知");

            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.view_folded);
            Notification notification = builder.build();
            notification.bigContentView = remoteViews;

            mNotificationManager.notify(NOTIFICATION_ID_1, notification);
        });

        mButtonHung.setOnClickListener(v -> {

            Notification.Builder builder = getNotificationBuilder("悬挂式通知");

            Intent hungIntent = new Intent();
            hungIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            hungIntent.setClass(this, MyNotificationActivity.class);

            //如果描述的PendingIntent已经存在，则在产生新的Intent之前会先取消掉当前的
            PendingIntent hungPendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, hungIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setFullScreenIntent(hungPendingIntent, true);

            mNotificationManager.notify(NOTIFICATION_ID_2, builder.build());
        });
    }

    private Notification.Builder getNotificationBuilder(String notificationMode) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cn.bing.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, intent, 0);

        Notification.Builder builder = new Notification.Builder(this)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground))
                .setAutoCancel(true)
                .setContentTitle(notificationMode);

        selectNotificationLevel(builder);

        return builder;
    }

    private void selectNotificationLevel(Notification.Builder builder) {
        switch (mRadioGroup.getCheckedRadioButtonId()) {
            case R.id.radio_button_public:
                builder.setVisibility(Notification.VISIBILITY_PUBLIC);
                builder.setContentText("public");
                break;
            case R.id.radio_button_private:
                builder.setVisibility(Notification.VISIBILITY_PRIVATE);
                builder.setContentText("private");
                break;
            case R.id.radio_button_secret:
                builder.setVisibility(Notification.VISIBILITY_SECRET);
                builder.setContentText("secret");
                break;
            default:
        }
    }
}
