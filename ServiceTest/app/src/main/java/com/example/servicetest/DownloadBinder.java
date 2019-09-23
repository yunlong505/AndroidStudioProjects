package com.example.servicetest;

import android.os.Binder;
import android.util.Log;

public class DownloadBinder extends Binder {
    public void startDownload() {
        Log.d("MyService", "startDownload executed");
    }

    public int getProgress() {
        Log.d("MyService", "getProgress executed");
        return 0;
    }
}
