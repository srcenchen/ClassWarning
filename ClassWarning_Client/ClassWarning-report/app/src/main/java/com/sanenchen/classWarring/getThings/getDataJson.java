package com.sanenchen.classWarring.getThings;

import android.content.Context;
import android.util.Log;

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

        int i;
        switch (MessageWhat) {
            case "getAllClassWarning":
            case "getTotalClassWarning":
            case "getMyClass":
                request = new Request.Builder().url("http://classwarning.cdn.lyqmc.cn/SearchSQL?messagewhat="
                        + MessageWhat + "&linkid=" + getlinkID.getLinkID(context)).build();
                break;
            case "getDetailedThings":
                request = new Request.Builder().url("http://classwarning.cdn.lyqmc.cn/SearchSQL?messagewhat="
                        + MessageWhat + "&MysqlID=" + MysqlID).build();
                break;
            case "SearchClassWarning":
                request = new Request.Builder().url("http://classwarning.cdn.lyqmc.cn/SearchSQL?messagewhat="
                        + MessageWhat + "&linkid=" + getlinkID.getLinkID(context) + "&FirstNa=" + FirstNa).build();
                break;
            case "getSupportSchool":
                request = new Request.Builder().url("http://classwarning.cdn.lyqmc.cn/SearchSQL?messagewhat=getSchool").build();
                break;
            default:
                break;
        }
        try {
            Response response = client.newCall(request).execute();
            String ReData = response.body().string();
            Log.i("Result", ReData);
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

    public String getAddUserReply(String user, String password, String linkid, String schoolName, String grade, String worker) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://classwarning.cdn.lyqmc.cn/addUser?user=" + user +
                "&password=" + password + "&linkid=" + linkid +
                "&schoolName=" + schoolName + "&grade=" + grade + "&worker=" + worker).build();

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

