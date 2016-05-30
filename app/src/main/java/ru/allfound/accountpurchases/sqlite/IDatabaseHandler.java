package ru.allfound.accountpurchases.sqlite;

/*
 * IDatabaseHandler.java    v.1.0 05.05.2016
 *
 * Copyright (c) 2015-2016 Vladislav Laptev,
 * All rights reserved. Used by permission.
 */

public interface IDatabaseHandler {
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "ACCOUNTPURCHASES_DB";
    static final String TABLE_PURCHASES = "purchases";

    static final String KEY_ID = "_id";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_CATEGORY = "category";
    static final String KEY_DATE = "date";
    static final String KEY_TIME = "time";
    static final String KEY_PRICE = "price";

    String CREATE_PURCHASES_TABLE = "CREATE TABLE "
            + TABLE_PURCHASES + "("
            + KEY_ID + " integer primary key autoincrement,"
            + KEY_DESCRIPTION + " TEXT, "
            + KEY_CATEGORY + " TEXT, "
            + KEY_DATE + " TEXT, "
            + KEY_TIME + " TEXT, "
            + KEY_PRICE + " INTEGER" + ")";
}
