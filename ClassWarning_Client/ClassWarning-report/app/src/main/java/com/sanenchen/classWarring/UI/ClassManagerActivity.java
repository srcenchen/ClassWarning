package com.sanenchen.classWarring.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sanenchen.classWarring.ListViewOrRecyclerView.ClassManager.ClassManagerAdapter;
import com.sanenchen.classWarring.ListViewOrRecyclerView.ClassManager.ClassManagerList;
import com.sanenchen.classWarring.R;
import com.sanenchen.classWarring.getThings.getDataJson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ClassManagerActivity extends Fragment {
    // 定义相关Case符号
    final int NotFoundClass = 0; // 在服务器中没有找到班级信息时
    final int FoundClass = 1; // 在服务器中找到班级信息时
    final int CreateClassRight = 2; // 当创建班级成功后
    final int JoinClassRight = 3; // 当加入班级成功后
    // 这是全局View
    View viewThis;
    // 定义全局变量
    int HowMany;
    String[] classID;
    String[] inSchoolID;
    String[] inGrade;
    String[] className;
    private List<ClassManagerList> classManagerLists = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_class_manager, container, false);
        viewThis = view;

        ListenCardView();
        getMyClass();
        return view;
    }

    private void ListenCardView() {
        /*监听创建班级CardView*/
        CardView cardView = viewThis.findViewById(R.id.new_class_card_view);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                final View layout = inflater.inflate(R.layout.alert_layout_new_class, null);
                // 获取基本用户信息
                SharedPreferences preferences = getActivity().getSharedPreferences("LoginData", MODE_PRIVATE);
                final String grade = preferences.getString("grade", "");
                String worker = preferences.getString("worker", "");
                // 判断权限
                if (!worker.equals("班主任") && !worker.equals("年级主任")) {
                    Toast.makeText(getActivity(), "权限不足", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 完善信息
                TextView textView = layout.findViewById(R.id.new_class_grade);
                textView.setText(grade);
                //新建对话框对象
                final Dialog mDialog= new AlertDialog.Builder(getActivity()).create();
                mDialog.setCancelable(true);
                mDialog.show();
                //设置弹出框的宽高
                mDialog.getWindow().setContentView(layout);
                // 监听两个按钮
                Button new_class_closedialog = layout.findViewById(R.id.new_class_closedialog);
                Button new_class_create = layout.findViewById(R.id.new_class_create);
                new_class_closedialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
                new_class_create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 创建班级
                        final getDataJson getDataJson = new getDataJson();
                        Spinner spinner = layout.findViewById(R.id.spinner);
                        final String SelectedItem = spinner.getSelectedItem().toString();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getDataJson.getAddClassReply(grade, SelectedItem, getActivity());
                                Message message = new Message();
                                message.what = CreateClassRight;
                                handler.sendMessage(message);
                            }
                        }).start();
                        mDialog.dismiss();
                    }
                });
            }
        });
        /*监听加入班级CardView*/
        CardView joinClass = viewThis.findViewById(R.id.joinClass);
        joinClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(getActivity());
                new AlertDialog.Builder(getActivity()).setTitle("请输入班级ID")
                        .setIcon(R.drawable.ic_baseline_class_24)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Boolean TestID = false;
                                for (int n = 0; n < classManagerLists.size(); n++) {
                                    ClassManagerList classManagerList = classManagerLists.get(n);
                                    if (classManagerList.getClassID().equals(et.getText().toString())) {
                                        TestID = true;
                                        Toast.makeText(getActivity(), "这个班级已经加入了", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                if (!TestID) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            getDataJson getDataJson = new getDataJson();
                                            if (getDataJson.getJoinClassReply(et.getText().toString(), getActivity()).equals("加入成功!")) {
                                                Message message = new Message();
                                                message.what = JoinClassRight;
                                                handler.sendMessage(message);
                                            } else {
                                                Looper.prepare();
                                                Toast.makeText(getActivity(), "班级不存在", Toast.LENGTH_SHORT).show();
                                                Looper.loop();
                                            }
                                        }
                                    }).start();
                                }
                            }
                        }).setNegativeButton("取消",null).show();
            }
        });
    }

    private void getMyClass() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDataJson getDataJson = new getDataJson();
                String Result = getDataJson.getSearchReply("getMyClass", getActivity(), null,null);
                if (Result.equals("[]")) { // 服务器返回 "[]" 代表在服务器中没有找到相关信息
                    Message message = new Message();
                    message.what = NotFoundClass;
                    handler.sendMessage(message);
                } else { // 服务器不返回“[]”就说明查到数据了，将班级信息放入RecyclerView
                    try {
                        JSONArray jsonArray = new JSONArray(Result);
                        HowMany = jsonArray.length();
                        classID = new String[HowMany];
                        inSchoolID = new String[HowMany];
                        inGrade = new String[HowMany];
                        className = new String[HowMany];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            classID[i] = jsonObject.getString("classID");
                            inSchoolID[i] = jsonObject.getString("inSchoolID");
                            inGrade[i] = jsonObject.getString("inGrade");
                            className[i] = jsonObject.getString("className");
                        }
                    } catch (Exception e) { }

                    classManagerLists = new ArrayList<>();
                    for (int i = 0; i < HowMany; i++) {
                        ClassManagerList classManagerList = new ClassManagerList(classID[i], className[i], inGrade[i], inSchoolID[i]);
                        classManagerLists.add(classManagerList);
                    }

                    Message message = new Message();
                    message.what = FoundClass;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            // 绑定控件
            RecyclerView classRecyclerView = viewThis.findViewById(R.id.classRecylerView);
            TextView tipsNoClass = viewThis.findViewById(R.id.tips_no_class);
            switch (msg.what) {
                case NotFoundClass:
                    tipsNoClass.setVisibility(LinearLayout.VISIBLE);
                    break;
                case FoundClass:
                    classRecyclerView.setVisibility(LinearLayout.VISIBLE); // RecyclerView显示
                    ClassManagerAdapter classManagerAdapter = new ClassManagerAdapter(classManagerLists);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    classRecyclerView.setLayoutManager(linearLayoutManager);
                    classRecyclerView.setAdapter(classManagerAdapter);
                    break;
                case CreateClassRight:
                    getMyClass();// 刷新班级列表
                    tipsNoClass.setVisibility(LinearLayout.GONE);
                    Toast.makeText(getActivity(), "班级创建成功", Toast.LENGTH_SHORT).show();
                    break;
                case JoinClassRight:
                    getMyClass();// 刷新班级列表
                    tipsNoClass.setVisibility(LinearLayout.GONE);
                    Toast.makeText(getActivity(), "班级加入成功", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            return false;
        }
    });
}