package com.example.samchatterappgentwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.example.samchatterappgentwo.databinding.ActivityMainBinding;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {

    private final String RESPONSE_KEY = "RESPONSE_CODE";

    //step2 viewbinding
    private ActivityMainBinding binding;
    //message os handler
    private Handler postDataHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle bundle = msg.getData();
            int responseCode = bundle.getInt(RESPONSE_KEY);
            if(responseCode == HttpURLConnection.HTTP_OK){
                Toast.makeText(MainActivity.this, "send chat was successful", Toast.LENGTH_SHORT).show();
                binding.activityMainEditText.setText("");
            }
            else {
                Toast.makeText(MainActivity.this, "send chat FAIL response " + responseCode, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        //viewbinding 3
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    public void onSendClick(View view) {

            //step6: create request body pairs
            Map<String,String> requestFormDataMap = new HashMap<>();
            requestFormDataMap.put("DATA",binding.activityMainEditText.getText().toString());
            requestFormDataMap.put("LOGIN_NAME","Bray");
            NetworkAPI networkAPI = new NetworkAPI();

            //need to thread async the following
        CompletableFuture<Void> postDataFuture = CompletableFuture.runAsync(()->{
            int responseCode = networkAPI.postFormData("https://capstone1.app.dmitcapstone.ca/api/jitters/JitterServlet", requestFormDataMap);
            Message msg = postDataHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putInt(RESPONSE_KEY, responseCode);
            msg.setData(bundle);
            postDataHandler.sendMessage(msg);
        });


    }

    public void onViewMessagesButton(View view) {
        Intent openMessagesIntent = new Intent(this, ViewRemoteChatterListViewActivity.class);
        startActivity(openMessagesIntent);
    }
}