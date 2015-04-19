package com.example.adarshgupta.library;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;



/**
 * Created by adarsh gupta on 4/19/2015.
 */
public class LibraryProvider extends ContentProvider {
   
    public static final int LEAVE_WITH_ID = 1001;

    private DBHelper db;

    private static final UriMatcher mUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DBContract.Content_Auth;

        
        matcher.addURI(authority, DBContract.Path_LIBRARY_TB + "/*", LEAVE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        db = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {

        Cursor retCur;

        switch(mUriMatcher.match(uri))
        {
            
            case LEAVE_WITH_ID:
               // String[] args = {DBContract.LibraryEntry.getExitDateFromUri(uri)};
                retCur = db.getReadableDatabase().query(
                        DBContract.LibraryEntry.TB_NAME,
                        projection,
                        DBContract.LibraryEntry.TITLE ,
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        retCur.setNotificationUri(getContext().getContentResolver(), uri);

        return retCur;
    }

    @Override
    public String getType(Uri uri) {

        final int match = mUriMatcher.match(uri);

        switch(match)
        {
           
            case LEAVE_WITH_ID:
                return DBContract.LibraryEntry.Content_Type_Item;
            default:
                throw new UnsupportedOperationException("Unknown Uri:" +uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        Uri retUri;
        final SQLiteDatabase db = new DBHelper(getContext()).getWritableDatabase();

        switch(mUriMatcher.match(uri))
        {
            case LEAVE_WITH_ID:
                long _id = db.insert(DBContract.LibraryEntry.TB_NAME, null, contentValues);
                if(_id > 0)
                    retUri = DBContract.LibraryEntry.buildLibraryUriWithId(_id);
                else
                    throw new SQLException("Failed to insert row to " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri:" +uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        final SQLiteDatabase db = new DBHelper(getContext()).getWritableDatabase();
        int rowAffected;

        switch(mUriMatcher.match(uri))
        {
            case LEAVE_WITH_ID:
                rowAffected = db.delete(DBContract.LibraryEntry.TB_NAME, selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        //because a null deletes all rows, so we notify change only if any or all rows are affected
        if(selection == null || rowAffected != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowAffected;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs)
    {
        final SQLiteDatabase db = new DBHelper(getContext()).getWritableDatabase();
        int rowAffected;

        switch(mUriMatcher.match(uri))
        {
            case LEAVE_WITH_ID:
                rowAffected = db.update(DBContract.LibraryEntry.TB_NAME, contentValues, selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        //because a null deletes all rows, so we notify change only if any or all rows are affected
        if(rowAffected != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowAffected;
    }
}
