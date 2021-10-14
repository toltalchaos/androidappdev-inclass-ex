package com.example.threading;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "mainActivity";
    Button buttonPost;
    EditText editTextMessage;
    ProgressDialog pd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextMessage = findViewById(R.id.edit_text_message);
        buttonPost = findViewById(R.id.button_post);
        buttonPost.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_start_service:{
                startService(new Intent(this, FetchService.class));
                break;
            }
            case R.id.menu_item_stop_service:{
                stopService(new Intent(this, FetchService.class));
                break;
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_post:{
                String msg = editTextMessage.getText().toString();
                pd = ProgressDialog.show(this, "Title", "posting message...");
                //execute calls doInBackground method - override in class
                //execute runs async in new thread.
                new ChatWriter().execute(msg);
                editTextMessage.setText("");
                break;
            }
        }
    }
    private class ChatWriter extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings){
            String msg = strings[0];
            try{
                // useLibrary 'org.apache.http.legacy' in build.gradle
                //build a request
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://www.youcode.ca/JitterServlet");
                List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("DATA", msg));
                postParameters.add(new BasicNameValuePair("LOGIN_NAME", "Bray"));
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                post.setEntity(formEntity);

                //execute request async task and log time difference
                long differenceInMills = 0;
                long startTime = System.currentTimeMillis();

                //do the thing
                HttpResponse response = client.execute(post);

                long endTime = System.currentTimeMillis();
                differenceInMills = endTime - startTime;
                Log.d(TAG, "time lapse = " + differenceInMills + "ms");


            } catch (Exception e) {
                Log.d(TAG, "error ------> " + e.getMessage().toString());
            }
            return null;
        }

//not sure when this is being called.... seeems to do nothing?         
        @Override
        protected void onPostExecute(String s) {
            pd.dismiss();
        }
    }
}
