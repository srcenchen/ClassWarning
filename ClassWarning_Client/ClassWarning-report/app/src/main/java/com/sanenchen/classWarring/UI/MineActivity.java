package com.sanenchen.classWarring.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.leon.lib.settingview.LSettingItem;
import com.sanenchen.classWarring.Setting.SettingActivity;
import com.sanenchen.classWarring.getThings.getDataJson;
import com.sanenchen.classWarring.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import jp.wasabeef.glide.transformations.BlurTransformation;
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

    public void SettingListen() {
        LSettingItem SearchLike = viewThis.findViewById(R.id.SearchLike);

        SearchLike.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Toast.makeText(getActivity(), "下线维护中···", Toast.LENGTH_SHORT).show();
            }
        });

        LSettingItem Setting = viewThis.findViewById(R.id.Setting);

        Setting.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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

    public void setPicture() {
        prefs = getActivity().getSharedPreferences("BingPicture", MODE_PRIVATE);
        imageView = viewThis.findViewById(R.id.h_head);
        getPrePic = prefs.getString("bingPic", "null");

        Glide.with(getActivity()).load("http://get-bing-pic.cdn.lyqmc.cn/")
                .bitmapTransform(new CropCircleTransformation(getActivity()))
                .into(imageView);
    }

    public void LoginSign() {
        // 验证身份
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDataJson getDataJson = new getDataJson();
                String jsonData = getDataJson.getSearchReply("1", getActivity(), null, null);

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


        SharedPreferences preferences = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String user = preferences.getString("user", null);
        String password = preferences.getString("password", null);

        GetUser = user;
        GetPassword = password;

        final Intent intent = getActivity().getIntent();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDataJson getDataJson = new getDataJson();

                try {
                    String jsonData1 = getDataJson.getSearchReply("1", getActivity(), null, null);

                    try {
                        JSONArray jsonArray1 = new JSONArray(jsonData1);
                        JSONObject jsonObject = jsonArray1.getJSONObject(0);
                        geta = jsonObject.getString("WarningTotal");
                    } catch (Exception e) {

                    }
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                } catch (Exception e) {
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
                    TextView userID = viewThis.findViewById(R.id.userID);
                    userID.setText(GetUser);
                    TextView getTotal = viewThis.findViewById(R.id.TotalWarning);
                    getTotal.setText("总违纪次数：" + geta + "次");

                case 3:
                    LinearLayout linearLayout;
                    ViewGroup.LayoutParams lp;
                    linearLayout = viewThis.findViewById(R.id.SHOWSELF);
                    lp = linearLayout.getLayoutParams();
                    Log.i("aaa", lp.height + "");
                    lp.height = Bi;
                    linearLayout.setLayoutParams(lp);
                case 4:
                    LinearLayout linearLayout1;
                    ViewGroup.LayoutParams lp1;
                    linearLayout1 = viewThis.findViewById(R.id.SHOWSELF);
                    lp1 = linearLayout1.getLayoutParams();
                    lp1.height = Bi;
                    linearLayout1.setLayoutParams(lp1);

                default:
                    return true;
            }
        }
    });
}
