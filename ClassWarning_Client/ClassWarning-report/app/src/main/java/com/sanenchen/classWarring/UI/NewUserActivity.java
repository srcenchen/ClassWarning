package com.sanenchen.classWarring.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.sanenchen.classWarring.R;
import com.sanenchen.classWarring.getThings.getDataJson;
import com.sanenchen.classWarring.libs.SHA224;

public class NewUserActivity extends AppCompatActivity {

    String GetUser = "";
    String GetPassword = "";
    String GetHowCall = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            //actionBar.setIcon(R.drawable.back);
        }

        Button NewMainButton = findViewById(R.id.NewMainButton);
        final EditText NewUserEditText = findViewById(R.id.NewUserEditText);
        final EditText NewPasswordEditText = findViewById(R.id.NewPasswordEditText);
        final EditText NewHowCallEditText = findViewById(R.id.NewHowCallEditText);

        // 监听注册
        NewMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取注册信息
                GetUser = NewUserEditText.getText().toString();
                GetPassword = NewPasswordEditText.getText().toString();
                GetHowCall = NewHowCallEditText.getText().toString();
                if (GetUser.equals("") || GetPassword.equals("") || GetHowCall.equals("")) {
                    Toast.makeText(NewUserActivity.this, "检查下是否全部填写完毕", Toast.LENGTH_SHORT).show();
                } else {
                    // 尝试注册
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getDataJson getDataJson = new getDataJson();
                            if (getDataJson.getLoginPassword(GetUser).equals("[]")) {
                                // 四位数的随机数
                                int dom = (int)(Math.random()*(9999-1000+1)+1000);

                                String re = getDataJson.getAddUserReply(GetUser, SHA224.Sha224_reply(GetPassword), SHA224.Sha224_reply(GetUser) + dom, GetHowCall);

                                if (re.equals("注册成功!")) {
                                    Looper.prepare();
                                    Toast.makeText(NewUserActivity.this, "注册成功！请登录！", Toast.LENGTH_SHORT).show();
                                    Message message = new Message();
                                    message.what = 1;
                                    handler.sendMessage(message);
                                    Looper.loop();
                                } else{
                                    Looper.prepare();
                                    Toast.makeText(NewUserActivity.this, "遇到未知错误！", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            } else if (getDataJson.getLoginPassword(GetUser).equals("")) {
                                Looper.prepare();
                                Toast.makeText(NewUserActivity.this, "网络错误！", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            } else {
                                Looper.prepare();
                                Toast.makeText(NewUserActivity.this, "注册失败！已有相同用户名！", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }
                    }).start();
                }

            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    finish();
                    break;
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }
}