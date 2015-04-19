package com.example.adarshgupta.library;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by adarsh gupta on 4/19/2015.
 */
public class DBContract {
    public static final String Content_Auth = "com.example.adarshgupta.library";

    public static final Uri Base_Content_Uri = Uri.parse("content://"+Content_Auth);

    public static final String Path_LIBRARY_TB = "library";

    public static final class LibraryEntry implements BaseColumns
    {

        public static final Uri Content_Uri = Base_Content_Uri.buildUpon().appendPath(Path_LIBRARY_TB).build();

        public static final String Content_Type_Dir = "vnd.android.cursor.dir/"+Content_Auth+"/"+ Path_LIBRARY_TB;
        public static final String Content_Type_Item = "vnd.android.cursor.item/"+Content_Auth+"/"+ Path_LIBRARY_TB;

        public static final String TB_NAME = "library";
        public static final String TITLE = "title";//text not null
        public static final String AUTHOR = "author";//text
        public static final String PUBLISHDATE = "publishdate";//text not null
        public static final String SUBJECT = "subject";//text not null
        public static final String LANGUAGE = "language";//text not null
        public static final String PUBLISHPLACE = "publishplace";//text not null
        public static final String PERSON = "person";//text not null
        public static final String ISBN = "isbn";//text not null


        public static Uri buildLibraryUriWithId (long _id)
        {
            return ContentUris.withAppendedId(Content_Uri, _id);
        }


    }
}
