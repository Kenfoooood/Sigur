package com.example.siapp;

public class Callback implements ICallback {
    String url;
    String data;
    ICallback callback;

    @Override
    public void callback(String answer) {
        System.out.println(answer);
    }

    public Callback(String url, String data) {
        this.callback = this;
        this.url = url;
        this.data = data;
    }


}



