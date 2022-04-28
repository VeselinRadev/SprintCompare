package com.veselin.sprintcompare.acitvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.veselin.sprintcompare.R;
import com.veselin.sprintcompare.models.Group;
import com.veselin.sprintcompare.models.Run;
import com.veselin.sprintcompare.utils.GroupRunsAdapter;
import com.veselin.sprintcompare.utils.MyRunsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupDetails extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Run> runs;
    private DatabaseReference groupRef;
    private Group group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);
        initGroupRef();
        runs = new ArrayList<>();
        initRecyclerView();
        displayRuns();
    }
    private void initGroupRef(){
        String groupID = getIntent().getStringExtra("id");
        groupRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupID);
        groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String groupName;
                groupName = snapshot.child("name").getValue().toString();
                group = new Group(groupName, groupID);
                initToolbar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(group.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_invite_code, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_get_invite_code:
                Toast.makeText(this, "Invite code: " + group.getInviteCode(), Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        updateAdapter();
    }

    private void updateAdapter(){
        GroupRunsAdapter adapter = new GroupRunsAdapter(GroupDetails.this, runs, getIntent().getStringExtra("id"));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void displayRuns(){
        DatabaseReference myRef = groupRef.child("Runs");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                runs.clear();
                for(DataSnapshot snap : snapshot.getChildren()){
                    Run run = snap.getValue(Run.class);
                    runs.add(run);
                }
                updateAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}