package com.sanenchen.classWarring.ListViewOrRecyclerView.UpdateLog;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sanenchen.classWarring.R;
import com.sanenchen.classWarring.UI.TabActivity;

import java.util.List;

public class UpdateLogAdapter extends RecyclerView.Adapter<UpdateLogAdapter.ViewHolder>{
    private List<UpdateLogList> updateLogLists;
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_update_log_title;
        TextView item_update_log_things;
        public ViewHolder(View view) {
            super(view);
            item_update_log_title = view.findViewById(R.id.item_update_log_title);
            item_update_log_things = view.findViewById(R.id.item_update_log_things);
        }
    }
    public UpdateLogAdapter(List<UpdateLogList> updateLogLists) {
        this.updateLogLists = updateLogLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_update_log, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UpdateLogList updateLogList = updateLogLists.get(position);
        holder.item_update_log_title.setText(updateLogList.getTitle());
        holder.item_update_log_things.setText(updateLogList.getThings());
    }

    @Override
    public int getItemCount() {
        return updateLogLists.size();
    }
}
