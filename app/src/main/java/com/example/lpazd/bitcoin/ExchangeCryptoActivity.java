package com.example.lpazd.bitcoin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

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



public class ExchangeCryptoActivity extends AppCompatActivity {
    public static final String API = "https://api.coinmarketcap.com/v2/ticker/";
    private Spinner spinner;
    private EditText et;
    private ListView lv;
    private List<String> currencies;
    public OkHttpClient okHttpClient = new OkHttpClient();


    private static final String TAG = "***MainActivity***";

    public HashMap<Integer, String> currencies2 = new HashMap() {{
        put(1, "BTC");
        put(1027, "ETH");
        put(52, "XRP");
        put(1765, "EOS");
        put(2, "LTC");
    }};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_crypto);


        spinner = (Spinner) findViewById(R.id.spinner);
        et = (EditText) findViewById(R.id.et);
        lv = (ListView) findViewById(R.id.lv);
        currencies = new ArrayList<>();
        currencies.add("USD");
        currencies.add("BTC");
        currencies.add("ETH");
        currencies.add("XRP");
        currencies.add("EOS");
        currencies.add("LTC");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = spinner.getSelectedItem().toString();
                showPrices(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                showPrices(spinner.getSelectedItem().toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showPrices(spinner.getSelectedItem().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                showPrices(spinner.getSelectedItem().toString());
            }
        });
    }

    private void showPrices(final String seletedItem) {
        final List<String> list = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : currencies2.entrySet()) {
            if (entry.getValue() != seletedItem) {
                Log.v(TAG, entry.getKey().toString());
                Request request = new Request.Builder().url(API + entry.getKey() + "/?convert=" + seletedItem).build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.v(TAG, "Failure hehe");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if ((!et.getText().toString().matches("")) && (et.getText().toString().matches("[+-]?([0-9]*[.])?[0-9]+"))){
                            list.add(jsonToString(response.body().string(), seletedItem, Double.parseDouble(et.getText().toString())));
                        }
                    }
                });
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,list);
        lv.setAdapter(adapter);
    }

    private String jsonToString(String body,String selectedItem,Double amount) {

        StringBuilder builder = new StringBuilder();
        double price;
        try {
            JSONObject jsonObject = new JSONObject(body);
            JSONObject dataObj = jsonObject.getJSONObject("data");
//            builder.append(dataObj.getString("name"));
            JSONObject quotesObj = dataObj.getJSONObject("quotes");
            JSONObject cryptoObj = quotesObj.getJSONObject(selectedItem);
           if(amount != 0){
                   price = amount / cryptoObj.getDouble( "price");
           }else {
               price = 0 ;
           }
            builder.append(" "+ price + " "+ dataObj.getString("symbol"));
            Log.v(TAG, builder.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}