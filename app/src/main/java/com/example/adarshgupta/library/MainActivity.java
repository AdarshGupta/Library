package com.example.adarshgupta.library;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
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
    //tag associated with the FAB menu button that sorts by name
    private static final String TAG_REQUEST = "request";
    //tag associated with the FAB menu button that requests developer for a book


    ProgressDialog mDialog;
    public int pane=0;

    NetworkInfo activeNetworkInfo;
    ConnectivityManager connectivityManager;



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
        //trial
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                SearchsuggestionProvider.AUTHORITY,
                SearchsuggestionProvider.MODE);

        suggestions.saveRecentQuery(String.valueOf(mainEditText),null);
        //trial




        //  the ListView
        mainListView = (ListView) findViewById(R.id.main_listview);



        // Sets the ListView to use the ArrayAdapter
        mainListView.setAdapter(mArrayAdapter);

        //  Sets this activity to react to list items being pressed
        mainListView.setOnItemClickListener(this);

        // Creates a JSONAdapter for the ListView
        mJSONAdapter = new JSONAdapter(this, getLayoutInflater());

// Sets the ListView to use the ArrayAdapter
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

         connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

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
        // automatically handle clicks on the Home/Up button,
        // as i've specified a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.main_button) {


            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if(activeNetworkInfo != null && activeNetworkInfo.isConnected()){
                    queryBooks(mainEditText.getText().toString());
                }
                else
                    Toast.makeText(this,"Please connect to the internet!",Toast.LENGTH_LONG).show();

            //  Take what was typed into the EditText and use in search


        }

      else if (v.getTag().equals(TAG_SORT_NAME)) {
            //calls the sort by name method
            Toast.makeText(this,"List sorted alphabetically.Feature under development",Toast.LENGTH_LONG).show();

        }
     else if (v.getTag().equals(TAG_REQUEST)) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "adarsh035@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Request for a book/journal/research paper/magazines/ebook");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));

        }
        else{
            queryBooks(mainEditText.getText().toString());

        }


        }



    //connection
    private void queryBooks(String searchString) {

        // Prepares search string to be put in a URL
        // It might have reserved characters or something
        String urlString = "";
        try {
            urlString = URLEncoder.encode(searchString, "UTF-8");
        } catch (UnsupportedEncodingException e) {

            // if this fails for some reason, let the user know why
            e.printStackTrace();

            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Creates a client to perform networking
        AsyncHttpClient client = new AsyncHttpClient();
        // Shows ProgressDialog to inform user that a task in the background is occurring
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Searching for Book");
        mDialog.setCancelable(true);
        mDialog.show();
        client.setTimeout(10000);


        //  gets a JSONArray of data
        // and defines how to respond
        client.get(QUERY_URL + urlString,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //  Dismisses the ProgressDialog
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

        //  Now that the user's chosen a book, grab the data
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

        //  an Intent to take you over to a new DetailActivity
        Intent detailIntent = new Intent(this, DetailActivity.class);

// into the Intent before you head out
        detailIntent.putExtra("coverID", coverID)
                .putExtra("title",title)
                .putExtra("author_name",author)
                .putExtra("publish_date",publishDate)
                .putExtra("subject",subject)
                .putExtra("publish_place",publishPlace)
                .putExtra("language",language)
                .putExtra("person",person)
                .putExtra("isbn",isbn);
        // starts the next Activity using prepared Intent
        startActivity(detailIntent);
    }
}
