package com.sanenchen.classWarring;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<WarningSearchAd> {
    private int resource;
    public ItemAdapter(Context context, int textViewResourceId, List<WarningSearchAd> objects) {
        super(context, textViewResourceId, objects);
        resource = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        WarningSearchAd warningSearchAd = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        TextView Title = view.findViewById(R.id.Title);
        TextView Student = view.findViewById(R.id.Student);
        Title.setText(warningSearchAd.getTitle());
        Student.setText("涉事学生：" + warningSearchAd.getStudent());
        return view;
    }
}
