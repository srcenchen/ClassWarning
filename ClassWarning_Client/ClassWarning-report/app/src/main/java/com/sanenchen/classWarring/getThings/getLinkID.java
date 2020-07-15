package com.sanenchen.classWarring.getThings;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;


public class getLinkID {
    public String getLinkID (Context context){
        String linkID = "";
        try {
            SharedPreferences preferences = context.getSharedPreferences("LoginData", MODE_PRIVATE);
            linkID = preferences.getString("LinkID", null);
        } catch (Exception e) { }
        return linkID;
    }
}
