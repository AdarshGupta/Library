package com.example.adarshgupta.library;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

/**
 * Created by adarsh gupta on 4/19/2015.
 */
public class TestContentProvider extends AndroidTestCase {
    public void testDelDb() throws Throwable
    {

        mContext.deleteDatabase(DBHelper.DB_Name);

    }

    public void testInsertProvider()
    {

        final String title = "Harry Potter series";
        final String author = "J.K. Rowling";
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
        values.put(DBContract.LibraryEntry.SUBJECT,subject);
        values.put(DBContract.LibraryEntry.LANGUAGE, language);
        values.put(DBContract.LibraryEntry.PUBLISHPLACE, publishplace);
        values.put(DBContract.LibraryEntry.PERSON, person);
        values.put(DBContract.LibraryEntry.ISBN, isbn);


//       Uri ins = mContext.getContentResolver().insert(DBContract.LibraryEntry.Content_Uri, values);


       long id=1001;

        Cursor cursor = mContext.getContentResolver().query(DBContract.LibraryEntry.buildLibraryUriWithId(id),
                null,//leave column null to return all columns
                null,//Columns for where clause
                null,//values for where clause
                null//columns to sort order
        );



    }


    public void testGetType()
    {


        long id = 1001;//test data

       String type = mContext.getContentResolver().getType(DBContract.LibraryEntry.buildLibraryUriWithId(id));
        assertEquals(DBContract.LibraryEntry.Content_Type_Item, type);

    }
}
