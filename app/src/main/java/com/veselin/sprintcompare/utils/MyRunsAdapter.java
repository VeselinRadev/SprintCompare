package com.veselin.sprintcompare.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.veselin.sprintcompare.R;
import com.veselin.sprintcompare.acitvities.RunStatisticsActivity;
import com.veselin.sprintcompare.models.Run;

import java.util.List;

public class MyRunsAdapter extends RecyclerView.Adapter<MyRunsAdapter.RecyclerViewHolder>{
    private List<Run> runs;
    private Context context;
    // RecyclerView recyclerView;
    public MyRunsAdapter(Context context, List<Run> runs) {
        this.context = context;
        this.runs = runs;
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
        holder.getRunTV().setText("\uD83C\uDFC1 " + mRun.getDistance() + " meters for " + mRun.getTime() + " seconds");
        holder.getRunTV().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, RunStatisticsActivity.class).putExtra("id", mRun.getId()));
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
