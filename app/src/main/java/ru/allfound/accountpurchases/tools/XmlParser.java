package ru.allfound.accountpurchases.tools;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ru.allfound.accountpurchases.model.Purchase;
import ru.allfound.accountpurchases.R;
import ru.allfound.accountpurchases.sqlite.DatabaseHandler;

/*
 * Purchase.java    v.1.0 06.05.2016
 *
 * Copyright (c) 2015-2016 Vladislav Laptev,
 * All rights reserved. Used by permission.
 */

public class XmlParser {
    private Context context;
    private Purchase purchase;
    DatabaseHandler databaseHandler;

    public XmlParser(Context context, DatabaseHandler databaseHandler) {
        this.context = context;
        this.databaseHandler = databaseHandler;
        purchase = new Purchase();
    }

    public void parser() {
        try {
            XmlPullParser xpp = prepareXpp();

            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (xpp.getEventType()) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        for (int i = 0; i < xpp.getAttributeCount(); i++) {
                            switch (i) {
                                case 0: purchase.setDate(xpp.getAttributeValue(i));
                                        break;
                                case 1: purchase.setTime(xpp.getAttributeValue(i));
                                    break;
                                case 2: purchase.setCategory(xpp.getAttributeValue(i));
                                    break;
                                case 3: purchase.setPrice(Integer.valueOf(xpp.getAttributeValue(i)));
                                    break;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        purchase.setDescription(xpp.getText());
                        databaseHandler.addPurchase(purchase);
                        break;

                    default:
                        break;
                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    XmlPullParser prepareXpp() {
        return context.getResources().getXml(R.xml.data);
    }
}
