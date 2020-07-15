package com.sanenchen.classWarring.Setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.leon.lib.settingview.LSettingItem;
import com.sanenchen.classWarring.R;
import com.sanenchen.classWarring.UI.AboutActivity;
import com.sanenchen.classWarring.UI.LoginActivity;
import com.sanenchen.classWarring.UI.TabActivity;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = findViewById(R.id.toolbarSetting);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        SettingListen();
    }

    public void SettingListen() {
        LSettingItem ExitLogin = findViewById(R.id.ExitLogin);
        LSettingItem AboutMe = findViewById(R.id.About);

        ExitLogin.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                SharedPreferences.Editor editor = getSharedPreferences("LoginData", MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                TabActivity.instance.finish();
                finish();
            }
        });

        AboutMe.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(SettingActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }
}