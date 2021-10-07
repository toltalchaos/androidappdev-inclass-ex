package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText mUsernameEditText;
    private RadioGroup mGameDiffGroup;
    private String mSelectedDifficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUsernameEditText = findViewById(R.id.activity_name_username_textview);
        mGameDiffGroup = findViewById(R.id.game_radios);

    }

    public void onStartButtonPress(View view) {
        switch (mGameDiffGroup.getCheckedRadioButtonId()){
            case R.id.easy_button:{
                mSelectedDifficulty = "Easy";
                break;
            }
            case R.id.normal_button:{
                mSelectedDifficulty = "Normal";
                break;
            }
            case R.id.hard_button:{
                mSelectedDifficulty = "Hard";
                break;
            }
        }
        String username = mUsernameEditText.getText().toString();

        Bundle bundle = new Bundle();
        bundle.putString("DIFFICULTY", mSelectedDifficulty);
        bundle.putString("USERNAME", username);

        if(mSelectedDifficulty.isEmpty() || username.isEmpty()){
            Toast.makeText(this, "fill all fields", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent openGameIntent = new Intent(this, GameActivity.class);
            openGameIntent.putExtras(bundle);
            startActivity(openGameIntent);
        }
    }
}