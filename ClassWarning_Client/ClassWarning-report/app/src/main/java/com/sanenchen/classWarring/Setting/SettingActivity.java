package com.sanenchen.classWarring.Setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.sanenchen.classWarring.R;
import com.sanenchen.classWarring.UI.HomeActivity;
import com.sanenchen.classWarring.UI.LoginActivity;
import com.sanenchen.classWarring.UI.TabActivity;

import static android.content.Context.MODE_PRIVATE;

public class SettingActivity extends Fragment {
    View viewThis;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_setting, container, false);
        viewThis = view;
        SettingListen();

        return view;
    }

    public void SettingListen() {
        FrameLayout exitLoginButton = viewThis.findViewById(R.id.exitLoginButton);
        //LSettingItem AboutMe = findViewById(R.id.About);

        exitLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("LoginData", MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                HomeActivity.instance.finish();
                getActivity().finish();
            }
        });
    }
}