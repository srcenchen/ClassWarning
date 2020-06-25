package com.sanenchen.classWarring.UI;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;


import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.sanenchen.classWarring.R;

public class TabActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        listenButtonActionPerformed();

    }

    private void listenButtonActionPerformed() {
        BottomBar bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (tabId) {
                    case R.id.tab_recents:
                        transaction.replace(R.id.frame, new AllWarningActivity(), "FragTab1").commit();
                        break;
                    case R.id.tab_nearby:
                        transaction.replace(R.id.frame, new ClassWarringUpLoad(), "FragTab1").commit();
                        break;

                    case R.id.tab_mine:
                        transaction.replace(R.id.frame, new MainActivity(), "FragTab1").commit();
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Intent intent = new Intent(TabActivity.this, ClassWarningSearchActivity.class);
                startActivity(intent);

            case R.id.about:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {

    }
}