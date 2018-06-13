package com.example.lpazd.bitcoin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class CryptoActivity extends AppCompatActivity {

    Spinner spinner;
    EditText et;
    Button submit;
    List currencies = new ArrayList();
    public static Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto);

        db = new Database(this);

        spinner = (Spinner) findViewById(R.id.spinner);
        et = (EditText) findViewById(R.id.et);
        submit = (Button) findViewById(R.id.btn);
        currencies = new ArrayList<>();
        currencies.add("BTC");
        currencies.add("ETH");
        currencies.add("XRP");
        currencies.add("EOS");
        currencies.add("LTC");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currencies);
        spinner.setAdapter(adapter);
    }

    public void addData(View view) {
        String currency = spinner.getSelectedItem().toString();
        Double amount = Double.parseDouble(et.getText().toString());
        if(amount >= 0){
            db.insertData(currency,amount);
        }

    }
}
