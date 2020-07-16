package com.sanenchen.classWarring.UI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sanenchen.classWarring.R;
import com.sanenchen.classWarring.getThings.getDataJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChooseSchoolActivity extends AppCompatActivity {
    // 监测步骤
    final int ChooseSchoolSTEP = 0;
    final int ChooseGradeSTEP = 1;
    final int ChooseWorkerSTEP = 2;
    int step = ChooseSchoolSTEP;

    // 定义数组
    String[] getSchool;
    final String[] getGrade = {"七年级","八年级","九年级"};
    final String[] getWorker = {"年级主任","班主任","任课教师"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_school);
        Toolbar toolbar = findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setDataToListView(ChooseSchoolSTEP);
        viewListen();

    }

    private void setDataToListView(int step) {
        ListView listView = findViewById(R.id.ChooseListView);
        if (step == ChooseSchoolSTEP) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getDataJson getDataJson = new getDataJson();
                    String Result = getDataJson.getSearchReply("getSupportSchool", null, null,null);
                    try {
                        JSONArray jsonArray = new JSONArray(Result);
                        getSchool = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i ++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            getSchool[i] = jsonObject.getString("schoolName");
                        }
                        Message message = new Message();
                        message.what = 0;
                        handler.sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else if (step == ChooseGradeSTEP){
            ArrayAdapter adapter = new ArrayAdapter(
                    ChooseSchoolActivity.this, android.R.layout.simple_list_item_1, getGrade);
            listView.setAdapter(adapter);
        } else if (step == ChooseWorkerSTEP) {
            ArrayAdapter adapter = new ArrayAdapter(
                    ChooseSchoolActivity.this, android.R.layout.simple_list_item_1, getWorker);
            listView.setAdapter(adapter);
        }
    }

    private void viewListen() {
        ListView listView = findViewById(R.id.ChooseListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = getSharedPreferences("ChooseCache", MODE_PRIVATE).edit();
                if (step == ChooseSchoolSTEP) {
                    editor.clear();
                    editor.putString("ChoseSchool", parent.getItemAtPosition(position).toString());
                    step = ChooseGradeSTEP;
                    setDataToListView(ChooseGradeSTEP);
                } else if (step == ChooseGradeSTEP) {
                    editor.putString("ChoseGrade", parent.getItemAtPosition(position).toString());
                    step = ChooseWorkerSTEP;
                    setDataToListView(ChooseWorkerSTEP);
                } else if (step == ChooseWorkerSTEP) {
                    editor.putString("ChoseWorker", parent.getItemAtPosition(position).toString());
                    editor.putBoolean("CheckALL", true);
                    finish();
                }
                editor.apply();
            }
        });
    }

    @Override
    public void onBackPressed() {
        SharedPreferences.Editor editor = getSharedPreferences("ChooseCache", MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        finish();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            ChooseSchoolActivity.this, android.R.layout.simple_list_item_1, getSchool);
                    ListView listView = findViewById(R.id.ChooseListView);
                    listView.setAdapter(adapter);
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                SharedPreferences.Editor editor = getSharedPreferences("ChooseCache", MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}