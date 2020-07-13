package com.sanenchen.classWarring.UI;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.sanenchen.classWarring.R;
import com.sanenchen.classWarring.getThings.getDataJson;

import org.json.JSONArray;
import org.json.JSONException;

public class TabActivity extends AppCompatActivity {

    String GetUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        listenButtonActionPerformed();
    }

    @Override
    protected void onRestart() {
        SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
        String user = preferences.getString("user", null);

        GetUser = user;
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDataJson getDataJson = new getDataJson();
                String jsonData = getDataJson.getLoginPassword(GetUser);

                try {
                    JSONArray jsonArray = new JSONArray(jsonData);
                    if (jsonArray.length() == 0) {
                        Looper.prepare();
                        //Toast.makeText(TabActivity.this, "登录身份过期，请重新登录！", Toast.LENGTH_SHORT).show();

                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        Looper.loop();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        super.onRestart();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    Intent intent = new Intent(TabActivity.this, LoginActivity.class);
                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                    editor.clear();
                    editor.apply();
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    private void listenButtonActionPerformed() {
        BottomBar bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (tabId) {
                    case R.id.tab_recents:
                        transaction.replace(R.id.frame, new AllWarningActivity(), "FragTab1").commit();
                        break;
                    case R.id.tab_nearby:
                        transaction.replace(R.id.frame, new ClassWarringUpLoad(), "FragTab1").commit();
                        break;

                    case R.id.tab_mine:
                        transaction.replace(R.id.frame, new MineActivity(), "FragTab1").commit();
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Intent intent = new Intent(TabActivity.this, ClassWarningSearchActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}