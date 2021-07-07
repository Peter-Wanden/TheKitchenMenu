package com.example.peter.thekitchenmenu.provider;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.Nullable;

import javax.annotation.Nonnull;

public class SuggestionsProvider extends ContentProvider {

    private static final String TAG = "SuggestionsProvider";

    public static String AUTHORITY = "com.example.peter.thekitchenmenu.provider.SuggestionsProvider";

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int SEARCH_SUGGEST = 1;

    static {
        MATCHER.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@Nonnull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }


//    @Nullable
//    @Override
//    public Cursor query(@Nonnull Uri uri,
//                        @Nullable String[] projection,
//                        @Nullable String selection,
//                        @Nullable String[] selectionArgs,
//                        @Nullable String sortOrder) {
//
//        // Match the Uri.
//        final int matchCode = MATCHER.match(uri);
//        if (matchCode == SEARCH_SUGGEST) {
//
//            if (selectionArgs == null) {
//                throw new IllegalArgumentException(
//                        "selectionArgs must be provided for the URI: " + uri);
//            }
//
//            // Get the cursor async and append a wildcard.
//            Repository<ProductEntity> repository = DatabaseInjection.provideProductDataSource(getContext());
////            return repository.getMatchingProducts(selectionArgs[0] + "*");
//
//        } else {
//            throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
//    }

    @Nullable
    @Override
    public String getType(@Nonnull Uri uri) {
        switch (MATCHER.match(uri)) {
            case SEARCH_SUGGEST:
                return SearchManager.SUGGEST_MIME_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@Nonnull Uri uri,
                      @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@Nonnull Uri uri,
                      @Nullable String s,
                      @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@Nonnull Uri uri,
                      @Nullable ContentValues contentValues,
                      @Nullable String s,
                      @Nullable String[] strings) {
        return 0;
    }
}
