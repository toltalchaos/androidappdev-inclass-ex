package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;

public class CustomListViewActivity extends AppCompatActivity {

    //string = key , string = value
    ArrayList<HashMap<String,String>> chatter = new ArrayList<HashMap<String,String>>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list_view);

        listView = findViewById(R.id.list_view_custom);
        displayChatter();

    }

    private void displayChatter() {
        String[] keys = new String[]{"SENDER","DATE" , "MESSAGE"};
        int[] ids = new int[]{R.id.text_view_row_sender, R.id.text_view_row_date, R.id.text_view_row_message };
        SimpleAdapter adapter = new SimpleAdapter( this, chatter, R.layout.custom_row, keys, ids);
        populateArrayList();
        listView.setAdapter(adapter);
    }

    private void populateArrayList() {
        BufferedReader in = null;

        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI("http://www.youcode.ca/JitterServlet"));
            HttpResponse response = client.execute(request);
            InputStreamReader input = new InputStreamReader(response.getEntity().getContent());
            in = new BufferedReader(input);

            String line = "";

            while ((line = in.readLine()) != null){
                HashMap<String, String> temp = new HashMap<String,String>();
                temp.put("SENDER", line);
                line = in.readLine();
                temp.put("MESSAGE", line);
                line = in.readLine();
                temp.put("DATE", line);
                chatter.add(temp);
            }
            in.close();

        }
        catch (Exception e){
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }
    }
}