package com.example.samchatterappgentwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.example.samchatterappgentwo.databinding.ActivityMainBinding;
import com.example.samchatterappgentwo.databinding.ActivityViewRemoteChatterListViewBinding;

import java.io.BufferedReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ViewRemoteChatterListViewActivity extends AppCompatActivity {

    private  ChatterMessageViewAdapter chatterAdapter = new ChatterMessageViewAdapter();
    private ActivityViewRemoteChatterListViewBinding binding;


    private Handler getdataHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //super.handleMessage(msg);
            try {
                Bundle bundle = msg.getData();
                String responseCode = bundle.getString("RESPONSE_BODY");
                BufferedReader reader = new BufferedReader(new StringReader(responseCode));
                String line;
                String newLineChar = System.getProperty("line.separator");
                while ((line = reader.readLine()) != null){
                    Chatter currentChatter = new Chatter();
                    currentChatter.setLoginName(line);
                    line = reader.readLine();
                    currentChatter.setMessage(line);
                    line = reader.readLine();
                    currentChatter.setDate(line);

                    chatterAdapter.addItem(currentChatter);
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_view_remote_chatter_list_view);
        binding = ActivityViewRemoteChatterListViewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.activityViewRemoteListView.setAdapter(chatterAdapter);
        NetworkAPI networkAPI = new NetworkAPI();
        CompletableFuture<Void> getDataFuture = CompletableFuture.runAsync(()->{
            try {
                String responseBody = networkAPI.getResponseString("https://capstone1.app.dmitcapstone.ca/api/jitters/JitterServlet");
                Message msg = getdataHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("RESPONSE_BODY", responseBody);
                msg.setData(bundle);
                getdataHandler.sendMessage(msg);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });


    }
}