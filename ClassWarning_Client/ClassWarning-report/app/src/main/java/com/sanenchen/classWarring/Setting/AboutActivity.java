package com.sanenchen.classWarring.Setting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sanenchen.classWarring.ListViewOrRecyclerView.UpdateLog.UpdateLogAdapter;
import com.sanenchen.classWarring.ListViewOrRecyclerView.UpdateLog.UpdateLogList;
import com.sanenchen.classWarring.R;
import com.sanenchen.classWarring.getThings.getDataJson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AboutActivity extends Fragment {

    List<UpdateLogList> logLists = new ArrayList<>();
    RecyclerView recyclerView;
    View viewThis;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_about, container, false);
        viewThis = view;

        ListenView();
        return view;
    }

    public void ListenView() {
        FrameLayout updateLog = viewThis.findViewById(R.id.update_log);
        updateLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                final View layout = inflater.inflate(R.layout.alert_layout_update_log, null);
                recyclerView = layout.findViewById(R.id.update_log_recycler_view);
                //新建对话框对象
                final Dialog mDialog= new AlertDialog.Builder(getActivity()).create();
                mDialog.setCancelable(true);
                // 设置内容
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getDataJson getDataJson = new getDataJson();
                        String result = getDataJson.getUpdateThings();
                        try {
                            String[] title;
                            String[] things;
                            JSONArray jsonArray = new JSONArray(result);
                            title = new String[jsonArray.length()];
                            things = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                title[i] = jsonObject.getString("version");
                                things[i] = jsonObject.getString("updateThings");
                                UpdateLogList updateLogList = new UpdateLogList(title[i], things[i]);
                                logLists.add(updateLogList);
                            }

                            Message message = new Message();
                            message.what = 0;
                            handler.sendMessage(message);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                mDialog.show();
                //设置弹出框的宽高
                mDialog.getWindow().setContentView(layout);
                Button button = layout.findViewById(R.id.IKONW);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
            }
        });

        // 在GitHub上的项目
        FrameLayout theAppInGithub = viewThis.findViewById(R.id.theAppInGithub);
        theAppInGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://github.com/sanenchen-hub/ClassWarning");
                intent.setData(content_url);
                getActivity().startActivity(intent);
            }
        });

        // 个人博客
        FrameLayout selfBlog = viewThis.findViewById(R.id.selfBlog);
        selfBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://blog.lyqmc.cn");
                intent.setData(content_url);
                getActivity().startActivity(intent);
            }
        });

        // 个人Github
        FrameLayout selfGithub = viewThis.findViewById(R.id.selfGithub);
        selfGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://github.com/sanenchen-hub");
                intent.setData(content_url);
                getActivity().startActivity(intent);
            }
        });
    }


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    UpdateLogAdapter adapter = new UpdateLogAdapter(logLists);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(adapter);
                    break;
            }
            return true;
        }
    });
}