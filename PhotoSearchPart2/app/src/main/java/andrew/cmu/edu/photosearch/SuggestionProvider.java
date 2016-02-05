package andrew.cmu.edu.photosearch;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Author  : KAILIANG CHEN
 * Version : 0.1
 * Date    : 1/29/16
 */
public class SuggestionProvider extends SearchRecentSuggestionsProvider {

    // part of content uri which is defined in AndroidManifest.xml
    public static final String AUTHORITY = "andrew.cmu.edu.photosearch" +
            ".SuggestionProvider";

    // suggestion mode which gives recent queries
    public static final int MODE = DATABASE_MODE_QUERIES;

    public SuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
