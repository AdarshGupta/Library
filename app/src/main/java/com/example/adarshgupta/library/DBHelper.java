package com.example.adarshgupta.library;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by adarsh gupta on 4/19/2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_Name = "library";
    public static final int DB_Ver = 1;

    public DBHelper(Context context) {
        super(context, DB_Name, null, DB_Ver);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String createTB_Qry = "CREATE TABLE IF NOT EXISTS " + DBContract.LibraryEntry.TB_NAME + " ( " +
                DBContract.LibraryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBContract.LibraryEntry.TITLE + " TEXT NOT NULL, " +
                DBContract.LibraryEntry.AUTHOR + " TEXT NOT NULL, " +
                DBContract.LibraryEntry.PUBLISHDATE + " TEXT NOT NULL, " +
                DBContract.LibraryEntry.SUBJECT + " TEXT NOT NULL, " +
                DBContract.LibraryEntry.LANGUAGE + " TEXT NOT NULL, " +
                DBContract.LibraryEntry.PUBLISHPLACE + " TEXT NOT NULL, " +
                DBContract.LibraryEntry.PERSON + " TEXT NOT NULL, " +
                DBContract.LibraryEntry.ISBN + " TEXT NOT NULL, " +
                " UNIQUE (  " + DBContract.LibraryEntry.ISBN + " ) ON CONFLICT IGNORE );";

        sqLiteDatabase.execSQL(createTB_Qry);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBContract.LibraryEntry.TB_NAME);
        onCreate(sqLiteDatabase);

    }
}
