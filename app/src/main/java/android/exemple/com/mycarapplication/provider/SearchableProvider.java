package android.exemple.com.mycarapplication.provider;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by lenovo on 15/01/2016.
 */
public class SearchableProvider extends SearchRecentSuggestionsProvider{
    public static final String AUTHORITY = "android.exemple.com.mycarapplication.provider.SearchableProvider";
    public static final int MODE = DATABASE_MODE_QUERIES;

    public SearchableProvider(){
        setupSuggestions(AUTHORITY, MODE);
    }
}
