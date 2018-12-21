package com.example.peter.thekitchenmenu.provider;

import android.content.SearchRecentSuggestionsProvider;

public class RecentSuggestionsProvider extends SearchRecentSuggestionsProvider {

    public static String AUTHORITY = "com.example.peter.thekitchenmenu.provider.RecentSuggestionsProvider";

    public static int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;

    public RecentSuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
