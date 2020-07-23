package com.sanenchen.classWarring.UI;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sanenchen.classWarring.R;
import com.sanenchen.classWarring.getThings.getDataJson;
import com.sanenchen.classWarring.libs.SHA224;

public class NewUserActivity extends AppCompatActivity {

    String GetUser = "";
    String GetPassword = "";
    String schoolName;
    String grade;
    String Worker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setIcon(R.drawable.back);
        }
        ViewListen();
    }

    private void ViewListen() {
        Button NewMainButton = findViewById(R.id.NewMainButton);
        final EditText NewUserEditText = findViewById(R.id.NewUserEditText);
        final EditText NewPasswordEditText = findViewById(R.id.NewPasswordEditText);
        LinearLayout linearLayout = findViewById(R.id.ChooseSchool);
        final TextView GetSchool = findViewById(R.id.textView25);

        // 监听选择院校
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewUserActivity.this, ChooseSchoolActivity.class);
                startActivity(intent);
            }
        });

        // 监听注册
        NewMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取注册信息
                GetUser = NewUserEditText.getText().toString();
                GetPassword = NewPasswordEditText.getText().toString();
                if (GetUser.equals("") || GetPassword.equals("") || GetSchool.getText().toString().equals("未选择院校")) {
                    Toast.makeText(NewUserActivity.this, "检查下是否全部填写完毕", Toast.LENGTH_SHORT).show();
                } else {
                    // 尝试注册
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getDataJson getDataJson = new getDataJson();
                            if (getDataJson.getLoginPassword(GetUser).equals("[]")) { // 如果没找查询到就会只返回[]，说明这个用户名可以注册
                                // 四位数的随机数
                                int dom = (int) (Math.random() * (9999 - 1000 + 1) + 1000);

                                String re = getDataJson.getAddUserReply(GetUser, SHA224.Sha224_reply(GetPassword), SHA224.Sha224_reply(GetUser) + dom, schoolName
                                        , grade, Worker);

                                Looper.prepare();
                                Toast.makeText(NewUserActivity.this, "注册成功！请登录！", Toast.LENGTH_SHORT).show();
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                                Looper.loop();

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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences sharedPreferences = getSharedPreferences("ChooseCache", MODE_PRIVATE);
        Boolean CheckALL = sharedPreferences.getBoolean("CheckALL", false);
        if (CheckALL) {
            schoolName = sharedPreferences.getString("ChoseSchool", "");
            grade = sharedPreferences.getString("ChoseGrade", "");
            Worker = sharedPreferences.getString("ChoseWorker", "");

            TextView textView = findViewById(R.id.textView25);
            textView.setText(schoolName + "/" + grade + "/" + Worker);
        }

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
                break;
        }
        return true;
    }
}