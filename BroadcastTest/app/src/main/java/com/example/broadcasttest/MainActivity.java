package com.example.broadcasttest;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MainActivity extends AppCompatActivity {
    MyBroadcastReceiver myBroadcastReceiver;
    LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        //动态注册
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.broadcasttest.MY_BROADCAST");
        myBroadcastReceiver = new MyBroadcastReceiver();
        intentFilter.setPriority(100);
        //注册普通广播
        registerReceiver(myBroadcastReceiver, intentFilter);

        //注册本地广播
        localBroadcastManager.registerReceiver(myBroadcastReceiver, intentFilter);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.broadcasttest.MY_BROADCAST");
                //intent.setComponent(new ComponentName("com.example.broadcasttest","com.example.broadcasttest.MyBroadcastReceiver"));
                //标准广播
                //sendBroadcast(intent);
                //有序广播
               // sendOrderedBroadcast(intent, null);
                //本地广播
                localBroadcastManager.sendBroadcast(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //普通广播的销毁
        // unregisterReceiver(myBroadcastReceiver);
        //本地广播的销毁
        localBroadcastManager.unregisterReceiver(myBroadcastReceiver);
    }

}