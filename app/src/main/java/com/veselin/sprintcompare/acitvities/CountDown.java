package com.veselin.sprintcompare.acitvities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.veselin.sprintcompare.R;

public class CountDown extends AppCompatActivity {
    private int counter = 3;
    CountDownTimer ct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down_timer);
        TextView textView = findViewById(R.id.textView);
        ct = new CountDownTimer(4000, 1000){
            public void onTick(long millisUntilFinished){
                if(counter == 0){
                    textView.setText("START");
                    textView.setTextSize(96);
                    MediaPlayer.create(CountDown.this, R.raw.horn).start();
                }else {
                    textView.setText(String.valueOf(counter));
                }
                counter--;
            }
            public  void onFinish(){
                startActivity(new Intent(CountDown.this, SpeedometerActivity.class).putExtra("distance", getIntent().getStringExtra("distance")));
                finish();
            }
        };
        ct.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ct.cancel();
        finish();
    }
}