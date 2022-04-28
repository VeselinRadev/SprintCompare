package com.veselin.sprintcompare.acitvities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.veselin.sprintcompare.R;
import com.veselin.sprintcompare.fragments.GroupsFragment;
import com.veselin.sprintcompare.fragments.MyRunsFragment;
import com.veselin.sprintcompare.fragments.StartRunFragment;
public class MainActivity extends FragmentActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Start new run");
        checkPermissions();
        initializeBottomNavigationView();
    }
    private void checkPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
            return;
        }
    }
    private void initializeBottomNavigationView(){
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, new StartRunFragment())
                .commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                Fragment activeFragment = new StartRunFragment();
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        activeFragment = new StartRunFragment();
                        toolbar.setTitle("Start new run");
                        break;
                    case R.id.navigation_dashboard:
                        activeFragment = new MyRunsFragment();
                        toolbar.setTitle("Your runs");
                        break;
                    case R.id.navigation_notifications:
                        activeFragment = new GroupsFragment();
                        toolbar.setTitle("Your Groups");
                        break;

                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, activeFragment)
                        .commit();
                return true;
            };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}