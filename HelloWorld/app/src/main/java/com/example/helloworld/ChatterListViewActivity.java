package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

public class ChatterListViewActivity extends AppCompatActivity {
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatter_list_view);

        listview = findViewById(R.id.list_view_chatter);

        getFromCloud();
    }

    private void getFromCloud() {
        BufferedReader in = null;
        ArrayList chatter = new ArrayList();
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI("http://www.youcode.ca/JSONServlet"));
            HttpResponse response = client.execute(request);
            InputStreamReader input = new InputStreamReader(response.getEntity().getContent());
            in = new BufferedReader(input);

            String line = "";

            while ((line = in.readLine()) != null){
                chatter.add(line);
            }
            in.close();
            ArrayAdapter<ArrayList> adapter = new ArrayAdapter<ArrayList>
                    (this, android.R.layout.simple_list_item_1, chatter);
            listview.setAdapter(adapter);

        }
        catch (Exception e){
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }
    }
}