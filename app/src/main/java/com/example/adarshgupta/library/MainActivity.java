package com.example.adarshgupta.library;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Toolbar mToolbar;
    private static final String QUERY_URL = "http://openlibrary.org/search.json?q=";
    JSONAdapter mJSONAdapter;
    EditText mainEditText;
    ListView mainListView;
    TextView mainTextView;
    Button mainButton;
    ArrayAdapter mArrayAdapter;
    private static final String TAG_SORT_NAME = "sortName";
    //tag associated with the FAB menu button that sorts by date
    private static final String TAG_REQUEST = "request";
    //tag associated with the FAB menu button that sorts by ratings

 //   ArrayList mNameList = new ArrayList();
 ProgressDialog mDialog;
    public int pane=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            // do something
            setContentView(R.layout.tablet_activity_main);
            pane=1;
        } else {
            // do something else
            setContentView(R.layout.activity_main);
            pane=0;
        }


        mToolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //library
        //  the TextView defined in layout XML

        mainTextView = (TextView) findViewById(R.id.main_textview);


        //  the Button defined in layout XML
        // and listener for it
        mainButton = (Button) findViewById(R.id.main_button);
        mainButton.setOnClickListener(this);

        // the EditText defined in layout XML
        mainEditText = (EditText) findViewById(R.id.main_edittext);

        //  the ListView
        mainListView = (ListView) findViewById(R.id.main_listview);



        // Set the ListView to use the ArrayAdapter
        mainListView.setAdapter(mArrayAdapter);

        //  Set this activity to react to list items being pressed
        mainListView.setOnItemClickListener(this);

        // Create a JSONAdapter for the ListView
        mJSONAdapter = new JSONAdapter(this, getLayoutInflater());

// Set the ListView to use the ArrayAdapter
        mainListView.setAdapter(mJSONAdapter);



        //library



        NavigationDrawerFragment drawerFragment=(NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setup(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
//floating button
        ImageView imageView=new ImageView(this);
        imageView.setImageResource(R.drawable.ic_action_new);

        FloatingActionButton actionButton=new FloatingActionButton.Builder(this)
                .setContentView(imageView)
                .setBackgroundDrawable(R.drawable.selector_button_red)
                .build();

        //for sub menu of FAB
        ImageView sort1=new ImageView(this);
        sort1.setImageResource(R.drawable.ic_number2);



        ImageView sort2=new ImageView(this);
        sort2.setImageResource(R.drawable.ic_number4);

        SubActionButton.Builder itemBuilder=new  SubActionButton.Builder(this);
        itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_sub_button_grey)).build();
        SubActionButton button1=itemBuilder.setContentView(sort1).build();
        SubActionButton button2=itemBuilder.setContentView(sort2).build();

        button1.setTag(TAG_SORT_NAME);
        button2.setTag(TAG_REQUEST);



        FloatingActionMenu actionMenu=new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .attachTo(actionButton)
                .build();
//     //here i have specified the listener. now check down for on click method to detect which button was clicked
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
                        //  Take what was typed into the EditText and use in search
                queryBooks(mainEditText.getText().toString());

        if (v.getTag().equals(TAG_SORT_NAME)) {
            //call the sort by name method
            Toast.makeText(this,"List sorted alphabetically.Feature under development",Toast.LENGTH_LONG).show();

        }
        if (v.getTag().equals(TAG_REQUEST)) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "adarsh035@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Request for a book/journal/research paper/magazines/ebook");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));

        }


        }



    //connection
    private void queryBooks(String searchString) {

        // Prepare search string to be put in a URL
        // It might have reserved characters or something
        String urlString = "";
        try {
            urlString = URLEncoder.encode(searchString, "UTF-8");
        } catch (UnsupportedEncodingException e) {

            // if this fails for some reason, let the user know why
            e.printStackTrace();

            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Create a client to perform networking
        AsyncHttpClient client = new AsyncHttpClient();
        // Show ProgressDialog to inform user that a task in the background is occurring
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Searching for Book");
        mDialog.setCancelable(true);
        mDialog.show();
        client.setTimeout(10000);


        // Have the client get a JSONArray of data
        // and define how to respond
        client.get(QUERY_URL + urlString,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //  Dismiss the ProgressDialog
                        mDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Success!"+response, Toast.LENGTH_LONG).show();


                        mJSONAdapter.updateData(response.optJSONArray("docs"));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        // 11. Dismiss the ProgressDialog
                        mDialog.dismiss();
                    }


                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //  Now that the user's chosen a book, grab the cover data
        JSONObject jsonObject = (JSONObject) mJSONAdapter.getItem(position);
        String coverID = jsonObject.optString("cover_i","");
        String title = jsonObject.optString("title","");
        String author = jsonObject.optString("author_name","");
        String publishDate = jsonObject.optString("publish_date","");
        String subject = jsonObject.optString("subject","");
        String publishPlace = jsonObject.optString("publish_place","");
        String language = jsonObject.optString("language","");
        String person = jsonObject.optString("person","");
        String isbn = jsonObject.optString("isbn","");

        // create an Intent to take you over to a new DetailActivity
        Intent detailIntent = new Intent(this, DetailActivity.class);
        // pack away the data about the cover
// into your Intent before you head out
        detailIntent.putExtra("coverID", coverID)
                .putExtra("title",title)
                .putExtra("author_name",author)
                .putExtra("publish_date",publishDate)
                .putExtra("subject",subject)
                .putExtra("publish_place",publishPlace)
                .putExtra("language",language)
                .putExtra("person",person)
                .putExtra("isbn",isbn);
        // start the next Activity using your prepared Intent
        startActivity(detailIntent);
    }
}
