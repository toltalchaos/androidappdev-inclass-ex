package com.example.samchatterappgentwo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChatterMessageViewAdapter extends BaseAdapter {

    private List<Chatter> chatterList;

    public ChatterMessageViewAdapter() {
        chatterList = new ArrayList<>();
    }

    public ChatterMessageViewAdapter(List<Chatter> chatterList) {
        this.chatterList = chatterList;
    }

    public void addItem(Chatter newChatter){
        chatterList.add(newChatter);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return chatterList.size();
    }

    @Override
    public Chatter getItem(int i) {
        return chatterList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View listItemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chatter_listview_item, viewGroup, false);

        TextView loginNameTextView = listItemView.findViewById(R.id.chatter_listview_item_name);
        TextView messageTextView = listItemView.findViewById(R.id.chatter_listview_item_message);
        TextView dateTextView = listItemView.findViewById(R.id.chatter_listview_item_date);

        Chatter currentChatter = getItem(i);

        loginNameTextView.setText(currentChatter.getLoginName());
        messageTextView.setText(currentChatter.getMessage());
        dateTextView.setText(currentChatter.getDate());

        return listItemView;
    }
}
