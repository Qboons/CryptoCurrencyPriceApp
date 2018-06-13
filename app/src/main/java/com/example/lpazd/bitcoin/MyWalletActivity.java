package com.example.lpazd.bitcoin;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyWalletActivity extends AppCompatActivity {
    public static Database db;
    public static final String API = "https://api.coinmarketcap.com/v2/ticker/";
    public OkHttpClient okHttpClient = new OkHttpClient();
    public HashMap<Integer, String> currencies = new HashMap() {{
        put(1, "BTC");
        put(1027, "ETH");
        put(52, "XRP");
        put(1765, "EOS");
        put(2, "LTC");
    }};

    public HashMap<String, Double> prices = new HashMap<>();

    double wallet = 0;
    private TextView tv;
    private ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        returnPrices();
        db = new Database(this);
        tv = (TextView) findViewById(R.id.tv);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        returnPrices();
        load();
    }

    @Override
    protected void onStart() {
        super.onStart();
        returnPrices();
    }

    public void load() {
        List list = new ArrayList();
        wallet = 0;
        Cursor cursor = db.getAllData();
        while (cursor.moveToNext()) {

            StringBuilder builder = new StringBuilder();
            String name = cursor.getString(1);
            builder.append(name);
            builder.append(" ");
            Double amount = cursor.getDouble(2);
            builder.append(String.valueOf(amount));
            builder.append(" ");
            Double value = 0.0;
            for(Map.Entry<String, Double> entry : prices.entrySet()){
                if(entry.getKey().equals(name)){
                    value =amount * entry.getValue();
                }
            }
            tv = (TextView) findViewById(R.id.tv);
            wallet+=value;
            tv.setText(wallet+"$");
            builder.append("-> "+value +"$");


            list.add(builder);

//            Log.v("*********", cursor.getString(1));
        }
        lv = (ListView) findViewById(R.id.lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,list);
        lv.setAdapter(adapter);
    }



    private void returnPrices() {
        int id = -1 ;
        for (final Map.Entry<Integer, String> entry : currencies.entrySet()){
            id = entry.getKey();
            if(id > 0){
                final Request request = new Request.Builder().url(API + id).build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    prices.put(entry.getValue(),jsonToString(response.body().string()));
                                    load();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                });
            }
        }

    }


    private double jsonToString(String body) {
        double price = 0.0;
        try {
            JSONObject jsonObject = new JSONObject(body);
            JSONObject dataObj = jsonObject.getJSONObject("data");
            JSONObject quotesObj = dataObj.getJSONObject("quotes");
            JSONObject usd = quotesObj.getJSONObject("USD");
            price = usd.getDouble("price");
            Log.v("jsonToString price", String.valueOf(price));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return price;
    }

}
