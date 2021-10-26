package com.example.sqlitedemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.sqlitedemo.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //viewbinding 2
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        
    }

    public void onSaveButtonClick(View view){
        String categoryName = binding.activityMainCategoryEditText.getText().toString();
        if (categoryName.isEmpty()){
            Toast.makeText(this, "nothing to insert", Toast.LENGTH_SHORT).show();
        }
        else {
            Category newCategory = new Category();
            newCategory.setCategoryName(categoryName);
            //create instance of DB
            DBHelper dbHelper = new DBHelper(this);
            long categoryID = dbHelper.addCategory(newCategory);
            Toast.makeText(this, "inserted " + categoryName + " as " + categoryID, Toast.LENGTH_SHORT).show();
            reBindRecyclerView();
        }


    }
    //rebind
    private void reBindRecyclerView(){
        DBHelper dbHelper = new DBHelper(this);
        List<Category> categories = dbHelper.getCategoriesList();
        CategoryRecyclerViewAdapter recyclerViewAdapter = new CategoryRecyclerViewAdapter(this, categories);
        binding.activityMainRecyclerView.setAdapter(recyclerViewAdapter);
        binding.activityMainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public static final String INTENT_ACTION_CATEGORY_DELETE = "intent.delete.test.CATEGORY_DELETE";
    public static final String EXTRA_CATEGORY_CATEGORY_ID = "intent.ID.CATEGORY_ID";
    //define a broadcast reciever class
    class DeleteCategoryBrodcastReciever extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(INTENT_ACTION_CATEGORY_DELETE)){
                int categoryId = intent.getIntExtra(EXTRA_CATEGORY_CATEGORY_ID, 0);
                if (categoryId > 0){
                    //delete confirmation
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("DELTE CONFIRMATION");
                    builder.setMessage("do you want to delete item #" + categoryId+"?");
                    builder.setPositiveButton("yes", (dialogInterface, i) -> {
                        DBHelper dbHelper = new DBHelper(context);
                        dbHelper.deleteCategory(categoryId);
                        reBindRecyclerView();
                        Toast.makeText(context, "delete successful ", Toast.LENGTH_SHORT ).show();
                    });
                    //alt+spce for lambda autogen
                    builder.setNegativeButton("No",(dialogInterface, i) -> {});
                    builder.show();
                }
            }
        }

    }
    private DeleteCategoryBrodcastReciever currentDeleteCategoryBroadcastReciever = new DeleteCategoryBrodcastReciever();

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter categoryDeleteIntentFilter = new IntentFilter();
        categoryDeleteIntentFilter.addAction(INTENT_ACTION_CATEGORY_DELETE);
        registerReceiver(currentDeleteCategoryBroadcastReciever, categoryDeleteIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(currentDeleteCategoryBroadcastReciever);
    }
}