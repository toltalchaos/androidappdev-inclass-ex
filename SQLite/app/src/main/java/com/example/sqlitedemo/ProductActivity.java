package com.example.sqlitedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;

import com.example.sqlitedemo.databinding.ActivityProductBinding;

public class ProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ActivityProductBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_product);
        binding = ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        DBHelper dbHelper = new DBHelper(this);
        Cursor categoryQueryResultCursor = dbHelper.getCategoriesCursor();
        String[] fromColumnNames = {DatabaseContract.CategoryEntry.COLUMN_NAME_CATEGORYNAME};
        int[] toViewIds = {android.R.id.text1};
        int flags = 0;

        SimpleCursorAdapter categoryAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_spinner_item,
                categoryQueryResultCursor,
                fromColumnNames,
                toViewIds,
                flags);
        binding.productsSpinner.setAdapter(categoryAdapter);

        binding.productsSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int selectedCategoryID = i == 0 ? 1 : i;

        DBHelper dbHelper = new DBHelper(this);
        Cursor categoryProductQueryResultCursor = dbHelper.getProductsByCategoryId(selectedCategoryID);
        String[] fromColumnNames = {DatabaseContract.ProductEntry.COLUMN_NAME_PRODUCTNAME};
        int[] toViewIds = {android.R.id.text1};
        int flags = 0;

        SimpleCursorAdapter categoryProductAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                categoryProductQueryResultCursor,
                fromColumnNames,
                toViewIds,
                flags);
        binding.productsListview.setAdapter(categoryProductAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}