package com.example.threadingsamdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import com.example.threadingsamdemo.databinding.ActivityMainBinding;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private Handler mainThreadHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle bundle = msg.getData();
            String result = bundle.getString("RESULT_KEY");
            binding.resultTextView.setText(result);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        // viewbinding setup
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

    }
    public String fetchFromServer(){
        try {
            Thread.sleep(1000);
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return "Data Returned from server placeholder";
    }
    public String processData(String data){
        try {
            Thread.sleep(2000);
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return data.toUpperCase();
    }
    private String calculateFirstResult(String data){
        try {
            Thread.sleep(3000);
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return String.format("length of Data is %d", data.length()) ;
    }
    private String calculateSecondResult(String data){
        try {
            Thread.sleep(4000);
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return data.replaceAll("Data", "Information");
    }

    public void onSubmitClick(View view) {

        CompletableFuture<Void> fetchDataFuture = CompletableFuture.supplyAsync(()->{

            String fetchServerdata = fetchFromServer();
            String processData = processData(fetchServerdata);
            String firstReault = calculateFirstResult(processData);
            String secondResult = calculateSecondResult(firstReault);
            String resultSummary = String.format("first result %s \n secondResult %s ", firstReault, secondResult);
            return resultSummary;
        }).thenAccept(result -> {
//            binding.resultTextView.post(()->{
//                binding.resultTextView.setText(result);
//            });
        Message msg = mainThreadHandler.obtainMessage();
        Bundle bundle = new Bundle();
        //bundle. datatype to pass through
        bundle.putString("RESULT_KEY", result);
        msg.setData(bundle);
        mainThreadHandler.sendMessage(msg);

        });


    }
}