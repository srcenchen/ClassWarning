package com.sanenchen.classWarring.UI;

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

import com.sanenchen.classWarring.getThings.getDataJson;
import com.sanenchen.classWarring.ItemAdapter;
import com.sanenchen.classWarring.R;
import com.sanenchen.classWarring.WarningSearchAd;
import com.sanenchen.classWarring.getThings.getLinkID;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllWarningActivity extends AppCompatActivity {


    static int HowMany = 0;
    static String[] WarningTitle = null;
    static String[] WarningStudent = null;
    static String[] WarningID = null;
    static String[] WarningDate = null;
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

                getDataJson getDataJson = new getDataJson();
                String jsonData = getDataJson.getSearchReply("0",AllWarningActivity.this, null, null);

                try {
                    JSONArray jsonArray = new JSONArray(jsonData);
                    HowMany = jsonArray.length();
                    WarningTitle = new String[HowMany];
                    WarningStudent = new String[HowMany];
                    WarningID = new String[HowMany];
                    WarningDate = new String[HowMany];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        WarningTitle[i] = jsonObject.getString("WarringT");
                        WarningStudent[i] = jsonObject.getString("WarringStudent");
                        WarningID[i] = jsonObject.getString("id");
                        WarningDate[i] = jsonObject.getString("uplodDate");
                    }


                } catch (Exception e) {

                }

                if (HowMany == 0) {
                    Looper.prepare();

                    Toast.makeText(AllWarningActivity.this, "没有违纪信息可显示", Toast.LENGTH_SHORT).show();
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                    Looper.loop();
                } else {
                    SearchList = new ArrayList<>();
                    for (int i = HowMany - 1; i >= 0; i--) {
                        WarningSearchAd warningSearchAd = new WarningSearchAd(WarningTitle[i], WarningStudent[i], WarningID[i], WarningDate[i]);
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
}