package com.example.sqlitedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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
            CategoryDBHelper dbHelper = new CategoryDBHelper(this);
            long categoryID = dbHelper.addCategory(newCategory);
            Toast.makeText(this, "inserted " + categoryName + " as " + categoryID, Toast.LENGTH_SHORT).show();
            reBindRecyclerView();
        }


    }
    //rebind
    private void reBindRecyclerView(){
        CategoryDBHelper dbHelper = new CategoryDBHelper(this);
        List<Category> categories = dbHelper.getCategoriesList();
        CategoryRecyclerViewAdapter recyclerViewAdapter = new CategoryRecyclerViewAdapter(this, categories);
        binding.activityMainRecyclerView.setAdapter(recyclerViewAdapter);
        binding.activityMainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}