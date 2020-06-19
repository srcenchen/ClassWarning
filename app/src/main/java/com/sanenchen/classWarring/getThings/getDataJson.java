package com.sanenchen.classWarring.getThings;

import android.content.Context;

import com.sanenchen.classWarring.UI.AllWarningActivity;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class getDataJson {
    public String getLoginPassword(String user) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://classwarning.cdn.lyqmc.cn/loginSign?user=" + user).build();
        try {
            Response response = client.newCall(request).execute();
            String ReData = response.body().string();
            return ReData;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public String getSearchReply(String MessageWhat, Context context, String MysqlID, String FirstNa) {
        getLinkID getlinkID = new getLinkID();
        OkHttpClient client = new OkHttpClient();
        Request request = null;
        if (MessageWhat.equals("2")) {
            request = new Request.Builder().url("http://classwarning.cdn.lyqmc.cn/SearchSQL?messagewhat=" + MessageWhat + "&MysqlID=" + MysqlID).build();
        } else if (MessageWhat.equals("3")){
            request = new Request.Builder().url("http://classwarning.cdn.lyqmc.cn/SearchSQL?messagewhat=" + MessageWhat + "&linkid=" + getlinkID.getLinkID(context) + "&FirstNa=" + FirstNa).build();
        } else {
            request = new Request.Builder().url("http://classwarning.cdn.lyqmc.cn/SearchSQL?messagewhat=" + MessageWhat + "&linkid=" + getlinkID.getLinkID(context)).build();
        }
        try {
            Response response = client.newCall(request).execute();
            String ReData = response.body().string();
            return ReData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getAddWarnReply(String Title, String WarningGroup, String WarningStudent, String WarningFun, String FunStartTime, String FunEndTime
            , String BeizhuSS, String ee, int geta, Context context) {
        getLinkID getlinkID = new getLinkID();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://classwarning.cdn.lyqmc.cn/AddWarning?Title=" + Title
                + "&WarningGroup=" + WarningGroup + "&WarningStudent="
                + WarningStudent + "&WarningFun=" + WarningFun + "&FunStartTime="
                + FunStartTime + "&FunEndTime=" + FunEndTime + "&BeizhuSS=" + BeizhuSS
                + "&ee=" + ee + "&linkid=" + getlinkID.getLinkID(context) + "&Total=" + geta).build();
        try {
            Response response = client.newCall(request).execute();
            String ReData = response.body().string();
            return ReData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
