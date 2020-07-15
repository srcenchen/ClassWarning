package com.sanenchen.classWarring.UI;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.sanenchen.classWarring.R;
import com.sanenchen.classWarring.getThings.getDataJson;

public class ClassManagerActivity extends Fragment {
    // 定义相关Case符号
    final int NotFoundClass = 0; // 在服务器中没有找到班级信息时
    final int FoundClass = 1; // 在服务器中找到班级信息时
    // 这是全局View
    View viewThis;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_class_manager, container, false);
        viewThis = view;

        getMyClass();
        return view;
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
            TextView TipsNewClass = viewThis.findViewById(R.id.TipsNewClass);
            RecyclerView classRecyclerView = viewThis.findViewById(R.id.classRecylerView);
            CardView TipsNewClassCardView = viewThis.findViewById(R.id.TipsNewClassCardView);
            switch (msg.what) {
                case NotFoundClass:
                    // 将“提示新建班级显示出来”
                    TipsNewClass.setText("您暂时没有班级可管理");
                    classRecyclerView.setVisibility(LinearLayout.GONE); // RecyclerView不显示
                    TipsNewClassCardView.setVisibility(LinearLayout.VISIBLE);

                    break;
                case FoundClass:
                    TipsNewClass.setText("还有班级需要管理？继续添加吧");
                    classRecyclerView.setVisibility(LinearLayout.VISIBLE); // RecyclerView显示
                    TipsNewClassCardView.setVisibility(LinearLayout.VISIBLE);
                default:
                    break;
            }
            return false;
        }
    });
}