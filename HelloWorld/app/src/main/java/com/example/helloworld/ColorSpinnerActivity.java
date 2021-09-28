package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public class ColorSpinnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_spinner);

        Spinner colorspinner = findViewById(R.id.spinner_color_picker);
        colorspinner.setOnItemSelectedListener(new MySpinnerListener());
    }
}

class MySpinnerListener implements AdapterView.OnItemSelectedListener{

    @Override
    public void onItemSelected(AdapterView<?> spinner, View row, int index, long id)
    {
        View liniarlayout = (View) spinner.getParent();
        String bgcolor = spinner.getResources().getStringArray(R.array.color_values)[index];
        liniarlayout.setBackgroundColor(Color.parseColor(bgcolor));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //do nothing
    }
}