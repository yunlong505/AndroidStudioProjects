package com.example.networktest;

public interface HttpCallbackListener {
    void onFinish(String response);//返回成功时调用

    void onError(Exception e);//网络操作出现错误时调用
}
