package ru.allfound.accountpurchases.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ru.allfound.accountpurchases.model.Purchase;

/*
 * DatabaseHandler.java    v.1.0 05.05.2016
 *
 * Copyright (c) 2015-2016 Vladislav Laptev,
 * All rights reserved. Used by permission.
 */

public class DatabaseHandler extends SQLiteOpenHelper implements IDatabaseHandler {

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PURCHASES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + TABLE_PURCHASES);
        onCreate(db);
    }

    public void addPurchase(Purchase purchase) {
        long rowID = -1;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        putPurchase2DB(contentValues, purchase);
        try {
            rowID = db.insert(TABLE_PURCHASES, null, contentValues);
        } catch (SQLiteConstraintException ex) {
            ex.printStackTrace();
        }
        purchase.setId(rowID);
        db.close();
    }

    /**
     * Add new department to database.
     * @return The row ID of the newly inserted department, or -1 if an error occurred.
     * */
    public long addPurchase(ContentValues contentValues) {
        long rowID = -1;

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            rowID = db.insert(TABLE_PURCHASES, null, contentValues);
        } catch (SQLiteConstraintException ex) {
            ex.printStackTrace();
        }
        return rowID;
    }

    // updates an existing purchase in the database
    public void update(Purchase purchase) {
        ContentValues contentValues = new ContentValues();
        putPurchase2DB(contentValues, purchase);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_PURCHASES, contentValues, KEY_ID + "=" + purchase.getId(), null);
        db.close();
    }

    public List<Purchase> fetchPurchases() {
        ArrayList<Purchase> purchases = new ArrayList<Purchase>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_PURCHASES, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Purchase purchase = new Purchase();
            purchase.setId(cursor.getLong(0));
            purchase.setDescription(cursor.getString(1));
            purchase.setCategory(cursor.getString(2));
            purchase.setDate(cursor.getString(3));
            purchase.setTime(cursor.getString(4));
            purchase.setPrice(cursor.getInt(5));
            purchases.add(0, purchase);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return purchases;
    }

    public Purchase findById(long id) {
        Purchase purchase = null;
        String sql = "SELECT * FROM " + TABLE_PURCHASES
                + " WHERE " + KEY_ID + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[] { id + "" });
        if (cursor.moveToNext()) {
            purchase = new Purchase();
            purchase.setId(cursor.getLong(0));
            purchase.setDescription(cursor.getString(1));
            purchase.setCategory(cursor.getString(2));
            purchase.setDate(cursor.getString(3));
            purchase.setTime(cursor.getString(4));
            purchase.setPrice(cursor.getInt(5));
        }
        cursor.close();
        db.close();
        return purchase;
    }

    public boolean deletePurchase(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int status = db.delete(TABLE_PURCHASES, KEY_ID + "=" + id, null);
        db.close();
        if (status == 1) return true;
        return false;
    }

    public void deletePurchases() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PURCHASES, null, null);
        close();
    }

    private void putPurchase2DB(ContentValues contentValues, Purchase purchase) {
        contentValues.put(KEY_DESCRIPTION, purchase.getDescription());
        contentValues.put(KEY_CATEGORY, purchase.getCategory());
        contentValues.put(KEY_DATE, purchase.getDate());
        contentValues.put(KEY_TIME, purchase.getTime());
        contentValues.put(KEY_PRICE, purchase.getPrice());
    }
}
