package com.example.adarshgupta.library;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by adarsh gupta on 4/12/2015.
 */
public class SearchsuggestionProvider extends SearchRecentSuggestionsProvider {

    public static final String AUTHORITY =SearchsuggestionProvider.class.getName();

    public static final int MODE = DATABASE_MODE_QUERIES;

    public SearchsuggestionProvider() {

        setupSuggestions(AUTHORITY, MODE);

    }

}
