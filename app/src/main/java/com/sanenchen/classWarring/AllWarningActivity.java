package com.sanenchen.classWarring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AllWarningActivity extends AppCompatActivity {


    static int HowMany = 0;
    static String[] WarningTitle = null;
    static String[] WarningStudent = null;
    static String[] WarningID = null;
    private List<WarningSearchAd> SearchList = new ArrayList<>();
    ProgressDialog progressDialog = null;
    ItemAdapter itemAdapter = null;
    public static final int ListViewSet = 1;
    ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_warning);

        listView = findViewById(R.id.AllWarningListView);
        progressDialog = new ProgressDialog(AllWarningActivity.this);
        progressDialog.setTitle("查询中");
        progressDialog.setMessage("正在查询，请稍后...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WarningSearchAd warningSearchAd = SearchList.get(position);
                Intent intent = new Intent(AllWarningActivity.this, TellWarning.class);
                intent.putExtra("MysqlID", warningSearchAd.getID());
                startActivity(intent);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {

                DBUtilsGet();

                if (HowMany == 0) {
                    Looper.prepare();

                    Toast.makeText(AllWarningActivity.this, "未找到！！", Toast.LENGTH_SHORT).show();
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                    Looper.loop();
                } else {
                    SearchList = new ArrayList<>();
                    for (int i = 0; i < HowMany; i++) {
                        WarningSearchAd warningSearchAd = new WarningSearchAd(WarningTitle[i], WarningStudent[i], WarningID[i]);
                        SearchList.add(warningSearchAd);
                    }
                    itemAdapter = new ItemAdapter(AllWarningActivity.this,
                            R.layout.search_item, SearchList);

                    Message message = new Message();
                    message.what = ListViewSet;
                    handler.sendMessage(message);

                }
            }
        }).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case ListViewSet:
                    listView.setAdapter(itemAdapter);
                    progressDialog.dismiss();
                    break;
                case 2:
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
            sql = "SELECT * FROM WarringA";
            rs = stmt.executeQuery(sql);

            HowMany = 0;
            // 展开结果集数据库
            while (rs.next()) {
                rs.getString("WarringT");
                HowMany++;
            }
            WarningTitle = new String[HowMany];
            WarningStudent = new String[HowMany];
            WarningID = new String[HowMany];

            sql = "SELECT * FROM WarringA";
            rs = stmt.executeQuery(sql);
            int NowNumber = 0;
            while (rs.next()) {
                WarningTitle[NowNumber] = rs.getString("WarringT");
                WarningStudent[NowNumber] = rs.getString("WarringStudent");
                WarningID[NowNumber] = rs.getString("id");
                NowNumber++;
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