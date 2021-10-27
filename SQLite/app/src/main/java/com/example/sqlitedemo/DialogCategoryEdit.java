package com.example.sqlitedemo;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogCategoryEdit extends DialogFragment {
    private Category editCategory;
    //refrence avtivity - because this is just a dialog
    private MainActivity mainActivity;

    //alt+insert - constructor
    public DialogCategoryEdit(Category editCategory, MainActivity mainActivity) {
        this.editCategory = editCategory;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
        //build and inflate layout
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.dialog_category_edit, null);

        //register and focus on edit text
        EditText categoryNameEditText = dialogView.findViewById(R.id.dialog_edit_category_name);
        categoryNameEditText.setText(editCategory.getCategoryName());
        categoryNameEditText.requestFocus();

        Button cancelButton = dialogView.findViewById(R.id.dialog_edit_cancel_button);
        Button saveButton = dialogView.findViewById(R.id.dilog_edit_save_button);

        cancelButton.setOnClickListener(view -> {
            //on cancel button press, show toast and dismiss dialog
            Toast.makeText(getActivity(), "cancel editing", Toast.LENGTH_SHORT).show();
            dismiss();
        });
        saveButton.setOnClickListener(view -> {
            //call a method in MainActivity to update
            String categoryName = categoryNameEditText.getText().toString();
            if(!categoryName.isEmpty()){
                editCategory.setCategoryName(categoryName);
                mainActivity.updateCategory(editCategory.getCategoryId(), editCategory);
                dismiss();
            }
        });
        builder.setView(dialogView).setTitle("Edit");
        return builder.create();
    }
}
