package com.example.lpazd.bitcoin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "***MainActivity***";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }


    public void goToBit(View view) {
        Intent intent = new Intent(MainActivity.this, TopCryptoActivity.class);
        startActivity(intent);
    }

    public void goToConverter(View view) {
        Intent intent = new Intent(MainActivity.this, ExchangeCryptoActivity.class);
        startActivity(intent);
    }

    public void goToWallet(View view) {
        Intent intent = new Intent(MainActivity.this, MyWalletActivity.class);
        startActivity(intent);
    }

    public void goToAbout(View view) {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }
}
