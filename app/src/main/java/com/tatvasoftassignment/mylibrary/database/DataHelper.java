package com.tatvasoftassignment.mylibrary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tatvasoftassignment.mylibrary.Utils.Constants;


public class DataHelper extends SQLiteOpenHelper {
    public static int id = 1;

    public DataHelper(Context context) {
        super(context, "AddBookList.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table BookListNew(bookId NUMBER,bookName TEXT,bookAuthorName TEXT,genre TEXT,bookType TEXT,launchDate TEXT,agePrefer TEXT )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertData(String bookName, String bookAuthorName, String genre, String bookType, String launchDate, String agePrefer) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.bookId, id);
        id = id + 1;
        contentValues.put(Constants.bookName, bookName);
        contentValues.put(Constants.bookAuthorName, bookAuthorName);
        contentValues.put(Constants.genre, genre);
        contentValues.put(Constants.bookType, bookType);
        contentValues.put(Constants.launchDate, launchDate);
        contentValues.put(Constants.agePrefer, agePrefer);
         DB.insert(Constants.BookListNew, null, contentValues);

    }

    public Cursor getData() {
        SQLiteDatabase Db = this.getWritableDatabase();
        return Db.rawQuery("Select * from BookListNew", null);
    }

    public Cursor getDataBookName(int id) {
        SQLiteDatabase Db = this.getWritableDatabase();

        return Db.rawQuery("select * from BookListNew Where bookId=?", new String[]{String.valueOf(id)});
    }

    public Boolean updateData(int id, String bookName, String bookAuthorName, String genre, String bookType, String launchDate, String agePrefer) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.bookName, bookName);
        contentValues.put(Constants.bookAuthorName, bookAuthorName);
        contentValues.put(Constants.genre, genre);
        contentValues.put(Constants.bookType, bookType);
        contentValues.put(Constants.launchDate, launchDate);
        contentValues.put(Constants.agePrefer, agePrefer);
        long result = DB.update(Constants.BookListNew, contentValues, "bookId=?", new String[]{String.valueOf(id)});
        return result != -1;
    }
}

