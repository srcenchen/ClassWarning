package com.sanenchen.classWarring.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sanenchen.classWarring.R;
import com.sanenchen.classWarring.Sql.DBUtils;

public class LoginActivity extends AppCompatActivity {

    final int LoginRight = 1;
    final int LoginERRORUserOrPassWord = 2;
    final int LoginERRORInternet = 3;
    String userName;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.loginButton);
        final EditText getUser = findViewById(R.id.getUser);
        final EditText getPassword = findViewById(R.id.getPassword);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("登录中");
        progressDialog.setMessage("正在登录，请稍候...");
        progressDialog.setCancelable(false);


        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DBUtils dbUtils = new DBUtils();
                        String repliedInfo = dbUtils.CheckLoginInfoDB(getUser.getText().toString(), getPassword.getText().toString());
                        if(repliedInfo.equals("0")) {
                            Message message = new Message();
                            message.what = LoginERRORUserOrPassWord;
                            handler.sendMessage(message);
                        } else if(repliedInfo.equals("3")){
                            Message message = new Message();
                            message.what = LoginERRORInternet;
                            handler.sendMessage(message);
                        } else {
                            userName = repliedInfo;
                            Message message = new Message();
                            message.what = LoginRight;
                            handler.sendMessage(message);
                        }
                    }
                }).start();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case LoginRight:
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("userName", userName);
                    startActivity(intent);
                    progressDialog.dismiss();
                    finish();
                    break;
                case LoginERRORUserOrPassWord:
                    Toast.makeText(LoginActivity.this,"登录失败，用户名或密码错误！", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    break;
                case LoginERRORInternet:
                    Toast.makeText(LoginActivity.this,"登录失败，网络错误！", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    break;
            }
        }
    };
}