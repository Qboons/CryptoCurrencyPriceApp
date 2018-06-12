package com.example.lpazd.bitcoin;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TopCryptoActivity extends AppCompatActivity {

    public static final String BPI_ENDPOINT = "https://api.coinmarketcap.com/v2/ticker/?limit=20";
    private OkHttpClient okHttpClient = new OkHttpClient();
    private ProgressDialog progressDialog;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bit_coin_acvivity);
        lv = findViewById(R.id.lv);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Wait");
    }

    private static final String TAG = "***MainActivity***";


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_load) {
//            load();
//        }
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    protected void onStart() {
        super.onStart();
        load();
    }

    private void load() {
        Request request = new Request.Builder().url(BPI_ENDPOINT).build();
        progressDialog.show();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(TopCryptoActivity.this, "Error during loading data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String body = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        pareeBpiResponse(body);
                    }
                });
            }
        });

    }

    private void pareeBpiResponse(String body) {
//        Log.v(TAG, body);
        List list = new ArrayList();
        try {

            JSONObject jsonObject = new JSONObject(body);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            Iterator<?> keys = dataObject.keys();

            while (keys.hasNext()){
                String key = (String) keys.next();
                if(dataObject.get(key) instanceof JSONObject){
                    StringBuilder builder = new StringBuilder();
                    JSONObject cryptoObj = (JSONObject) dataObject.get(key);
//                    Log.v(TAG, cryptoObj.get("name").toString());
                    builder.append(cryptoObj.get("name"));
                    builder.append(" ");
                    builder.append(cryptoObj.get("symbol"));
                    builder.append(" ");
                    JSONObject quotes = cryptoObj.getJSONObject("quotes");
                    JSONObject usd = quotes.getJSONObject("USD");
                    builder.append(usd.getString("price")+ "$");
                    list.add(builder.toString());
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,list);
            lv.setAdapter(adapter);
//            builder.append(timeObject.getString("updated")).append("\n\n");
//
//            JSONObject bpiObject = jsonObject.getJSONObject("bpi");
//            JSONObject usdObject = bpiObject.getJSONObject("USD");
//            builder.append(usdObject.getString("rate")).append("$").append("\n");
//
//            JSONObject gbpObject = bpiObject.getJSONObject("GBP");
//            builder.append(gbpObject.getString("rate")).append("Ł").append("\n");
//
//            JSONObject eurObject = bpiObject.getJSONObject("EUR");
//            builder.append(eurObject.getString("rate")).append("E").append("\n");
//
//            txt.setText(builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
