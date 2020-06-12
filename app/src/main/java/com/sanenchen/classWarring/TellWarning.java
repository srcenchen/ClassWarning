package com.sanenchen.classWarring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TellWarning extends AppCompatActivity {

    String TitleSt, ItemWarnSt, StudentSt, FunSt, TimeSt, BeizhuSt = null;
    String MysqlID;
    public static final int ListViewSet = 1;
    ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tell_warning);
        Toolbar toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        MysqlID = intent.getStringExtra("MysqlID");

        progressDialog = new ProgressDialog(TellWarning.this);
        progressDialog.setTitle("加载数据中");
        progressDialog.setMessage("正在加载数据，请稍后...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                DBUtilsGet();
                Message message = new Message();
                message.what = ListViewSet;
                handler.sendMessage(message);
            }
        }).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case ListViewSet:
                    TextView TitleS = findViewById(R.id.TitleS);
                    TextView ItemWarnS = findViewById(R.id.ItemWarnS);
                    TextView StudentS = findViewById(R.id.StudentS);
                    TextView FunS = findViewById(R.id.FunS);
                    TextView TimeS = findViewById(R.id.TimeS);
                    TextView BeizhuS = findViewById(R.id.BeizhuS);
                    LinearLayout linearLayout = findViewById(R.id.TimeZoneF);
                    if (FunSt.equals("回家反省")) {
                        linearLayout.setVisibility(LinearLayout.VISIBLE);
                    }


                    TitleS.setText(TitleSt);
                    ItemWarnS.setText(ItemWarnSt);
                    StudentS.setText(StudentSt);
                    FunS.setText(FunSt);
                    TimeS.setText(TimeSt);
                    if (BeizhuSt == null) {
                        BeizhuS.setText("无备注");
                    } else {
                        BeizhuS.setText(BeizhuSt);
                    }

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
            sql = "SELECT * FROM WarringA WHERE id=" + MysqlID;
            rs = stmt.executeQuery(sql);

            // 展开结果集数据库
            while (rs.next()) {
                TitleSt = rs.getString("WarringT");
                ItemWarnSt = rs.getString("WarringWhat");
                StudentSt = rs.getString("WarringStudent");
                FunSt = rs.getString("HowToWarring");
                TimeSt = rs.getString("StartAlone") + "至" + rs.getString("EndAlone");
                BeizhuSt = rs.getString("Beizhu");
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



}