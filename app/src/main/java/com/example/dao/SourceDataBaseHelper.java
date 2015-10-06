package com.example.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jack on 2015/6/13.
 */
public class SourceDataBaseHelper extends SQLiteOpenHelper{

    private String CREATE_SOURCE="create table source(" +
            "id integer primary key,"+
            "source0 text,"+
            "source1 text,"+
            "source2 text,"+
            "source3 test,"+
            "source4 test,"+
            "source5 test,"+
            "source6 test,"+
            "source7 test"+
            ")";

    public SourceDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SOURCE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
