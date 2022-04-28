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
import com.veselin.sprintcompare.acitvities.GroupDetails;
import com.veselin.sprintcompare.models.Group;

import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.RecyclerViewHolder>{
    private List<Group> groups;
    private Context context;
    // RecyclerView recyclerView;
    public GroupsAdapter(Context context, List<Group> groups) {
        this.context = context;
        this.groups = groups;
    }

    @NonNull
    @Override
    public GroupsAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_runs_list_item, parent, false);
        return new GroupsAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GroupsAdapter.RecyclerViewHolder holder, int position) {
        final Group mGroup = groups.get(position);
        holder.getRunTV().setText(mGroup.getName());
        holder.getRunTV().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, GroupDetails.class).putExtra("id", mGroup.getId()));
            }
        });

    }


    @Override
    public int getItemCount() {
        return groups.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView groupTV;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            this.groupTV = itemView.findViewById(R.id.run);
        }

        public TextView getRunTV(){
            return groupTV;
        }

    }
}
