package com.veselin.sprintcompare.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.veselin.sprintcompare.R;
import com.veselin.sprintcompare.acitvities.MainActivity;
import com.veselin.sprintcompare.acitvities.SpeedometerActivity;
import com.veselin.sprintcompare.models.Group;
import com.veselin.sprintcompare.utils.GroupsAdapter;
import com.veselin.sprintcompare.utils.MyRunsAdapter;

import java.util.ArrayList;
import java.util.List;


public class GroupsFragment extends Fragment {
    private DatabaseReference groupsRef;
    private RecyclerView recyclerView;
    private List<Group> groups;
    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_groups, container, false);
        groupsRef = FirebaseDatabase.getInstance().getReference("Groups");
        groups = new ArrayList<>();
        initRecyclerView(v);
        loadGroups(v);
        initFAB(v);
        return v;
    }

    private void initFAB(View v){
        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(view -> showDialog());
    }

    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Create or join group?");
        alertDialogBuilder.setNegativeButton("Create", (dialogInterface, i) -> {
            createGroup();
        });
        alertDialogBuilder.setPositiveButton("Join", (dialogInterface, i) -> {
            joinGroup();
        });
        alertDialogBuilder.create().show();
    }

    private void createGroup(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Your group name");
        EditText editText = new EditText(getContext());
        alertDialogBuilder.setView(editText);
        alertDialogBuilder.setPositiveButton("Finish", (dialogInterface, i) -> {
            DatabaseReference newGroup = groupsRef.push();
            Group group = new Group(editText.getText().toString(), newGroup.getKey());
            List<String> members = new ArrayList<>();
            members.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
            group.setMemberIDs(members);
            newGroup.setValue(group.getHashMap());
            newGroup.child("Members").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("true");
            Toast.makeText(getContext(), "Successfully", Toast.LENGTH_SHORT).show();
            FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Groups").child(group.getId()).setValue("true");
        });
        alertDialogBuilder.setNegativeButton("cancel", (dialogInterface, i) -> { });
        alertDialogBuilder.create().show();
    }

    private void joinGroup(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Invite code");
        EditText editText = new EditText(getContext());
        alertDialogBuilder.setView(editText);
        alertDialogBuilder.setPositiveButton("Finish", (dialogInterface, i) -> {
            String code = editText.getText().toString();
            groupsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snap : snapshot.getChildren()){
                        if(snap.child("code").getValue().toString().equals(code)){
                            groupsRef.child(snap.child("id").getValue().toString()).child("Members").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("true");
                            Toast.makeText(getContext(), "Successfully", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("Groups").child(snap.child("id").getValue().toString()).setValue("true");
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
        alertDialogBuilder.setNegativeButton("cancel", (dialogInterface, i) -> { });
        alertDialogBuilder.create().show();
    }

    private void loadGroups(View v){
        groups = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Groups").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    groupsRef.child(snap.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Group group = new Group(snapshot.child("name").getValue().toString(), snapshot.child("id").getValue().toString());
                            groups.add(group);
                            updateAdapter();
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
    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        updateAdapter();
    }

    private void updateAdapter(){
        GroupsAdapter adapter = new GroupsAdapter(getContext(), groups);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}