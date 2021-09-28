package com.example.helloworld;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MySpinAdapter extends ArrayAdapter
{
    private Context context;
    private ArrayList chatter;


    public MySpinAdapter(@NonNull Context context, int textViewResourceId, @NonNull ArrayList objects) {
        super(context, textViewResourceId, objects);

        this.context = context;
        this.chatter = objects;
    }
    public int getCount(){
        return chatter.size();
    }
    public Chat getItem(int index){
        return (Chat) chatter.get(index);
    }
    public View getView(int position, View convertView, ViewGroup parent)
    {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(((Chat)(chatter.get(position))).getChatMessage());
        return label;
    }
    //populate and return a single custom_row.xml
    public View getDropDownView (int position, View convertView, ViewGroup parent){
        ObjectSpinnerActivity activity = (ObjectSpinnerActivity) context;
        LayoutInflater inflater = activity.getLayoutInflater();
        View spinnerRow = inflater.inflate(R.layout.custom_row, null);

        TextView sender = (TextView) spinnerRow.findViewById(R.id.text_view_row_sender);
        sender.setText(((Chat)(chatter.get(position))).getChatSender());

        TextView date = (TextView) spinnerRow.findViewById(R.id.text_view_row_date);
        date.setText(((Chat)(chatter.get(position))).getChatDate());

        TextView message = (TextView) spinnerRow.findViewById(R.id.text_view_row_message);
        message.setText(((Chat)(chatter.get(position))).getChatMessage());

        return spinnerRow;
    }
}
