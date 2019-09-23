package com.example.broadcasttest2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.broadcasttest.MY_BROADCAST");
        AnotherBroadcastReceiver anotherBroadcastReceiver = new AnotherBroadcastReceiver();
        intentFilter.setPriority(49);
        registerReceiver(anotherBroadcastReceiver, intentFilter);
    }
}
