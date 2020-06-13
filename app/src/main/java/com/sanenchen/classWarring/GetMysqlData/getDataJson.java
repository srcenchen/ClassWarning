package com.sanenchen.classWarring.GetMysqlData;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class getDataJson {
    public String getDataJson(String sql) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://classwarning.cdn.lyqmc.cn/runSQL?sql=" + sql).build();
        try {
            Response response = client.newCall(request).execute();
            String ReData = response.body().string();
            Log.i("aaa", ReData);
            return ReData;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
