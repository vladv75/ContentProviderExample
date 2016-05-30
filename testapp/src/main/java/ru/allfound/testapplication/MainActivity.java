package ru.allfound.testapplication;

import android.content.ContentProviderClient;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.net.URI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        		/* Initialize UI components */
        ListView list = (ListView) findViewById(R.id.list);
        registerForContextMenu(list);

        Uri requestURI = Uri.parse("content://ru.allfound.providers.Purchase/purchases");
        ContentProviderClient client = getContentResolver().acquireContentProviderClient(requestURI);
        Cursor cursor = null;
        try {
            assert client != null;
            cursor = client.query(requestURI, null, null, null, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        /* Create arrays of columns and UI elements */
        String[] from = {"description", "category", "date", "time", "price"};
        int[] to = {R.id.textViewDescription, R.id.textViewCategory,
                R.id.textViewDate, R.id.textViewTime, R.id.textViewPrice};

		/* Create simple Cursor adapter */
        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this, R.layout.purchase_item, cursor, from, to, 1);

        // Setting up adapter for list
        if (list != null) list.setAdapter(mAdapter);
    }
}
