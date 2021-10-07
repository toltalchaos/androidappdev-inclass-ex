package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    private TextView mGamedifficulty;
    private TextView mGameUSername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mGamedifficulty = findViewById(R.id.textView2);
        mGameUSername = findViewById(R.id.textView3);

        if(getIntent() != null){
            Bundle extras = getIntent().getExtras();
            mGamedifficulty.setText(extras.getString("DIFFICULTY"));
            mGameUSername.setText((extras.getString("USERNAME")));

        }
    }
}