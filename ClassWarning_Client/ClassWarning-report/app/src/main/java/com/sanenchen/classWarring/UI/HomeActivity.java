package com.sanenchen.classWarring.UI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.sanenchen.classWarring.R;
import com.sanenchen.classWarring.Setting.AboutActivity;
import com.sanenchen.classWarring.Setting.SettingActivity;

import java.io.IOException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {
    String ReDataPic = null;
    String getPrePic;
    View headView;
    SharedPreferences prefs;
    ImageView imageView;
    NavigationView navigationView;
    DrawerLayout mDrawerLayout;
    ActionBar actionBar;
    public static Activity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        // 全局变量进行关联
        navigationView = findViewById(R.id.navigationView);
        mDrawerLayout = findViewById(R.id.home_drawer_layout);
        actionBar = getSupportActionBar();
        // 初始化
        navigationView.setCheckedItem(R.id.home_item); //默认选中
        headView = navigationView.getHeaderView(0);
        actionBar.setDisplayHomeAsUpEnabled(true); // 显示标题旁边的按钮
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        // 使用方法
        setPicture();
        LoginSign();
        ListenItem();

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_frame1, new ClassManagerActivity());
        transaction.commit();
        instance = this;
    }

    // 将头像设置为Bing每日一图
    public void setPicture() {
        // 设置Bing一图
        prefs = getSharedPreferences("BingPicture", MODE_PRIVATE);
        headView = navigationView.getHeaderView(0);
        imageView = headView.findViewById(R.id.h_head2);
        getPrePic = prefs.getString("bingPic", "null");

        if (!getPrePic.equals("null")) {
            Glide.with(this)
                    .load(getPrePic)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(imageView);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("http://get-bing-pic.cdn.lyqmc.cn/geturl").build();
                try {
                    Response response = client.newCall(request).execute();
                    ReDataPic = response.body().string();
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 放入个人信息
    @SuppressLint("SetTextI18n")
    public void LoginSign() {
        // 获取基本用户信息
        SharedPreferences preferences = getSharedPreferences("LoginData", MODE_PRIVATE);
        String user = preferences.getString("user", null);
        String schoolName = preferences.getString("schoolName", "");
        String grade = preferences.getString("grade", "");
        String worker = preferences.getString("worker", "");

        TextView userID = headView.findViewById(R.id.userID_nv);
        TextView SchoolInformation = headView.findViewById(R.id.SchoolInformation_nv);
        userID.setText(user); // 放入用户名
        SchoolInformation.setText(schoolName + " / " + grade + " / " + worker);
    }

    // 监听Item
    private void ListenItem() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = HomeActivity.this.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.home_item:
                        fragmentTransaction.replace(R.id.fragment_frame1, new ClassManagerActivity());
                        mDrawerLayout.closeDrawers();
                        actionBar.setTitle("班级违纪-上报端");
                        break;
                    case R.id.setting_item:
                        fragmentTransaction.replace(R.id.fragment_frame1, new SettingActivity());
                        mDrawerLayout.closeDrawers();
                        actionBar.setTitle("设置");
                        break;
                    case R.id.about_item:
                        fragmentTransaction.replace(R.id.fragment_frame1, new AboutActivity());
                        mDrawerLayout.closeDrawers();
                        actionBar.setTitle("关于");
                        break;
                    default:
                        return false;
                }
                // 事务提交
                fragmentTransaction.commit();
                return true;
            }
        });
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    if (!getPrePic.equals(ReDataPic)) {
                        Glide.with(HomeActivity.this)
                                .load(ReDataPic)
                                .bitmapTransform(new CropCircleTransformation(HomeActivity.this))
                                .into(imageView);
                        SharedPreferences.Editor editor = getSharedPreferences("BingPicture", MODE_PRIVATE).edit();
                        editor.clear();
                        editor.putString("bingPic", ReDataPic);
                        editor.apply();
                    }
                    break;
                default:
                    return false;
            }
            return true;
        }
    });

    // 监听ToolBar上的按钮

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                return false;
        }
        return true;
    }
}