package com.veselin.sprintcompare.acitvities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.veselin.sprintcompare.R;
import com.veselin.sprintcompare.models.Group;
import com.veselin.sprintcompare.models.Run;
import com.veselin.sprintcompare.utils.Speedometer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class SpeedometerActivity extends AppCompatActivity implements LocationListener {
    private TextView tvTimer;
    private Run run;
    private double distance;
    private double topSpeed = 0.0;
    private List<Group> groups;
    private AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speedometer);
        tvTimer = findViewById(R.id.time);
        initToolbar();
        getGroups();
        Button finishBtn = findViewById(R.id.finish_btn);
        //Asking for Permission
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
            return;
        }
        //asking the gps to start tracking location
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        this.updateSpeed(null);
        start(tvTimer);
        //TODO Show Dialog to ask to each group to save this result
        finishBtn.setOnClickListener(view -> {
            distance = Double.parseDouble(getIntent().getStringExtra("distance"));
            saveRun();
            createGroupPicker();
        });
    }

    private void saveRun() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Runs").push();
                run = new Run(String.format( "%.2f", topSpeed), String.format( "%.2f", stop(tvTimer)), String.format( "%.2f", distance), ref.getKey());
                run.setUserID(FirebaseAuth.getInstance().getCurrentUser().getUid());
                ref.setValue(run.getHashMap());
                //ref.setValue(run.getHashMap()).addOnCompleteListener(task -> finish());

    }
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("SpeedoMeter");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        showDialog();
        return true;
    }
    public void finish()
    {
        super.finish();
        System.exit(0);
    }

    private void updateSpeed(Location location) {
        // TODO Auto-generated method stub
        float nCurrentSpeed = 0;
        //Get the current speed if the location is properly setup
        if(location != null)
        {
            nCurrentSpeed = location.getSpeed();
            if(nCurrentSpeed > topSpeed)topSpeed = nCurrentSpeed;
        }
        Speedometer speedometer = findViewById(R.id.tv_speed);
        speedometer.speedTo(nCurrentSpeed);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null)
        {
            Location myLocation = new Location(location);
            this.updateSpeed(myLocation);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    //Creating Timer
    long startTime, timeInMilliseconds = 0;
    Handler customHandler = new Handler();

    public static String getDateFromMillis(long d) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(d);
    }

    public void start(View v) {
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    public double stop(View v) {
        customHandler.removeCallbacks(updateTimerThread);
        return timeInMilliseconds/1000.0;
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            tvTimer.setText(getDateFromMillis(timeInMilliseconds));
            customHandler.postDelayed(this, 1000);
        }
    };

    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to quit?");
        alertDialogBuilder.setNegativeButton("Yes", (dialogInterface, i) -> {
            startActivity(new Intent(SpeedometerActivity.this, MainActivity.class));
            finish();
        });
        alertDialogBuilder.setPositiveButton("No", (dialogInterface, i) -> { });
        alertDialogBuilder.create().show();
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    private void getGroups(){
        groups = new ArrayList<>();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Groups");
        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("Groups");
        groups.clear();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    groupRef.child(snap.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Group group =  snapshot.getValue(Group.class);
                            groups.add(group);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void createGroupPicker(){
        ArrayList<Integer> selectedGrouslist = new ArrayList();
        String[] groupNames = new String[groups.size()];
        String[] groupIds = new String[groups.size()];
        for(int i = 0; i < groups.size(); i++){
            groupNames[i] = groups.get(i).getName();
            groupIds[i] = groups.get(i).getId();
        }
        boolean icount[] = new boolean[groups.size()];

        AlertDialog.Builder builder = new AlertDialog.Builder(SpeedometerActivity.this);
        builder.setTitle("Choose Groups where to upload your run")
                .setMultiChoiceItems(groupNames,icount, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
                        if (arg2) {
                            // If user select a item then add it in selected items
                            selectedGrouslist.add(arg1);
                        } else if (selectedGrouslist.contains(arg1)) {
                            // if the item is already selected then remove it
                            selectedGrouslist.remove(Integer.valueOf(arg1));
                        }
                    }
                })      .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(Integer id : selectedGrouslist) {
                            DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupIds[id]).child("Runs");
                            DatabaseReference runRef = groupRef.child(run.getId());
                            runRef.setValue(run.getHashMapWithUId());
                        }
                        finish();
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SpeedometerActivity.this,"Saved in your profile",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        //Creating dialog box
        AlertDialog dialog  = builder.create();
        dialog.show();
    }


}

