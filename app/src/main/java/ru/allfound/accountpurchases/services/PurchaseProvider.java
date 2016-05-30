package ru.allfound.accountpurchases.services;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.allfound.accountpurchases.sqlite.DatabaseHandler;
import ru.allfound.accountpurchases.sqlite.IDatabaseHandler;

/*
 * PurchaseProvider.java    v.1.0 23.05.2016
 *
 * Copyright (c) 2015-2016 Vladislav Laptev,
 * All rights reserved. Used by permission.
 */

public class PurchaseProvider extends ContentProvider implements IDatabaseHandler{

    /** The unique identifier of this {@link ContentProvider}. */
    private static final String AUTHORITY = "ru.allfound.providers.Purchase";

    /** The multiple rows in table TABLE_PURCHASES. */
    private static final int MATCH_PURCHASE_DATA = 1;

    /** The one row in table TABLE_PURCHASES. */
    private static final int MATCH_PURCHASE_ITEM = 2;

    /** The name field in table TABLE_PURCHASES. */
    private static final int MATCH_PURCHASE_ITEM_DESCRIPTION = 3;

    /** The content URI for table TABLE_PURCHASES. */
    public static final Uri CONTENT_PURCHASE;

    /** The MIME-type for all rows in table TABLE_PURCHASES. */
    public static final String CONTENT_TYPE_PURCHASE_DATA;

    /** The MIME-type for one row in table TABLE_PURCHASES. */
    public static final String CONTENT_TYPE_PURCHASE_ITEM;

    /** The MIME-type for fields in table TABLE_PURCHASES. */
    public static final String CONTENT_TYPE_PURCHASE_ITEM_FIELD = "text/plain";

    /** The object for matching URIs. */
    private static final UriMatcher uriMatcher;
    /* Static initialisation section */
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // All rows in table TABLE_PURCHASES
        uriMatcher.addURI(AUTHORITY, TABLE_PURCHASES, MATCH_PURCHASE_DATA);
        // One row in table TABLE_PURCHASES
        uriMatcher.addURI(AUTHORITY, TABLE_PURCHASES + "/#", MATCH_PURCHASE_ITEM);
        // Name field in table TABLE_PURCHASES
        uriMatcher.addURI(AUTHORITY, TABLE_PURCHASES + "/" + KEY_DESCRIPTION,
                MATCH_PURCHASE_ITEM_DESCRIPTION);

        // Content URI for table TABLE_PURCHASES
        final String all = "content://" + AUTHORITY + "/" + TABLE_PURCHASES;
        CONTENT_PURCHASE = Uri.parse(all);

        // Types for table TABLE_PURCHASES
        CONTENT_TYPE_PURCHASE_DATA = "vnd.android.cursor.dir/" + AUTHORITY + "/" + TABLE_PURCHASES;
        CONTENT_TYPE_PURCHASE_ITEM = "vnd.android.cursor.item/" + AUTHORITY + "/" + TABLE_PURCHASES;
    }

    /** The internal database. */
    private DatabaseHandler databaseHandler = null;

    /** The {@link} for quick access. */
    private ContentResolver contentResolver = null;

    /**
     * Implement this to initialize your content provider on startup.
     * @return True if the provider was successfully loaded, false otherwise.
     * */
    @Override
    public boolean onCreate() {
        contentResolver = this.getContext().getContentResolver();
        databaseHandler = new DatabaseHandler(this.getContext());
        return true;
    }

    /**
     * Implement this to handle query requests from clients.
     * @return a Cursor or {@code null}.
     * */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // All rows from table TableDep
        if (uriMatcher.match(uri) == MATCH_PURCHASE_DATA) {
            final Cursor c = databaseHandler.getReadableDatabase().query(TABLE_PURCHASES,
                    projection, selection, selectionArgs, null, null, sortOrder);
            c.setNotificationUri(contentResolver, uri); // For notify about changes
            return c;
        }

        // Default value
        return null;
    }

    /**
     * Implement this to handle requests for the MIME type of the data at the given URI. The
     * returned MIME type should start with <code>vnd.android.cursor.item</code> for a single
     * record, or <code>vnd.android.cursor.dir/</code> for multiple items.
     * @return A MIME type string, or {@code null} if there is no type.
     * */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        // All rows in table TABLE_PURCHASES
        if (uriMatcher.match(uri) == MATCH_PURCHASE_DATA) {
            return CONTENT_TYPE_PURCHASE_DATA;
        }

        // One row in table TABLE_PURCHASES
        if (uriMatcher.match(uri) == MATCH_PURCHASE_ITEM) {
            return CONTENT_TYPE_PURCHASE_ITEM;
        }

        // Name field in table TABLE_PURCHASES
        if (uriMatcher.match(uri) == MATCH_PURCHASE_ITEM_DESCRIPTION) {
            return CONTENT_TYPE_PURCHASE_ITEM_FIELD;
        }

        // By default
        return null;
    }

    /**
     * Implement this to handle requests to insert a new row.
     * @return The URI for the newly inserted item.
     * */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        // Insert into TABLE_PURCHASES
        if (uriMatcher.match(uri) == MATCH_PURCHASE_DATA) {
            long id = databaseHandler.addPurchase(values);
            Uri itemUri = ContentUris.withAppendedId(CONTENT_PURCHASE, id);
            contentResolver.notifyChange(itemUri, null);
            return itemUri;
        }

        // Default value
        return null;
    }

    /**
     * Implement this to handle requests to delete one or more rows.
     * @return The number of rows affected.
     */
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        // Delete one row from TABLE_PURCHASES
        if (uriMatcher.match(uri) == MATCH_PURCHASE_ITEM) {
            long id = ContentUris.parseId(uri);
            if (databaseHandler.deletePurchase(id)) {
                contentResolver.notifyChange(uri, null);
                return 1;
            }
            return 0;
        }

        // Delete all rows from TABLE_PURCHASES
        if (uriMatcher.match(uri) == MATCH_PURCHASE_DATA) {
            int count = databaseHandler.getWritableDatabase().delete(TABLE_PURCHASES, null,
                    null);
            contentResolver.notifyChange(uri, null);
            return count;
        }

        // By default
        return 0;
    }

    /**
     * Implement this to handle requests to update one or more rows.
     * @return the number of rows affected.
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
