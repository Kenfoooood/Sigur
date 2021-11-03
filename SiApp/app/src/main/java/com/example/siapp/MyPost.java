package com.example.siapp;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MyPost extends AsyncTask<String, String, String> {
    ICallback object;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(String... strings) {
        final String charset = "UTF-8";
        URL url = null;
        try {
            url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true); // Triggers POST.
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/json;charset=" + charset);
            //connection.setRequestProperty("Content-Length", String.valueOf(data0.length));
            connection.connect();
            if (strings.length > 1) {
                if (!strings[1].isEmpty()) {
                    byte[] bytes = strings[1].getBytes();
                    try (OutputStream output = connection.getOutputStream()) {
                        output.write(bytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            connection.getResponseCode();
            String data0 = "";
            InputStream iStream = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream, StandardCharsets.UTF_8));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data0 = sb.toString();
            object.callback(data0);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void dowork(String url, String json, ICallback inter) {
        object = inter;
        this.execute(url, json);
    }
}
