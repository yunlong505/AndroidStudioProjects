package com.example.notificationtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String id = "channel01";
    NotificationChannel mChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendNotice = (Button) findViewById(R.id.send_notice);
        sendNotice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_notice:
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                //创建启动另一个活动的意图
                Intent intent = new Intent(this, NotificationActivity.class);
                PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);

                //判断api版本在26以上的使用新的调用方法
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    mChannel = new NotificationChannel(id, "channel01", NotificationManager.IMPORTANCE_HIGH);
                    mChannel.setVibrationPattern(new long[]{0, 1000, 1000,});
                    mChannel.enableVibration(true);
                    mChannel.enableLights(true);
                    mChannel.setLightColor(Color.GREEN);
                    manager.createNotificationChannel(mChannel);
                }

                Notification notification = new NotificationCompat.Builder(this, id)
                        .setContentTitle("This is content title")
                //        .setContentText("This is content text")
                        .setContentText("Learn how to build notifications, send and sync data, and use voice actions. Get the official Android IDE and developer tools to build apps for Android.")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Learn how to build notifications, send and sync data, and use voice actions. Get the official Android IDE and developer tools to build apps for Android."))
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.ab)))
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.adb)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ab))
                        .setContentIntent(pi)
                        .setLights(Color.GREEN, 1000, 1000)
                       // .setPriority(NotificationCompat.PRIORITY_MAX)
                        //.setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Free.ogg")))//通知调用音频文件
                        //.setAutoCancel(true)  //调用完后自动将通知取消
                        .build();

                manager.notify(1, notification);
                break;
            default:
                break;
        }
    }
}
