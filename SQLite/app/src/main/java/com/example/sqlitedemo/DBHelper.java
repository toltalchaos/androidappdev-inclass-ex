package com.example.sqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "SqliteDemo.db";

    private static final String SQL_CREATE_CATEGORY_ENTRIES =
            "CREATE TABLE " + DatabaseContract.CategoryEntry.TABLE_NAME + "(" +
             DatabaseContract.CategoryEntry._ID + " INTEGER PRIMARY KEY," + DatabaseContract.CategoryEntry.COLUMN_NAME_CATEGORYNAME + " TEXT"+")";
    private static final String SQL_DELETE_CATEGORY_ENTRIES = "DROP TABLE IF EXISTS " + DatabaseContract.CategoryEntry.TABLE_NAME;

    private static final String SQL_CREATE_PRODUCT_ENTRIES = "CREATE TABLE " + DatabaseContract.ProductEntry.TABLE_NAME + "("
            + DatabaseContract.ProductEntry._ID + " INTEGER PRIMARY KEY, " + DatabaseContract.ProductEntry.COLUMN_NAME_PRODUCTNAME + " TEXT, " +
            DatabaseContract.ProductEntry.COLUMN_NAME_UNITPRICE + " REAL," + DatabaseContract.ProductEntry.COLUMN_NAME_CATEGORY_ID + " INTEGER, " +
            "FOREIGN KEY (" + DatabaseContract.ProductEntry.COLUMN_NAME_CATEGORY_ID + ")" + " REFERENCES " +
            DatabaseContract.CategoryEntry.TABLE_NAME + "(" + DatabaseContract.CategoryEntry._ID + ")";
    private static final String SQL_DELETE_PRODUCT_ENTRIES = "DROP TABLE IF EXISTS" + DatabaseContract.ProductEntry.TABLE_NAME;

    public DBHelper(Context context){
        //custtom constructor passing context
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_CATEGORY_ENTRIES);

        Category categoryOne = new Category();
        categoryOne.setCategoryName("Category 1");
        addCategory(categoryOne);

        sqLiteDatabase.execSQL("INSERT INTO " + DatabaseContract.CategoryEntry.TABLE_NAME + "(" +
                DatabaseContract.CategoryEntry.COLUMN_NAME_CATEGORYNAME + ")" + " VALUES('Category 2')");

        sqLiteDatabase.execSQL("INSERT INTO " + DatabaseContract.CategoryEntry.TABLE_NAME + "(" +
                DatabaseContract.CategoryEntry.COLUMN_NAME_CATEGORYNAME + ")" + " VALUES('Category 4')");

        Category categorythree = new Category();
        categorythree.setCategoryName("cat#3");
        addCategory(categorythree);

        //ADD PRODUCTS
        final String cat1prod1 = "INSERT INTO product_table(product_name, unit_price, category_id) VALUES('Cat1 product1',1.23,1)";
        final String cat2prod1 = "INSERT INTO product_table(product_name, unit_price, category_id) VALUES('Cat1 product1',1.23,2)";
        final String cat1prod2 = "INSERT INTO product_table(product_name, unit_price, category_id) VALUES('Cat1 product2',2.23,1)";
        sqLiteDatabase.execSQL(cat1prod1);
        sqLiteDatabase.execSQL(cat1prod2);
        sqLiteDatabase.execSQL(cat2prod1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_CATEGORY_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    //add
    public long addCategory(Category newCategory){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CategoryEntry.COLUMN_NAME_CATEGORYNAME, newCategory.getCategoryName());
        return db.insert(DatabaseContract.CategoryEntry.TABLE_NAME, null, values);
    }
    //get
    public Cursor getCategoriesCursor(){
        //create readable db instance
        SQLiteDatabase db = getReadableDatabase();

        //select columns
        String[] columns = {
                DatabaseContract.CategoryEntry._ID,
                DatabaseContract.CategoryEntry.COLUMN_NAME_CATEGORYNAME
        };
        //sql params for query
        String selection = null;
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String sortOrder = DatabaseContract.CategoryEntry.COLUMN_NAME_CATEGORYNAME + "ASC";

        //execute query -> returns cursor
        return db.query(
                DatabaseContract.CategoryEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy);
    }
    public Cursor getProductsByCategoryId(int categoryId){
        //create readable db instance
        SQLiteDatabase db = getReadableDatabase();

        //select columns
        String[] columns = {
                DatabaseContract.ProductEntry._ID,
                DatabaseContract.ProductEntry.COLUMN_NAME_PRODUCTNAME,
                DatabaseContract.ProductEntry.COLUMN_NAME_UNITPRICE,
                DatabaseContract.ProductEntry.COLUMN_NAME_CATEGORY_ID
        };
        //sql params for query
        String selection = DatabaseContract.ProductEntry.COLUMN_NAME_CATEGORY_ID + "=?";
        String[] selectionArgs = {String.valueOf(categoryId)};
        String groupBy = null;
        String having = null;
        String orderBy = DatabaseContract.ProductEntry.COLUMN_NAME_PRODUCTNAME + " ASC";
        String sortOrder = DatabaseContract.CategoryEntry.COLUMN_NAME_CATEGORYNAME + "ASC";

        //execute query -> returns cursor
        return db.query(
                DatabaseContract.CategoryEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy);
    }
    public List<Category> getCategoriesList(){
        Cursor queryResultCursor = getCategoriesCursor();
        List<Category> Categories = new ArrayList<>();

        //process cursor
        //.movetonext() reurns false on no rows left
        while (queryResultCursor.moveToNext()){
//            Category currentCategory = new Category();
//            int columnIdfield = queryResultCursor.getColumnIndexOrThrow(DatabaseContract.CategoryEntry._ID);
//            int columnNameField = queryResultCursor.getColumnIndexOrThrow(DatabaseContract.CategoryEntry.COLUMN_NAME_CATEGORYNAME);
//            currentCategory.setCategoryId(queryResultCursor.getInt(columnIdfield));
//            currentCategory.setCategoryName(queryResultCursor.getString(columnNameField));

            Category currentCategory = mapCursorToCategory(queryResultCursor);
            Categories.add(currentCategory);
        }

        return Categories;
    }
    public Cursor findOneCategoryCursorById(int categoryId){
        //create readable db instance
        SQLiteDatabase db = getReadableDatabase();

        //select columns
        String[] columns = {
                DatabaseContract.CategoryEntry._ID,
                DatabaseContract.CategoryEntry.COLUMN_NAME_CATEGORYNAME
        };
        //sql params for query
        String selection = DatabaseContract.CategoryEntry._ID + "=?";
        String[] selectionArgs = {String.valueOf(categoryId)};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String sortOrder = DatabaseContract.CategoryEntry.COLUMN_NAME_CATEGORYNAME + "ASC";

        //execute query -> returns cursor
        return db.query(
                DatabaseContract.CategoryEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy);
    }
    public Category findOneCategoryById(int categoryId){
        Cursor queryResultCursor = findOneCategoryCursorById(categoryId);
        Category existingCategory = null;
        if(queryResultCursor.moveToNext()){
            existingCategory = mapCursorToCategory(queryResultCursor);
        }
        return existingCategory;
    }
    private Category mapCursorToCategory(Cursor queryResultCursor){
        Category currentCategory = new Category();

//        currentCategory.setCategoryId(queryResultCursor.getInt(0));
//        currentCategory.setCategoryName(queryResultCursor.getString(1));
        currentCategory.setCategoryId(queryResultCursor.getInt(queryResultCursor.getColumnIndexOrThrow(DatabaseContract.CategoryEntry._ID)));
        currentCategory.setCategoryName(queryResultCursor.getString(queryResultCursor.getColumnIndexOrThrow(DatabaseContract.CategoryEntry.COLUMN_NAME_CATEGORYNAME)));



        return currentCategory;
    }
    //put
    public int updateCategory(int categoryId, Category updatedCategory){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        //only need to put values on changed columns
        values.put(DatabaseContract.CategoryEntry.COLUMN_NAME_CATEGORYNAME, updatedCategory.getCategoryName());
        final String whereClause = DatabaseContract.CategoryEntry._ID + "=?";
        final String[] whereArgs = {String.valueOf(categoryId)}; //values for "=?"

        return db.update(DatabaseContract.CategoryEntry.TABLE_NAME, values, whereClause, whereArgs);
    }
    //delete
    public int deleteCategory(int categoryId){
        SQLiteDatabase db = getWritableDatabase();
        final String whereClause = DatabaseContract.CategoryEntry._ID + "=?";
        final String[] whereArgs = {String.valueOf(categoryId)};
        return db.delete(DatabaseContract.CategoryEntry.TABLE_NAME, whereClause, whereArgs);
    }
}
