package com.veselin.sprintcompare.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.veselin.sprintcompare.R;
import com.veselin.sprintcompare.acitvities.RunStatisticsActivity;
import com.veselin.sprintcompare.acitvities.RunStatisticsFromGroupActivity;
import com.veselin.sprintcompare.models.Run;

import java.util.List;
import java.util.Objects;

public class GroupRunsAdapter extends RecyclerView.Adapter<GroupRunsAdapter.RecyclerViewHolder>{
    private List<Run> runs;
    private Context context;
    private String groupId;
    // RecyclerView recyclerView;
    public GroupRunsAdapter(Context context, List<Run> runs, String groupId) {
        this.context = context;
        this.runs = runs;
        this.groupId = groupId;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_runs_list_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final Run mRun = runs.get(position);
        Log.d("TAG", "onBindViewHolder: " + mRun.getUserID());
        FirebaseDatabase.getInstance().getReference("Users")
                .child(mRun.getUserID()).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = (String) snapshot.getValue();
                holder.getRunTV().setText(name + " - " + mRun.getDistance() + " m. for " + mRun.getTime() + " s.");
                holder.getRunTV().setOnClickListener(view -> context.startActivity(new Intent(context, RunStatisticsFromGroupActivity.class)
                        .putExtra("runId", mRun.getId()).putExtra("groupId", groupId)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    @Override
    public int getItemCount() {
        return runs.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView runTV;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            this.runTV = itemView.findViewById(R.id.run);
        }

        public TextView getRunTV(){
            return runTV;
        }

    }
}
