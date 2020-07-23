package com.sanenchen.classWarring.ListViewOrRecyclerView.ClassManager;

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

public class ClassManagerAdapter extends RecyclerView.Adapter<ClassManagerAdapter.ViewHolder>{
    private List<ClassManagerList> classManagerLists;
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView class_name_item;
        TextView class_id_item;
        CardView class_item;
        public ViewHolder(View view) {
            super(view);
            class_name_item = view.findViewById(R.id.class_name_item);
            class_id_item = view.findViewById(R.id.class_id_item);
            class_item = view.findViewById(R.id.class_item_cardview);
        }
    }
    public ClassManagerAdapter(List<ClassManagerList> classManagerLists) {
        this.classManagerLists = classManagerLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_class_manager, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.class_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                ClassManagerList classManagerList = classManagerLists.get(position);
                Context context = v.getContext();
                Intent intent = new Intent(context, TabActivity.class);
                intent.putExtra("classID", classManagerList.getClassID());
                context.startActivity(intent);
            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClassManagerList classManagerList = classManagerLists.get(position);
        holder.class_name_item.setText(String.format("%s%s", classManagerList.getInGrade(), classManagerList.getClassName()));
        holder.class_id_item.setText(String.format("%s", classManagerList.getClassID()));
    }

    @Override
    public int getItemCount() {
        return classManagerLists.size();
    }
}
