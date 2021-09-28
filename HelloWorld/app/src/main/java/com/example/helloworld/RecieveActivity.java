package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class RecieveActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve);
        //register button
        Button submitButton = findViewById(R.id.launch_button);
        //pipe to method
        submitButton.setOnClickListener(this);

        //pull bundle apart
        Bundle bundle = getIntent().getExtras();
        String prefix = bundle.getString("PREFIX");
        String data = bundle.getString("DATA");

        //register and set edit text
        EditText editText = findViewById(R.id.edit_text_message);
        editText.setText(prefix + data);
        
        getFromCloud();
    }

    private void getFromCloud() {
        BufferedReader in = null;
        TextView textView = findViewById(R.id.edit_text_message);
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI("http://www.youcode.ca/JSONServlet"));
            HttpResponse response = client.execute(request);
            InputStreamReader input = new InputStreamReader(response.getEntity().getContent());
            in = new BufferedReader(input);

            String line = "";

            while ((line = in.readLine()) != null){
                textView.append(line + "\n");
            }
            in.close();

        }
        catch (Exception e){
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, EmptyScreen.class);
        this.startActivity(intent);
    }
}