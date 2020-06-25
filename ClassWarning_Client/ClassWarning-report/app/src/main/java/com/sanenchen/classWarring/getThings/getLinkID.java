package com.sanenchen.classWarring.getThings;

import android.content.Context;
import android.content.SharedPreferences;

import com.sanenchen.classWarring.UI.MainActivity;

import static android.content.Context.MODE_PRIVATE;


public class getLinkID {
    public String getLinkID (Context context){
        SharedPreferences preferences = context.getSharedPreferences("data", MODE_PRIVATE);
        String linkID = preferences.getString("LinkID", null);
        return linkID;
    }
}
