package com.sanenchen.classWarring.getThings;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;


public class getIDs {
    public String getLinkID(Context context) {
        String linkID = null;
        try {
            SharedPreferences preferences = context.getSharedPreferences("LoginData", MODE_PRIVATE);
            linkID = preferences.getString("LinkID", null);
        } catch (Exception e) {
        }
        return linkID;
    }

    public String getClassID(Context context) {
        String classID = null;
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("ChooseCache", MODE_PRIVATE);
            classID = sharedPreferences.getString("chooseClassID", null);
        } catch (Exception e) {

        }
        return classID;
    }
}
