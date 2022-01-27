package com.tatvasoftassignment.mylibrary.model;


import static android.content.ContentValues.TAG;

import android.util.Log;

public class Books {
    int id;
    String bookNames;
    String bookAuthorName;
    String bookLaunchDate;
    String bookType;
    String bookGenre;
    String bookAgePrefer;

    public Books(int id, String bookNames, String bookAuthorName, String bookGenre, String bookType, String bookLaunchDate, String bookAgePrefer) {
        this.id = id;
        this.bookNames = bookNames;
        this.bookAuthorName = bookAuthorName;
        this.bookLaunchDate = bookLaunchDate;
        this.bookType = bookType;
        this.bookGenre = bookGenre;
        this.bookAgePrefer = bookAgePrefer;
    }

    public int getId() {
        return id;
    }


    public String getBookNames() {
        return bookNames;
    }


    public String getBookAuthorNames() {
        return bookAuthorName;
    }

    public String getBookLaunchDate() {
        return bookLaunchDate;
    }

    public String getBookType() {
        return bookType;
    }

    public String getBookGenre() {
        Log.i(TAG, bookGenre);
        return bookGenre;
    }

    public String getBookAgePrefer() {
        return bookAgePrefer;
    }

}