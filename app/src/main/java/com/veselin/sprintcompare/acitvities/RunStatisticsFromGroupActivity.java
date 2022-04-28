package com.veselin.sprintcompare.acitvities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.veselin.sprintcompare.R;
import com.veselin.sprintcompare.models.Run;

public class RunStatisticsFromGroupActivity extends AppCompatActivity {
    Run run;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_statistics);
        initToolbar();
        getRun();
    }

    private void getRun() {
        LinearLayout llProgressBar = findViewById(R.id.llProgressBar);
        llProgressBar.setVisibility(View.VISIBLE);
         DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups")
                .child(getIntent().getStringExtra("groupId"))
                .child("Runs").child(getIntent().getStringExtra("runId"));
         ref.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 run = snapshot.getValue(Run.class);
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
        new Thread() {
            @Override
            public void run() {
                while(run == null) {
                    try {
                        sleep(200);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initViews();
                        llProgressBar.setVisibility(View.GONE);
                    }
                });
            }
        }.start();
    }

    private void initViews() {
        TextView distanceTxt, timeTxt, avgSpeedTxt, maxSpeedTxt;
        distanceTxt = findViewById(R.id.distance_text);
        distanceTxt.setText("\uD83D\uDCCF " + run.getDistance() + " m.");
        timeTxt = findViewById(R.id.time_txt);
        timeTxt.setText("⏲️ " + run.getTime() + " s.");
        avgSpeedTxt = findViewById(R.id.avg_speed_txt);
        avgSpeedTxt.setText("\uD83D\uDEB6 " + run.getAvgSpeed() + " m/s.");
        maxSpeedTxt = findViewById(R.id.max_speed_txt);
        maxSpeedTxt.setText("\uD83C\uDFC3 " + run.getTopSpeed() + " m/s.");

    }
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}