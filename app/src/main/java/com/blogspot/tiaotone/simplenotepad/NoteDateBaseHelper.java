package com.blogspot.tiaotone.simplenotepad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class NoteDateBaseHelper extends SQLiteOpenHelper{
    public static final  String CreatNote = "create table note (" + "id integer primary key autoincrement," + "content text," + "date text)";

    public NoteDateBaseHelper(Context context) {
        super(context, "note", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreatNote);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
