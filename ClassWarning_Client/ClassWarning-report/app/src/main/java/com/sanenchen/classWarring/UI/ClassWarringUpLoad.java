package com.sanenchen.classWarring.UI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sanenchen.classWarring.R;
import com.sanenchen.classWarring.getThings.getDataJson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ClassWarringUpLoad extends Fragment {

    String FunCheck = "";
    ProgressDialog progressDialog = null;
    View viewThis;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_class_warring_up_load, container, false);
        viewThis = view;
        RadioButton FunOther = viewThis.findViewById(R.id.FunOther);
        final RadioButton FunAlone = viewThis.findViewById(R.id.FunAlone);
        RadioButton FunGoHome = viewThis.findViewById(R.id.FunGoHome);
        final Button FunStartTime = viewThis.findViewById(R.id.FunStartTime);
        final Button FunEndTime = viewThis.findViewById(R.id.FunEndTime);
        final LinearLayout linearLayout1 = viewThis.findViewById(R.id.GoHomePanel);
        Button InButton = viewThis.findViewById(R.id.InButton);
        final EditText WarningTitle = viewThis.findViewById(R.id.WarningTitle);
        final EditText WarningStudent = viewThis.findViewById(R.id.WarningStudent);
        final CheckBox WarningTalk = viewThis.findViewById(R.id.WarningTalk);
        final CheckBox WarningPa = viewThis.findViewById(R.id.WarningPa);
        final CheckBox WarningSit = viewThis.findViewById(R.id.WarningSit);
        final CheckBox WarningOther = viewThis.findViewById(R.id.WarningOther);
        final EditText BeiZhuEd = viewThis.findViewById(R.id.Beizhu);

        // Button
        FunStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View dialogView = View.inflate(getActivity(), R.layout.dialog_date, null);
                final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
                builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FunStartTime.setText(datePicker.getYear() + "/" + datePicker.getMonth() + "/" + datePicker.getDayOfMonth());
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = builder.create();

                dialog.setTitle("设置日期");
                dialog.setView(dialogView);
                dialog.show();
            }
        });

        FunEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View dialogView = View.inflate(getActivity(), R.layout.dialog_date, null);
                final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
                builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FunEndTime.setText(datePicker.getYear() + "/" + datePicker.getMonth() + "/" + datePicker.getDayOfMonth());
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = builder.create();

                dialog.setTitle("设置日期");
                dialog.setView(dialogView);
                dialog.show();
            }
        });

        InButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("录入中");
                progressDialog.setMessage("正在录入，请稍后...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String WarningGroup = "";
                        if (WarningTalk.isChecked()) {
                            WarningGroup = "讲话 ";
                        }
                        if(WarningSit.isChecked()) {
                            WarningGroup = WarningGroup + "坐姿不正 ";
                        }
                        if (WarningPa.isChecked()) {
                            WarningGroup = WarningGroup + "上课趴着 ";
                        }
                        if (WarningOther.isChecked()) {
                            WarningGroup = WarningGroup + "其他违纪 ";
                        } else {

                        }
                        try {
                            EditText FunOtherText = viewThis.findViewById(R.id.FunOtherText);
                            if (!WarningGroup.equals("") && !FunCheck.equals("") && !WarningTitle.getText().toString().equals("") && !WarningStudent.getText().toString().equals("")) {
                                if (FunAlone.isChecked()) {
                                    if (!FunStartTime.getText().toString().equals("开始时间") && !FunEndTime.getText().toString().equals("结束时间")) {
                                        UpLod(WarningTitle.getText().toString(),WarningGroup,WarningStudent.getText().toString(),FunCheck,
                                                FunStartTime.getText().toString(), FunEndTime.getText().toString(), BeiZhuEd.getText().toString());
                                        Looper.prepare();
                                        Toast.makeText(getActivity(), "录入成功！", Toast.LENGTH_SHORT).show();
                                        Message message = new Message();
                                        message.what = 1;
                                        handler.sendMessage(message);
                                        Looper.loop();
                                    } else {
                                        Looper.prepare();
                                        Toast.makeText(getActivity(), "有东西是不是忘记选/填啦？", Toast.LENGTH_SHORT).show();
                                        Message message = new Message();
                                        message.what = 2;
                                        handler.sendMessage(message);
                                        Looper.loop();
                                    }

                                } else {
                                    UpLod(WarningTitle.getText().toString(),WarningGroup,WarningStudent.getText().toString(),FunCheck,
                                            " ", " ", BeiZhuEd.getText().toString());
                                    Looper.prepare();
                                    Toast.makeText(getActivity(), "录入成功！", Toast.LENGTH_SHORT).show();
                                    Message message = new Message();
                                    message.what = 1;
                                    handler.sendMessage(message);

                                    Looper.loop();
                                }

                            }else if(!WarningGroup.equals("") && !FunOtherText.getText().toString().equals("") &&
                                    !WarningTitle.getText().toString().equals("") && !WarningStudent.getText().toString().equals("")
                                    && !FunCheck.equals("回家反省") && !FunCheck.equals("走读")) {
                                UpLod(WarningTitle.getText().toString(),WarningGroup
                                        ,WarningStudent.getText().toString(),FunOtherText.getText().toString(),
                                        " ", " ", BeiZhuEd.getText().toString());
                                Looper.prepare();
                                Toast.makeText(getActivity(), "录入成功！", Toast.LENGTH_SHORT).show();
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);

                                Looper.loop();
                            } else {
                                //Toast.makeText(ClassWarringUpLoad.class, "", Toast.LENGTH_SHORT).show();
                                Looper.prepare();
                                Toast.makeText(getActivity(), "有东西是不是忘记选/填啦？", Toast.LENGTH_SHORT).show();
                                Message message = new Message();
                                message.what = 2;
                                handler.sendMessage(message);
                                Looper.loop();
                            }

                        } catch (Exception e) {
                            Looper.prepare();
                            Toast.makeText(getActivity(), "录入失败，未知错误！", Toast.LENGTH_SHORT).show();
                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);
                            Looper.loop();
                        }

                    }
                }).start();
            }
        });

        // Radio监听
        FunOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText FunOtherText = viewThis.findViewById(R.id.FunOtherText);
                FunOtherText.setEnabled(true);linearLayout1.setVisibility(View.GONE);

                FunCheck = FunOtherText.getText().toString();
            }
        });
        FunAlone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText FunOtherText = viewThis.findViewById(R.id.FunOtherText);
                FunOtherText.setEnabled(false);
                linearLayout1.setVisibility(View.VISIBLE);
                FunCheck = "回家反省";
            }
        });
        FunGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText FunOtherText = viewThis.findViewById(R.id.FunOtherText);
                FunOtherText.setEnabled(false);
                linearLayout1.setVisibility(View.GONE);
                FunCheck = "走读";
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    public void UpLod(String Title, String WarningGroup, String WarningStudent, String WarningFun, String FunStartTime, String FunEndTime, String BeizhuSS) {
        getDataJson getDataJson = new getDataJson();

        SimpleDateFormat dff = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String ee = dff.format(new Date());
        // 获取日期
        try {
            String jsonData = getDataJson.getSearchReply("getTotalClassWarning", getActivity(), null, null);
            JSONArray jsonArray = new JSONArray(jsonData);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            int geta = jsonObject.getInt("warningTotal") + 1;
            getDataJson.getAddWarnReply(Title, WarningGroup, WarningStudent, WarningFun, FunStartTime, FunEndTime, BeizhuSS, ee, geta, getActivity());
        } catch (Exception e) {

        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                case 2:
                    progressDialog.dismiss();
                    break;
            }
            return false;
        }
    });


}
