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

        binding.activityMainFab.setOnClickListener(view1 -> {
            DialogCategoryAdd addDialog = new DialogCategoryAdd(this);
            addDialog.show(getSupportFragmentManager(), "DialogCategoryAdd");
        });

        
    }

//    public void onSaveButtonClick(View view){
//        String categoryName = binding.activityMainCategoryEditText.getText().toString();
//        if (categoryName.isEmpty()){
//            Toast.makeText(this, "nothing to insert", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            Category newCategory = new Category();
//            newCategory.setCategoryName(categoryName);
//            //create instance of DB
//            DBHelper dbHelper = new DBHelper(this);
//            long categoryID = dbHelper.addCategory(newCategory);
//            Toast.makeText(this, "inserted " + categoryName + " as " + categoryID, Toast.LENGTH_SHORT).show();
//            reBindRecyclerView();
//        }
//
//
//    }
    //rebind
    private void reBindRecyclerView(){
        DBHelper dbHelper = new DBHelper(this);
        List<Category> categories = dbHelper.getCategoriesList();
        CategoryRecyclerViewAdapter recyclerViewAdapter = new CategoryRecyclerViewAdapter(this, categories);
        binding.activityMainRecyclerView.setAdapter(recyclerViewAdapter);
        binding.activityMainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public static final String INTENT_ACTION_CATEGORY_DELETE = "intent.delete.test.CATEGORY_DELETE";
    public static final String INTENT_ACTION_CATEGORY_EDIT = "intent.delete.test.CATEGORY_EDIT";
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
    //define edit brodcast reciever class
    class EditCategoryBrodcastReciever extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //make sure edit intent is passed
            if(intent.getAction().equals(INTENT_ACTION_CATEGORY_EDIT)){
                int categoryId = intent.getIntExtra(EXTRA_CATEGORY_CATEGORY_ID, 0);
                if(categoryId > 0){
                    DBHelper dbHelper = new DBHelper(MainActivity.this);
                    Category editCategory = dbHelper.findOneCategoryById(categoryId);
                    DialogCategoryEdit editDialog = new DialogCategoryEdit(editCategory, MainActivity.this);
                    editDialog.show(getSupportFragmentManager(), "MainActivity_Edit_Dialog");
                }
            }
        }
    }
    //create instances of brodcast reciever classes
    private EditCategoryBrodcastReciever currentEditCategoryBrodcastReciever = new EditCategoryBrodcastReciever();
    private DeleteCategoryBrodcastReciever currentDeleteCategoryBroadcastReciever = new DeleteCategoryBrodcastReciever();

    //register recievers on android OS
    @Override
    protected void onResume() {
        super.onResume();
        //init intent filter to look for intents using constant values
        IntentFilter categoryDeleteIntentFilter = new IntentFilter();
        categoryDeleteIntentFilter.addAction(INTENT_ACTION_CATEGORY_DELETE);
        registerReceiver(currentDeleteCategoryBroadcastReciever, categoryDeleteIntentFilter);

        IntentFilter categoryEditIntentFilter = new IntentFilter();
        categoryEditIntentFilter.addAction(INTENT_ACTION_CATEGORY_EDIT);
        registerReceiver(currentEditCategoryBrodcastReciever, categoryEditIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregister
        unregisterReceiver(currentDeleteCategoryBroadcastReciever);
        unregisterReceiver(currentEditCategoryBrodcastReciever);
    }

    public void updateCategory(int categoryId, Category updatedCategory){
        DBHelper dbHelper = new DBHelper(this);
        if(dbHelper.updateCategory(categoryId,updatedCategory) > 0){
            //success
            reBindRecyclerView();
            Toast.makeText(this, "update was successful", Toast.LENGTH_SHORT).show();
        }
        else{
            //Fail
            Toast.makeText(this, "update NOT successful", Toast.LENGTH_SHORT).show();
        }
    }
    public void addCategory(Category newCategory){
        DBHelper dbHelper = new DBHelper(this);
        if(dbHelper.addCategory(newCategory) > 0){
            reBindRecyclerView();
            Toast.makeText(this, "Add Successful", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Add was unsuccessful", Toast.LENGTH_SHORT).show();
        }
    }
}