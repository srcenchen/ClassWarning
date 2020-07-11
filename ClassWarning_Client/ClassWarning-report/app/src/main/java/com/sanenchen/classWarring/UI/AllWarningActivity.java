package com.sanenchen.classWarring.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.sanenchen.classWarring.ItemRAdapter;
import com.sanenchen.classWarring.getThings.getDataJson;
import com.sanenchen.classWarring.R;
import com.sanenchen.classWarring.WarningSearchAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AllWarningActivity extends Fragment {

    private List<WarningSearchAd> musicList = new ArrayList<>();
    static int HowMany = 0;
    static String[] WarningTitle = null;
    static String[] WarningStudent = null;
    static String[] WarningID = null;
    static String[] WarningDate = null;
    private List<WarningSearchAd> SearchList = new ArrayList<>();
    public static final int ListViewSet = 1;
    ListView listView = null;
    String GetUser;
    String GetPassword;
    RecyclerView recyclerView;

    View viewT;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_all_warning, container, false);
        viewT = view;
        recyclerView = viewT.findViewById(R.id.RecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        SharedPreferences preferences = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String user = preferences.getString("user", null);
        String password = preferences.getString("password", null);

        GetUser = user;
        GetPassword = password;
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDataJson getDataJson = new getDataJson();
                String jsonData = getDataJson.getLoginPassword(GetUser);

                try {
                    JSONArray jsonArray = new JSONArray(jsonData);
                    if (jsonArray.length() == 0) {
                        Looper.prepare();
                        Toast.makeText(getActivity(), "登录身份过期，请重新登录！", Toast.LENGTH_SHORT).show();

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

        new Thread(new Runnable() {
            @Override
            public void run() {

                getDataJson getDataJson = new getDataJson();
                String jsonData = getDataJson.getSearchReply("0", getActivity(), null, null);

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
                } catch (Exception e) { }

                if (HowMany == 0) {
                    Looper.prepare();

                    // Toast.makeText(getActivity(), "没有违纪信息可显示", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    SearchList = new ArrayList<>();
                    for (int i = HowMany - 1; i >= 0; i--) {
                        WarningSearchAd warningSearchAd = new WarningSearchAd(WarningTitle[i], WarningStudent[i], WarningID[i], WarningDate[i]);
                        musicList.add(warningSearchAd);
                    }

                    Message message = new Message();
                    message.what = ListViewSet;
                    handler.sendMessage(message);

                }
            }
        }).start();

        listViewScrollListen();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void listViewScrollListen() {
        RecyclerView recyclerView = viewT.findViewById(R.id.RecyclerView);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case ListViewSet:
                    ItemRAdapter adapter = new ItemRAdapter(musicList);
                    recyclerView.setAdapter(adapter);
                    break;
                case 2:
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("data", MODE_PRIVATE).edit();
                    editor.clear();
                    editor.apply();
                    startActivity(intent);
                    getActivity().finish();
                    break;
            }
        }
    };
}