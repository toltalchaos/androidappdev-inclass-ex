package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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

public class ObjectSpinnerActivity extends AppCompatActivity
{
    private Spinner spinner;
    private MySpinAdapter mySpinAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_spinner);

        //init the fucking thing
        ArrayList chatter = new ArrayList();

        //populate the shit.. this goes through and adds to the array.
        populateArrayList(chatter);
        //setup the custom adapter and slap in the array we populated
        mySpinAdapter = new MySpinAdapter(this, android.R.layout.simple_spinner_item, chatter);
        //grab the spinner by the balls
        spinner = (Spinner) findViewById(R.id.spinner_object);
        //force the adapter on it
        spinner.setAdapter(mySpinAdapter);

        spinner.setOnItemSelectedListener(new MySpinItemSelectedListener(this));
    }

    private void populateArrayList(ArrayList chatter) {
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
                Chat temp = new Chat();
                temp.setChatSender(line);
                line = in.readLine();
                temp.setChatMessage(line);
                line = in.readLine();
                temp.setChatDate(line);
                chatter.add(temp);
            }
            in.close();

        }
        catch (Exception e){
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }
    }

}

class MySpinItemSelectedListener implements AdapterView.OnItemSelectedListener
{
    static ObjectSpinnerActivity thisActivity;

    //init the class to hang on to the current activity page
    public MySpinItemSelectedListener(Context context){
        thisActivity = (ObjectSpinnerActivity) context;
    }

    @Override
    public void onItemSelected(AdapterView<?> spinner, View row, int position, long id) {
        //grab the incoming spinner object and select the chat item at the selected position
        Chat chatitem = (Chat)spinner.getAdapter().getItem(position);

        //grab the message field in this activity
        TextView tvMessage = thisActivity.findViewById(R.id.object_spinner_message);
        //assign the text from the selected chat item
        tvMessage.setText(chatitem.getChatMessage());

        TextView tvSender = thisActivity.findViewById(R.id.object_spinner_sender);
        tvSender.setText(chatitem.getChatSender());

        TextView tvDate = thisActivity.findViewById(R.id.object_spinner_date);
        tvDate.setText(chatitem.getChatDate());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // do nothing
    }
}