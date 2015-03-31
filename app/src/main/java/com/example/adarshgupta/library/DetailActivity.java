package com.example.adarshgupta.library;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by adarsh gupta on 3/26/2015.
 */


public class DetailActivity extends ActionBarActivity {

    private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";
    String mImageURL;
    ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tell the activity which XML layout is right
        setContentView(R.layout.activity_detail);

        // Enable the "Up" button for more navigation options
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Access the imageview from XML
        ImageView imageView = (ImageView) findViewById(R.id.img_cover);
        TextView title1 = (TextView) findViewById(R.id.title);
        TextView author1 = (TextView) findViewById(R.id.author);
        TextView publishdate1 = (TextView) findViewById(R.id.publish_date);
        TextView subject1 = (TextView) findViewById(R.id.subject);
        TextView publishplace1 = (TextView) findViewById(R.id.publish_place);
        TextView language1 = (TextView) findViewById(R.id.language);
        TextView person1 = (TextView) findViewById(R.id.person);
        TextView isbn1 = (TextView) findViewById(R.id.isbn);



        //  unpack the coverID from its trip inside your Intent
        String coverID = this.getIntent().getExtras().getString("coverID");
        String title = this.getIntent().getExtras().getString("title");
        String author =  this.getIntent().getExtras().getString("author_name");
        String publishDate =  this.getIntent().getExtras().getString("publish_date");
        String subject =  this.getIntent().getExtras().getString("subject");
        String publishPlace =  this.getIntent().getExtras().getString("publish_place");
        String language =  this.getIntent().getExtras().getString("language");
        String person =  this.getIntent().getExtras().getString("person");
        String isbn = this.getIntent().getExtras().getString("isbn");

        imageView.setContentDescription("The book is"+title);

        title1.setText(title);
        author1.setText(author);
        publishdate1.setText(publishDate);
       subject1.setText(subject);
        publishplace1.setText(publishPlace);
        language1.setText(language);
        person1.setText(person);
        isbn1.setText(isbn);


// See if there is a valid coverID
        if (coverID.length() > 0) {

            // Use the ID to construct an image URL
            mImageURL = IMAGE_URL_BASE + coverID + "-L.jpg";

            // Use Picasso to load the image
            Picasso.with(this).load(mImageURL).placeholder(R.drawable.ic_number2).into(imageView);
        }
    }

    private void setShareIntent() {

        // create an Intent with the contents of the TextView
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,
                "Book Recommendation!");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mImageURL);

        // Make sure the provider knows
        // it should work with that Intent
        mShareActionProvider.setShareIntent(shareIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu
        // this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Access the Share Item defined in menu XML
        MenuItem shareItem = menu.findItem(R.menu.menu_detail);

        // Access the object responsible for
        // putting together the sharing submenu
        if (shareItem != null) {
            mShareActionProvider
                    = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        }

        setShareIntent();

        return true;
    }
}


