package com.sanenchen.classWarring.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sanenchen.classWarring.getThings.getDataJson;
import com.sanenchen.classWarring.R;
import com.sanenchen.classWarring.libs.SHA224;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class MainActivity extends Fragment {

    String geta = null;
    ProgressDialog progressDialog;
    String GetUser = null;
    String GetPassword = null;

    View viewThis;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container , false);
        viewThis = view;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("加载数据中");
        progressDialog.setMessage("正在加载数据，请稍候...");
        progressDialog.setCancelable(false);
        //progressDialog.show();
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

        final Intent intent = getActivity().getIntent();
        TextView idUser = viewThis.findViewById(R.id.idUser);
        idUser.setText(intent.getStringExtra("userName"));

        SharedPreferences preferences = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String user = preferences.getString("user", null);
        String password = preferences.getString("password", null);

        if (intent.getStringExtra("Sign").equals("No")) {
            GetUser = user;
            GetPassword = password;
            new Thread(new Runnable() {
                @Override
                public void run() {
//                        DBUtils dbUtils = new DBUtils();
                    //                      String repliedInfo = dbUtils.CheckLoginInfoDB(getUser.getText().toString(), getPassword.getText().toString());

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
        }


        Button exitLoginButton = viewThis.findViewById(R.id.exitLoginButton);
        exitLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    TextView cishu = viewThis.findViewById(R.id.cishu);
                    cishu.setText(geta + "次");
                    progressDialog.dismiss();
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
            return true;
        }
    });
}
