package com.sanenchen.classWarring.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.sanenchen.classWarring.ClassWarringUpLoad;
import com.sanenchen.classWarring.GetMysqlData.getDataJson;
import com.sanenchen.classWarring.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    String geta = null;
    ProgressDialog progressDialog;

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
                String jsonData = getDataJson.getDataJson("SELECT * FROM WarningTotal");

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

        Intent intent = getIntent();
        TextView idUser = findViewById(R.id.idUser);
        idUser.setText(intent.getStringExtra("userName"));

        Button exitLoginButton =findViewById(R.id.exitLoginButton);
        exitLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
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
                String jsonData = getDataJson.getDataJson("SELECT * FROM WarningTotal");

                try {
                    JSONArray jsonArray = new JSONArray(jsonData);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    int TotalNum = jsonObject.getInt("WarningTotal") + 1;

                } catch (Exception e) {

                }
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
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
