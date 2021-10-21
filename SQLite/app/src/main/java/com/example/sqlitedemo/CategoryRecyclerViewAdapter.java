package com.example.sqlitedemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.List;

//step2: extends recyclerview.adapter
public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.CategoryViewHolder> {
    //3 define fields for datasource
    private Context context;
    private List<Category> categories;

    //initialize context and categories
    public CategoryRecyclerViewAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    public CategoryRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void addItem(Category newCategory){
        categories.add(newCategory);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CategoryViewHolder viewHolder = new CategoryViewHolder(inflater.inflate(R.layout.list_item_category, parent, false));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category currentCategory = categories.get(position);
        holder.categoryIdTextview.setText("" + currentCategory.getCategoryId());
        holder.categoryNameTextView.setText(""+currentCategory.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    // step 1 viewholder class that defines views for single item
    public static class CategoryViewHolder extends ViewHolder {

        public TextView categoryIdTextview;
        public TextView categoryNameTextView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryIdTextview = itemView.findViewById(R.id.list_item_category_id);
            categoryNameTextView = itemView.findViewById(R.id.list_item_category_name);
        }
    }
}
