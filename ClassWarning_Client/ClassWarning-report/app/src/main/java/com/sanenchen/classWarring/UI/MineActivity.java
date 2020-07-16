package com.sanenchen.classWarring.UI;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.sanenchen.classWarring.R;
import com.sanenchen.classWarring.Setting.SettingActivity;
import com.sanenchen.classWarring.getThings.getDataJson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class MineActivity extends Fragment {

    String geta = null;
    ProgressDialog progressDialog;
    String GetUser = null;
    String GetPassword = null;

    View viewThis;
    String ReDataPic = null;
    SharedPreferences prefs;
    String getPrePic;
    ImageView imageView;
    int MoHuDU = 1;
    int scrollYZ;
    int Bi = 120;
    LinearLayout linearLayout11;
    ViewGroup.LayoutParams lp11;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_mine, container, false);
        viewThis = view;

        Bi = (int) (Bi * (view.getResources().getDisplayMetrics().density) + 0.5f);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("加载数据中");
        progressDialog.setMessage("正在加载数据，请稍候...");
        progressDialog.setCancelable(false);

        setPicture();
        LoginSign();
        ViewListen();
        SettingListen();
        return view;
    }
    //监听那些按钮
    public void SettingListen() {
        // 监听设置按钮
        FrameLayout settingButton = viewThis.findViewById(R.id.settingButton);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });
        FrameLayout aboutThisButton = viewThis.findViewById(R.id.aboutThisButton);
        aboutThisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    // 主要是滚动时的动画
    public void ViewListen() {
        ScrollView scrollView = viewThis.findViewById(R.id.MineScrollView);

        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, final int scrollY, int oldScrollX, int oldScrollY) {
                scrollYZ = scrollY;

                linearLayout11 = viewThis.findViewById(R.id.SHOWSELF);
                lp11 = linearLayout11.getLayoutParams();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (scrollYZ == 0) {
                            for (int i = (int) (60 * (viewThis.getResources().getDisplayMetrics().density) + 0.5f); i <= (int) (120 * (viewThis.getResources().getDisplayMetrics().density) + 0.5f); i++) {
                                Bi = i;
                                Message message = new Message();
                                message.what = 3;
                                handler.sendMessage(message);
                                if ((int) (120 * (viewThis.getResources().getDisplayMetrics().density) + 0.5f) >= 300) {
                                    try {
                                        Thread.sleep(1);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } else if (scrollY != 0 && lp11.height == (int) (120 * (viewThis.getResources().getDisplayMetrics().density) + 0.5f)) {
                            for (int i = (int) (120 * (viewThis.getResources().getDisplayMetrics().density) + 0.5f); i >= (int) (60 * (viewThis.getResources().getDisplayMetrics().density) + 0.5f); i--) {
                                Bi = i;
                                Message message = new Message();
                                message.what = 4;
                                handler.sendMessage(message);
                                if ((int) (120 * (viewThis.getResources().getDisplayMetrics().density) + 0.5f) >= 300) {
                                    try {
                                        Thread.sleep(1);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
                    }
                }).start();
            }
        });
    }

    // 将头像设置为Bing每日一图
    public void setPicture() {
        // 设置Bing一图
        prefs = getActivity().getSharedPreferences("BingPicture", MODE_PRIVATE);
        imageView = viewThis.findViewById(R.id.h_head);
        getPrePic = prefs.getString("bingPic", "null");

        if (!getPrePic.equals("null")) {
            Glide.with(getActivity()).load(getPrePic)
                    .bitmapTransform(new CropCircleTransformation(getActivity()))
                    .into(imageView);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("http://get-bing-pic.cdn.lyqmc.cn/geturl").build();
                try {
                    Response response = client.newCall(request).execute();
                    ReDataPic = response.body().string();
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    // 获取总违纪数
    @SuppressLint("SetTextI18n")
    public void LoginSign() {
        // 获取基本用户信息
        SharedPreferences preferences = getActivity().getSharedPreferences("LoginData", MODE_PRIVATE);
        String user = preferences.getString("user", null);
        String password = preferences.getString("password", null);
        String schoolName = preferences.getString("schoolName", "");
        String grade = preferences.getString("grade", "");
        String worker = preferences.getString("worker", "");
        GetUser = user;
        GetPassword = password;
        TextView userID = viewThis.findViewById(R.id.userID);
        TextView SchoolInformation = viewThis.findViewById(R.id.SchoolInformation);
        userID.setText(GetUser); // 放入用户名
        SchoolInformation.setText(schoolName + " / " + grade + " / " + worker);
        // 获取总违纪次数
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDataJson getDataJson = new getDataJson();
                String jsonData = getDataJson.getSearchReply("getTotalClassWarning", getActivity(), null, null);

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
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    TextView getTotal = viewThis.findViewById(R.id.TotalWarning);
                    getTotal.setText("总违纪次数：" + geta + "次");
                    break;
                case 2:
                    if (getPrePic.equals(ReDataPic)) {

                    } else {
                        Glide.with(getActivity()).load(ReDataPic)
                                .bitmapTransform(new CropCircleTransformation(getActivity()))
                                .into(imageView);
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("BingPicture", MODE_PRIVATE).edit();
                        editor.clear();
                        editor.putString("bingPic", ReDataPic);
                        editor.apply();
                    }

                    break;
                case 3:
                    LinearLayout linearLayout;
                    ViewGroup.LayoutParams lp;
                    linearLayout = viewThis.findViewById(R.id.SHOWSELF);
                    lp = linearLayout.getLayoutParams();
                    Log.i("aaa", lp.height + "");
                    lp.height = Bi;
                    linearLayout.setLayoutParams(lp);
                    break;
                case 4:
                    LinearLayout linearLayout1;
                    ViewGroup.LayoutParams lp1;
                    linearLayout1 = viewThis.findViewById(R.id.SHOWSELF);
                    lp1 = linearLayout1.getLayoutParams();
                    lp1.height = Bi;
                    linearLayout1.setLayoutParams(lp1);
                    break;
                default:
                    return true;
            }
            return true;
        }
    });
}
