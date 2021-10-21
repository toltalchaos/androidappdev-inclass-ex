package com.example.sqlitedemo;

import android.provider.BaseColumns;

public final class DatabaseContract {
    private DatabaseContract() {
    }

    public static class CategoryEntry implements BaseColumns{
        //id is implied
        public static final String TABLE_NAME = "category_table";
        public static final String COLUMN_NAME = "category_name";

    }
}
