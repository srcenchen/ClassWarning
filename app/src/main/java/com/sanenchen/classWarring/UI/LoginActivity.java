package com.sanenchen.classWarring.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sanenchen.classWarring.getThings.getDataJson;
import com.sanenchen.classWarring.R;
import com.sanenchen.classWarring.libs.SHA224;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    final int LoginRight = 1;
    final int LoginERRORUserOrPassWord = 2;
    final int LoginERRORInternet = 3;
    String userName;
    ProgressDialog progressDialog;
    String GetUser = null;
    String GetPassword = null;
    String LinkID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.loginButton);
        final EditText getUser = findViewById(R.id.getUser);
        final EditText getPassword = findViewById(R.id.getPassword);
        final Button NewUserButton = findViewById(R.id.NewUserButton);
        NewUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, NewUserActivity.class);
                startActivity(intent);
            }
        });

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("登录中");
        progressDialog.setMessage("正在登录，请稍候...");
        progressDialog.setCancelable(false);

        SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
        String user = preferences.getString("user", null);

        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("userName", user);
            intent.putExtra("Sign", "No");
            startActivity(intent);
            finish();
        }


        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        DBUtils dbUtils = new DBUtils();
                        //                      String repliedInfo = dbUtils.CheckLoginInfoDB(getUser.getText().toString(), getPassword.getText().toString());

                        getDataJson getDataJson = new getDataJson();
                        String jsonData = getDataJson.getLoginPassword(getUser.getText().toString());

                        GetUser = getUser.getText().toString();
                        GetPassword = getPassword.getText().toString();

                        String repliedInfo = "LoginERRORInternet";
                        try {
                            if (jsonData == "") {
                                repliedInfo = "LoginERRORInternet";
                            }
                            JSONArray jsonArray = new JSONArray(jsonData);
                            if (jsonArray.length() == 0) {
                                repliedInfo = "LoginERRORUserOrPassWord";
                            } else {
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String PassWordBase64 = SHA224.Sha224_reply(getPassword.getText().toString());
                                if (jsonObject.getString("passWord").equals(PassWordBase64)) {
                                    repliedInfo = "LoginRight";
                                    userName = getUser.getText().toString();
                                    LinkID = jsonObject.getString("LinkID");
                                } else {
                                    repliedInfo = "LoginERRORUserOrPassWord";
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (repliedInfo.equals("LoginERRORUserOrPassWord")) {
                            Message message = new Message();
                            message.what = LoginERRORUserOrPassWord;
                            handler.sendMessage(message);
                        } else if (repliedInfo.equals("LoginERRORInternet")) {
                            Message message = new Message();
                            message.what = LoginERRORInternet;
                            handler.sendMessage(message);
                        } else if (repliedInfo.equals("LoginRight")){
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
                    intent.putExtra("Sign", "Yes");
                    startActivity(intent);
                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                    editor.clear();
                    editor.putString("user", GetUser);
                    editor.putString("password", GetPassword);
                    editor.putString("LinkID", LinkID);
                    editor.apply();
                    progressDialog.dismiss();
                    finish();
                    break;
                case LoginERRORUserOrPassWord:
                    Toast.makeText(LoginActivity.this, "登录失败，用户名或密码错误！", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    break;
                case LoginERRORInternet:
                    Toast.makeText(LoginActivity.this, "登录失败，网络错误！", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    break;
            }
        }
    };
}