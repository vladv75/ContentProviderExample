package ru.allfound.accountpurchases.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.rohit.recycleritemclicksupport.RecyclerItemClickSupport;

import java.util.ArrayList;

import ru.allfound.accountpurchases.sqlite.DatabaseHandler;
import ru.allfound.accountpurchases.model.Purchase;
import ru.allfound.accountpurchases.adapters.PurchaseRecyclerAdapter;
import ru.allfound.accountpurchases.R;
import ru.allfound.accountpurchases.tools.XmlParser;

/*
 * MainActivity.java    v.1.1 10.05.2016
 *
 * Copyright (c) 2015-2016 Vladislav Laptev,
 * All rights reserved. Used by permission.
 */

public class MainActivity extends AppCompatActivity {

    private ArrayList<Purchase> purchases;
    private Purchase purchase;
    private RecyclerView recyclerView;
    private DatabaseHandler databaseHandler;
    PurchaseRecyclerAdapter purchaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddPurchaseActivity.class);
                intent.putExtra("id", 0);
                startActivity(intent);
            }
        });

        purchases = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        databaseHandler = new DatabaseHandler(getApplicationContext());
        registerForContextMenu(recyclerView);

        RecyclerItemClickSupport.addTo(recyclerView).setOnItemClickListener(new RecyclerItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                purchase = purchases.get(position);
                Intent intent = new Intent(getApplication(), AddPurchaseActivity.class);
                long id = purchase.getId();
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        AsyncLoadFromBD asyncLoadFromBD = new AsyncLoadFromBD();
        asyncLoadFromBD.execute();
    }

    private class AsyncLoadFromBD extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            purchases = (ArrayList<Purchase>) databaseHandler.fetchPurchases();
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            setupRecyclerView();
        }
    }

    private void setupRecyclerView() {
        purchaseRecyclerAdapter = new PurchaseRecyclerAdapter(purchases, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(purchaseRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_load) {
            XmlParser xmlParser = new XmlParser(getApplicationContext(), databaseHandler);
            xmlParser.parser();
            AsyncLoadFromBD asyncLoadFromBD = new AsyncLoadFromBD();
            asyncLoadFromBD.execute();
            return true;
        } else if (id == R.id.action_clear) {
            databaseHandler.deletePurchases();
            purchases.clear();
            setupRecyclerView();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        try {
            position = purchaseRecyclerAdapter.getPosition();
        } catch (Exception e) {
            e.getLocalizedMessage();
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.ctx_menu_delete:
                databaseHandler.deletePurchase(purchases.get(position).getId());
                purchases.remove(position);
                setupRecyclerView();
                break;
            case R.id.ctx_menu_copy:
                Purchase purchaseNew = Purchase.newInstance(purchases.get(position));
                databaseHandler.addPurchase(purchaseNew);
                purchases.add(0, purchaseNew);
                setupRecyclerView();
                break;
        }
        return super.onContextItemSelected(item);
    }
}
