package com.example.adarshgupta.library;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

/**
 * Created by adarsh gupta on 4/19/2015.
 */
public class Testdb extends AndroidTestCase {
    public void testCreateDb() throws Throwable
    {

        mContext.deleteDatabase(DBHelper.DB_Name);
        SQLiteDatabase db = new DBHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();

    }

    public void testInsertDb()
    {

        final String title = "Harry Potter series";
        final String author = "J.K.Rowling";
        final String publishdate = "1/1/2000";
        final String subject = "Magical Fantasy";
        final String language = "English";
        final String publishplace = "USA";
        final String person = "Harry Potter";
        final String isbn = "1001";


        SQLiteDatabase db = new DBHelper(this.mContext).getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(DBContract.LibraryEntry.TITLE, title);
        values.put(DBContract.LibraryEntry.AUTHOR, author);
        values.put(DBContract.LibraryEntry.PUBLISHDATE, publishdate);
        values.put(DBContract.LibraryEntry.SUBJECT, subject);
        values.put(DBContract.LibraryEntry.LANGUAGE, language);
        values.put(DBContract.LibraryEntry.PUBLISHPLACE, language);
        values.put(DBContract.LibraryEntry.PERSON, language);
        values.put(DBContract.LibraryEntry.ISBN, language);


        long returnRowNum;
        returnRowNum = db.insert(DBContract.LibraryEntry.TB_NAME, null, values);

        assertTrue(returnRowNum != -1);


        String[] columns = {DBContract.LibraryEntry._ID, DBContract.LibraryEntry.TITLE, DBContract.LibraryEntry.AUTHOR, DBContract.LibraryEntry.PUBLISHDATE,
                DBContract.LibraryEntry.SUBJECT, DBContract.LibraryEntry.LANGUAGE, DBContract.LibraryEntry.PUBLISHPLACE, DBContract.LibraryEntry.PERSON, DBContract.LibraryEntry.ISBN};

        Cursor cursor = db.query(
                DBContract.LibraryEntry.TB_NAME,
                columns,
                null,//Columns for where clause
                null,//values for where clause
                null,//columns to group by
                null,//columns to filter by row group
                null//columns to sort order
        );

        if(cursor.moveToFirst())
        {
            int id_index = cursor.getColumnIndex(DBContract.LibraryEntry._ID);
            int id_val = cursor.getInt(id_index);

            assertEquals(1, id_val);

        }
        else
        {
            fail("No Values");
        }

    }
}
