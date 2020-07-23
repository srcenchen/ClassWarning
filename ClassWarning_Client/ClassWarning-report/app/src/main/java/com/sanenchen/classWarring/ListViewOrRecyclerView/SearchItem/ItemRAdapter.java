package com.sanenchen.classWarring.ListViewOrRecyclerView.SearchItem;

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
import com.sanenchen.classWarring.UI.TellWarning;

import java.util.List;

public class ItemRAdapter extends RecyclerView.Adapter<ItemRAdapter.ViewHolder> {
    private List<WarningSearchAd> mMusicList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Title;
        TextView Student;
        TextView UpLodDate;
        CardView cardView;

        public ViewHolder(View view) {
            super(view);
            Title = view.findViewById(R.id.Title);
            Student = view.findViewById(R.id.Student);
            UpLodDate = view.findViewById(R.id.UplodDate);
            cardView = view.findViewById(R.id.cardView);
        }
    }

    public ItemRAdapter(List<WarningSearchAd> musicList) {
        mMusicList = musicList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                WarningSearchAd music = mMusicList.get(position);
                // Toast.makeText(v.getContext(), "你点击了" + music.getTitle(), Toast.LENGTH_LONG).show();
                Context context = v.getContext();
                WarningSearchAd warningSearchAd = mMusicList.get(position);
                Intent intent = new Intent(context, TellWarning.class);
                intent.putExtra("MysqlID", warningSearchAd.getID());
                context.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WarningSearchAd music = mMusicList.get(position);
        holder.Title.setText(music.getTitle());
        holder.Student.setText("学生：" + music.getStudent());
        holder.UpLodDate.setText(music.getUplodDate());
    }

    @Override
    public int getItemCount() {
        return mMusicList.size();
    }
}
