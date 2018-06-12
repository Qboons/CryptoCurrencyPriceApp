package com.example.lpazd.bitcoin;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MyWalletActivity extends AppCompatActivity {
    public static Database db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadDb();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyWalletActivity.this,CryptoActivity.class));
            }
        });
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        loadDb();

    }
    private void loadDb(){
        db = new Database(this);
        List list = new ArrayList();
        Cursor cursor = db.getAllData();
        while (cursor.moveToNext()) {
            StringBuilder builder = new StringBuilder();
            builder.append(cursor.getString(2));
            builder.append(" ");
            builder.append(cursor.getString(1));
            list.add(builder);
//            Log.v("*********", cursor.getString(1));
        }
        final ListView lv = (ListView) findViewById(R.id.lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,list);
        lv.setAdapter(adapter);
    }

}
