package com.sanenchen.classWarring.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sanenchen.classWarring.getThings.getDataJson;
import com.sanenchen.classWarring.R;
import com.sanenchen.classWarring.libs.SHA224;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    String geta = null;
    ProgressDialog progressDialog;
    String GetUser = null;
    String GetPassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("加载数据中");
        progressDialog.setMessage("正在加载数据，请稍候...");
        progressDialog.setCancelable(false);
        //progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDataJson getDataJson = new getDataJson();
                String jsonData = getDataJson.getSearchReply("1", MainActivity.this, null, null);

                try {
                    JSONArray jsonArray = new JSONArray(jsonData);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    geta = jsonObject.getString("WarningTotal");
                } catch (Exception e) {

                }
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();

        final Intent intent = getIntent();
        TextView idUser = findViewById(R.id.idUser);
        idUser.setText(intent.getStringExtra("userName"));

        SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
        String user = preferences.getString("user", null);
        String password = preferences.getString("password", null);

        if (intent.getStringExtra("Sign").equals("No")) {
            GetUser = user;
            GetPassword = password;
            new Thread(new Runnable() {
                @Override
                public void run() {
//                        DBUtils dbUtils = new DBUtils();
                    //                      String repliedInfo = dbUtils.CheckLoginInfoDB(getUser.getText().toString(), getPassword.getText().toString());

                    getDataJson getDataJson = new getDataJson();
                    String jsonData = getDataJson.getLoginPassword(GetUser);

                    try {
                        JSONArray jsonArray = new JSONArray(jsonData);
                        if (jsonArray.length() == 0) {
                            Looper.prepare();
                            Toast.makeText(MainActivity.this, "登录身份过期，请重新登录！", Toast.LENGTH_SHORT).show();

                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);
                            Looper.loop();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }


        Button exitLoginButton = findViewById(R.id.exitLoginButton);
        exitLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();
                startActivity(intent);
                finish();
            }
        });

    }


    protected void onRestart() {
        super.onRestart();
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("加载数据中");
        progressDialog.setMessage("正在加载数据，请稍候...");
        progressDialog.setCancelable(false);
        //progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {

                getDataJson getDataJson = new getDataJson();
                String jsonData = getDataJson.getSearchReply("1", MainActivity.this, null, null);

                try {
                    JSONArray jsonArray = new JSONArray(jsonData);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    geta = jsonObject.getString("WarningTotal");
                } catch (Exception e) {

                }
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();

        SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
        String user = preferences.getString("user", null);
        String password = preferences.getString("password", null);

        GetUser = user;
        GetPassword = password;
        new Thread(new Runnable() {
            @Override
            public void run() {
//                        DBUtils dbUtils = new DBUtils();
                //                      String repliedInfo = dbUtils.CheckLoginInfoDB(getUser.getText().toString(), getPassword.getText().toString());

                getDataJson getDataJson = new getDataJson();
                String jsonData = getDataJson.getLoginPassword(GetUser);

                try {
                    JSONArray jsonArray = new JSONArray(jsonData);
                    if (jsonArray.length() == 0) {
                        Looper.prepare();
                        Toast.makeText(MainActivity.this, "登录身份过期，请重新登录！", Toast.LENGTH_SHORT).show();

                        Message message = new Message();
                        message.what = 2;
                        handler.sendMessage(message);
                        Looper.loop();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    TextView cishu = findViewById(R.id.cishu);
                    cishu.setText(geta + "次");
                    progressDialog.dismiss();
                    break;
                case 2:
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                    editor.clear();
                    editor.apply();
                    startActivity(intent);
                    finish();
                    break;
            }
            return true;
        }
    });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.uplod:
                Intent intent = new Intent(MainActivity.this, ClassWarringUpLoad.class);
                startActivity(intent);
                break;
            case R.id.search:
                Intent intent1 = new Intent(MainActivity.this, ClassWarningSearchActivity.class);
                startActivity(intent1);
                break;
            case R.id.AllWarn:
                Intent intent2 = new Intent(MainActivity.this, AllWarningActivity.class);
                startActivity(intent2);
                break;
        }
        return true;
    }
}
