package com.sanenchen.classWarring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

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
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {

                DBUtilsGet();
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();

        WebView webView = findViewById(R.id.WebView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://blog.lyqmc.cn/index.php/archives/426/");

    }


    protected void onRestart() {
        super.onRestart();
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("加载数据中");
        progressDialog.setMessage("正在加载数据，请稍候...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {

                DBUtilsGet();
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    TextView cishu = findViewById(R.id.cishu);
                    cishu.setText(geta + "次");
                    progressDialog.dismiss();
                    break;

            }
        }
    };
    static final String USER = "classWarring";
    static final String PASS = "classWarring";
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://182.92.87.134:3306/classWarring";

    public void DBUtilsGet() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            conn = DriverManager.getConnection(DB_URL, USER, PASS);


            // 执行查询
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM WarningTotal";
            rs = stmt.executeQuery(sql);

            // 展开结果集数据库
            while (rs.next()) {
                geta = rs.getString("WarningTotal");
            }
            //return rs;
            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        //return rs;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.uplod:
                Intent intent = new Intent(MainActivity.this,ClassWarringUpLoad.class);
                startActivity(intent);
                break;
            case R.id.search:
                Intent intent1 = new Intent(MainActivity.this,ClassWarningSearchActivity.class);
                startActivity(intent1);
                break;
            case R.id.AllWarn:
                Intent intent2 = new Intent(MainActivity.this,AllWarningActivity.class);
                startActivity(intent2);
                break;
        }
        return true;
    }
}
